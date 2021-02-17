// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.math.BigInteger;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKIStatusInfo extends ASN1Encodable
{
    DERInteger status;
    PKIFreeText statusString;
    DERBitString failInfo;
    
    public static PKIStatusInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static PKIStatusInfo getInstance(final Object o) {
        if (o instanceof PKIStatusInfo) {
            return (PKIStatusInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PKIStatusInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public PKIStatusInfo(final ASN1Sequence asn1Sequence) {
        this.status = DERInteger.getInstance(asn1Sequence.getObjectAt(0));
        this.statusString = null;
        this.failInfo = null;
        if (asn1Sequence.size() > 2) {
            this.statusString = PKIFreeText.getInstance(asn1Sequence.getObjectAt(1));
            this.failInfo = DERBitString.getInstance(asn1Sequence.getObjectAt(2));
        }
        else if (asn1Sequence.size() > 1) {
            final DEREncodable object = asn1Sequence.getObjectAt(1);
            if (object instanceof DERBitString) {
                this.failInfo = DERBitString.getInstance(object);
            }
            else {
                this.statusString = PKIFreeText.getInstance(object);
            }
        }
    }
    
    public PKIStatusInfo(final int n) {
        this.status = new DERInteger(n);
    }
    
    public PKIStatusInfo(final int n, final PKIFreeText statusString) {
        this.status = new DERInteger(n);
        this.statusString = statusString;
    }
    
    public PKIStatusInfo(final int n, final PKIFreeText statusString, final PKIFailureInfo failInfo) {
        this.status = new DERInteger(n);
        this.statusString = statusString;
        this.failInfo = failInfo;
    }
    
    public BigInteger getStatus() {
        return this.status.getValue();
    }
    
    public PKIFreeText getStatusString() {
        return this.statusString;
    }
    
    public DERBitString getFailInfo() {
        return this.failInfo;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.status);
        if (this.statusString != null) {
            asn1EncodableVector.add(this.statusString);
        }
        if (this.failInfo != null) {
            asn1EncodableVector.add(this.failInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
