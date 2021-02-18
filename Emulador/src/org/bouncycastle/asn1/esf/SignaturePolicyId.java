// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class SignaturePolicyId extends ASN1Encodable
{
    private DERObjectIdentifier sigPolicyId;
    private OtherHashAlgAndValue sigPolicyHash;
    private SigPolicyQualifiers sigPolicyQualifiers;
    
    public static SignaturePolicyId getInstance(final Object o) {
        if (o == null || o instanceof SignaturePolicyId) {
            return (SignaturePolicyId)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SignaturePolicyId((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Unknown object in 'SignaturePolicyId' factory : " + o.getClass().getName() + ".");
    }
    
    public SignaturePolicyId(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2 && asn1Sequence.size() != 3) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.sigPolicyId = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.sigPolicyHash = OtherHashAlgAndValue.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() == 3) {
            this.sigPolicyQualifiers = SigPolicyQualifiers.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public SignaturePolicyId(final DERObjectIdentifier derObjectIdentifier, final OtherHashAlgAndValue otherHashAlgAndValue) {
        this(derObjectIdentifier, otherHashAlgAndValue, null);
    }
    
    public SignaturePolicyId(final DERObjectIdentifier sigPolicyId, final OtherHashAlgAndValue sigPolicyHash, final SigPolicyQualifiers sigPolicyQualifiers) {
        this.sigPolicyId = sigPolicyId;
        this.sigPolicyHash = sigPolicyHash;
        this.sigPolicyQualifiers = sigPolicyQualifiers;
    }
    
    public DERObjectIdentifier getSigPolicyId() {
        return this.sigPolicyId;
    }
    
    public OtherHashAlgAndValue getSigPolicyHash() {
        return this.sigPolicyHash;
    }
    
    public SigPolicyQualifiers getSigPolicyQualifiers() {
        return this.sigPolicyQualifiers;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.sigPolicyId);
        asn1EncodableVector.add(this.sigPolicyHash);
        if (this.sigPolicyQualifiers != null) {
            asn1EncodableVector.add(this.sigPolicyQualifiers);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
