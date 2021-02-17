// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.isismtt.x509;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class DeclarationOfMajority extends ASN1Encodable implements ASN1Choice
{
    public static final int notYoungerThan = 0;
    public static final int fullAgeAtCountry = 1;
    public static final int dateOfBirth = 2;
    private ASN1TaggedObject declaration;
    
    public DeclarationOfMajority(final int n) {
        this.declaration = new DERTaggedObject(false, 0, new DERInteger(n));
    }
    
    public DeclarationOfMajority(final boolean b, final String s) {
        if (s.length() > 2) {
            throw new IllegalArgumentException("country can only be 2 characters");
        }
        if (b) {
            this.declaration = new DERTaggedObject(false, 1, new DERSequence(new DERPrintableString(s, true)));
        }
        else {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            asn1EncodableVector.add(DERBoolean.FALSE);
            asn1EncodableVector.add(new DERPrintableString(s, true));
            this.declaration = new DERTaggedObject(false, 1, new DERSequence(asn1EncodableVector));
        }
    }
    
    public DeclarationOfMajority(final DERGeneralizedTime derGeneralizedTime) {
        this.declaration = new DERTaggedObject(false, 2, derGeneralizedTime);
    }
    
    public static DeclarationOfMajority getInstance(final Object o) {
        if (o == null || o instanceof DeclarationOfMajority) {
            return (DeclarationOfMajority)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new DeclarationOfMajority((ASN1TaggedObject)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    private DeclarationOfMajority(final ASN1TaggedObject declaration) {
        if (declaration.getTagNo() > 2) {
            throw new IllegalArgumentException("Bad tag number: " + declaration.getTagNo());
        }
        this.declaration = declaration;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.declaration;
    }
    
    public int getType() {
        return this.declaration.getTagNo();
    }
    
    public int notYoungerThan() {
        if (this.declaration.getTagNo() != 0) {
            return -1;
        }
        return DERInteger.getInstance(this.declaration, false).getValue().intValue();
    }
    
    public ASN1Sequence fullAgeAtCountry() {
        if (this.declaration.getTagNo() != 1) {
            return null;
        }
        return ASN1Sequence.getInstance(this.declaration, false);
    }
    
    public DERGeneralizedTime getDateOfBirth() {
        if (this.declaration.getTagNo() != 2) {
            return null;
        }
        return DERGeneralizedTime.getInstance(this.declaration, false);
    }
}
