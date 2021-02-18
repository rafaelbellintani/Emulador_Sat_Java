// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509.extension;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1InputStream;
import java.security.cert.X509Certificate;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;

public class AuthorityKeyIdentifierStructure extends AuthorityKeyIdentifier
{
    public AuthorityKeyIdentifierStructure(final byte[] array) throws IOException {
        super((ASN1Sequence)X509ExtensionUtil.fromExtensionValue(array));
    }
    
    private static ASN1Sequence fromCertificate(final X509Certificate x509Certificate) throws CertificateParsingException {
        try {
            if (x509Certificate.getVersion() != 3) {
                return (ASN1Sequence)new AuthorityKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(x509Certificate.getPublicKey().getEncoded()).readObject()), new GeneralNames(new GeneralName(PrincipalUtil.getIssuerX509Principal(x509Certificate))), x509Certificate.getSerialNumber()).toASN1Object();
            }
            final GeneralName generalName = new GeneralName(PrincipalUtil.getIssuerX509Principal(x509Certificate));
            final byte[] extensionValue = x509Certificate.getExtensionValue(X509Extensions.SubjectKeyIdentifier.getId());
            if (extensionValue != null) {
                return (ASN1Sequence)new AuthorityKeyIdentifier(((ASN1OctetString)X509ExtensionUtil.fromExtensionValue(extensionValue)).getOctets(), new GeneralNames(generalName), x509Certificate.getSerialNumber()).toASN1Object();
            }
            return (ASN1Sequence)new AuthorityKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(x509Certificate.getPublicKey().getEncoded()).readObject()), new GeneralNames(generalName), x509Certificate.getSerialNumber()).toASN1Object();
        }
        catch (Exception ex) {
            throw new CertificateParsingException("Exception extracting certificate details: " + ex.toString());
        }
    }
    
    private static ASN1Sequence fromKey(final PublicKey publicKey) throws InvalidKeyException {
        try {
            return (ASN1Sequence)new AuthorityKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(publicKey.getEncoded()).readObject())).toASN1Object();
        }
        catch (Exception obj) {
            throw new InvalidKeyException("can't process key: " + obj);
        }
    }
    
    public AuthorityKeyIdentifierStructure(final X509Certificate x509Certificate) throws CertificateParsingException {
        super(fromCertificate(x509Certificate));
    }
    
    public AuthorityKeyIdentifierStructure(final PublicKey publicKey) throws InvalidKeyException {
        super(fromKey(publicKey));
    }
}
