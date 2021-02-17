// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cmp.PKIStatusInfo;
import org.bouncycastle.asn1.ASN1Encodable;

public class TimeStampResp extends ASN1Encodable
{
    PKIStatusInfo pkiStatusInfo;
    ContentInfo timeStampToken;
    
    public static TimeStampResp getInstance(final Object o) {
        if (o == null || o instanceof TimeStampResp) {
            return (TimeStampResp)o;
        }
        if (o instanceof ASN1Sequence) {
            return new TimeStampResp((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in 'TimeStampResp' factory : " + o.getClass().getName() + ".");
    }
    
    public TimeStampResp(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.pkiStatusInfo = PKIStatusInfo.getInstance(objects.nextElement());
        if (objects.hasMoreElements()) {
            this.timeStampToken = ContentInfo.getInstance(objects.nextElement());
        }
    }
    
    public TimeStampResp(final PKIStatusInfo pkiStatusInfo, final ContentInfo timeStampToken) {
        this.pkiStatusInfo = pkiStatusInfo;
        this.timeStampToken = timeStampToken;
    }
    
    public PKIStatusInfo getStatus() {
        return this.pkiStatusInfo;
    }
    
    public ContentInfo getTimeStampToken() {
        return this.timeStampToken;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pkiStatusInfo);
        if (this.timeStampToken != null) {
            asn1EncodableVector.add(this.timeStampToken);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
