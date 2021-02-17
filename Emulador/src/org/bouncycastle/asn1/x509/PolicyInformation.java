// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class PolicyInformation extends ASN1Encodable
{
    private DERObjectIdentifier policyIdentifier;
    private ASN1Sequence policyQualifiers;
    
    public PolicyInformation(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 1 || asn1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.policyIdentifier = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.policyQualifiers = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
        }
    }
    
    public PolicyInformation(final DERObjectIdentifier policyIdentifier) {
        this.policyIdentifier = policyIdentifier;
    }
    
    public PolicyInformation(final DERObjectIdentifier policyIdentifier, final ASN1Sequence policyQualifiers) {
        this.policyIdentifier = policyIdentifier;
        this.policyQualifiers = policyQualifiers;
    }
    
    public static PolicyInformation getInstance(final Object o) {
        if (o == null || o instanceof PolicyInformation) {
            return (PolicyInformation)o;
        }
        return new PolicyInformation(ASN1Sequence.getInstance(o));
    }
    
    public DERObjectIdentifier getPolicyIdentifier() {
        return this.policyIdentifier;
    }
    
    public ASN1Sequence getPolicyQualifiers() {
        return this.policyQualifiers;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.policyIdentifier);
        if (this.policyQualifiers != null) {
            asn1EncodableVector.add(this.policyQualifiers);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
