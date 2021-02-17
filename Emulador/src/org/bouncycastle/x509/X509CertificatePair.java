// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import java.security.cert.CertificateEncodingException;
import java.io.IOException;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.ASN1InputStream;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.asn1.x509.CertificatePair;
import java.security.cert.X509Certificate;

public class X509CertificatePair
{
    private X509Certificate forward;
    private X509Certificate reverse;
    
    public X509CertificatePair(final X509Certificate forward, final X509Certificate reverse) {
        this.forward = forward;
        this.reverse = reverse;
    }
    
    public X509CertificatePair(final CertificatePair certificatePair) throws CertificateParsingException {
        if (certificatePair.getForward() != null) {
            this.forward = new X509CertificateObject(certificatePair.getForward());
        }
        if (certificatePair.getReverse() != null) {
            this.reverse = new X509CertificateObject(certificatePair.getReverse());
        }
    }
    
    public byte[] getEncoded() throws CertificateEncodingException {
        X509CertificateStructure instance = null;
        X509CertificateStructure instance2 = null;
        try {
            if (this.forward != null) {
                instance = X509CertificateStructure.getInstance(new ASN1InputStream(this.forward.getEncoded()).readObject());
            }
            if (this.reverse != null) {
                instance2 = X509CertificateStructure.getInstance(new ASN1InputStream(this.reverse.getEncoded()).readObject());
            }
            return new CertificatePair(instance, instance2).getDEREncoded();
        }
        catch (IllegalArgumentException ex) {
            throw new ExtCertificateEncodingException(ex.toString(), ex);
        }
        catch (IOException ex2) {
            throw new ExtCertificateEncodingException(ex2.toString(), ex2);
        }
    }
    
    public X509Certificate getForward() {
        return this.forward;
    }
    
    public X509Certificate getReverse() {
        return this.reverse;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof X509CertificatePair)) {
            return false;
        }
        final X509CertificatePair x509CertificatePair = (X509CertificatePair)o;
        boolean equals = true;
        boolean equals2 = true;
        if (this.forward != null) {
            equals2 = this.forward.equals(x509CertificatePair.forward);
        }
        else if (x509CertificatePair.forward != null) {
            equals2 = false;
        }
        if (this.reverse != null) {
            equals = this.reverse.equals(x509CertificatePair.reverse);
        }
        else if (x509CertificatePair.reverse != null) {
            equals = false;
        }
        return equals2 && equals;
    }
    
    @Override
    public int hashCode() {
        int n = -1;
        if (this.forward != null) {
            n ^= this.forward.hashCode();
        }
        if (this.reverse != null) {
            n = (n * 17 ^ this.reverse.hashCode());
        }
        return n;
    }
}
