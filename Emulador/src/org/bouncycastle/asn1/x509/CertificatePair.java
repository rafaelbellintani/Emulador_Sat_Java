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
import org.bouncycastle.asn1.ASN1Encodable;

public class CertificatePair extends ASN1Encodable
{
    private X509CertificateStructure forward;
    private X509CertificateStructure reverse;
    
    public static CertificatePair getInstance(final Object o) {
        if (o == null || o instanceof CertificatePair) {
            return (CertificatePair)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertificatePair((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    private CertificatePair(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 1 && asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            if (instance.getTagNo() == 0) {
                this.forward = X509CertificateStructure.getInstance(instance, true);
            }
            else {
                if (instance.getTagNo() != 1) {
                    throw new IllegalArgumentException("Bad tag number: " + instance.getTagNo());
                }
                this.reverse = X509CertificateStructure.getInstance(instance, true);
            }
        }
    }
    
    public CertificatePair(final X509CertificateStructure forward, final X509CertificateStructure reverse) {
        this.forward = forward;
        this.reverse = reverse;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.forward != null) {
            asn1EncodableVector.add(new DERTaggedObject(0, this.forward));
        }
        if (this.reverse != null) {
            asn1EncodableVector.add(new DERTaggedObject(1, this.reverse));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    public X509CertificateStructure getForward() {
        return this.forward;
    }
    
    public X509CertificateStructure getReverse() {
        return this.reverse;
    }
}
