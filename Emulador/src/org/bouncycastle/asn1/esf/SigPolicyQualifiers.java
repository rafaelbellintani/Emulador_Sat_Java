// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class SigPolicyQualifiers extends ASN1Encodable
{
    ASN1Sequence qualifiers;
    
    public static SigPolicyQualifiers getInstance(final Object o) {
        if (o instanceof SigPolicyQualifiers) {
            return (SigPolicyQualifiers)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SigPolicyQualifiers((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in 'SigPolicyQualifiers' factory: " + o.getClass().getName() + ".");
    }
    
    public SigPolicyQualifiers(final ASN1Sequence qualifiers) {
        this.qualifiers = qualifiers;
    }
    
    public SigPolicyQualifiers(final SigPolicyQualifierInfo[] array) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i < array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.qualifiers = new DERSequence(asn1EncodableVector);
    }
    
    public int size() {
        return this.qualifiers.size();
    }
    
    public SigPolicyQualifierInfo getStringAt(final int n) {
        return SigPolicyQualifierInfo.getInstance(this.qualifiers.getObjectAt(n));
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.qualifiers;
    }
}
