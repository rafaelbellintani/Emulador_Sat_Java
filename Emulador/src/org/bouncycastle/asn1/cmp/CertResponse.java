// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertResponse extends ASN1Encodable
{
    private DERInteger certReqId;
    private PKIStatusInfo status;
    private CertifiedKeyPair certifiedKeyPair;
    private ASN1OctetString rspInfo;
    
    private CertResponse(final ASN1Sequence asn1Sequence) {
        this.certReqId = DERInteger.getInstance(asn1Sequence.getObjectAt(0));
        this.status = PKIStatusInfo.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() >= 3) {
            if (asn1Sequence.size() == 3) {
                final DEREncodable object = asn1Sequence.getObjectAt(2);
                if (object instanceof ASN1OctetString) {
                    this.rspInfo = ASN1OctetString.getInstance(object);
                }
                else {
                    this.certifiedKeyPair = CertifiedKeyPair.getInstance(object);
                }
            }
            else {
                this.certifiedKeyPair = CertifiedKeyPair.getInstance(asn1Sequence.getObjectAt(2));
                this.rspInfo = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(3));
            }
        }
    }
    
    public static CertResponse getInstance(final Object o) {
        if (o instanceof CertResponse) {
            return (CertResponse)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertResponse((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERInteger getCertReqId() {
        return this.certReqId;
    }
    
    public PKIStatusInfo getStatus() {
        return this.status;
    }
    
    public CertifiedKeyPair getCertifiedKeyPair() {
        return this.certifiedKeyPair;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certReqId);
        asn1EncodableVector.add(this.status);
        if (this.certifiedKeyPair != null) {
            asn1EncodableVector.add(this.certifiedKeyPair);
        }
        if (this.rspInfo != null) {
            asn1EncodableVector.add(this.rspInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
