// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509.sigi;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.DirectoryString;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class NameOrPseudonym extends ASN1Encodable implements ASN1Choice
{
    private DirectoryString pseudonym;
    private DirectoryString surname;
    private ASN1Sequence givenName;
    
    public static NameOrPseudonym getInstance(final Object o) {
        if (o == null || o instanceof NameOrPseudonym) {
            return (NameOrPseudonym)o;
        }
        if (o instanceof DERString) {
            return new NameOrPseudonym(DirectoryString.getInstance(o));
        }
        if (o instanceof ASN1Sequence) {
            return new NameOrPseudonym((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public NameOrPseudonym(final DirectoryString pseudonym) {
        this.pseudonym = pseudonym;
    }
    
    private NameOrPseudonym(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        if (!(asn1Sequence.getObjectAt(0) instanceof DERString)) {
            throw new IllegalArgumentException("Bad object encountered: " + asn1Sequence.getObjectAt(0).getClass());
        }
        this.surname = DirectoryString.getInstance(asn1Sequence.getObjectAt(0));
        this.givenName = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public NameOrPseudonym(final String s) {
        this(new DirectoryString(s));
    }
    
    public NameOrPseudonym(final DirectoryString surname, final ASN1Sequence givenName) {
        this.surname = surname;
        this.givenName = givenName;
    }
    
    public DirectoryString getPseudonym() {
        return this.pseudonym;
    }
    
    public DirectoryString getSurname() {
        return this.surname;
    }
    
    public DirectoryString[] getGivenName() {
        final DirectoryString[] array = new DirectoryString[this.givenName.size()];
        int n = 0;
        final Enumeration objects = this.givenName.getObjects();
        while (objects.hasMoreElements()) {
            array[n++] = DirectoryString.getInstance(objects.nextElement());
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        if (this.pseudonym != null) {
            return this.pseudonym.toASN1Object();
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.surname);
        asn1EncodableVector.add(this.givenName);
        return new DERSequence(asn1EncodableVector);
    }
}
