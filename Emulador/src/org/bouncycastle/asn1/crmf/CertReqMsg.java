// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertReqMsg extends ASN1Encodable
{
    private CertRequest certReq;
    private ProofOfPossession pop;
    private ASN1Sequence regInfo;
    
    private CertReqMsg(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.certReq = CertRequest.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final Object nextElement = objects.nextElement();
            if (nextElement instanceof ASN1TaggedObject) {
                this.pop = ProofOfPossession.getInstance(nextElement);
            }
            else {
                this.regInfo = ASN1Sequence.getInstance(nextElement);
            }
        }
    }
    
    public static CertReqMsg getInstance(final Object o) {
        if (o instanceof CertReqMsg) {
            return (CertReqMsg)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertReqMsg((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public CertRequest getCertReq() {
        return this.certReq;
    }
    
    public ProofOfPossession getPop() {
        return this.pop;
    }
    
    public AttributeTypeAndValue[] getRegInfo() {
        if (this.regInfo == null) {
            return null;
        }
        final AttributeTypeAndValue[] array = new AttributeTypeAndValue[this.regInfo.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = AttributeTypeAndValue.getInstance(this.regInfo.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certReq);
        this.addOptional(asn1EncodableVector, this.pop);
        this.addOptional(asn1EncodableVector, this.regInfo);
        return new DERSequence(asn1EncodableVector);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(asn1Encodable);
        }
    }
}
