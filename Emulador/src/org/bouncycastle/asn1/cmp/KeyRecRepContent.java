// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class KeyRecRepContent extends ASN1Encodable
{
    private PKIStatusInfo status;
    private CMPCertificate newSigCert;
    private ASN1Sequence caCerts;
    private ASN1Sequence keyPairHist;
    
    private KeyRecRepContent(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.status = PKIStatusInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            switch (instance.getTagNo()) {
                case 0: {
                    this.newSigCert = CMPCertificate.getInstance(instance.getObject());
                    continue;
                }
                case 1: {
                    this.caCerts = ASN1Sequence.getInstance(instance.getObject());
                    continue;
                }
                case 2: {
                    this.keyPairHist = ASN1Sequence.getInstance(instance.getObject());
                    continue;
                }
                default: {
                    throw new IllegalArgumentException("unknown tag number: " + instance.getTagNo());
                }
            }
        }
    }
    
    public static KeyRecRepContent getInstance(final Object o) {
        if (o instanceof KeyRecRepContent) {
            return (KeyRecRepContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new KeyRecRepContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public PKIStatusInfo getStatus() {
        return this.status;
    }
    
    public CMPCertificate getNewSigCert() {
        return this.newSigCert;
    }
    
    public CMPCertificate[] getCaCerts() {
        if (this.caCerts == null) {
            return null;
        }
        final CMPCertificate[] array = new CMPCertificate[this.caCerts.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = CMPCertificate.getInstance(this.caCerts.getObjectAt(i));
        }
        return array;
    }
    
    public CertifiedKeyPair[] getKeyPairHist() {
        if (this.keyPairHist == null) {
            return null;
        }
        final CertifiedKeyPair[] array = new CertifiedKeyPair[this.keyPairHist.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = CertifiedKeyPair.getInstance(this.keyPairHist.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.status);
        this.addOptional(asn1EncodableVector, 0, this.newSigCert);
        this.addOptional(asn1EncodableVector, 1, this.caCerts);
        this.addOptional(asn1EncodableVector, 2, this.keyPairHist);
        return new DERSequence(asn1EncodableVector);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
}
