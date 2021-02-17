// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class SigPolicyQualifierInfo extends ASN1Encodable
{
    private DERObjectIdentifier sigPolicyQualifierId;
    private DEREncodable sigQualifier;
    
    public SigPolicyQualifierInfo(final DERObjectIdentifier sigPolicyQualifierId, final DEREncodable sigQualifier) {
        this.sigPolicyQualifierId = sigPolicyQualifierId;
        this.sigQualifier = sigQualifier;
    }
    
    public SigPolicyQualifierInfo(final ASN1Sequence asn1Sequence) {
        this.sigPolicyQualifierId = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.sigQualifier = asn1Sequence.getObjectAt(1);
    }
    
    public static SigPolicyQualifierInfo getInstance(final Object o) {
        if (o == null || o instanceof SigPolicyQualifierInfo) {
            return (SigPolicyQualifierInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SigPolicyQualifierInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in 'SigPolicyQualifierInfo' factory: " + o.getClass().getName() + ".");
    }
    
    public DERObjectIdentifier getSigPolicyQualifierId() {
        return this.sigPolicyQualifierId;
    }
    
    public DEREncodable getSigQualifier() {
        return this.sigQualifier;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.sigPolicyQualifierId);
        asn1EncodableVector.add(this.sigQualifier);
        return new DERSequence(asn1EncodableVector);
    }
}
