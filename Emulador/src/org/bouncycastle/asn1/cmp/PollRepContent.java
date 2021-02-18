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
import org.bouncycastle.asn1.ASN1Encodable;

public class PollRepContent extends ASN1Encodable
{
    private DERInteger certReqId;
    private DERInteger checkAfter;
    private PKIFreeText reason;
    
    private PollRepContent(final ASN1Sequence asn1Sequence) {
        this.certReqId = DERInteger.getInstance(asn1Sequence.getObjectAt(0));
        this.checkAfter = DERInteger.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() > 2) {
            this.reason = PKIFreeText.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public static PollRepContent getInstance(final Object o) {
        if (o instanceof PollRepContent) {
            return (PollRepContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PollRepContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERInteger getCertReqId() {
        return this.certReqId;
    }
    
    public DERInteger getCheckAfter() {
        return this.checkAfter;
    }
    
    public PKIFreeText getReason() {
        return this.reason;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certReqId);
        asn1EncodableVector.add(this.checkAfter);
        if (this.reason != null) {
            asn1EncodableVector.add(this.reason);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
