// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import java.util.Iterator;
import org.bouncycastle.asn1.x509.AttributeCertificateInfo;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.PrivateKey;
import java.io.IOException;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.DERGeneralizedTime;
import java.util.Date;
import org.bouncycastle.asn1.DERInteger;
import java.math.BigInteger;
import org.bouncycastle.asn1.x509.AttCertIssuer;
import org.bouncycastle.asn1.x509.X509ExtensionsGenerator;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.V2AttributeCertificateInfoGenerator;

public class X509V2AttributeCertificateGenerator
{
    private V2AttributeCertificateInfoGenerator acInfoGen;
    private DERObjectIdentifier sigOID;
    private AlgorithmIdentifier sigAlgId;
    private String signatureAlgorithm;
    private X509ExtensionsGenerator extGenerator;
    
    public X509V2AttributeCertificateGenerator() {
        this.acInfoGen = new V2AttributeCertificateInfoGenerator();
        this.extGenerator = new X509ExtensionsGenerator();
    }
    
    public void reset() {
        this.acInfoGen = new V2AttributeCertificateInfoGenerator();
        this.extGenerator.reset();
    }
    
    public void setHolder(final AttributeCertificateHolder attributeCertificateHolder) {
        this.acInfoGen.setHolder(attributeCertificateHolder.holder);
    }
    
    public void setIssuer(final AttributeCertificateIssuer attributeCertificateIssuer) {
        this.acInfoGen.setIssuer(AttCertIssuer.getInstance(attributeCertificateIssuer.form));
    }
    
    public void setSerialNumber(final BigInteger bigInteger) {
        this.acInfoGen.setSerialNumber(new DERInteger(bigInteger));
    }
    
    public void setNotBefore(final Date date) {
        this.acInfoGen.setStartDate(new DERGeneralizedTime(date));
    }
    
    public void setNotAfter(final Date date) {
        this.acInfoGen.setEndDate(new DERGeneralizedTime(date));
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
        this.acInfoGen.setSignature(this.sigAlgId);
    }
    
    public void addAttribute(final X509Attribute x509Attribute) {
        this.acInfoGen.addAttribute(Attribute.getInstance(x509Attribute.toASN1Object()));
    }
    
    public void setIssuerUniqueId(final boolean[] array) {
        throw new RuntimeException("not implemented (yet)");
    }
    
    public void addExtension(final String s, final boolean b, final ASN1Encodable asn1Encodable) throws IOException {
        this.extGenerator.addExtension(new DERObjectIdentifier(s), b, asn1Encodable);
    }
    
    public void addExtension(final String s, final boolean b, final byte[] array) {
        this.extGenerator.addExtension(new DERObjectIdentifier(s), b, array);
    }
    
    @Deprecated
    public X509AttributeCertificate generateCertificate(final PrivateKey privateKey, final String s) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        return this.generateCertificate(privateKey, s, null);
    }
    
    @Deprecated
    public X509AttributeCertificate generateCertificate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
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
            throw new SecurityException("exception creating certificate: " + obj);
        }
    }
    
    public X509AttributeCertificate generate(final PrivateKey privateKey, final String s) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        return this.generate(privateKey, s, null);
    }
    
    public X509AttributeCertificate generate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (!this.extGenerator.isEmpty()) {
            this.acInfoGen.setExtensions(this.extGenerator.generate());
        }
        final AttributeCertificateInfo generateAttributeCertificateInfo = this.acInfoGen.generateAttributeCertificateInfo();
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(generateAttributeCertificateInfo);
        asn1EncodableVector.add(this.sigAlgId);
        try {
            asn1EncodableVector.add(new DERBitString(X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, s, privateKey, secureRandom, generateAttributeCertificateInfo)));
            return new X509V2AttributeCertificate(new AttributeCertificate(new DERSequence(asn1EncodableVector)));
        }
        catch (IOException ex) {
            throw new ExtCertificateEncodingException("constructed invalid certificate", ex);
        }
    }
    
    public Iterator getSignatureAlgNames() {
        return X509Util.getAlgNames();
    }
}
