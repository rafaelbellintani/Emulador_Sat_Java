// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.ASN1Encodable;

public class CrlID extends ASN1Encodable
{
    DERIA5String crlUrl;
    DERInteger crlNum;
    DERGeneralizedTime crlTime;
    
    public CrlID(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            switch (asn1TaggedObject.getTagNo()) {
                case 0: {
                    this.crlUrl = DERIA5String.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 1: {
                    this.crlNum = DERInteger.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 2: {
                    this.crlTime = DERGeneralizedTime.getInstance(asn1TaggedObject, true);
                    continue;
                }
                default: {
                    throw new IllegalArgumentException("unknown tag number: " + asn1TaggedObject.getTagNo());
                }
            }
        }
    }
    
    public DERIA5String getCrlUrl() {
        return this.crlUrl;
    }
    
    public DERInteger getCrlNum() {
        return this.crlNum;
    }
    
    public DERGeneralizedTime getCrlTime() {
        return this.crlTime;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.crlUrl != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.crlUrl));
        }
        if (this.crlNum != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.crlNum));
        }
        if (this.crlTime != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.crlTime));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
