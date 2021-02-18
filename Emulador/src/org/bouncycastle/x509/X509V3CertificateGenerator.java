// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import java.util.Iterator;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
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
import java.security.PrivateKey;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1InputStream;
import java.security.PublicKey;
import org.bouncycastle.asn1.x509.Time;
import java.util.Date;
import java.io.IOException;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.X509Principal;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.DERInteger;
import java.math.BigInteger;
import org.bouncycastle.asn1.x509.X509ExtensionsGenerator;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;

public class X509V3CertificateGenerator
{
    private V3TBSCertificateGenerator tbsGen;
    private DERObjectIdentifier sigOID;
    private AlgorithmIdentifier sigAlgId;
    private String signatureAlgorithm;
    private X509ExtensionsGenerator extGenerator;
    
    public X509V3CertificateGenerator() {
        this.tbsGen = new V3TBSCertificateGenerator();
        this.extGenerator = new X509ExtensionsGenerator();
    }
    
    public void reset() {
        this.tbsGen = new V3TBSCertificateGenerator();
        this.extGenerator.reset();
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
    
    public void setPublicKey(final PublicKey publicKey) throws IllegalArgumentException {
        try {
            this.tbsGen.setSubjectPublicKeyInfo(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject()));
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("unable to process key - " + ex.toString());
        }
    }
    
    public void setSignatureAlgorithm(final String s) {
        this.signatureAlgorithm = s;
        try {
            this.sigOID = X509Util.getAlgorithmOID(s);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Unknown signature type requested: " + s);
        }
        this.sigAlgId = X509Util.getSigAlgID(this.sigOID, s);
        this.tbsGen.setSignature(this.sigAlgId);
    }
    
    public void setSubjectUniqueID(final boolean[] array) {
        this.tbsGen.setSubjectUniqueID(this.booleanToBitString(array));
    }
    
    public void setIssuerUniqueID(final boolean[] array) {
        this.tbsGen.setIssuerUniqueID(this.booleanToBitString(array));
    }
    
    private DERBitString booleanToBitString(final boolean[] array) {
        final byte[] array2 = new byte[(array.length + 7) / 8];
        for (int i = 0; i != array.length; ++i) {
            final byte[] array3 = array2;
            final int n = i / 8;
            array3[n] |= (byte)(array[i] ? (1 << 7 - i % 8) : 0);
        }
        final int n2 = array.length % 8;
        if (n2 == 0) {
            return new DERBitString(array2);
        }
        return new DERBitString(array2, 8 - n2);
    }
    
    public void addExtension(final String s, final boolean b, final DEREncodable derEncodable) {
        this.addExtension(new DERObjectIdentifier(s), b, derEncodable);
    }
    
    public void addExtension(final DERObjectIdentifier derObjectIdentifier, final boolean b, final DEREncodable derEncodable) {
        this.extGenerator.addExtension(derObjectIdentifier, b, derEncodable);
    }
    
    public void addExtension(final String s, final boolean b, final byte[] array) {
        this.addExtension(new DERObjectIdentifier(s), b, array);
    }
    
    public void addExtension(final DERObjectIdentifier derObjectIdentifier, final boolean b, final byte[] array) {
        this.extGenerator.addExtension(derObjectIdentifier, b, array);
    }
    
    public void copyAndAddExtension(final String str, final boolean b, final X509Certificate x509Certificate) throws CertificateParsingException {
        final byte[] extensionValue = x509Certificate.getExtensionValue(str);
        if (extensionValue == null) {
            throw new CertificateParsingException("extension " + str + " not present");
        }
        try {
            this.addExtension(str, b, X509ExtensionUtil.fromExtensionValue(extensionValue));
        }
        catch (IOException ex) {
            throw new CertificateParsingException(ex.toString());
        }
    }
    
    public void copyAndAddExtension(final DERObjectIdentifier derObjectIdentifier, final boolean b, final X509Certificate x509Certificate) throws CertificateParsingException {
        this.copyAndAddExtension(derObjectIdentifier.getId(), b, x509Certificate);
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
        final TBSCertificateStructure generateTbsCert = this.generateTbsCert();
        byte[] calculateSignature;
        try {
            calculateSignature = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, privateKey, secureRandom, generateTbsCert);
        }
        catch (IOException ex) {
            throw new ExtCertificateEncodingException("exception encoding TBS cert", ex);
        }
        try {
            return this.generateJcaObject(generateTbsCert, calculateSignature);
        }
        catch (CertificateParsingException ex2) {
            throw new ExtCertificateEncodingException("exception producing certificate object", ex2);
        }
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final String s) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, s, null);
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertificateStructure generateTbsCert = this.generateTbsCert();
        byte[] calculateSignature;
        try {
            calculateSignature = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, s, privateKey, secureRandom, generateTbsCert);
        }
        catch (IOException ex) {
            throw new ExtCertificateEncodingException("exception encoding TBS cert", ex);
        }
        try {
            return this.generateJcaObject(generateTbsCert, calculateSignature);
        }
        catch (CertificateParsingException ex2) {
            throw new ExtCertificateEncodingException("exception producing certificate object", ex2);
        }
    }
    
    private TBSCertificateStructure generateTbsCert() {
        if (!this.extGenerator.isEmpty()) {
            this.tbsGen.setExtensions(this.extGenerator.generate());
        }
        return this.tbsGen.generateTBSCertificate();
    }
    
    private X509Certificate generateJcaObject(final TBSCertificateStructure tbsCertificateStructure, final byte[] array) throws CertificateParsingException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(tbsCertificateStructure);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(new DERBitString(array));
        return new X509CertificateObject(new X509CertificateStructure(new DERSequence(asn1EncodableVector)));
    }
    
    public Iterator getSignatureAlgNames() {
        return X509Util.getAlgNames();
    }
}
