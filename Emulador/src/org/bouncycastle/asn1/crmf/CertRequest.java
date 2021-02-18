// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertRequest extends ASN1Encodable
{
    private DERInteger certReqId;
    private CertTemplate certTemplate;
    private Controls controls;
    
    private CertRequest(final ASN1Sequence asn1Sequence) {
        this.certReqId = DERInteger.getInstance(asn1Sequence.getObjectAt(0));
        this.certTemplate = CertTemplate.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() > 2) {
            this.controls = Controls.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public static CertRequest getInstance(final Object o) {
        if (o instanceof CertRequest) {
            return (CertRequest)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertRequest((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERInteger getCertReqId() {
        return this.certReqId;
    }
    
    public CertTemplate getCertTemplate() {
        return this.certTemplate;
    }
    
    public Controls getControls() {
        return this.controls;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certReqId);
        asn1EncodableVector.add(this.certTemplate);
        if (this.controls != null) {
            asn1EncodableVector.add(this.controls);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
