// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.util.Strings;
import javax.crypto.Mac;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.BEROutputStream;
import java.io.ByteArrayOutputStream;
import java.security.cert.CertificateEncodingException;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.BERConstructedOctetString;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.io.OutputStream;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.pkcs.MacData;
import org.bouncycastle.asn1.pkcs.ContentInfo;
import java.io.ByteArrayInputStream;
import org.bouncycastle.asn1.pkcs.CertBag;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.EncryptedData;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.AuthenticatedSafe;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.asn1.pkcs.SafeBag;
import org.bouncycastle.asn1.pkcs.Pfx;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.asn1.pkcs.PKCS12PBEParams;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.security.Principal;
import java.io.IOException;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.X509Extensions;
import java.security.cert.X509Certificate;
import java.util.Vector;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.Key;
import java.util.Enumeration;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import java.security.PublicKey;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.security.cert.CertificateFactory;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.security.Provider;
import org.bouncycastle.jce.interfaces.BCKeyStore;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.security.KeyStoreSpi;

public class JDKPKCS12KeyStore extends KeyStoreSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers, BCKeyStore
{
    private static final int SALT_SIZE = 20;
    private static final int MIN_ITERATIONS = 1024;
    private static final Provider bcProvider;
    private IgnoresCaseHashtable keys;
    private Hashtable localIds;
    private IgnoresCaseHashtable certs;
    private Hashtable chainCerts;
    private Hashtable keyCerts;
    static final int NULL = 0;
    static final int CERTIFICATE = 1;
    static final int KEY = 2;
    static final int SECRET = 3;
    static final int SEALED = 4;
    static final int KEY_PRIVATE = 0;
    static final int KEY_PUBLIC = 1;
    static final int KEY_SECRET = 2;
    protected SecureRandom random;
    private CertificateFactory certFact;
    private DERObjectIdentifier keyAlgorithm;
    private DERObjectIdentifier certAlgorithm;
    
    public JDKPKCS12KeyStore(final Provider provider, final DERObjectIdentifier keyAlgorithm, final DERObjectIdentifier certAlgorithm) {
        this.keys = new IgnoresCaseHashtable();
        this.localIds = new Hashtable();
        this.certs = new IgnoresCaseHashtable();
        this.chainCerts = new Hashtable();
        this.keyCerts = new Hashtable();
        this.random = new SecureRandom();
        this.keyAlgorithm = keyAlgorithm;
        this.certAlgorithm = certAlgorithm;
        try {
            if (provider != null) {
                this.certFact = CertificateFactory.getInstance("X.509", provider);
            }
            else {
                this.certFact = CertificateFactory.getInstance("X.509");
            }
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("can't create cert factory - " + ex.toString());
        }
    }
    
    private SubjectKeyIdentifier createSubjectKeyId(final PublicKey publicKey) {
        try {
            return new SubjectKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence)ASN1Object.fromByteArray(publicKey.getEncoded())));
        }
        catch (Exception ex) {
            throw new RuntimeException("error creating key");
        }
    }
    
    @Override
    public void setRandom(final SecureRandom random) {
        this.random = random;
    }
    
    @Override
    public Enumeration engineAliases() {
        final Hashtable<String, String> hashtable = new Hashtable<String, String>();
        final Enumeration keys = this.certs.keys();
        while (keys.hasMoreElements()) {
            hashtable.put(keys.nextElement(), "cert");
        }
        final Enumeration keys2 = this.keys.keys();
        while (keys2.hasMoreElements()) {
            final String s = keys2.nextElement();
            if (hashtable.get(s) == null) {
                hashtable.put(s, "key");
            }
        }
        return hashtable.keys();
    }
    
    @Override
    public boolean engineContainsAlias(final String s) {
        return this.certs.get(s) != null || this.keys.get(s) != null;
    }
    
    @Override
    public void engineDeleteEntry(final String s) throws KeyStoreException {
        final Key key = (Key)this.keys.remove(s);
        Certificate certificate = (Certificate)this.certs.remove(s);
        if (certificate != null) {
            this.chainCerts.remove(new CertId(certificate.getPublicKey()));
        }
        if (key != null) {
            final String key2 = this.localIds.remove(s);
            if (key2 != null) {
                certificate = (Certificate)this.keyCerts.remove(key2);
            }
            if (certificate != null) {
                this.chainCerts.remove(new CertId(certificate.getPublicKey()));
            }
        }
        if (certificate == null && key == null) {
            throw new KeyStoreException("no such entry as " + s);
        }
    }
    
    @Override
    public Certificate engineGetCertificate(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("null alias passed to getCertificate.");
        }
        Certificate certificate = (Certificate)this.certs.get(s);
        if (certificate == null) {
            final String key = this.localIds.get(s);
            if (key != null) {
                certificate = (Certificate)this.keyCerts.get(key);
            }
            else {
                certificate = this.keyCerts.get(s);
            }
        }
        return certificate;
    }
    
    @Override
    public String engineGetCertificateAlias(final Certificate certificate) {
        final Enumeration elements = this.certs.elements();
        final Enumeration keys = this.certs.keys();
        while (elements.hasMoreElements()) {
            final Certificate certificate2 = elements.nextElement();
            final String s = keys.nextElement();
            if (certificate2.equals(certificate)) {
                return s;
            }
        }
        final Enumeration<Certificate> elements2 = (Enumeration<Certificate>)this.keyCerts.elements();
        final Enumeration<String> keys2 = this.keyCerts.keys();
        while (elements2.hasMoreElements()) {
            final Certificate certificate3 = elements2.nextElement();
            final String s2 = keys2.nextElement();
            if (certificate3.equals(certificate)) {
                return s2;
            }
        }
        return null;
    }
    
    @Override
    public Certificate[] engineGetCertificateChain(final String s) {
        if (s == null) {
            throw new IllegalArgumentException("null alias passed to getCertificateChain.");
        }
        if (!this.engineIsKeyEntry(s)) {
            return null;
        }
        Certificate engineGetCertificate = this.engineGetCertificate(s);
        if (engineGetCertificate != null) {
            final Vector<X509Certificate> vector = new Vector<X509Certificate>();
            while (engineGetCertificate != null) {
                final X509Certificate x509Certificate = (X509Certificate)engineGetCertificate;
                Certificate certificate = null;
                final byte[] extensionValue = x509Certificate.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
                if (extensionValue != null) {
                    try {
                        final AuthorityKeyIdentifier authorityKeyIdentifier = new AuthorityKeyIdentifier((ASN1Sequence)new ASN1InputStream(((ASN1OctetString)new ASN1InputStream(extensionValue).readObject()).getOctets()).readObject());
                        if (authorityKeyIdentifier.getKeyIdentifier() != null) {
                            certificate = (Certificate)this.chainCerts.get(new CertId(authorityKeyIdentifier.getKeyIdentifier()));
                        }
                    }
                    catch (IOException ex) {
                        throw new RuntimeException(ex.toString());
                    }
                }
                if (certificate == null) {
                    final Principal issuerDN = x509Certificate.getIssuerDN();
                    if (!issuerDN.equals(x509Certificate.getSubjectDN())) {
                        final Enumeration<Object> keys = this.chainCerts.keys();
                        while (keys.hasMoreElements()) {
                            final X509Certificate x509Certificate2 = this.chainCerts.get(keys.nextElement());
                            if (x509Certificate2.getSubjectDN().equals(issuerDN)) {
                                try {
                                    x509Certificate.verify(x509Certificate2.getPublicKey());
                                    certificate = x509Certificate2;
                                    break;
                                }
                                catch (Exception ex2) {}
                            }
                        }
                    }
                }
                vector.addElement((X509Certificate)engineGetCertificate);
                if (certificate != engineGetCertificate) {
                    engineGetCertificate = certificate;
                }
                else {
                    engineGetCertificate = null;
                }
            }
            final Certificate[] array = new Certificate[vector.size()];
            for (int i = 0; i != array.length; ++i) {
                array[i] = vector.elementAt(i);
            }
            return array;
        }
        return null;
    }
    
    @Override
    public Date engineGetCreationDate(final String s) {
        return new Date();
    }
    
    @Override
    public Key engineGetKey(final String s, final char[] array) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        if (s == null) {
            throw new IllegalArgumentException("null alias passed to getKey.");
        }
        return (Key)this.keys.get(s);
    }
    
    @Override
    public boolean engineIsCertificateEntry(final String s) {
        return this.certs.get(s) != null && this.keys.get(s) == null;
    }
    
    @Override
    public boolean engineIsKeyEntry(final String s) {
        return this.keys.get(s) != null;
    }
    
    @Override
    public void engineSetCertificateEntry(final String str, final Certificate value) throws KeyStoreException {
        if (this.keys.get(str) != null) {
            throw new KeyStoreException("There is a key entry with the name " + str + ".");
        }
        this.certs.put(str, value);
        this.chainCerts.put(new CertId(value.getPublicKey()), value);
    }
    
    @Override
    public void engineSetKeyEntry(final String s, final byte[] array, final Certificate[] array2) throws KeyStoreException {
        throw new RuntimeException("operation not supported");
    }
    
    @Override
    public void engineSetKeyEntry(final String s, final Key key, final char[] array, final Certificate[] array2) throws KeyStoreException {
        if (key instanceof PrivateKey && array2 == null) {
            throw new KeyStoreException("no certificate chain for private key");
        }
        if (this.keys.get(s) != null) {
            this.engineDeleteEntry(s);
        }
        this.keys.put(s, key);
        this.certs.put(s, array2[0]);
        for (int i = 0; i != array2.length; ++i) {
            this.chainCerts.put(new CertId(array2[i].getPublicKey()), array2[i]);
        }
    }
    
    @Override
    public int engineSize() {
        final Hashtable<String, String> hashtable = new Hashtable<String, String>();
        final Enumeration keys = this.certs.keys();
        while (keys.hasMoreElements()) {
            hashtable.put(keys.nextElement(), "cert");
        }
        final Enumeration keys2 = this.keys.keys();
        while (keys2.hasMoreElements()) {
            final String s = keys2.nextElement();
            if (hashtable.get(s) == null) {
                hashtable.put(s, "key");
            }
        }
        return hashtable.size();
    }
    
    protected PrivateKey unwrapKey(final AlgorithmIdentifier algorithmIdentifier, final byte[] wrappedKey, final char[] password, final boolean tryWrongPKCS12Zero) throws IOException {
        final String id = algorithmIdentifier.getObjectId().getId();
        final PKCS12PBEParams pkcs12PBEParams = new PKCS12PBEParams((ASN1Sequence)algorithmIdentifier.getParameters());
        final PBEKeySpec keySpec = new PBEKeySpec(password);
        PrivateKey privateKey;
        try {
            final SecretKeyFactory instance = SecretKeyFactory.getInstance(id, JDKPKCS12KeyStore.bcProvider);
            final PBEParameterSpec params = new PBEParameterSpec(pkcs12PBEParams.getIV(), pkcs12PBEParams.getIterations().intValue());
            final SecretKey generateSecret = instance.generateSecret(keySpec);
            ((JCEPBEKey)generateSecret).setTryWrongPKCS12Zero(tryWrongPKCS12Zero);
            final Cipher instance2 = Cipher.getInstance(id, JDKPKCS12KeyStore.bcProvider);
            instance2.init(4, generateSecret, params);
            privateKey = (PrivateKey)instance2.unwrap(wrappedKey, "", 2);
        }
        catch (Exception ex) {
            throw new IOException("exception unwrapping private key - " + ex.toString());
        }
        return privateKey;
    }
    
    protected byte[] wrapKey(final String s, final Key key, final PKCS12PBEParams pkcs12PBEParams, final char[] password) throws IOException {
        final PBEKeySpec keySpec = new PBEKeySpec(password);
        byte[] wrap;
        try {
            final SecretKeyFactory instance = SecretKeyFactory.getInstance(s, JDKPKCS12KeyStore.bcProvider);
            final PBEParameterSpec params = new PBEParameterSpec(pkcs12PBEParams.getIV(), pkcs12PBEParams.getIterations().intValue());
            final Cipher instance2 = Cipher.getInstance(s, JDKPKCS12KeyStore.bcProvider);
            instance2.init(3, instance.generateSecret(keySpec), params);
            wrap = instance2.wrap(key);
        }
        catch (Exception ex) {
            throw new IOException("exception encrypting data - " + ex.toString());
        }
        return wrap;
    }
    
    protected byte[] cryptData(final boolean b, final AlgorithmIdentifier algorithmIdentifier, final char[] password, final boolean tryWrongPKCS12Zero, final byte[] input) throws IOException {
        final String id = algorithmIdentifier.getObjectId().getId();
        final PKCS12PBEParams pkcs12PBEParams = new PKCS12PBEParams((ASN1Sequence)algorithmIdentifier.getParameters());
        final PBEKeySpec keySpec = new PBEKeySpec(password);
        try {
            final SecretKeyFactory instance = SecretKeyFactory.getInstance(id, JDKPKCS12KeyStore.bcProvider);
            final PBEParameterSpec params = new PBEParameterSpec(pkcs12PBEParams.getIV(), pkcs12PBEParams.getIterations().intValue());
            final JCEPBEKey key = (JCEPBEKey)instance.generateSecret(keySpec);
            key.setTryWrongPKCS12Zero(tryWrongPKCS12Zero);
            final Cipher instance2 = Cipher.getInstance(id, JDKPKCS12KeyStore.bcProvider);
            instance2.init(b ? 1 : 2, key, params);
            return instance2.doFinal(input);
        }
        catch (Exception ex) {
            throw new IOException("exception decrypting data - " + ex.toString());
        }
    }
    
    @Override
    public void engineLoad(final InputStream in, final char[] array) throws IOException {
        if (in == null) {
            return;
        }
        if (array == null) {
            throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
        }
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
        bufferedInputStream.mark(10);
        if (bufferedInputStream.read() != 48) {
            throw new IOException("stream does not represent a PKCS12 key store");
        }
        bufferedInputStream.reset();
        final Pfx pfx = new Pfx((ASN1Sequence)new ASN1InputStream(bufferedInputStream).readObject());
        final ContentInfo authSafe = pfx.getAuthSafe();
        final Vector<SafeBag> vector = new Vector<SafeBag>();
        boolean b = false;
        boolean b2 = false;
        if (pfx.getMacData() != null) {
            final MacData macData = pfx.getMacData();
            final DigestInfo mac = macData.getMac();
            final AlgorithmIdentifier algorithmId = mac.getAlgorithmId();
            final byte[] salt = macData.getSalt();
            final int intValue = macData.getIterationCount().intValue();
            final byte[] octets = ((ASN1OctetString)authSafe.getContent()).getOctets();
            try {
                final byte[] calculatePbeMac = calculatePbeMac(algorithmId.getObjectId(), salt, intValue, array, false, octets);
                final byte[] digest = mac.getDigest();
                if (!Arrays.constantTimeAreEqual(calculatePbeMac, digest)) {
                    if (array.length > 0) {
                        throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
                    }
                    if (!Arrays.constantTimeAreEqual(calculatePbeMac(algorithmId.getObjectId(), salt, intValue, array, true, octets), digest)) {
                        throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
                    }
                    b2 = true;
                }
            }
            catch (IOException ex) {
                throw ex;
            }
            catch (Exception ex2) {
                throw new IOException("error constructing MAC: " + ex2.toString());
            }
        }
        this.keys = new IgnoresCaseHashtable();
        this.localIds = new Hashtable();
        if (authSafe.getContentType().equals(JDKPKCS12KeyStore.data)) {
            final ContentInfo[] contentInfo = new AuthenticatedSafe((ASN1Sequence)new ASN1InputStream(((ASN1OctetString)authSafe.getContent()).getOctets()).readObject()).getContentInfo();
            for (int i = 0; i != contentInfo.length; ++i) {
                if (contentInfo[i].getContentType().equals(JDKPKCS12KeyStore.data)) {
                    final ASN1Sequence asn1Sequence = (ASN1Sequence)new ASN1InputStream(((ASN1OctetString)contentInfo[i].getContent()).getOctets()).readObject();
                    for (int j = 0; j != asn1Sequence.size(); ++j) {
                        final SafeBag obj = new SafeBag((ASN1Sequence)asn1Sequence.getObjectAt(j));
                        if (obj.getBagId().equals(JDKPKCS12KeyStore.pkcs8ShroudedKeyBag)) {
                            final EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo((ASN1Sequence)obj.getBagValue());
                            final PrivateKey unwrapKey = this.unwrapKey(encryptedPrivateKeyInfo.getEncryptionAlgorithm(), encryptedPrivateKeyInfo.getEncryptedData(), array, b2);
                            final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier = (PKCS12BagAttributeCarrier)unwrapKey;
                            String string = null;
                            ASN1OctetString asn1OctetString = null;
                            if (obj.getBagAttributes() != null) {
                                final Enumeration objects = obj.getBagAttributes().getObjects();
                                while (objects.hasMoreElements()) {
                                    final ASN1Sequence asn1Sequence2 = objects.nextElement();
                                    final DERObjectIdentifier derObjectIdentifier = (DERObjectIdentifier)asn1Sequence2.getObjectAt(0);
                                    final ASN1Set set = (ASN1Set)asn1Sequence2.getObjectAt(1);
                                    DERObject derObject = null;
                                    if (set.size() > 0) {
                                        derObject = (DERObject)set.getObjectAt(0);
                                        final DEREncodable bagAttribute = pkcs12BagAttributeCarrier.getBagAttribute(derObjectIdentifier);
                                        if (bagAttribute != null) {
                                            if (!bagAttribute.getDERObject().equals(derObject)) {
                                                throw new IOException("attempt to add existing attribute with different value");
                                            }
                                        }
                                        else {
                                            pkcs12BagAttributeCarrier.setBagAttribute(derObjectIdentifier, derObject);
                                        }
                                    }
                                    if (derObjectIdentifier.equals(JDKPKCS12KeyStore.pkcs_9_at_friendlyName)) {
                                        string = ((DERBMPString)derObject).getString();
                                        this.keys.put(string, unwrapKey);
                                    }
                                    else {
                                        if (!derObjectIdentifier.equals(JDKPKCS12KeyStore.pkcs_9_at_localKeyId)) {
                                            continue;
                                        }
                                        asn1OctetString = (ASN1OctetString)derObject;
                                    }
                                }
                            }
                            if (asn1OctetString != null) {
                                final String value = new String(Hex.encode(asn1OctetString.getOctets()));
                                if (string == null) {
                                    this.keys.put(value, unwrapKey);
                                }
                                else {
                                    this.localIds.put(string, value);
                                }
                            }
                            else {
                                b = true;
                                this.keys.put("unmarked", unwrapKey);
                            }
                        }
                        else if (obj.getBagId().equals(JDKPKCS12KeyStore.certBag)) {
                            vector.addElement(obj);
                        }
                        else {
                            System.out.println("extra in data " + obj.getBagId());
                            System.out.println(ASN1Dump.dumpAsString(obj));
                        }
                    }
                }
                else if (contentInfo[i].getContentType().equals(JDKPKCS12KeyStore.encryptedData)) {
                    final EncryptedData encryptedData = new EncryptedData((ASN1Sequence)contentInfo[i].getContent());
                    final ASN1Sequence asn1Sequence3 = (ASN1Sequence)ASN1Object.fromByteArray(this.cryptData(false, encryptedData.getEncryptionAlgorithm(), array, b2, encryptedData.getContent().getOctets()));
                    for (int k = 0; k != asn1Sequence3.size(); ++k) {
                        final SafeBag obj2 = new SafeBag((ASN1Sequence)asn1Sequence3.getObjectAt(k));
                        if (obj2.getBagId().equals(JDKPKCS12KeyStore.certBag)) {
                            vector.addElement(obj2);
                        }
                        else if (obj2.getBagId().equals(JDKPKCS12KeyStore.pkcs8ShroudedKeyBag)) {
                            final EncryptedPrivateKeyInfo encryptedPrivateKeyInfo2 = new EncryptedPrivateKeyInfo((ASN1Sequence)obj2.getBagValue());
                            final PrivateKey unwrapKey2 = this.unwrapKey(encryptedPrivateKeyInfo2.getEncryptionAlgorithm(), encryptedPrivateKeyInfo2.getEncryptedData(), array, b2);
                            final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier2 = (PKCS12BagAttributeCarrier)unwrapKey2;
                            String string2 = null;
                            ASN1OctetString asn1OctetString2 = null;
                            final Enumeration objects2 = obj2.getBagAttributes().getObjects();
                            while (objects2.hasMoreElements()) {
                                final ASN1Sequence asn1Sequence4 = objects2.nextElement();
                                final DERObjectIdentifier derObjectIdentifier2 = (DERObjectIdentifier)asn1Sequence4.getObjectAt(0);
                                final ASN1Set set2 = (ASN1Set)asn1Sequence4.getObjectAt(1);
                                DERObject derObject2 = null;
                                if (set2.size() > 0) {
                                    derObject2 = (DERObject)set2.getObjectAt(0);
                                    final DEREncodable bagAttribute2 = pkcs12BagAttributeCarrier2.getBagAttribute(derObjectIdentifier2);
                                    if (bagAttribute2 != null) {
                                        if (!bagAttribute2.getDERObject().equals(derObject2)) {
                                            throw new IOException("attempt to add existing attribute with different value");
                                        }
                                    }
                                    else {
                                        pkcs12BagAttributeCarrier2.setBagAttribute(derObjectIdentifier2, derObject2);
                                    }
                                }
                                if (derObjectIdentifier2.equals(JDKPKCS12KeyStore.pkcs_9_at_friendlyName)) {
                                    string2 = ((DERBMPString)derObject2).getString();
                                    this.keys.put(string2, unwrapKey2);
                                }
                                else {
                                    if (!derObjectIdentifier2.equals(JDKPKCS12KeyStore.pkcs_9_at_localKeyId)) {
                                        continue;
                                    }
                                    asn1OctetString2 = (ASN1OctetString)derObject2;
                                }
                            }
                            final String value2 = new String(Hex.encode(asn1OctetString2.getOctets()));
                            if (string2 == null) {
                                this.keys.put(value2, unwrapKey2);
                            }
                            else {
                                this.localIds.put(string2, value2);
                            }
                        }
                        else if (obj2.getBagId().equals(JDKPKCS12KeyStore.keyBag)) {
                            final PrivateKey privateKeyFromPrivateKeyInfo = JDKKeyFactory.createPrivateKeyFromPrivateKeyInfo(new PrivateKeyInfo((ASN1Sequence)obj2.getBagValue()));
                            final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier3 = (PKCS12BagAttributeCarrier)privateKeyFromPrivateKeyInfo;
                            String string3 = null;
                            ASN1OctetString asn1OctetString3 = null;
                            final Enumeration objects3 = obj2.getBagAttributes().getObjects();
                            while (objects3.hasMoreElements()) {
                                final ASN1Sequence asn1Sequence5 = objects3.nextElement();
                                final DERObjectIdentifier derObjectIdentifier3 = (DERObjectIdentifier)asn1Sequence5.getObjectAt(0);
                                final ASN1Set set3 = (ASN1Set)asn1Sequence5.getObjectAt(1);
                                DERObject derObject3 = null;
                                if (set3.size() > 0) {
                                    derObject3 = (DERObject)set3.getObjectAt(0);
                                    final DEREncodable bagAttribute3 = pkcs12BagAttributeCarrier3.getBagAttribute(derObjectIdentifier3);
                                    if (bagAttribute3 != null) {
                                        if (!bagAttribute3.getDERObject().equals(derObject3)) {
                                            throw new IOException("attempt to add existing attribute with different value");
                                        }
                                    }
                                    else {
                                        pkcs12BagAttributeCarrier3.setBagAttribute(derObjectIdentifier3, derObject3);
                                    }
                                }
                                if (derObjectIdentifier3.equals(JDKPKCS12KeyStore.pkcs_9_at_friendlyName)) {
                                    string3 = ((DERBMPString)derObject3).getString();
                                    this.keys.put(string3, privateKeyFromPrivateKeyInfo);
                                }
                                else {
                                    if (!derObjectIdentifier3.equals(JDKPKCS12KeyStore.pkcs_9_at_localKeyId)) {
                                        continue;
                                    }
                                    asn1OctetString3 = (ASN1OctetString)derObject3;
                                }
                            }
                            final String value3 = new String(Hex.encode(asn1OctetString3.getOctets()));
                            if (string3 == null) {
                                this.keys.put(value3, privateKeyFromPrivateKeyInfo);
                            }
                            else {
                                this.localIds.put(string3, value3);
                            }
                        }
                        else {
                            System.out.println("extra in encryptedData " + obj2.getBagId());
                            System.out.println(ASN1Dump.dumpAsString(obj2));
                        }
                    }
                }
                else {
                    System.out.println("extra " + contentInfo[i].getContentType().getId());
                    System.out.println("extra " + ASN1Dump.dumpAsString(contentInfo[i].getContent()));
                }
            }
        }
        this.certs = new IgnoresCaseHashtable();
        this.chainCerts = new Hashtable();
        this.keyCerts = new Hashtable();
        for (int l = 0; l != vector.size(); ++l) {
            final SafeBag safeBag = vector.elementAt(l);
            final CertBag certBag = new CertBag((ASN1Sequence)safeBag.getBagValue());
            if (!certBag.getCertId().equals(JDKPKCS12KeyStore.x509Certificate)) {
                throw new RuntimeException("Unsupported certificate type: " + certBag.getCertId());
            }
            Certificate generateCertificate;
            try {
                generateCertificate = this.certFact.generateCertificate(new ByteArrayInputStream(((ASN1OctetString)certBag.getCertValue()).getOctets()));
            }
            catch (Exception ex3) {
                throw new RuntimeException(ex3.toString());
            }
            ASN1OctetString asn1OctetString4 = null;
            String string4 = null;
            if (safeBag.getBagAttributes() != null) {
                final Enumeration objects4 = safeBag.getBagAttributes().getObjects();
                while (objects4.hasMoreElements()) {
                    final ASN1Sequence asn1Sequence6 = objects4.nextElement();
                    final DERObjectIdentifier derObjectIdentifier4 = (DERObjectIdentifier)asn1Sequence6.getObjectAt(0);
                    final DERObject derObject4 = (DERObject)((ASN1Set)asn1Sequence6.getObjectAt(1)).getObjectAt(0);
                    if (generateCertificate instanceof PKCS12BagAttributeCarrier) {
                        final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier4 = (PKCS12BagAttributeCarrier)generateCertificate;
                        final DEREncodable bagAttribute4 = pkcs12BagAttributeCarrier4.getBagAttribute(derObjectIdentifier4);
                        if (bagAttribute4 != null) {
                            if (!bagAttribute4.getDERObject().equals(derObject4)) {
                                throw new IOException("attempt to add existing attribute with different value");
                            }
                        }
                        else {
                            pkcs12BagAttributeCarrier4.setBagAttribute(derObjectIdentifier4, derObject4);
                        }
                    }
                    if (derObjectIdentifier4.equals(JDKPKCS12KeyStore.pkcs_9_at_friendlyName)) {
                        string4 = ((DERBMPString)derObject4).getString();
                    }
                    else {
                        if (!derObjectIdentifier4.equals(JDKPKCS12KeyStore.pkcs_9_at_localKeyId)) {
                            continue;
                        }
                        asn1OctetString4 = (ASN1OctetString)derObject4;
                    }
                }
            }
            this.chainCerts.put(new CertId(generateCertificate.getPublicKey()), generateCertificate);
            if (b) {
                if (this.keyCerts.isEmpty()) {
                    final String key = new String(Hex.encode(this.createSubjectKeyId(generateCertificate.getPublicKey()).getKeyIdentifier()));
                    this.keyCerts.put(key, generateCertificate);
                    this.keys.put(key, this.keys.remove("unmarked"));
                }
            }
            else {
                if (asn1OctetString4 != null) {
                    this.keyCerts.put(new String(Hex.encode(asn1OctetString4.getOctets())), generateCertificate);
                }
                if (string4 != null) {
                    this.certs.put(string4, generateCertificate);
                }
            }
        }
    }
    
    @Override
    public void engineStore(final OutputStream outputStream, final char[] array) throws IOException {
        if (array == null) {
            throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration keys = this.keys.keys();
        while (keys.hasMoreElements()) {
            final byte[] bytes = new byte[20];
            this.random.nextBytes(bytes);
            final String anObject = keys.nextElement();
            final PrivateKey privateKey = (PrivateKey)this.keys.get(anObject);
            final PKCS12PBEParams pkcs12PBEParams = new PKCS12PBEParams(bytes, 1024);
            final EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(new AlgorithmIdentifier(this.keyAlgorithm, pkcs12PBEParams.getDERObject()), this.wrapKey(this.keyAlgorithm.getId(), privateKey, pkcs12PBEParams, array));
            boolean b = false;
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            if (privateKey instanceof PKCS12BagAttributeCarrier) {
                final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier = (PKCS12BagAttributeCarrier)privateKey;
                final DERBMPString derbmpString = (DERBMPString)pkcs12BagAttributeCarrier.getBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_friendlyName);
                if (derbmpString == null || !derbmpString.getString().equals(anObject)) {
                    pkcs12BagAttributeCarrier.setBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_friendlyName, new DERBMPString(anObject));
                }
                if (pkcs12BagAttributeCarrier.getBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_localKeyId) == null) {
                    pkcs12BagAttributeCarrier.setBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_localKeyId, this.createSubjectKeyId(this.engineGetCertificate(anObject).getPublicKey()));
                }
                final Enumeration bagAttributeKeys = pkcs12BagAttributeCarrier.getBagAttributeKeys();
                while (bagAttributeKeys.hasMoreElements()) {
                    final DERObjectIdentifier derObjectIdentifier = bagAttributeKeys.nextElement();
                    final ASN1EncodableVector asn1EncodableVector3 = new ASN1EncodableVector();
                    asn1EncodableVector3.add(derObjectIdentifier);
                    asn1EncodableVector3.add(new DERSet(pkcs12BagAttributeCarrier.getBagAttribute(derObjectIdentifier)));
                    b = true;
                    asn1EncodableVector2.add(new DERSequence(asn1EncodableVector3));
                }
            }
            if (!b) {
                final ASN1EncodableVector asn1EncodableVector4 = new ASN1EncodableVector();
                final Certificate engineGetCertificate = this.engineGetCertificate(anObject);
                asn1EncodableVector4.add(JDKPKCS12KeyStore.pkcs_9_at_localKeyId);
                asn1EncodableVector4.add(new DERSet(this.createSubjectKeyId(engineGetCertificate.getPublicKey())));
                asn1EncodableVector2.add(new DERSequence(asn1EncodableVector4));
                final ASN1EncodableVector asn1EncodableVector5 = new ASN1EncodableVector();
                asn1EncodableVector5.add(JDKPKCS12KeyStore.pkcs_9_at_friendlyName);
                asn1EncodableVector5.add(new DERSet(new DERBMPString(anObject)));
                asn1EncodableVector2.add(new DERSequence(asn1EncodableVector5));
            }
            asn1EncodableVector.add(new SafeBag(JDKPKCS12KeyStore.pkcs8ShroudedKeyBag, encryptedPrivateKeyInfo.getDERObject(), new DERSet(asn1EncodableVector2)));
        }
        final BERConstructedOctetString berConstructedOctetString = new BERConstructedOctetString(new DERSequence(asn1EncodableVector).getDEREncoded());
        final byte[] bytes2 = new byte[20];
        this.random.nextBytes(bytes2);
        final ASN1EncodableVector asn1EncodableVector6 = new ASN1EncodableVector();
        final AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(this.certAlgorithm, new PKCS12PBEParams(bytes2, 1024).getDERObject());
        final Hashtable<Certificate, Certificate> hashtable = new Hashtable<Certificate, Certificate>();
        final Enumeration keys2 = this.keys.keys();
        while (keys2.hasMoreElements()) {
            try {
                final String anObject2 = keys2.nextElement();
                final Certificate engineGetCertificate2 = this.engineGetCertificate(anObject2);
                boolean b2 = false;
                final CertBag certBag = new CertBag(JDKPKCS12KeyStore.x509Certificate, new DEROctetString(engineGetCertificate2.getEncoded()));
                final ASN1EncodableVector asn1EncodableVector7 = new ASN1EncodableVector();
                if (engineGetCertificate2 instanceof PKCS12BagAttributeCarrier) {
                    final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier2 = (PKCS12BagAttributeCarrier)engineGetCertificate2;
                    final DERBMPString derbmpString2 = (DERBMPString)pkcs12BagAttributeCarrier2.getBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_friendlyName);
                    if (derbmpString2 == null || !derbmpString2.getString().equals(anObject2)) {
                        pkcs12BagAttributeCarrier2.setBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_friendlyName, new DERBMPString(anObject2));
                    }
                    if (pkcs12BagAttributeCarrier2.getBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_localKeyId) == null) {
                        pkcs12BagAttributeCarrier2.setBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_localKeyId, this.createSubjectKeyId(engineGetCertificate2.getPublicKey()));
                    }
                    final Enumeration bagAttributeKeys2 = pkcs12BagAttributeCarrier2.getBagAttributeKeys();
                    while (bagAttributeKeys2.hasMoreElements()) {
                        final DERObjectIdentifier derObjectIdentifier2 = bagAttributeKeys2.nextElement();
                        final ASN1EncodableVector asn1EncodableVector8 = new ASN1EncodableVector();
                        asn1EncodableVector8.add(derObjectIdentifier2);
                        asn1EncodableVector8.add(new DERSet(pkcs12BagAttributeCarrier2.getBagAttribute(derObjectIdentifier2)));
                        asn1EncodableVector7.add(new DERSequence(asn1EncodableVector8));
                        b2 = true;
                    }
                }
                if (!b2) {
                    final ASN1EncodableVector asn1EncodableVector9 = new ASN1EncodableVector();
                    asn1EncodableVector9.add(JDKPKCS12KeyStore.pkcs_9_at_localKeyId);
                    asn1EncodableVector9.add(new DERSet(this.createSubjectKeyId(engineGetCertificate2.getPublicKey())));
                    asn1EncodableVector7.add(new DERSequence(asn1EncodableVector9));
                    final ASN1EncodableVector asn1EncodableVector10 = new ASN1EncodableVector();
                    asn1EncodableVector10.add(JDKPKCS12KeyStore.pkcs_9_at_friendlyName);
                    asn1EncodableVector10.add(new DERSet(new DERBMPString(anObject2)));
                    asn1EncodableVector7.add(new DERSequence(asn1EncodableVector10));
                }
                asn1EncodableVector6.add(new SafeBag(JDKPKCS12KeyStore.certBag, certBag.getDERObject(), new DERSet(asn1EncodableVector7)));
                hashtable.put(engineGetCertificate2, engineGetCertificate2);
                continue;
            }
            catch (CertificateEncodingException ex) {
                throw new IOException("Error encoding certificate: " + ex.toString());
            }
            break;
        }
        final Enumeration keys3 = this.certs.keys();
        while (keys3.hasMoreElements()) {
            try {
                final String anObject3 = keys3.nextElement();
                final Certificate certificate = (Certificate)this.certs.get(anObject3);
                boolean b3 = false;
                if (this.keys.get(anObject3) != null) {
                    continue;
                }
                final CertBag certBag2 = new CertBag(JDKPKCS12KeyStore.x509Certificate, new DEROctetString(certificate.getEncoded()));
                final ASN1EncodableVector asn1EncodableVector11 = new ASN1EncodableVector();
                if (certificate instanceof PKCS12BagAttributeCarrier) {
                    final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier3 = (PKCS12BagAttributeCarrier)certificate;
                    final DERBMPString derbmpString3 = (DERBMPString)pkcs12BagAttributeCarrier3.getBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_friendlyName);
                    if (derbmpString3 == null || !derbmpString3.getString().equals(anObject3)) {
                        pkcs12BagAttributeCarrier3.setBagAttribute(JDKPKCS12KeyStore.pkcs_9_at_friendlyName, new DERBMPString(anObject3));
                    }
                    final Enumeration bagAttributeKeys3 = pkcs12BagAttributeCarrier3.getBagAttributeKeys();
                    while (bagAttributeKeys3.hasMoreElements()) {
                        final DERObjectIdentifier derObjectIdentifier3 = bagAttributeKeys3.nextElement();
                        if (derObjectIdentifier3.equals(PKCSObjectIdentifiers.pkcs_9_at_localKeyId)) {
                            continue;
                        }
                        final ASN1EncodableVector asn1EncodableVector12 = new ASN1EncodableVector();
                        asn1EncodableVector12.add(derObjectIdentifier3);
                        asn1EncodableVector12.add(new DERSet(pkcs12BagAttributeCarrier3.getBagAttribute(derObjectIdentifier3)));
                        asn1EncodableVector11.add(new DERSequence(asn1EncodableVector12));
                        b3 = true;
                    }
                }
                if (!b3) {
                    final ASN1EncodableVector asn1EncodableVector13 = new ASN1EncodableVector();
                    asn1EncodableVector13.add(JDKPKCS12KeyStore.pkcs_9_at_friendlyName);
                    asn1EncodableVector13.add(new DERSet(new DERBMPString(anObject3)));
                    asn1EncodableVector11.add(new DERSequence(asn1EncodableVector13));
                }
                asn1EncodableVector6.add(new SafeBag(JDKPKCS12KeyStore.certBag, certBag2.getDERObject(), new DERSet(asn1EncodableVector11)));
                hashtable.put(certificate, certificate);
                continue;
            }
            catch (CertificateEncodingException ex2) {
                throw new IOException("Error encoding certificate: " + ex2.toString());
            }
            break;
        }
        final Enumeration<CertId> keys4 = (Enumeration<CertId>)this.chainCerts.keys();
        while (keys4.hasMoreElements()) {
            try {
                final Certificate key = this.chainCerts.get(keys4.nextElement());
                if (hashtable.get(key) != null) {
                    continue;
                }
                final CertBag certBag3 = new CertBag(JDKPKCS12KeyStore.x509Certificate, new DEROctetString(key.getEncoded()));
                final ASN1EncodableVector asn1EncodableVector14 = new ASN1EncodableVector();
                if (key instanceof PKCS12BagAttributeCarrier) {
                    final PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier4 = (PKCS12BagAttributeCarrier)key;
                    final Enumeration bagAttributeKeys4 = pkcs12BagAttributeCarrier4.getBagAttributeKeys();
                    while (bagAttributeKeys4.hasMoreElements()) {
                        final DERObjectIdentifier derObjectIdentifier4 = bagAttributeKeys4.nextElement();
                        if (derObjectIdentifier4.equals(PKCSObjectIdentifiers.pkcs_9_at_localKeyId)) {
                            continue;
                        }
                        final ASN1EncodableVector asn1EncodableVector15 = new ASN1EncodableVector();
                        asn1EncodableVector15.add(derObjectIdentifier4);
                        asn1EncodableVector15.add(new DERSet(pkcs12BagAttributeCarrier4.getBagAttribute(derObjectIdentifier4)));
                        asn1EncodableVector14.add(new DERSequence(asn1EncodableVector15));
                    }
                }
                asn1EncodableVector6.add(new SafeBag(JDKPKCS12KeyStore.certBag, certBag3.getDERObject(), new DERSet(asn1EncodableVector14)));
                continue;
            }
            catch (CertificateEncodingException ex3) {
                throw new IOException("Error encoding certificate: " + ex3.toString());
            }
            break;
        }
        final AuthenticatedSafe authenticatedSafe = new AuthenticatedSafe(new ContentInfo[] { new ContentInfo(JDKPKCS12KeyStore.data, berConstructedOctetString), new ContentInfo(JDKPKCS12KeyStore.encryptedData, new EncryptedData(JDKPKCS12KeyStore.data, algorithmIdentifier, new BERConstructedOctetString(this.cryptData(true, algorithmIdentifier, array, false, new DERSequence(asn1EncodableVector6).getDEREncoded()))).getDERObject()) });
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new BEROutputStream(byteArrayOutputStream).writeObject(authenticatedSafe);
        final ContentInfo contentInfo = new ContentInfo(JDKPKCS12KeyStore.data, new BERConstructedOctetString(byteArrayOutputStream.toByteArray()));
        final byte[] bytes3 = new byte[20];
        final int n = 1024;
        this.random.nextBytes(bytes3);
        final byte[] octets = ((ASN1OctetString)contentInfo.getContent()).getOctets();
        MacData macData;
        try {
            macData = new MacData(new DigestInfo(new AlgorithmIdentifier(JDKPKCS12KeyStore.id_SHA1, new DERNull()), calculatePbeMac(JDKPKCS12KeyStore.id_SHA1, bytes3, n, array, false, octets)), bytes3, n);
        }
        catch (Exception ex4) {
            throw new IOException("error constructing MAC: " + ex4.toString());
        }
        new BEROutputStream(outputStream).writeObject(new Pfx(contentInfo, macData));
    }
    
    private static byte[] calculatePbeMac(final DERObjectIdentifier derObjectIdentifier, final byte[] salt, final int iterationCount, final char[] password, final boolean tryWrongPKCS12Zero, final byte[] input) throws Exception {
        final SecretKeyFactory instance = SecretKeyFactory.getInstance(derObjectIdentifier.getId(), JDKPKCS12KeyStore.bcProvider);
        final PBEParameterSpec params = new PBEParameterSpec(salt, iterationCount);
        final JCEPBEKey key = (JCEPBEKey)instance.generateSecret(new PBEKeySpec(password));
        key.setTryWrongPKCS12Zero(tryWrongPKCS12Zero);
        final Mac instance2 = Mac.getInstance(derObjectIdentifier.getId(), JDKPKCS12KeyStore.bcProvider);
        instance2.init(key, params);
        instance2.update(input);
        return instance2.doFinal();
    }
    
    static {
        bcProvider = new BouncyCastleProvider();
    }
    
    public static class BCPKCS12KeyStore extends JDKPKCS12KeyStore
    {
        public BCPKCS12KeyStore() {
            super(JDKPKCS12KeyStore.bcProvider, BCPKCS12KeyStore.pbeWithSHAAnd3_KeyTripleDES_CBC, BCPKCS12KeyStore.pbewithSHAAnd40BitRC2_CBC);
        }
    }
    
    public static class BCPKCS12KeyStore3DES extends JDKPKCS12KeyStore
    {
        public BCPKCS12KeyStore3DES() {
            super(JDKPKCS12KeyStore.bcProvider, BCPKCS12KeyStore3DES.pbeWithSHAAnd3_KeyTripleDES_CBC, BCPKCS12KeyStore3DES.pbeWithSHAAnd3_KeyTripleDES_CBC);
        }
    }
    
    private class CertId
    {
        byte[] id;
        
        CertId(final PublicKey publicKey) {
            this.id = JDKPKCS12KeyStore.this.createSubjectKeyId(publicKey).getKeyIdentifier();
        }
        
        CertId(final byte[] id) {
            this.id = id;
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(this.id);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || (o instanceof CertId && Arrays.areEqual(this.id, ((CertId)o).id));
        }
    }
    
    public static class DefPKCS12KeyStore extends JDKPKCS12KeyStore
    {
        public DefPKCS12KeyStore() {
            super(null, DefPKCS12KeyStore.pbeWithSHAAnd3_KeyTripleDES_CBC, DefPKCS12KeyStore.pbewithSHAAnd40BitRC2_CBC);
        }
    }
    
    public static class DefPKCS12KeyStore3DES extends JDKPKCS12KeyStore
    {
        public DefPKCS12KeyStore3DES() {
            super(null, DefPKCS12KeyStore3DES.pbeWithSHAAnd3_KeyTripleDES_CBC, DefPKCS12KeyStore3DES.pbeWithSHAAnd3_KeyTripleDES_CBC);
        }
    }
    
    private static class IgnoresCaseHashtable
    {
        private Hashtable orig;
        private Hashtable keys;
        
        private IgnoresCaseHashtable() {
            this.orig = new Hashtable();
            this.keys = new Hashtable();
        }
        
        public void put(final String s, final Object value) {
            final String lowerCase = Strings.toLowerCase(s);
            final String key = this.keys.get(lowerCase);
            if (key != null) {
                this.orig.remove(key);
            }
            this.keys.put(lowerCase, s);
            this.orig.put(s, value);
        }
        
        public Enumeration keys() {
            return this.orig.keys();
        }
        
        public Object remove(final String s) {
            final String key = this.keys.remove(Strings.toLowerCase(s));
            if (key == null) {
                return null;
            }
            return this.orig.remove(key);
        }
        
        public Object get(final String s) {
            final String key = this.keys.get(Strings.toLowerCase(s));
            if (key == null) {
                return null;
            }
            return this.orig.get(key);
        }
        
        public Enumeration elements() {
            return this.orig.elements();
        }
    }
}
