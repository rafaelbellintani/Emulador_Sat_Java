// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertStatus extends ASN1Encodable
{
    private ASN1OctetString certHash;
    private DERInteger certReqId;
    private PKIStatusInfo statusInfo;
    
    private CertStatus(final ASN1Sequence asn1Sequence) {
        this.certHash = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0));
        this.certReqId = DERInteger.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() > 2) {
            this.statusInfo = PKIStatusInfo.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public static CertStatus getInstance(final Object o) {
        if (o instanceof CertStatus) {
            return (CertStatus)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertStatus((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERInteger getCertReqId() {
        return this.certReqId;
    }
    
    public PKIStatusInfo getStatusInfo() {
        return this.statusInfo;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certHash);
        asn1EncodableVector.add(this.certReqId);
        if (this.statusInfo != null) {
            asn1EncodableVector.add(this.statusInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
