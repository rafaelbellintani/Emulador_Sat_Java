// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.ASN1Encodable;

public class RevokedInfo extends ASN1Encodable
{
    private DERGeneralizedTime revocationTime;
    private CRLReason revocationReason;
    
    public RevokedInfo(final DERGeneralizedTime revocationTime, final CRLReason revocationReason) {
        this.revocationTime = revocationTime;
        this.revocationReason = revocationReason;
    }
    
    public RevokedInfo(final ASN1Sequence asn1Sequence) {
        this.revocationTime = (DERGeneralizedTime)asn1Sequence.getObjectAt(0);
        if (asn1Sequence.size() > 1) {
            this.revocationReason = new CRLReason(DEREnumerated.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true));
        }
    }
    
    public static RevokedInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static RevokedInfo getInstance(final Object o) {
        if (o == null || o instanceof RevokedInfo) {
            return (RevokedInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RevokedInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public DERGeneralizedTime getRevocationTime() {
        return this.revocationTime;
    }
    
    public CRLReason getRevocationReason() {
        return this.revocationReason;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.revocationTime);
        if (this.revocationReason != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.revocationReason));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
