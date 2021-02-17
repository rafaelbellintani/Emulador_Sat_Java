// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class OtherSigningCertificate extends ASN1Encodable
{
    ASN1Sequence certs;
    ASN1Sequence policies;
    
    public static OtherSigningCertificate getInstance(final Object o) {
        if (o == null || o instanceof OtherSigningCertificate) {
            return (OtherSigningCertificate)o;
        }
        if (o instanceof ASN1Sequence) {
            return new OtherSigningCertificate((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in 'OtherSigningCertificate' factory : " + o.getClass().getName() + ".");
    }
    
    public OtherSigningCertificate(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 1 || asn1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.certs = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.policies = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
        }
    }
    
    public OtherSigningCertificate(final OtherCertID otherCertID) {
        this.certs = new DERSequence(otherCertID);
    }
    
    public OtherCertID[] getCerts() {
        final OtherCertID[] array = new OtherCertID[this.certs.size()];
        for (int i = 0; i != this.certs.size(); ++i) {
            array[i] = OtherCertID.getInstance(this.certs.getObjectAt(i));
        }
        return array;
    }
    
    public PolicyInformation[] getPolicies() {
        if (this.policies == null) {
            return null;
        }
        final PolicyInformation[] array = new PolicyInformation[this.policies.size()];
        for (int i = 0; i != this.policies.size(); ++i) {
            array[i] = PolicyInformation.getInstance(this.policies.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certs);
        if (this.policies != null) {
            asn1EncodableVector.add(this.policies);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
