// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce;

import org.bouncycastle.asn1.x509.TBSCertList;
import java.security.cert.CRLException;
import org.bouncycastle.jce.provider.X509CRLObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.x509.X509Extensions;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.X509CRL;
import java.security.PrivateKey;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.DEROctetString;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.DEROutputStream;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.util.Strings;
import org.bouncycastle.asn1.DERInteger;
import java.math.BigInteger;
import org.bouncycastle.asn1.DERUTCTime;
import java.util.Date;
import org.bouncycastle.asn1.x509.X509Name;
import java.util.TimeZone;
import java.util.Vector;
import java.util.Hashtable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.V2TBSCertListGenerator;
import java.util.SimpleTimeZone;
import java.text.SimpleDateFormat;

public class X509V2CRLGenerator
{
    private SimpleDateFormat dateF;
    private SimpleTimeZone tz;
    private V2TBSCertListGenerator tbsGen;
    private DERObjectIdentifier sigOID;
    private AlgorithmIdentifier sigAlgId;
    private String signatureAlgorithm;
    private Hashtable extensions;
    private Vector extOrdering;
    private static Hashtable algorithms;
    
    public X509V2CRLGenerator() {
        this.dateF = new SimpleDateFormat("yyMMddHHmmss");
        this.tz = new SimpleTimeZone(0, "Z");
        this.extensions = null;
        this.extOrdering = null;
        this.dateF.setTimeZone(this.tz);
        this.tbsGen = new V2TBSCertListGenerator();
    }
    
    public void reset() {
        this.tbsGen = new V2TBSCertListGenerator();
    }
    
    public void setIssuerDN(final X509Name issuer) {
        this.tbsGen.setIssuer(issuer);
    }
    
    public void setThisUpdate(final Date date) {
        this.tbsGen.setThisUpdate(new DERUTCTime(this.dateF.format(date) + "Z"));
    }
    
    public void setNextUpdate(final Date date) {
        this.tbsGen.setNextUpdate(new DERUTCTime(this.dateF.format(date) + "Z"));
    }
    
    public void addCRLEntry(final BigInteger bigInteger, final Date date, final int n) {
        this.tbsGen.addCRLEntry(new DERInteger(bigInteger), new DERUTCTime(this.dateF.format(date) + "Z"), n);
    }
    
    public void setSignatureAlgorithm(final String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.sigOID = X509V2CRLGenerator.algorithms.get(Strings.toUpperCase(signatureAlgorithm));
        if (this.sigOID == null) {
            throw new IllegalArgumentException("Unknown signature type requested");
        }
        this.sigAlgId = new AlgorithmIdentifier(this.sigOID, null);
        this.tbsGen.setSignature(this.sigAlgId);
    }
    
    public void addExtension(final String s, final boolean b, final DEREncodable derEncodable) {
        this.addExtension(new DERObjectIdentifier(s), b, derEncodable);
    }
    
    public void addExtension(final DERObjectIdentifier derObjectIdentifier, final boolean b, final DEREncodable derEncodable) {
        if (this.extensions == null) {
            this.extensions = new Hashtable();
            this.extOrdering = new Vector();
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DEROutputStream derOutputStream = new DEROutputStream(byteArrayOutputStream);
        try {
            derOutputStream.writeObject(derEncodable);
        }
        catch (IOException obj) {
            throw new IllegalArgumentException("error encoding value: " + obj);
        }
        this.addExtension(derObjectIdentifier, b, byteArrayOutputStream.toByteArray());
    }
    
    public void addExtension(final String s, final boolean b, final byte[] array) {
        this.addExtension(new DERObjectIdentifier(s), b, array);
    }
    
    public void addExtension(final DERObjectIdentifier derObjectIdentifier, final boolean b, final byte[] array) {
        if (this.extensions == null) {
            this.extensions = new Hashtable();
            this.extOrdering = new Vector();
        }
        this.extensions.put(derObjectIdentifier, new X509Extension(b, new DEROctetString(array)));
        this.extOrdering.addElement(derObjectIdentifier);
    }
    
    public X509CRL generateX509CRL(final PrivateKey privateKey) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509CRL(privateKey, "BC", null);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    public X509CRL generateX509CRL(final PrivateKey privateKey, final SecureRandom secureRandom) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509CRL(privateKey, "BC", secureRandom);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    public X509CRL generateX509CRL(final PrivateKey privateKey, final String s) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        return this.generateX509CRL(privateKey, s, null);
    }
    
    public X509CRL generateX509CRL(final PrivateKey privateKey, final String s, final SecureRandom random) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        Signature signature;
        try {
            signature = Signature.getInstance(this.sigOID.getId(), s);
        }
        catch (NoSuchAlgorithmException ex3) {
            try {
                signature = Signature.getInstance(this.signatureAlgorithm, s);
            }
            catch (NoSuchAlgorithmException ex) {
                throw new SecurityException("exception creating signature: " + ex.toString());
            }
        }
        if (random != null) {
            signature.initSign(privateKey, random);
        }
        else {
            signature.initSign(privateKey);
        }
        if (this.extensions != null) {
            this.tbsGen.setExtensions(new X509Extensions(this.extOrdering, this.extensions));
        }
        final TBSCertList generateTBSCertList = this.tbsGen.generateTBSCertList();
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new DEROutputStream(byteArrayOutputStream).writeObject(generateTBSCertList);
            signature.update(byteArrayOutputStream.toByteArray());
        }
        catch (Exception obj) {
            throw new SecurityException("exception encoding TBS cert - " + obj);
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(generateTBSCertList);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(new DERBitString(signature.sign()));
        try {
            return new X509CRLObject(new CertificateList(new DERSequence(asn1EncodableVector)));
        }
        catch (CRLException ex2) {
            throw new IllegalStateException("attempt to create malformed CRL: " + ex2.getMessage());
        }
    }
    
    static {
        (X509V2CRLGenerator.algorithms = new Hashtable()).put("MD2WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.2"));
        X509V2CRLGenerator.algorithms.put("MD2WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.2"));
        X509V2CRLGenerator.algorithms.put("MD5WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.4"));
        X509V2CRLGenerator.algorithms.put("MD5WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.4"));
        X509V2CRLGenerator.algorithms.put("SHA1WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.5"));
        X509V2CRLGenerator.algorithms.put("SHA1WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.5"));
        X509V2CRLGenerator.algorithms.put("RIPEMD160WITHRSAENCRYPTION", new DERObjectIdentifier("1.3.36.3.3.1.2"));
        X509V2CRLGenerator.algorithms.put("RIPEMD160WITHRSA", new DERObjectIdentifier("1.3.36.3.3.1.2"));
        X509V2CRLGenerator.algorithms.put("SHA1WITHDSA", new DERObjectIdentifier("1.2.840.10040.4.3"));
        X509V2CRLGenerator.algorithms.put("DSAWITHSHA1", new DERObjectIdentifier("1.2.840.10040.4.3"));
        X509V2CRLGenerator.algorithms.put("SHA1WITHECDSA", new DERObjectIdentifier("1.2.840.10045.4.1"));
        X509V2CRLGenerator.algorithms.put("ECDSAWITHSHA1", new DERObjectIdentifier("1.2.840.10045.4.1"));
    }
}
