// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import org.bouncycastle.jce.provider.X509CRLObject;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.x509.TBSCertList;
import org.bouncycastle.asn1.ASN1Encodable;
import java.security.NoSuchAlgorithmException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.PrivateKey;
import org.bouncycastle.asn1.DEREncodable;
import java.util.Iterator;
import java.util.Set;
import java.security.cert.CRLException;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1InputStream;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509CRL;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERInteger;
import java.math.BigInteger;
import org.bouncycastle.asn1.x509.Time;
import java.util.Date;
import java.io.IOException;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.X509Principal;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.X509ExtensionsGenerator;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.V2TBSCertListGenerator;

public class X509V2CRLGenerator
{
    private V2TBSCertListGenerator tbsGen;
    private DERObjectIdentifier sigOID;
    private AlgorithmIdentifier sigAlgId;
    private String signatureAlgorithm;
    private X509ExtensionsGenerator extGenerator;
    
    public X509V2CRLGenerator() {
        this.tbsGen = new V2TBSCertListGenerator();
        this.extGenerator = new X509ExtensionsGenerator();
    }
    
    public void reset() {
        this.tbsGen = new V2TBSCertListGenerator();
        this.extGenerator.reset();
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
    
    public void setThisUpdate(final Date date) {
        this.tbsGen.setThisUpdate(new Time(date));
    }
    
    public void setNextUpdate(final Date date) {
        this.tbsGen.setNextUpdate(new Time(date));
    }
    
    public void addCRLEntry(final BigInteger bigInteger, final Date date, final int n) {
        this.tbsGen.addCRLEntry(new DERInteger(bigInteger), new Time(date), n);
    }
    
    public void addCRLEntry(final BigInteger bigInteger, final Date date, final int n, final Date date2) {
        this.tbsGen.addCRLEntry(new DERInteger(bigInteger), new Time(date), n, new DERGeneralizedTime(date2));
    }
    
    public void addCRLEntry(final BigInteger bigInteger, final Date date, final X509Extensions x509Extensions) {
        this.tbsGen.addCRLEntry(new DERInteger(bigInteger), new Time(date), x509Extensions);
    }
    
    public void addCRL(final X509CRL x509CRL) throws CRLException {
        final Set<? extends X509CRLEntry> revokedCertificates = x509CRL.getRevokedCertificates();
        if (revokedCertificates != null) {
            final Iterator<? extends X509CRLEntry> iterator = revokedCertificates.iterator();
            while (iterator.hasNext()) {
                final ASN1InputStream asn1InputStream = new ASN1InputStream(((X509CRLEntry)iterator.next()).getEncoded());
                try {
                    this.tbsGen.addCRLEntry(ASN1Sequence.getInstance(asn1InputStream.readObject()));
                }
                catch (IOException ex) {
                    throw new CRLException("exception processing encoding of CRL: " + ex.toString());
                }
            }
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
    
    @Deprecated
    public X509CRL generateX509CRL(final PrivateKey privateKey) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509CRL(privateKey, "BC", null);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    @Deprecated
    public X509CRL generateX509CRL(final PrivateKey privateKey, final SecureRandom secureRandom) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509CRL(privateKey, "BC", secureRandom);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    @Deprecated
    public X509CRL generateX509CRL(final PrivateKey privateKey, final String s) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        return this.generateX509CRL(privateKey, s, null);
    }
    
    @Deprecated
    public X509CRL generateX509CRL(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
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
    
    public X509CRL generate(final PrivateKey privateKey) throws CRLException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, (SecureRandom)null);
    }
    
    public X509CRL generate(final PrivateKey privateKey, final SecureRandom secureRandom) throws CRLException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertList generateCertList = this.generateCertList();
        byte[] calculateSignature;
        try {
            calculateSignature = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, privateKey, secureRandom, generateCertList);
        }
        catch (IOException ex) {
            throw new ExtCRLException("cannot generate CRL encoding", ex);
        }
        return this.generateJcaObject(generateCertList, calculateSignature);
    }
    
    public X509CRL generate(final PrivateKey privateKey, final String s) throws CRLException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, s, null);
    }
    
    public X509CRL generate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws CRLException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertList generateCertList = this.generateCertList();
        byte[] calculateSignature;
        try {
            calculateSignature = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, s, privateKey, secureRandom, generateCertList);
        }
        catch (IOException ex) {
            throw new ExtCRLException("cannot generate CRL encoding", ex);
        }
        return this.generateJcaObject(generateCertList, calculateSignature);
    }
    
    private TBSCertList generateCertList() {
        if (!this.extGenerator.isEmpty()) {
            this.tbsGen.setExtensions(this.extGenerator.generate());
        }
        return this.tbsGen.generateTBSCertList();
    }
    
    private X509CRL generateJcaObject(final TBSCertList list, final byte[] array) throws CRLException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(list);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(new DERBitString(array));
        return new X509CRLObject(new CertificateList(new DERSequence(asn1EncodableVector)));
    }
    
    public Iterator getSignatureAlgNames() {
        return X509Util.getAlgNames();
    }
    
    private static class ExtCRLException extends CRLException
    {
        Throwable cause;
        
        ExtCRLException(final String message, final Throwable cause) {
            super(message);
            this.cause = cause;
        }
        
        @Override
        public Throwable getCause() {
            return this.cause;
        }
    }
}
