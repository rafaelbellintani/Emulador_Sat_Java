// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.ASN1Encodable;

public class AttCertValidityPeriod extends ASN1Encodable
{
    DERGeneralizedTime notBeforeTime;
    DERGeneralizedTime notAfterTime;
    
    public static AttCertValidityPeriod getInstance(final Object o) {
        if (o instanceof AttCertValidityPeriod) {
            return (AttCertValidityPeriod)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AttCertValidityPeriod((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public AttCertValidityPeriod(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.notBeforeTime = DERGeneralizedTime.getInstance(asn1Sequence.getObjectAt(0));
        this.notAfterTime = DERGeneralizedTime.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public AttCertValidityPeriod(final DERGeneralizedTime notBeforeTime, final DERGeneralizedTime notAfterTime) {
        this.notBeforeTime = notBeforeTime;
        this.notAfterTime = notAfterTime;
    }
    
    public DERGeneralizedTime getNotBeforeTime() {
        return this.notBeforeTime;
    }
    
    public DERGeneralizedTime getNotAfterTime() {
        return this.notAfterTime;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.notBeforeTime);
        asn1EncodableVector.add(this.notAfterTime);
        return new DERSequence(asn1EncodableVector);
    }
}
