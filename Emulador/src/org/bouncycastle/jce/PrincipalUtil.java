// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce;

import java.security.cert.CRLException;
import org.bouncycastle.asn1.x509.TBSCertList;
import java.security.cert.X509CRL;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.ASN1Object;
import java.security.cert.X509Certificate;

public class PrincipalUtil
{
    public static X509Principal getIssuerX509Principal(final X509Certificate x509Certificate) throws CertificateEncodingException {
        try {
            return new X509Principal(TBSCertificateStructure.getInstance(ASN1Object.fromByteArray(x509Certificate.getTBSCertificate())).getIssuer());
        }
        catch (IOException ex) {
            throw new CertificateEncodingException(ex.toString());
        }
    }
    
    public static X509Principal getSubjectX509Principal(final X509Certificate x509Certificate) throws CertificateEncodingException {
        try {
            return new X509Principal(TBSCertificateStructure.getInstance(ASN1Object.fromByteArray(x509Certificate.getTBSCertificate())).getSubject());
        }
        catch (IOException ex) {
            throw new CertificateEncodingException(ex.toString());
        }
    }
    
    public static X509Principal getIssuerX509Principal(final X509CRL x509CRL) throws CRLException {
        try {
            return new X509Principal(TBSCertList.getInstance(ASN1Object.fromByteArray(x509CRL.getTBSCertList())).getIssuer());
        }
        catch (IOException ex) {
            throw new CRLException(ex.toString());
        }
    }
}
