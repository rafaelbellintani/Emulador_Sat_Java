// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import br.com.satcfe.satbl.controles.ControleLogs;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.io.IOException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.pkcs.CertificationRequest;

public class PKCS10CertificationRequest extends CertificationRequest
{
    static AlgorithmIdentifier keyAlgorithmIdentifier;
    static AlgorithmIdentifier signatureAlgorithmIdentifierSHA1;
    static AlgorithmIdentifier signatureAlgorithmIdentifierSHA256;
    static AlgorithmIdentifier signatureAlgorithmIdentifierSHA512;
    
    static {
        PKCS10CertificationRequest.keyAlgorithmIdentifier = new AlgorithmIdentifier(new DERObjectIdentifier("1.2.840.113549.1.1.1"), (DEREncodable)new DERNull());
        PKCS10CertificationRequest.signatureAlgorithmIdentifierSHA1 = new AlgorithmIdentifier(new DERObjectIdentifier("1.2.840.113549.1.1.5"), (DEREncodable)new DERNull());
        PKCS10CertificationRequest.signatureAlgorithmIdentifierSHA256 = new AlgorithmIdentifier(new DERObjectIdentifier("1.2.840.113549.1.1.11"), (DEREncodable)new DERNull());
        PKCS10CertificationRequest.signatureAlgorithmIdentifierSHA512 = new AlgorithmIdentifier(new DERObjectIdentifier("1.2.840.113549.1.1.13"), (DEREncodable)new DERNull());
    }
    
    public PKCS10CertificationRequest(final X509Name subject, final RSAKeyParameters publicKey, final ASN1Set attributes, final RSAKeyParameters privateKey) throws DataLengthException, CryptoException, IOException {
        if (subject == null) {
            throw new IllegalArgumentException("subject must not be null");
        }
        if (publicKey == null) {
            throw new IllegalArgumentException("public key must not be null");
        }
        if (privateKey == null) {
            throw new IllegalArgumentException("private key must not be null");
        }
        this.sigAlgId = PKCS10CertificationRequest.signatureAlgorithmIdentifierSHA256;
        final RSAPublicKeyStructure pubKey = new RSAPublicKeyStructure(publicKey.getModulus(), publicKey.getExponent());
        final SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo(PKCS10CertificationRequest.keyAlgorithmIdentifier, pubKey.toASN1Object().getEncoded());
        this.reqInfo = new CertificationRequestInfo(subject, spki, attributes);
        final byte[] requestInfoBytes = this.reqInfo.getDEREncoded();
        final RSADigestSigner sig = new RSADigestSigner((Digest)new SHA256Digest());
        sig.init(true, (CipherParameters)privateKey);
        sig.update(requestInfoBytes, 0, requestInfoBytes.length);
        this.sigBits = new DERBitString(sig.generateSignature());
    }
    
    public PKCS10CertificationRequest(final X509Name subject, final PublicKey publicKey, final ASN1Set attributes, final PrivateKey privateKey) throws DataLengthException, CryptoException, IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (subject == null) {
            throw new IllegalArgumentException("subject must not be null");
        }
        if (publicKey == null) {
            throw new IllegalArgumentException("public key must not be null");
        }
        if (privateKey == null) {
            throw new IllegalArgumentException("private key must not be null");
        }
        this.sigAlgId = PKCS10CertificationRequest.signatureAlgorithmIdentifierSHA256;
        final RSAKeyParameters keyPri = (RSAKeyParameters)PrivateKeyFactory.createKey(privateKey.getEncoded());
        final RSAKeyParameters keyPub = (RSAKeyParameters)PublicKeyFactory.createKey(publicKey.getEncoded());
        final RSAPublicKeyStructure pubKey = new RSAPublicKeyStructure(keyPub.getModulus(), keyPub.getExponent());
        final SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo(PKCS10CertificationRequest.keyAlgorithmIdentifier, pubKey.toASN1Object().getEncoded());
        this.reqInfo = new CertificationRequestInfo(subject, spki, attributes);
        final byte[] requestInfoBytes = this.reqInfo.getDEREncoded();
        final RSADigestSigner sig = new RSADigestSigner((Digest)new SHA256Digest());
        sig.init(true, (CipherParameters)keyPri);
        sig.update(requestInfoBytes, 0, requestInfoBytes.length);
        this.sigBits = new DERBitString(sig.generateSignature());
    }
    
    public byte[] getEncoded() {
        try {
            return this.toASN1Object().getEncoded();
        }
        catch (Exception e) {
            ControleLogs.logar("PKCS10CertificationRequest.getEncoded erro = " + e.toString());
            return null;
        }
    }
    
    public static ASN1Sequence toDERSequence(final byte[] bytes) throws IOException {
        final ASN1InputStream dIn = new ASN1InputStream(bytes);
        return (ASN1Sequence)dIn.readObject();
    }
}
