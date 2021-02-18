// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509.extension;

import java.security.cert.CertificateParsingException;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import java.security.PublicKey;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;

public class SubjectKeyIdentifierStructure extends SubjectKeyIdentifier
{
    private AuthorityKeyIdentifier authKeyID;
    
    public SubjectKeyIdentifierStructure(final byte[] array) throws IOException {
        super((ASN1OctetString)X509ExtensionUtil.fromExtensionValue(array));
    }
    
    private static ASN1OctetString fromPublicKey(final PublicKey publicKey) throws CertificateParsingException {
        try {
            return (ASN1OctetString)new SubjectKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(publicKey.getEncoded()).readObject())).toASN1Object();
        }
        catch (Exception ex) {
            throw new CertificateParsingException("Exception extracting certificate details: " + ex.toString());
        }
    }
    
    public SubjectKeyIdentifierStructure(final PublicKey publicKey) throws CertificateParsingException {
        super(fromPublicKey(publicKey));
    }
}
