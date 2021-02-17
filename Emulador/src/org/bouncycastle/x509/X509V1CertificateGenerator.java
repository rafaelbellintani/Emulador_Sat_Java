// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import java.util.Iterator;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.ASN1Encodable;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.ByteArrayInputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import java.security.PublicKey;
import org.bouncycastle.asn1.x509.Time;
import java.util.Date;
import java.io.IOException;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.X509Principal;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.DERInteger;
import java.math.BigInteger;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.V1TBSCertificateGenerator;

public class X509V1CertificateGenerator
{
    private V1TBSCertificateGenerator tbsGen;
    private DERObjectIdentifier sigOID;
    private AlgorithmIdentifier sigAlgId;
    private String signatureAlgorithm;
    
    public X509V1CertificateGenerator() {
        this.tbsGen = new V1TBSCertificateGenerator();
    }
    
    public void reset() {
        this.tbsGen = new V1TBSCertificateGenerator();
    }
    
    public void setSerialNumber(final BigInteger bigInteger) {
        if (bigInteger.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("serial number must be a positive integer");
        }
        this.tbsGen.setSerialNumber(new DERInteger(bigInteger));
    }
    
    public void setIssuerDN(final X500Principal x500Principal) {
        try {
            this.tbsGen.setIssuer(new X509Principal(x500Principal.getEncoded()));
        }
        catch (IOException obj) {
            throw new IllegalArgumentException("can't process principal: " + obj);
        }
    }
    
    public void setIssuerDN(final X509Name issuer) {
        this.tbsGen.setIssuer(issuer);
    }
    
    public void setNotBefore(final Date date) {
        this.tbsGen.setStartDate(new Time(date));
    }
    
    public void setNotAfter(final Date date) {
        this.tbsGen.setEndDate(new Time(date));
    }
    
    public void setSubjectDN(final X500Principal x500Principal) {
        try {
            this.tbsGen.setSubject(new X509Principal(x500Principal.getEncoded()));
        }
        catch (IOException obj) {
            throw new IllegalArgumentException("can't process principal: " + obj);
        }
    }
    
    public void setSubjectDN(final X509Name subject) {
        this.tbsGen.setSubject(subject);
    }
    
    public void setPublicKey(final PublicKey publicKey) {
        try {
            this.tbsGen.setSubjectPublicKeyInfo(new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(new ByteArrayInputStream(publicKey.getEncoded())).readObject()));
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("unable to process key - " + ex.toString());
        }
    }
    
    public void setSignatureAlgorithm(final String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
        try {
            this.sigOID = X509Util.getAlgorithmOID(signatureAlgorithm);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Unknown signature type requested");
        }
        this.sigAlgId = X509Util.getSigAlgID(this.sigOID, signatureAlgorithm);
        this.tbsGen.setSignature(this.sigAlgId);
    }
    
    @Deprecated
    public X509Certificate generateX509Certificate(final PrivateKey privateKey) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509Certificate(privateKey, "BC", null);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    @Deprecated
    public X509Certificate generateX509Certificate(final PrivateKey privateKey, final SecureRandom secureRandom) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509Certificate(privateKey, "BC", secureRandom);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    @Deprecated
    public X509Certificate generateX509Certificate(final PrivateKey privateKey, final String s) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        return this.generateX509Certificate(privateKey, s, null);
    }
    
    @Deprecated
    public X509Certificate generateX509Certificate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generate(privateKey, s, secureRandom);
        }
        catch (NoSuchProviderException ex) {
            throw ex;
        }
        catch (SignatureException ex2) {
            throw ex2;
        }
        catch (InvalidKeyException ex3) {
            throw ex3;
        }
        catch (GeneralSecurityException obj) {
            throw new SecurityException("exception: " + obj);
        }
    }
    
    public X509Certificate generate(final PrivateKey privateKey) throws CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, (SecureRandom)null);
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final SecureRandom secureRandom) throws CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertificateStructure generateTBSCertificate = this.tbsGen.generateTBSCertificate();
        byte[] calculateSignature;
        try {
            calculateSignature = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, privateKey, secureRandom, generateTBSCertificate);
        }
        catch (IOException ex) {
            throw new ExtCertificateEncodingException("exception encoding TBS cert", ex);
        }
        return this.generateJcaObject(generateTBSCertificate, calculateSignature);
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final String s) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, s, null);
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertificateStructure generateTBSCertificate = this.tbsGen.generateTBSCertificate();
        byte[] calculateSignature;
        try {
            calculateSignature = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, s, privateKey, secureRandom, generateTBSCertificate);
        }
        catch (IOException ex) {
            throw new ExtCertificateEncodingException("exception encoding TBS cert", ex);
        }
        return this.generateJcaObject(generateTBSCertificate, calculateSignature);
    }
    
    private X509Certificate generateJcaObject(final TBSCertificateStructure tbsCertificateStructure, final byte[] array) throws CertificateEncodingException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(tbsCertificateStructure);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(new DERBitString(array));
        try {
            return new X509CertificateObject(new X509CertificateStructure(new DERSequence(asn1EncodableVector)));
        }
        catch (CertificateParsingException ex) {
            throw new ExtCertificateEncodingException("exception producing certificate object", ex);
        }
    }
    
    public Iterator getSignatureAlgNames() {
        return X509Util.getAlgNames();
    }
}
