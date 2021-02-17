// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce;

import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
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
import java.security.cert.X509Certificate;
import java.security.PrivateKey;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.DEROctetString;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.DEROutputStream;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.util.Strings;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.ByteArrayInputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import java.security.PublicKey;
import org.bouncycastle.asn1.x509.Time;
import java.util.Date;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.DERInteger;
import java.math.BigInteger;
import java.util.Vector;
import java.util.Hashtable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;

public class X509V3CertificateGenerator
{
    private V3TBSCertificateGenerator tbsGen;
    private DERObjectIdentifier sigOID;
    private AlgorithmIdentifier sigAlgId;
    private String signatureAlgorithm;
    private Hashtable extensions;
    private Vector extOrdering;
    private static Hashtable algorithms;
    
    public X509V3CertificateGenerator() {
        this.extensions = null;
        this.extOrdering = null;
        this.tbsGen = new V3TBSCertificateGenerator();
    }
    
    public void reset() {
        this.tbsGen = new V3TBSCertificateGenerator();
        this.extensions = null;
        this.extOrdering = null;
    }
    
    public void setSerialNumber(final BigInteger bigInteger) {
        this.tbsGen.setSerialNumber(new DERInteger(bigInteger));
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
        this.sigOID = X509V3CertificateGenerator.algorithms.get(Strings.toUpperCase(signatureAlgorithm));
        if (this.sigOID == null) {
            throw new IllegalArgumentException("Unknown signature type requested");
        }
        this.sigAlgId = new AlgorithmIdentifier(this.sigOID, new DERNull());
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
    
    public X509Certificate generateX509Certificate(final PrivateKey privateKey) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509Certificate(privateKey, "BC", null);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    public X509Certificate generateX509Certificate(final PrivateKey privateKey, final SecureRandom secureRandom) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509Certificate(privateKey, "BC", secureRandom);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    public X509Certificate generateX509Certificate(final PrivateKey privateKey, final String s) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        return this.generateX509Certificate(privateKey, s, null);
    }
    
    public X509Certificate generateX509Certificate(final PrivateKey privateKey, final String s, final SecureRandom random) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        if (this.sigOID == null) {
            throw new IllegalStateException("no signature algorithm specified");
        }
        Signature signature;
        try {
            signature = Signature.getInstance(this.sigOID.getId(), s);
        }
        catch (NoSuchAlgorithmException ex2) {
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
        final TBSCertificateStructure generateTBSCertificate = this.tbsGen.generateTBSCertificate();
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new DEROutputStream(byteArrayOutputStream).writeObject(generateTBSCertificate);
            signature.update(byteArrayOutputStream.toByteArray());
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            asn1EncodableVector.add(generateTBSCertificate);
            asn1EncodableVector.add(this.sigAlgId);
            asn1EncodableVector.add(new DERBitString(signature.sign()));
            return new X509CertificateObject(new X509CertificateStructure(new DERSequence(asn1EncodableVector)));
        }
        catch (Exception obj) {
            throw new SecurityException("exception encoding TBS cert - " + obj);
        }
    }
    
    static {
        (X509V3CertificateGenerator.algorithms = new Hashtable()).put("MD2WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.2"));
        X509V3CertificateGenerator.algorithms.put("MD2WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.2"));
        X509V3CertificateGenerator.algorithms.put("MD5WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.4"));
        X509V3CertificateGenerator.algorithms.put("MD5WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.4"));
        X509V3CertificateGenerator.algorithms.put("SHA1WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.5"));
        X509V3CertificateGenerator.algorithms.put("SHA1WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.5"));
        X509V3CertificateGenerator.algorithms.put("RIPEMD160WITHRSAENCRYPTION", new DERObjectIdentifier("1.3.36.3.3.1.2"));
        X509V3CertificateGenerator.algorithms.put("RIPEMD160WITHRSA", new DERObjectIdentifier("1.3.36.3.3.1.2"));
        X509V3CertificateGenerator.algorithms.put("SHA1WITHDSA", new DERObjectIdentifier("1.2.840.10040.4.3"));
        X509V3CertificateGenerator.algorithms.put("DSAWITHSHA1", new DERObjectIdentifier("1.2.840.10040.4.3"));
        X509V3CertificateGenerator.algorithms.put("SHA1WITHECDSA", new DERObjectIdentifier("1.2.840.10045.4.1"));
        X509V3CertificateGenerator.algorithms.put("ECDSAWITHSHA1", new DERObjectIdentifier("1.2.840.10045.4.1"));
    }
}
