// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.isismtt.ocsp;

import org.bouncycastle.asn1.DERObject;
import java.io.IOException;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class RequestedCertificate extends ASN1Encodable implements ASN1Choice
{
    public static final int certificate = -1;
    public static final int publicKeyCertificate = 0;
    public static final int attributeCertificate = 1;
    private X509CertificateStructure cert;
    private byte[] publicKeyCert;
    private byte[] attributeCert;
    
    public static RequestedCertificate getInstance(final Object o) {
        if (o == null || o instanceof RequestedCertificate) {
            return (RequestedCertificate)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RequestedCertificate(X509CertificateStructure.getInstance(o));
        }
        if (o instanceof ASN1TaggedObject) {
            return new RequestedCertificate((ASN1TaggedObject)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static RequestedCertificate getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (!b) {
            throw new IllegalArgumentException("choice item must be explicitly tagged");
        }
        return getInstance(asn1TaggedObject.getObject());
    }
    
    private RequestedCertificate(final ASN1TaggedObject asn1TaggedObject) {
        if (asn1TaggedObject.getTagNo() == 0) {
            this.publicKeyCert = ASN1OctetString.getInstance(asn1TaggedObject, true).getOctets();
        }
        else {
            if (asn1TaggedObject.getTagNo() != 1) {
                throw new IllegalArgumentException("unknown tag number: " + asn1TaggedObject.getTagNo());
            }
            this.attributeCert = ASN1OctetString.getInstance(asn1TaggedObject, true).getOctets();
        }
    }
    
    public RequestedCertificate(final X509CertificateStructure cert) {
        this.cert = cert;
    }
    
    public RequestedCertificate(final int n, final byte[] array) {
        this(new DERTaggedObject(n, new DEROctetString(array)));
    }
    
    public int getType() {
        if (this.cert != null) {
            return -1;
        }
        if (this.publicKeyCert != null) {
            return 0;
        }
        return 1;
    }
    
    public byte[] getCertificateBytes() {
        if (this.cert != null) {
            try {
                return this.cert.getEncoded();
            }
            catch (IOException obj) {
                throw new IllegalStateException("can't decode certificate: " + obj);
            }
        }
        if (this.publicKeyCert != null) {
            return this.publicKeyCert;
        }
        return this.attributeCert;
    }
    
    @Override
    public DERObject toASN1Object() {
        if (this.publicKeyCert != null) {
            return new DERTaggedObject(0, new DEROctetString(this.publicKeyCert));
        }
        if (this.attributeCert != null) {
            return new DERTaggedObject(1, new DEROctetString(this.attributeCert));
        }
        return this.cert.getDERObject();
    }
}
