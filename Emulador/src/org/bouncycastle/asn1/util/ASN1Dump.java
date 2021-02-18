// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.util;

import org.bouncycastle.asn1.DEREncodable;
import java.io.IOException;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERExternal;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.BERApplicationSpecific;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.asn1.DERUnknownTag;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.DERT61String;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DERVisibleString;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.BERConstructedOctetString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERConstructedSet;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERConstructedSequence;
import org.bouncycastle.asn1.BERConstructedSequence;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;

public class ASN1Dump
{
    private static final String TAB = "    ";
    private static final int SAMPLE_SIZE = 32;
    
    static void _dumpAsString(final String str, final boolean b, final DERObject derObject, final StringBuffer sb) {
        final String property = System.getProperty("line.separator");
        if (derObject instanceof ASN1Sequence) {
            final Enumeration objects = ((ASN1Sequence)derObject).getObjects();
            final String string = str + "    ";
            sb.append(str);
            if (derObject instanceof BERConstructedSequence) {
                sb.append("BER ConstructedSequence");
            }
            else if (derObject instanceof DERConstructedSequence) {
                sb.append("DER ConstructedSequence");
            }
            else if (derObject instanceof BERSequence) {
                sb.append("BER Sequence");
            }
            else if (derObject instanceof DERSequence) {
                sb.append("DER Sequence");
            }
            else {
                sb.append("Sequence");
            }
            sb.append(property);
            while (objects.hasMoreElements()) {
                final DERObject nextElement = objects.nextElement();
                if (nextElement == null || nextElement.equals(new DERNull())) {
                    sb.append(string);
                    sb.append("NULL");
                    sb.append(property);
                }
                else if (nextElement instanceof DERObject) {
                    _dumpAsString(string, b, nextElement, sb);
                }
                else {
                    _dumpAsString(string, b, nextElement.getDERObject(), sb);
                }
            }
        }
        else if (derObject instanceof DERTaggedObject) {
            final String string2 = str + "    ";
            sb.append(str);
            if (derObject instanceof BERTaggedObject) {
                sb.append("BER Tagged [");
            }
            else {
                sb.append("Tagged [");
            }
            final DERTaggedObject derTaggedObject = (DERTaggedObject)derObject;
            sb.append(Integer.toString(derTaggedObject.getTagNo()));
            sb.append(']');
            if (!derTaggedObject.isExplicit()) {
                sb.append(" IMPLICIT ");
            }
            sb.append(property);
            if (derTaggedObject.isEmpty()) {
                sb.append(string2);
                sb.append("EMPTY");
                sb.append(property);
            }
            else {
                _dumpAsString(string2, b, derTaggedObject.getObject(), sb);
            }
        }
        else if (derObject instanceof DERConstructedSet) {
            final Enumeration objects2 = ((ASN1Set)derObject).getObjects();
            final String string3 = str + "    ";
            sb.append(str);
            sb.append("ConstructedSet");
            sb.append(property);
            while (objects2.hasMoreElements()) {
                final DERObject nextElement2 = objects2.nextElement();
                if (nextElement2 == null) {
                    sb.append(string3);
                    sb.append("NULL");
                    sb.append(property);
                }
                else if (nextElement2 instanceof DERObject) {
                    _dumpAsString(string3, b, nextElement2, sb);
                }
                else {
                    _dumpAsString(string3, b, nextElement2.getDERObject(), sb);
                }
            }
        }
        else if (derObject instanceof BERSet) {
            final Enumeration objects3 = ((ASN1Set)derObject).getObjects();
            final String string4 = str + "    ";
            sb.append(str);
            sb.append("BER Set");
            sb.append(property);
            while (objects3.hasMoreElements()) {
                final DERObject nextElement3 = objects3.nextElement();
                if (nextElement3 == null) {
                    sb.append(string4);
                    sb.append("NULL");
                    sb.append(property);
                }
                else if (nextElement3 instanceof DERObject) {
                    _dumpAsString(string4, b, nextElement3, sb);
                }
                else {
                    _dumpAsString(string4, b, nextElement3.getDERObject(), sb);
                }
            }
        }
        else if (derObject instanceof DERSet) {
            final Enumeration objects4 = ((ASN1Set)derObject).getObjects();
            final String string5 = str + "    ";
            sb.append(str);
            sb.append("DER Set");
            sb.append(property);
            while (objects4.hasMoreElements()) {
                final DERObject nextElement4 = objects4.nextElement();
                if (nextElement4 == null) {
                    sb.append(string5);
                    sb.append("NULL");
                    sb.append(property);
                }
                else if (nextElement4 instanceof DERObject) {
                    _dumpAsString(string5, b, nextElement4, sb);
                }
                else {
                    _dumpAsString(string5, b, nextElement4.getDERObject(), sb);
                }
            }
        }
        else if (derObject instanceof DERObjectIdentifier) {
            sb.append(str + "ObjectIdentifier(" + ((DERObjectIdentifier)derObject).getId() + ")" + property);
        }
        else if (derObject instanceof DERBoolean) {
            sb.append(str + "Boolean(" + ((DERBoolean)derObject).isTrue() + ")" + property);
        }
        else if (derObject instanceof DERInteger) {
            sb.append(str + "Integer(" + ((DERInteger)derObject).getValue() + ")" + property);
        }
        else if (derObject instanceof BERConstructedOctetString) {
            final ASN1OctetString asn1OctetString = (ASN1OctetString)derObject;
            sb.append(str + "BER Constructed Octet String" + "[" + asn1OctetString.getOctets().length + "] ");
            if (b) {
                sb.append(dumpBinaryDataAsString(str, asn1OctetString.getOctets()));
            }
            else {
                sb.append(property);
            }
        }
        else if (derObject instanceof DEROctetString) {
            final ASN1OctetString asn1OctetString2 = (ASN1OctetString)derObject;
            sb.append(str + "DER Octet String" + "[" + asn1OctetString2.getOctets().length + "] ");
            if (b) {
                sb.append(dumpBinaryDataAsString(str, asn1OctetString2.getOctets()));
            }
            else {
                sb.append(property);
            }
        }
        else if (derObject instanceof DERBitString) {
            final DERBitString derBitString = (DERBitString)derObject;
            sb.append(str + "DER Bit String" + "[" + derBitString.getBytes().length + ", " + derBitString.getPadBits() + "] ");
            if (b) {
                sb.append(dumpBinaryDataAsString(str, derBitString.getBytes()));
            }
            else {
                sb.append(property);
            }
        }
        else if (derObject instanceof DERIA5String) {
            sb.append(str + "IA5String(" + ((DERIA5String)derObject).getString() + ") " + property);
        }
        else if (derObject instanceof DERUTF8String) {
            sb.append(str + "UTF8String(" + ((DERUTF8String)derObject).getString() + ") " + property);
        }
        else if (derObject instanceof DERPrintableString) {
            sb.append(str + "PrintableString(" + ((DERPrintableString)derObject).getString() + ") " + property);
        }
        else if (derObject instanceof DERVisibleString) {
            sb.append(str + "VisibleString(" + ((DERVisibleString)derObject).getString() + ") " + property);
        }
        else if (derObject instanceof DERBMPString) {
            sb.append(str + "BMPString(" + ((DERBMPString)derObject).getString() + ") " + property);
        }
        else if (derObject instanceof DERT61String) {
            sb.append(str + "T61String(" + ((DERT61String)derObject).getString() + ") " + property);
        }
        else if (derObject instanceof DERUTCTime) {
            sb.append(str + "UTCTime(" + ((DERUTCTime)derObject).getTime() + ") " + property);
        }
        else if (derObject instanceof DERGeneralizedTime) {
            sb.append(str + "GeneralizedTime(" + ((DERGeneralizedTime)derObject).getTime() + ") " + property);
        }
        else if (derObject instanceof DERUnknownTag) {
            sb.append(str + "Unknown " + Integer.toString(((DERUnknownTag)derObject).getTag(), 16) + " " + new String(Hex.encode(((DERUnknownTag)derObject).getData())) + property);
        }
        else if (derObject instanceof BERApplicationSpecific) {
            sb.append(outputApplicationSpecific("BER", str, b, derObject, property));
        }
        else if (derObject instanceof DERApplicationSpecific) {
            sb.append(outputApplicationSpecific("DER", str, b, derObject, property));
        }
        else if (derObject instanceof DEREnumerated) {
            sb.append(str + "DER Enumerated(" + ((DEREnumerated)derObject).getValue() + ")" + property);
        }
        else if (derObject instanceof DERExternal) {
            final DERExternal derExternal = (DERExternal)derObject;
            sb.append(str + "External " + property);
            final String string6 = str + "    ";
            if (derExternal.getDirectReference() != null) {
                sb.append(string6 + "Direct Reference: " + derExternal.getDirectReference().getId() + property);
            }
            if (derExternal.getIndirectReference() != null) {
                sb.append(string6 + "Indirect Reference: " + derExternal.getIndirectReference().toString() + property);
            }
            if (derExternal.getDataValueDescriptor() != null) {
                _dumpAsString(string6, b, derExternal.getDataValueDescriptor(), sb);
            }
            sb.append(string6 + "Encoding: " + derExternal.getEncoding() + property);
            _dumpAsString(string6, b, derExternal.getExternalContent(), sb);
        }
        else {
            sb.append(str + derObject.toString() + property);
        }
    }
    
    private static String outputApplicationSpecific(final String s, final String str, final boolean b, final DERObject derObject, final String s2) {
        final DERApplicationSpecific derApplicationSpecific = (DERApplicationSpecific)derObject;
        final StringBuffer sb = new StringBuffer();
        if (derApplicationSpecific.isConstructed()) {
            try {
                final ASN1Sequence instance = ASN1Sequence.getInstance(derApplicationSpecific.getObject(16));
                sb.append(str + s + " ApplicationSpecific[" + derApplicationSpecific.getApplicationTag() + "]" + s2);
                final Enumeration objects = instance.getObjects();
                while (objects.hasMoreElements()) {
                    _dumpAsString(str + "    ", b, objects.nextElement(), sb);
                }
            }
            catch (IOException obj) {
                sb.append(obj);
            }
            return sb.toString();
        }
        return str + s + " ApplicationSpecific[" + derApplicationSpecific.getApplicationTag() + "] (" + new String(Hex.encode(derApplicationSpecific.getContents())) + ")" + s2;
    }
    
    public static String dumpAsString(final Object o) {
        return dumpAsString(o, false);
    }
    
    public static String dumpAsString(final Object o, final boolean b) {
        final StringBuffer sb = new StringBuffer();
        if (o instanceof DERObject) {
            _dumpAsString("", b, (DERObject)o, sb);
        }
        else {
            if (!(o instanceof DEREncodable)) {
                return "unknown object type " + o.toString();
            }
            _dumpAsString("", b, ((DEREncodable)o).getDERObject(), sb);
        }
        return sb.toString();
    }
    
    private static String dumpBinaryDataAsString(String string, final byte[] array) {
        final String property = System.getProperty("line.separator");
        final StringBuffer sb = new StringBuffer();
        string += "    ";
        sb.append(property);
        for (int i = 0; i < array.length; i += 32) {
            if (array.length - i > 32) {
                sb.append(string);
                sb.append(new String(Hex.encode(array, i, 32)));
                sb.append("    ");
                sb.append(calculateAscString(array, i, 32));
                sb.append(property);
            }
            else {
                sb.append(string);
                sb.append(new String(Hex.encode(array, i, array.length - i)));
                for (int j = array.length - i; j != 32; ++j) {
                    sb.append("  ");
                }
                sb.append("    ");
                sb.append(calculateAscString(array, i, array.length - i));
                sb.append(property);
            }
        }
        return sb.toString();
    }
    
    private static String calculateAscString(final byte[] array, final int n, final int n2) {
        final StringBuffer sb = new StringBuffer();
        for (int i = n; i != n + n2; ++i) {
            if (array[i] >= 32 && array[i] <= 126) {
                sb.append((char)array[i]);
            }
        }
        return sb.toString();
    }
}
