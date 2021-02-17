// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class CMPCertificate extends ASN1Encodable implements ASN1Choice
{
    private X509CertificateStructure x509v3PKCert;
    
    public CMPCertificate(final X509CertificateStructure x509v3PKCert) {
        if (x509v3PKCert.getVersion() != 3) {
            throw new IllegalArgumentException("only version 3 certificates allowed");
        }
        this.x509v3PKCert = x509v3PKCert;
    }
    
    public static CMPCertificate getInstance(final Object o) {
        if (o instanceof CMPCertificate) {
            return (CMPCertificate)o;
        }
        if (o instanceof X509CertificateStructure) {
            return new CMPCertificate((X509CertificateStructure)o);
        }
        if (o instanceof ASN1Sequence) {
            return new CMPCertificate(X509CertificateStructure.getInstance(o));
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public X509CertificateStructure getX509v3PKCert() {
        return this.x509v3PKCert;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.x509v3PKCert.toASN1Object();
    }
}
