// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

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
import org.bouncycastle.asn1.ASN1Encodable;

public class PrivateKeyUsagePeriod extends ASN1Encodable
{
    private DERGeneralizedTime _notBefore;
    private DERGeneralizedTime _notAfter;
    
    public static PrivateKeyUsagePeriod getInstance(final Object o) {
        if (o instanceof PrivateKeyUsagePeriod) {
            return (PrivateKeyUsagePeriod)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PrivateKeyUsagePeriod((ASN1Sequence)o);
        }
        if (o instanceof X509Extension) {
            return getInstance(X509Extension.convertValueToObject((X509Extension)o));
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + o.getClass().getName());
    }
    
    private PrivateKeyUsagePeriod(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            if (asn1TaggedObject.getTagNo() == 0) {
                this._notBefore = DERGeneralizedTime.getInstance(asn1TaggedObject, false);
            }
            else {
                if (asn1TaggedObject.getTagNo() != 1) {
                    continue;
                }
                this._notAfter = DERGeneralizedTime.getInstance(asn1TaggedObject, false);
            }
        }
    }
    
    public DERGeneralizedTime getNotBefore() {
        return this._notBefore;
    }
    
    public DERGeneralizedTime getNotAfter() {
        return this._notAfter;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this._notBefore != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this._notBefore));
        }
        if (this._notAfter != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this._notAfter));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
