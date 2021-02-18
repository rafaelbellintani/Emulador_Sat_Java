// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

import java.security.MessageDigest;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.ocsp.CertID;

public class CertificateID
{
    public static final String HASH_SHA1 = "1.3.14.3.2.26";
    private final CertID id;
    
    public CertificateID(final CertID id) {
        if (id == null) {
            throw new IllegalArgumentException("'id' cannot be null");
        }
        this.id = id;
    }
    
    public CertificateID(final String s, final X509Certificate x509Certificate, final BigInteger bigInteger, final String s2) throws OCSPException {
        this.id = createCertID(new AlgorithmIdentifier(new DERObjectIdentifier(s), DERNull.INSTANCE), x509Certificate, new DERInteger(bigInteger), s2);
    }
    
    public CertificateID(final String s, final X509Certificate x509Certificate, final BigInteger bigInteger) throws OCSPException {
        this(s, x509Certificate, bigInteger, "BC");
    }
    
    public String getHashAlgOID() {
        return this.id.getHashAlgorithm().getObjectId().getId();
    }
    
    public byte[] getIssuerNameHash() {
        return this.id.getIssuerNameHash().getOctets();
    }
    
    public byte[] getIssuerKeyHash() {
        return this.id.getIssuerKeyHash().getOctets();
    }
    
    public BigInteger getSerialNumber() {
        return this.id.getSerialNumber().getValue();
    }
    
    public boolean matchesIssuer(final X509Certificate x509Certificate, final String s) throws OCSPException {
        return createCertID(this.id.getHashAlgorithm(), x509Certificate, this.id.getSerialNumber(), s).equals(this.id);
    }
    
    public CertID toASN1Object() {
        return this.id;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof CertificateID && this.id.getDERObject().equals(((CertificateID)o).id.getDERObject());
    }
    
    @Override
    public int hashCode() {
        return this.id.getDERObject().hashCode();
    }
    
    private static CertID createCertID(final AlgorithmIdentifier algorithmIdentifier, final X509Certificate x509Certificate, final DERInteger derInteger, final String s) throws OCSPException {
        try {
            final MessageDigest digestInstance = OCSPUtil.createDigestInstance(algorithmIdentifier.getObjectId().getId(), s);
            digestInstance.update(PrincipalUtil.getSubjectX509Principal(x509Certificate).getEncoded());
            final DEROctetString derOctetString = new DEROctetString(digestInstance.digest());
            digestInstance.update(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(x509Certificate.getPublicKey().getEncoded()).readObject()).getPublicKeyData().getBytes());
            return new CertID(algorithmIdentifier, derOctetString, new DEROctetString(digestInstance.digest()), derInteger);
        }
        catch (Exception obj) {
            throw new OCSPException("problem creating ID: " + obj, obj);
        }
    }
}
