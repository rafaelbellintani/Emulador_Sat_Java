// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce;

import java.io.OutputStream;
import org.bouncycastle.asn1.DEROutputStream;
import java.io.ByteArrayOutputStream;
import java.security.cert.X509CRL;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERTaggedObject;
import java.security.SignatureException;
import java.security.cert.CRL;
import java.security.cert.Certificate;
import java.util.Iterator;
import java.math.BigInteger;
import org.bouncycastle.asn1.pkcs.IssuerAndSerialNumber;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.pkcs.SignerInfo;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.HashSet;
import org.bouncycastle.jce.provider.X509CRLObject;
import org.bouncycastle.asn1.x509.CertificateList;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.asn1.ASN1Set;
import java.util.ArrayList;
import org.bouncycastle.asn1.pkcs.SignedData;
import org.bouncycastle.asn1.pkcs.ContentInfo;
import org.bouncycastle.asn1.ASN1Sequence;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.InvalidKeyException;
import java.security.cert.CRLException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Set;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

public class PKCS7SignedData implements PKCSObjectIdentifiers
{
    private int version;
    private int signerversion;
    private Set digestalgos;
    private Collection certs;
    private Collection crls;
    private X509Certificate signCert;
    private byte[] digest;
    private String digestAlgorithm;
    private String digestEncryptionAlgorithm;
    private Signature sig;
    private transient PrivateKey privKey;
    private final String ID_PKCS7_DATA = "1.2.840.113549.1.7.1";
    private final String ID_PKCS7_SIGNED_DATA = "1.2.840.113549.1.7.2";
    private final String ID_MD5 = "1.2.840.113549.2.5";
    private final String ID_MD2 = "1.2.840.113549.2.2";
    private final String ID_SHA1 = "1.3.14.3.2.26";
    private final String ID_RSA = "1.2.840.113549.1.1.1";
    private final String ID_DSA = "1.2.840.10040.4.1";
    
    public PKCS7SignedData(final byte[] array) throws SecurityException, CRLException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException {
        this(array, "BC");
    }
    
    public PKCS7SignedData(final byte[] buf, final String provider) throws SecurityException, CRLException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException {
        final ASN1InputStream asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(buf));
        DERObject object;
        try {
            object = asn1InputStream.readObject();
        }
        catch (IOException ex2) {
            throw new SecurityException("can't decode PKCS7SignedData object");
        }
        if (!(object instanceof ASN1Sequence)) {
            throw new SecurityException("Not a valid PKCS#7 object - not a sequence");
        }
        final ContentInfo instance = ContentInfo.getInstance(object);
        if (!instance.getContentType().equals(PKCS7SignedData.signedData)) {
            throw new SecurityException("Not a valid PKCS#7 signed-data object - wrong header " + instance.getContentType().getId());
        }
        final SignedData instance2 = SignedData.getInstance(instance.getContent());
        this.certs = new ArrayList();
        if (instance2.getCertificates() != null) {
            final Enumeration objects = ASN1Set.getInstance(instance2.getCertificates()).getObjects();
            while (objects.hasMoreElements()) {
                try {
                    this.certs.add(new X509CertificateObject(X509CertificateStructure.getInstance(objects.nextElement())));
                    continue;
                }
                catch (CertificateParsingException ex) {
                    throw new SecurityException(ex.toString());
                }
                break;
            }
        }
        this.crls = new ArrayList();
        if (instance2.getCRLs() != null) {
            final Enumeration objects2 = ASN1Set.getInstance(instance2.getCRLs()).getObjects();
            while (objects2.hasMoreElements()) {
                this.crls.add(new X509CRLObject(CertificateList.getInstance(objects2.nextElement())));
            }
        }
        this.version = instance2.getVersion().getValue().intValue();
        this.digestalgos = new HashSet();
        final Enumeration objects3 = instance2.getDigestAlgorithms().getObjects();
        while (objects3.hasMoreElements()) {
            this.digestalgos.add(((DERObjectIdentifier)objects3.nextElement().getObjectAt(0)).getId());
        }
        final ASN1Set signerInfos = instance2.getSignerInfos();
        if (signerInfos.size() != 1) {
            throw new SecurityException("This PKCS#7 object has multiple SignerInfos - only one is supported at this time");
        }
        final SignerInfo instance3 = SignerInfo.getInstance(signerInfos.getObjectAt(0));
        this.signerversion = instance3.getVersion().getValue().intValue();
        final IssuerAndSerialNumber issuerAndSerialNumber = instance3.getIssuerAndSerialNumber();
        final BigInteger value = issuerAndSerialNumber.getCertificateSerialNumber().getValue();
        final X509Principal x509Principal = new X509Principal(issuerAndSerialNumber.getName());
        for (final X509Certificate signCert : this.certs) {
            if (value.equals(signCert.getSerialNumber()) && x509Principal.equals(signCert.getIssuerDN())) {
                this.signCert = signCert;
                break;
            }
        }
        if (this.signCert == null) {
            throw new SecurityException("Can't find signing certificate with serial " + value.toString(16));
        }
        this.digestAlgorithm = instance3.getDigestAlgorithm().getObjectId().getId();
        this.digest = instance3.getEncryptedDigest().getOctets();
        this.digestEncryptionAlgorithm = instance3.getDigestEncryptionAlgorithm().getObjectId().getId();
        (this.sig = Signature.getInstance(this.getDigestAlgorithm(), provider)).initVerify(this.signCert.getPublicKey());
    }
    
    public PKCS7SignedData(final PrivateKey privateKey, final Certificate[] array, final String s) throws SecurityException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException {
        this(privateKey, array, s, "BC");
    }
    
    public PKCS7SignedData(final PrivateKey privateKey, final Certificate[] array, final String s, final String s2) throws SecurityException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException {
        this(privateKey, array, null, s, s2);
    }
    
    public PKCS7SignedData(final PrivateKey privateKey, final Certificate[] array, final CRL[] array2, final String str, final String provider) throws SecurityException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException {
        this.privKey = privateKey;
        if (str.equals("MD5")) {
            this.digestAlgorithm = "1.2.840.113549.2.5";
        }
        else if (str.equals("MD2")) {
            this.digestAlgorithm = "1.2.840.113549.2.2";
        }
        else if (str.equals("SHA")) {
            this.digestAlgorithm = "1.3.14.3.2.26";
        }
        else {
            if (!str.equals("SHA1")) {
                throw new NoSuchAlgorithmException("Unknown Hash Algorithm " + str);
            }
            this.digestAlgorithm = "1.3.14.3.2.26";
        }
        final int n = 1;
        this.signerversion = n;
        this.version = n;
        this.certs = new ArrayList();
        this.crls = new ArrayList();
        (this.digestalgos = new HashSet()).add(this.digestAlgorithm);
        this.signCert = (X509Certificate)array[0];
        for (int i = 0; i < array.length; ++i) {
            this.certs.add(array[i]);
        }
        if (array2 != null) {
            for (int j = 0; j < array2.length; ++j) {
                this.crls.add(array2[j]);
            }
        }
        this.digestEncryptionAlgorithm = privateKey.getAlgorithm();
        if (this.digestEncryptionAlgorithm.equals("RSA")) {
            this.digestEncryptionAlgorithm = "1.2.840.113549.1.1.1";
        }
        else {
            if (!this.digestEncryptionAlgorithm.equals("DSA")) {
                throw new NoSuchAlgorithmException("Unknown Key Algorithm " + this.digestEncryptionAlgorithm);
            }
            this.digestEncryptionAlgorithm = "1.2.840.10040.4.1";
        }
        (this.sig = Signature.getInstance(this.getDigestAlgorithm(), provider)).initSign(privateKey);
    }
    
    public String getDigestAlgorithm() {
        String digestAlgorithm = this.digestAlgorithm;
        String digestEncryptionAlgorithm = this.digestEncryptionAlgorithm;
        if (this.digestAlgorithm.equals("1.2.840.113549.2.5")) {
            digestAlgorithm = "MD5";
        }
        else if (this.digestAlgorithm.equals("1.2.840.113549.2.2")) {
            digestAlgorithm = "MD2";
        }
        else if (this.digestAlgorithm.equals("1.3.14.3.2.26")) {
            digestAlgorithm = "SHA1";
        }
        if (this.digestEncryptionAlgorithm.equals("1.2.840.113549.1.1.1")) {
            digestEncryptionAlgorithm = "RSA";
        }
        else if (this.digestEncryptionAlgorithm.equals("1.2.840.10040.4.1")) {
            digestEncryptionAlgorithm = "DSA";
        }
        return digestAlgorithm + "with" + digestEncryptionAlgorithm;
    }
    
    public void reset() {
        try {
            if (this.privKey == null) {
                this.sig.initVerify(this.signCert.getPublicKey());
            }
            else {
                this.sig.initSign(this.privKey);
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }
    
    public Certificate[] getCertificates() {
        return this.certs.toArray(new X509Certificate[this.certs.size()]);
    }
    
    public Collection getCRLs() {
        return this.crls;
    }
    
    public X509Certificate getSigningCertificate() {
        return this.signCert;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public int getSigningInfoVersion() {
        return this.signerversion;
    }
    
    public void update(final byte b) throws SignatureException {
        this.sig.update(b);
    }
    
    public void update(final byte[] data, final int off, final int len) throws SignatureException {
        this.sig.update(data, off, len);
    }
    
    public boolean verify() throws SignatureException {
        return this.sig.verify(this.digest);
    }
    
    private DERObject getIssuer(final byte[] buf) {
        try {
            final ASN1Sequence asn1Sequence = (ASN1Sequence)new ASN1InputStream(new ByteArrayInputStream(buf)).readObject();
            return (DERObject)asn1Sequence.getObjectAt((asn1Sequence.getObjectAt(0) instanceof DERTaggedObject) ? 3 : 2);
        }
        catch (IOException obj) {
            throw new Error("IOException reading from ByteArray: " + obj);
        }
    }
    
    public byte[] getEncoded() {
        try {
            this.digest = this.sig.sign();
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            final Iterator<String> iterator = (Iterator<String>)this.digestalgos.iterator();
            while (iterator.hasNext()) {
                asn1EncodableVector.add(new AlgorithmIdentifier(new DERObjectIdentifier(iterator.next()), null));
            }
            final DERSet set = new DERSet(asn1EncodableVector);
            final DERSequence derSequence = new DERSequence(new DERObjectIdentifier("1.2.840.113549.1.7.1"));
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            final Iterator<X509Certificate> iterator2 = (Iterator<X509Certificate>)this.certs.iterator();
            while (iterator2.hasNext()) {
                asn1EncodableVector2.add(new ASN1InputStream(new ByteArrayInputStream(iterator2.next().getEncoded())).readObject());
            }
            final DERSet set2 = new DERSet(asn1EncodableVector2);
            final ASN1EncodableVector asn1EncodableVector3 = new ASN1EncodableVector();
            asn1EncodableVector3.add(new DERInteger(this.signerversion));
            asn1EncodableVector3.add(new IssuerAndSerialNumber(new X509Name((ASN1Sequence)this.getIssuer(this.signCert.getTBSCertificate())), new DERInteger(this.signCert.getSerialNumber())));
            asn1EncodableVector3.add(new AlgorithmIdentifier(new DERObjectIdentifier(this.digestAlgorithm), new DERNull()));
            asn1EncodableVector3.add(new AlgorithmIdentifier(new DERObjectIdentifier(this.digestEncryptionAlgorithm), new DERNull()));
            asn1EncodableVector3.add(new DEROctetString(this.digest));
            final ASN1EncodableVector asn1EncodableVector4 = new ASN1EncodableVector();
            asn1EncodableVector4.add(new DERInteger(this.version));
            asn1EncodableVector4.add(set);
            asn1EncodableVector4.add(derSequence);
            asn1EncodableVector4.add(new DERTaggedObject(false, 0, set2));
            if (this.crls.size() > 0) {
                final ASN1EncodableVector asn1EncodableVector5 = new ASN1EncodableVector();
                final Iterator<X509CRL> iterator3 = (Iterator<X509CRL>)this.crls.iterator();
                while (iterator3.hasNext()) {
                    asn1EncodableVector5.add(new ASN1InputStream(new ByteArrayInputStream(iterator3.next().getEncoded())).readObject());
                }
                asn1EncodableVector4.add(new DERTaggedObject(false, 1, new DERSet(asn1EncodableVector5)));
            }
            asn1EncodableVector4.add(new DERSet(new DERSequence(asn1EncodableVector3)));
            final ASN1EncodableVector asn1EncodableVector6 = new ASN1EncodableVector();
            asn1EncodableVector6.add(new DERObjectIdentifier("1.2.840.113549.1.7.2"));
            asn1EncodableVector6.add(new DERTaggedObject(0, new DERSequence(asn1EncodableVector4)));
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final DEROutputStream derOutputStream = new DEROutputStream(byteArrayOutputStream);
            derOutputStream.writeObject(new DERSequence(asn1EncodableVector6));
            derOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }
}
