// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class PolicyQualifierInfo extends ASN1Encodable
{
    private DERObjectIdentifier policyQualifierId;
    private DEREncodable qualifier;
    
    public PolicyQualifierInfo(final DERObjectIdentifier policyQualifierId, final DEREncodable qualifier) {
        this.policyQualifierId = policyQualifierId;
        this.qualifier = qualifier;
    }
    
    public PolicyQualifierInfo(final String s) {
        this.policyQualifierId = PolicyQualifierId.id_qt_cps;
        this.qualifier = new DERIA5String(s);
    }
    
    public PolicyQualifierInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.policyQualifierId = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.qualifier = asn1Sequence.getObjectAt(1);
    }
    
    public static PolicyQualifierInfo getInstance(final Object o) {
        if (o instanceof PolicyQualifierInfo) {
            return (PolicyQualifierInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PolicyQualifierInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in getInstance.");
    }
    
    public DERObjectIdentifier getPolicyQualifierId() {
        return this.policyQualifierId;
    }
    
    public DEREncodable getQualifier() {
        return this.qualifier;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.policyQualifierId);
        asn1EncodableVector.add(this.qualifier);
        return new DERSequence(asn1EncodableVector);
    }
}
