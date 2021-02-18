// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.util.Strings;
import org.bouncycastle.asn1.DEREncodable;
import java.util.Enumeration;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.asn1.DERUniversalString;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import java.util.Vector;
import java.util.Hashtable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class X509Name extends ASN1Encodable
{
    public static final DERObjectIdentifier C;
    public static final DERObjectIdentifier O;
    public static final DERObjectIdentifier OU;
    public static final DERObjectIdentifier T;
    public static final DERObjectIdentifier CN;
    public static final DERObjectIdentifier SN;
    public static final DERObjectIdentifier STREET;
    public static final DERObjectIdentifier SERIALNUMBER;
    public static final DERObjectIdentifier L;
    public static final DERObjectIdentifier ST;
    public static final DERObjectIdentifier SURNAME;
    public static final DERObjectIdentifier GIVENNAME;
    public static final DERObjectIdentifier INITIALS;
    public static final DERObjectIdentifier GENERATION;
    public static final DERObjectIdentifier UNIQUE_IDENTIFIER;
    public static final DERObjectIdentifier BUSINESS_CATEGORY;
    public static final DERObjectIdentifier POSTAL_CODE;
    public static final DERObjectIdentifier DN_QUALIFIER;
    public static final DERObjectIdentifier PSEUDONYM;
    public static final DERObjectIdentifier DATE_OF_BIRTH;
    public static final DERObjectIdentifier PLACE_OF_BIRTH;
    public static final DERObjectIdentifier GENDER;
    public static final DERObjectIdentifier COUNTRY_OF_CITIZENSHIP;
    public static final DERObjectIdentifier COUNTRY_OF_RESIDENCE;
    public static final DERObjectIdentifier NAME_AT_BIRTH;
    public static final DERObjectIdentifier POSTAL_ADDRESS;
    public static final DERObjectIdentifier DMD_NAME;
    public static final DERObjectIdentifier TELEPHONE_NUMBER;
    public static final DERObjectIdentifier NAME;
    public static final DERObjectIdentifier EmailAddress;
    public static final DERObjectIdentifier UnstructuredName;
    public static final DERObjectIdentifier UnstructuredAddress;
    public static final DERObjectIdentifier E;
    public static final DERObjectIdentifier DC;
    public static final DERObjectIdentifier UID;
    public static boolean DefaultReverse;
    public static final Hashtable DefaultSymbols;
    public static final Hashtable RFC2253Symbols;
    public static final Hashtable RFC1779Symbols;
    public static final Hashtable DefaultLookUp;
    @Deprecated
    public static final Hashtable OIDLookUp;
    @Deprecated
    public static final Hashtable SymbolLookUp;
    private static final Boolean TRUE;
    private static final Boolean FALSE;
    private X509NameEntryConverter converter;
    private Vector ordering;
    private Vector values;
    private Vector added;
    private ASN1Sequence seq;
    private boolean isHashCodeCalculated;
    private int hashCodeValue;
    
    public static X509Name getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static X509Name getInstance(final Object o) {
        if (o == null || o instanceof X509Name) {
            return (X509Name)o;
        }
        if (o instanceof ASN1Sequence) {
            return new X509Name((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public X509Name(final ASN1Sequence seq) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.seq = seq;
        final Enumeration objects = seq.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1Set instance = ASN1Set.getInstance(objects.nextElement());
            for (int i = 0; i < instance.size(); ++i) {
                final ASN1Sequence instance2 = ASN1Sequence.getInstance(instance.getObjectAt(i));
                if (instance2.size() != 2) {
                    throw new IllegalArgumentException("badly sized pair");
                }
                this.ordering.addElement(DERObjectIdentifier.getInstance(instance2.getObjectAt(0)));
                final DEREncodable object = instance2.getObjectAt(1);
                if (object instanceof DERString && !(object instanceof DERUniversalString)) {
                    final String string = ((DERString)object).getString();
                    if (string.length() > 0 && string.charAt(0) == '#') {
                        this.values.addElement("\\" + string);
                    }
                    else {
                        this.values.addElement(string);
                    }
                }
                else {
                    this.values.addElement("#" + this.bytesToString(Hex.encode(object.getDERObject().getDEREncoded())));
                }
                this.added.addElement((i != 0) ? X509Name.TRUE : X509Name.FALSE);
            }
        }
    }
    
    @Deprecated
    public X509Name(final Hashtable hashtable) {
        this(null, hashtable);
    }
    
    public X509Name(final Vector vector, final Hashtable hashtable) {
        this(vector, hashtable, new X509DefaultEntryConverter());
    }
    
    public X509Name(final Vector vector, final Hashtable hashtable, final X509NameEntryConverter converter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = converter;
        if (vector != null) {
            for (int i = 0; i != vector.size(); ++i) {
                this.ordering.addElement(vector.elementAt(i));
                this.added.addElement(X509Name.FALSE);
            }
        }
        else {
            final Enumeration<Object> keys = hashtable.keys();
            while (keys.hasMoreElements()) {
                this.ordering.addElement(keys.nextElement());
                this.added.addElement(X509Name.FALSE);
            }
        }
        for (int j = 0; j != this.ordering.size(); ++j) {
            final DERObjectIdentifier derObjectIdentifier = this.ordering.elementAt(j);
            if (hashtable.get(derObjectIdentifier) == null) {
                throw new IllegalArgumentException("No attribute for object id - " + derObjectIdentifier.getId() + " - passed to distinguished name");
            }
            this.values.addElement(hashtable.get(derObjectIdentifier));
        }
    }
    
    public X509Name(final Vector vector, final Vector vector2) {
        this(vector, vector2, new X509DefaultEntryConverter());
    }
    
    public X509Name(final Vector vector, final Vector vector2, final X509NameEntryConverter converter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = converter;
        if (vector.size() != vector2.size()) {
            throw new IllegalArgumentException("oids vector must be same length as values.");
        }
        for (int i = 0; i < vector.size(); ++i) {
            this.ordering.addElement(vector.elementAt(i));
            this.values.addElement(vector2.elementAt(i));
            this.added.addElement(X509Name.FALSE);
        }
    }
    
    public X509Name(final String s) {
        this(X509Name.DefaultReverse, X509Name.DefaultLookUp, s);
    }
    
    public X509Name(final String s, final X509NameEntryConverter x509NameEntryConverter) {
        this(X509Name.DefaultReverse, X509Name.DefaultLookUp, s, x509NameEntryConverter);
    }
    
    public X509Name(final boolean b, final String s) {
        this(b, X509Name.DefaultLookUp, s);
    }
    
    public X509Name(final boolean b, final String s, final X509NameEntryConverter x509NameEntryConverter) {
        this(b, X509Name.DefaultLookUp, s, x509NameEntryConverter);
    }
    
    public X509Name(final boolean b, final Hashtable hashtable, final String s) {
        this(b, hashtable, s, new X509DefaultEntryConverter());
    }
    
    private DERObjectIdentifier decodeOID(final String str, final Hashtable hashtable) {
        if (Strings.toUpperCase(str).startsWith("OID.")) {
            return new DERObjectIdentifier(str.substring(4));
        }
        if (str.charAt(0) >= '0' && str.charAt(0) <= '9') {
            return new DERObjectIdentifier(str);
        }
        final DERObjectIdentifier derObjectIdentifier = hashtable.get(Strings.toLowerCase(str));
        if (derObjectIdentifier == null) {
            throw new IllegalArgumentException("Unknown object id - " + str + " - passed to distinguished name");
        }
        return derObjectIdentifier;
    }
    
    public X509Name(final boolean b, final Hashtable hashtable, final String s, final X509NameEntryConverter converter) {
        this.converter = null;
        this.ordering = new Vector();
        this.values = new Vector();
        this.added = new Vector();
        this.converter = converter;
        final X509NameTokenizer x509NameTokenizer = new X509NameTokenizer(s);
        while (x509NameTokenizer.hasMoreTokens()) {
            final String nextToken = x509NameTokenizer.nextToken();
            final int index = nextToken.indexOf(61);
            if (index == -1) {
                throw new IllegalArgumentException("badly formated directory string");
            }
            final String substring = nextToken.substring(0, index);
            final String substring2 = nextToken.substring(index + 1);
            final DERObjectIdentifier decodeOID = this.decodeOID(substring, hashtable);
            if (substring2.indexOf(43) > 0) {
                final X509NameTokenizer x509NameTokenizer2 = new X509NameTokenizer(substring2, '+');
                final String nextToken2 = x509NameTokenizer2.nextToken();
                this.ordering.addElement(decodeOID);
                this.values.addElement(nextToken2);
                this.added.addElement(X509Name.FALSE);
                while (x509NameTokenizer2.hasMoreTokens()) {
                    final String nextToken3 = x509NameTokenizer2.nextToken();
                    final int index2 = nextToken3.indexOf(61);
                    final String substring3 = nextToken3.substring(0, index2);
                    final String substring4 = nextToken3.substring(index2 + 1);
                    this.ordering.addElement(this.decodeOID(substring3, hashtable));
                    this.values.addElement(substring4);
                    this.added.addElement(X509Name.TRUE);
                }
            }
            else {
                this.ordering.addElement(decodeOID);
                this.values.addElement(substring2);
                this.added.addElement(X509Name.FALSE);
            }
        }
        if (b) {
            final Vector<Object> ordering = new Vector<Object>();
            final Vector<Object> values = new Vector<Object>();
            final Vector<Object> added = new Vector<Object>();
            int index3 = 1;
            for (int i = 0; i < this.ordering.size(); ++i) {
                if (this.added.elementAt(i)) {
                    ordering.insertElementAt(this.ordering.elementAt(i), index3);
                    values.insertElementAt(this.values.elementAt(i), index3);
                    added.insertElementAt(this.added.elementAt(i), index3);
                    ++index3;
                }
                else {
                    ordering.insertElementAt(this.ordering.elementAt(i), 0);
                    values.insertElementAt(this.values.elementAt(i), 0);
                    added.insertElementAt(this.added.elementAt(i), 0);
                    index3 = 1;
                }
            }
            this.ordering = ordering;
            this.values = values;
            this.added = added;
        }
    }
    
    public Vector getOIDs() {
        final Vector<Object> vector = new Vector<Object>();
        for (int i = 0; i != this.ordering.size(); ++i) {
            vector.addElement(this.ordering.elementAt(i));
        }
        return vector;
    }
    
    public Vector getValues() {
        final Vector<Object> vector = new Vector<Object>();
        for (int i = 0; i != this.values.size(); ++i) {
            vector.addElement(this.values.elementAt(i));
        }
        return vector;
    }
    
    public Vector getValues(final DERObjectIdentifier obj) {
        final Vector<String> vector = new Vector<String>();
        for (int i = 0; i != this.values.size(); ++i) {
            if (this.ordering.elementAt(i).equals(obj)) {
                final String obj2 = this.values.elementAt(i);
                if (obj2.length() > 2 && obj2.charAt(0) == '\\' && obj2.charAt(1) == '#') {
                    vector.addElement(obj2.substring(1));
                }
                else {
                    vector.addElement(obj2);
                }
            }
        }
        return vector;
    }
    
    @Override
    public DERObject toASN1Object() {
        if (this.seq == null) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            DERObjectIdentifier derObjectIdentifier = null;
            for (int i = 0; i != this.ordering.size(); ++i) {
                final ASN1EncodableVector asn1EncodableVector3 = new ASN1EncodableVector();
                final DERObjectIdentifier derObjectIdentifier2 = this.ordering.elementAt(i);
                asn1EncodableVector3.add(derObjectIdentifier2);
                asn1EncodableVector3.add(this.converter.getConvertedValue(derObjectIdentifier2, this.values.elementAt(i)));
                if (derObjectIdentifier == null || (boolean)this.added.elementAt(i)) {
                    asn1EncodableVector2.add(new DERSequence(asn1EncodableVector3));
                }
                else {
                    asn1EncodableVector.add(new DERSet(asn1EncodableVector2));
                    asn1EncodableVector2 = new ASN1EncodableVector();
                    asn1EncodableVector2.add(new DERSequence(asn1EncodableVector3));
                }
                derObjectIdentifier = derObjectIdentifier2;
            }
            asn1EncodableVector.add(new DERSet(asn1EncodableVector2));
            this.seq = new DERSequence(asn1EncodableVector);
        }
        return this.seq;
    }
    
    public boolean equals(final Object o, final boolean b) {
        if (!b) {
            return this.equals(o);
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof X509Name) && !(o instanceof ASN1Sequence)) {
            return false;
        }
        if (this.getDERObject().equals(((DEREncodable)o).getDERObject())) {
            return true;
        }
        X509Name instance;
        try {
            instance = getInstance(o);
        }
        catch (IllegalArgumentException ex) {
            return false;
        }
        final int size = this.ordering.size();
        if (size != instance.ordering.size()) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            if (!((DERObjectIdentifier)this.ordering.elementAt(i)).equals(instance.ordering.elementAt(i))) {
                return false;
            }
            if (!this.equivalentStrings((String)this.values.elementAt(i), (String)instance.values.elementAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        if (this.isHashCodeCalculated) {
            return this.hashCodeValue;
        }
        this.isHashCodeCalculated = true;
        for (int i = 0; i != this.ordering.size(); ++i) {
            this.hashCodeValue ^= this.stripInternalSpaces(this.canonicalize((String)this.values.elementAt(i))).hashCode();
        }
        return this.hashCodeValue;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof X509Name) && !(o instanceof ASN1Sequence)) {
            return false;
        }
        if (this.getDERObject().equals(((DEREncodable)o).getDERObject())) {
            return true;
        }
        X509Name instance;
        try {
            instance = getInstance(o);
        }
        catch (IllegalArgumentException ex) {
            return false;
        }
        final int size = this.ordering.size();
        if (size != instance.ordering.size()) {
            return false;
        }
        final boolean[] array = new boolean[size];
        int n;
        int n2;
        int n3;
        if (this.ordering.elementAt(0).equals(instance.ordering.elementAt(0))) {
            n = 0;
            n2 = size;
            n3 = 1;
        }
        else {
            n = size - 1;
            n2 = -1;
            n3 = -1;
        }
        for (int i = n; i != n2; i += n3) {
            boolean b = false;
            final DERObjectIdentifier derObjectIdentifier = this.ordering.elementAt(i);
            final String s = this.values.elementAt(i);
            for (int j = 0; j < size; ++j) {
                if (!array[j]) {
                    if (derObjectIdentifier.equals(instance.ordering.elementAt(j)) && this.equivalentStrings(s, (String)instance.values.elementAt(j))) {
                        array[j] = true;
                        b = true;
                        break;
                    }
                }
            }
            if (!b) {
                return false;
            }
        }
        return true;
    }
    
    private boolean equivalentStrings(final String s, final String s2) {
        final String canonicalize = this.canonicalize(s);
        final String canonicalize2 = this.canonicalize(s2);
        return canonicalize.equals(canonicalize2) || this.stripInternalSpaces(canonicalize).equals(this.stripInternalSpaces(canonicalize2));
    }
    
    private String canonicalize(final String s) {
        String s2 = Strings.toLowerCase(s.trim());
        if (s2.length() > 0 && s2.charAt(0) == '#') {
            final ASN1Object decodeObject = this.decodeObject(s2);
            if (decodeObject instanceof DERString) {
                s2 = Strings.toLowerCase(((DERString)decodeObject).getString().trim());
            }
        }
        return s2;
    }
    
    private ASN1Object decodeObject(final String s) {
        try {
            return ASN1Object.fromByteArray(Hex.decode(s.substring(1)));
        }
        catch (IOException obj) {
            throw new IllegalStateException("unknown encoding in name: " + obj);
        }
    }
    
    private String stripInternalSpaces(final String s) {
        final StringBuffer sb = new StringBuffer();
        if (s.length() != 0) {
            char char1 = s.charAt(0);
            sb.append(char1);
            for (int i = 1; i < s.length(); ++i) {
                final char char2 = s.charAt(i);
                if (char1 != ' ' || char2 != ' ') {
                    sb.append(char2);
                }
                char1 = char2;
            }
        }
        return sb.toString();
    }
    
    private void appendValue(final StringBuffer sb, final Hashtable hashtable, final DERObjectIdentifier key, final String str) {
        final String str2 = hashtable.get(key);
        if (str2 != null) {
            sb.append(str2);
        }
        else {
            sb.append(key.getId());
        }
        sb.append('=');
        int i = sb.length();
        sb.append(str);
        int length = sb.length();
        if (str.length() >= 2 && str.charAt(0) == '\\' && str.charAt(1) == '#') {
            i += 2;
        }
        while (i != length) {
            if (sb.charAt(i) == ',' || sb.charAt(i) == '\"' || sb.charAt(i) == '\\' || sb.charAt(i) == '+' || sb.charAt(i) == '=' || sb.charAt(i) == '<' || sb.charAt(i) == '>' || sb.charAt(i) == ';') {
                sb.insert(i, "\\");
                ++i;
                ++length;
            }
            ++i;
        }
    }
    
    public String toString(final boolean b, final Hashtable hashtable) {
        final StringBuffer sb = new StringBuffer();
        final Vector<StringBuffer> vector = new Vector<StringBuffer>();
        int n = 1;
        StringBuffer obj = null;
        for (int i = 0; i < this.ordering.size(); ++i) {
            if (this.added.elementAt(i)) {
                obj.append('+');
                this.appendValue(obj, hashtable, (DERObjectIdentifier)this.ordering.elementAt(i), (String)this.values.elementAt(i));
            }
            else {
                obj = new StringBuffer();
                this.appendValue(obj, hashtable, (DERObjectIdentifier)this.ordering.elementAt(i), (String)this.values.elementAt(i));
                vector.addElement(obj);
            }
        }
        if (b) {
            for (int j = vector.size() - 1; j >= 0; --j) {
                if (n != 0) {
                    n = 0;
                }
                else {
                    sb.append(',');
                }
                sb.append(vector.elementAt(j).toString());
            }
        }
        else {
            for (int k = 0; k < vector.size(); ++k) {
                if (n != 0) {
                    n = 0;
                }
                else {
                    sb.append(',');
                }
                sb.append(vector.elementAt(k).toString());
            }
        }
        return sb.toString();
    }
    
    private String bytesToString(final byte[] array) {
        final char[] value = new char[array.length];
        for (int i = 0; i != value.length; ++i) {
            value[i] = (char)(array[i] & 0xFF);
        }
        return new String(value);
    }
    
    @Override
    public String toString() {
        return this.toString(X509Name.DefaultReverse, X509Name.DefaultSymbols);
    }
    
    static {
        C = new DERObjectIdentifier("2.5.4.6");
        O = new DERObjectIdentifier("2.5.4.10");
        OU = new DERObjectIdentifier("2.5.4.11");
        T = new DERObjectIdentifier("2.5.4.12");
        CN = new DERObjectIdentifier("2.5.4.3");
        SN = new DERObjectIdentifier("2.5.4.5");
        STREET = new DERObjectIdentifier("2.5.4.9");
        SERIALNUMBER = X509Name.SN;
        L = new DERObjectIdentifier("2.5.4.7");
        ST = new DERObjectIdentifier("2.5.4.8");
        SURNAME = new DERObjectIdentifier("2.5.4.4");
        GIVENNAME = new DERObjectIdentifier("2.5.4.42");
        INITIALS = new DERObjectIdentifier("2.5.4.43");
        GENERATION = new DERObjectIdentifier("2.5.4.44");
        UNIQUE_IDENTIFIER = new DERObjectIdentifier("2.5.4.45");
        BUSINESS_CATEGORY = new DERObjectIdentifier("2.5.4.15");
        POSTAL_CODE = new DERObjectIdentifier("2.5.4.17");
        DN_QUALIFIER = new DERObjectIdentifier("2.5.4.46");
        PSEUDONYM = new DERObjectIdentifier("2.5.4.65");
        DATE_OF_BIRTH = new DERObjectIdentifier("1.3.6.1.5.5.7.9.1");
        PLACE_OF_BIRTH = new DERObjectIdentifier("1.3.6.1.5.5.7.9.2");
        GENDER = new DERObjectIdentifier("1.3.6.1.5.5.7.9.3");
        COUNTRY_OF_CITIZENSHIP = new DERObjectIdentifier("1.3.6.1.5.5.7.9.4");
        COUNTRY_OF_RESIDENCE = new DERObjectIdentifier("1.3.6.1.5.5.7.9.5");
        NAME_AT_BIRTH = new DERObjectIdentifier("1.3.36.8.3.14");
        POSTAL_ADDRESS = new DERObjectIdentifier("2.5.4.16");
        DMD_NAME = new DERObjectIdentifier("2.5.4.54");
        TELEPHONE_NUMBER = X509ObjectIdentifiers.id_at_telephoneNumber;
        NAME = X509ObjectIdentifiers.id_at_name;
        EmailAddress = PKCSObjectIdentifiers.pkcs_9_at_emailAddress;
        UnstructuredName = PKCSObjectIdentifiers.pkcs_9_at_unstructuredName;
        UnstructuredAddress = PKCSObjectIdentifiers.pkcs_9_at_unstructuredAddress;
        E = X509Name.EmailAddress;
        DC = new DERObjectIdentifier("0.9.2342.19200300.100.1.25");
        UID = new DERObjectIdentifier("0.9.2342.19200300.100.1.1");
        X509Name.DefaultReverse = false;
        DefaultSymbols = new Hashtable();
        RFC2253Symbols = new Hashtable();
        RFC1779Symbols = new Hashtable();
        DefaultLookUp = new Hashtable();
        OIDLookUp = X509Name.DefaultSymbols;
        SymbolLookUp = X509Name.DefaultLookUp;
        TRUE = new Boolean(true);
        FALSE = new Boolean(false);
        X509Name.DefaultSymbols.put(X509Name.C, "C");
        X509Name.DefaultSymbols.put(X509Name.O, "O");
        X509Name.DefaultSymbols.put(X509Name.T, "T");
        X509Name.DefaultSymbols.put(X509Name.OU, "OU");
        X509Name.DefaultSymbols.put(X509Name.CN, "CN");
        X509Name.DefaultSymbols.put(X509Name.L, "L");
        X509Name.DefaultSymbols.put(X509Name.ST, "ST");
        X509Name.DefaultSymbols.put(X509Name.SN, "SERIALNUMBER");
        X509Name.DefaultSymbols.put(X509Name.EmailAddress, "E");
        X509Name.DefaultSymbols.put(X509Name.DC, "DC");
        X509Name.DefaultSymbols.put(X509Name.UID, "UID");
        X509Name.DefaultSymbols.put(X509Name.STREET, "STREET");
        X509Name.DefaultSymbols.put(X509Name.SURNAME, "SURNAME");
        X509Name.DefaultSymbols.put(X509Name.GIVENNAME, "GIVENNAME");
        X509Name.DefaultSymbols.put(X509Name.INITIALS, "INITIALS");
        X509Name.DefaultSymbols.put(X509Name.GENERATION, "GENERATION");
        X509Name.DefaultSymbols.put(X509Name.UnstructuredAddress, "unstructuredAddress");
        X509Name.DefaultSymbols.put(X509Name.UnstructuredName, "unstructuredName");
        X509Name.DefaultSymbols.put(X509Name.UNIQUE_IDENTIFIER, "UniqueIdentifier");
        X509Name.DefaultSymbols.put(X509Name.DN_QUALIFIER, "DN");
        X509Name.DefaultSymbols.put(X509Name.PSEUDONYM, "Pseudonym");
        X509Name.DefaultSymbols.put(X509Name.POSTAL_ADDRESS, "PostalAddress");
        X509Name.DefaultSymbols.put(X509Name.NAME_AT_BIRTH, "NameAtBirth");
        X509Name.DefaultSymbols.put(X509Name.COUNTRY_OF_CITIZENSHIP, "CountryOfCitizenship");
        X509Name.DefaultSymbols.put(X509Name.COUNTRY_OF_RESIDENCE, "CountryOfResidence");
        X509Name.DefaultSymbols.put(X509Name.GENDER, "Gender");
        X509Name.DefaultSymbols.put(X509Name.PLACE_OF_BIRTH, "PlaceOfBirth");
        X509Name.DefaultSymbols.put(X509Name.DATE_OF_BIRTH, "DateOfBirth");
        X509Name.DefaultSymbols.put(X509Name.POSTAL_CODE, "PostalCode");
        X509Name.DefaultSymbols.put(X509Name.BUSINESS_CATEGORY, "BusinessCategory");
        X509Name.DefaultSymbols.put(X509Name.TELEPHONE_NUMBER, "TelephoneNumber");
        X509Name.DefaultSymbols.put(X509Name.NAME, "Name");
        X509Name.RFC2253Symbols.put(X509Name.C, "C");
        X509Name.RFC2253Symbols.put(X509Name.O, "O");
        X509Name.RFC2253Symbols.put(X509Name.OU, "OU");
        X509Name.RFC2253Symbols.put(X509Name.CN, "CN");
        X509Name.RFC2253Symbols.put(X509Name.L, "L");
        X509Name.RFC2253Symbols.put(X509Name.ST, "ST");
        X509Name.RFC2253Symbols.put(X509Name.STREET, "STREET");
        X509Name.RFC2253Symbols.put(X509Name.DC, "DC");
        X509Name.RFC2253Symbols.put(X509Name.UID, "UID");
        X509Name.RFC1779Symbols.put(X509Name.C, "C");
        X509Name.RFC1779Symbols.put(X509Name.O, "O");
        X509Name.RFC1779Symbols.put(X509Name.OU, "OU");
        X509Name.RFC1779Symbols.put(X509Name.CN, "CN");
        X509Name.RFC1779Symbols.put(X509Name.L, "L");
        X509Name.RFC1779Symbols.put(X509Name.ST, "ST");
        X509Name.RFC1779Symbols.put(X509Name.STREET, "STREET");
        X509Name.DefaultLookUp.put("c", X509Name.C);
        X509Name.DefaultLookUp.put("o", X509Name.O);
        X509Name.DefaultLookUp.put("t", X509Name.T);
        X509Name.DefaultLookUp.put("ou", X509Name.OU);
        X509Name.DefaultLookUp.put("cn", X509Name.CN);
        X509Name.DefaultLookUp.put("l", X509Name.L);
        X509Name.DefaultLookUp.put("st", X509Name.ST);
        X509Name.DefaultLookUp.put("sn", X509Name.SN);
        X509Name.DefaultLookUp.put("serialnumber", X509Name.SN);
        X509Name.DefaultLookUp.put("street", X509Name.STREET);
        X509Name.DefaultLookUp.put("emailaddress", X509Name.E);
        X509Name.DefaultLookUp.put("dc", X509Name.DC);
        X509Name.DefaultLookUp.put("e", X509Name.E);
        X509Name.DefaultLookUp.put("uid", X509Name.UID);
        X509Name.DefaultLookUp.put("surname", X509Name.SURNAME);
        X509Name.DefaultLookUp.put("givenname", X509Name.GIVENNAME);
        X509Name.DefaultLookUp.put("initials", X509Name.INITIALS);
        X509Name.DefaultLookUp.put("generation", X509Name.GENERATION);
        X509Name.DefaultLookUp.put("unstructuredaddress", X509Name.UnstructuredAddress);
        X509Name.DefaultLookUp.put("unstructuredname", X509Name.UnstructuredName);
        X509Name.DefaultLookUp.put("uniqueidentifier", X509Name.UNIQUE_IDENTIFIER);
        X509Name.DefaultLookUp.put("dn", X509Name.DN_QUALIFIER);
        X509Name.DefaultLookUp.put("pseudonym", X509Name.PSEUDONYM);
        X509Name.DefaultLookUp.put("postaladdress", X509Name.POSTAL_ADDRESS);
        X509Name.DefaultLookUp.put("nameofbirth", X509Name.NAME_AT_BIRTH);
        X509Name.DefaultLookUp.put("countryofcitizenship", X509Name.COUNTRY_OF_CITIZENSHIP);
        X509Name.DefaultLookUp.put("countryofresidence", X509Name.COUNTRY_OF_RESIDENCE);
        X509Name.DefaultLookUp.put("gender", X509Name.GENDER);
        X509Name.DefaultLookUp.put("placeofbirth", X509Name.PLACE_OF_BIRTH);
        X509Name.DefaultLookUp.put("dateofbirth", X509Name.DATE_OF_BIRTH);
        X509Name.DefaultLookUp.put("postalcode", X509Name.POSTAL_CODE);
        X509Name.DefaultLookUp.put("businesscategory", X509Name.BUSINESS_CATEGORY);
        X509Name.DefaultLookUp.put("telephonenumber", X509Name.TELEPHONE_NUMBER);
        X509Name.DefaultLookUp.put("name", X509Name.NAME);
    }
}
