// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.io.DigestOutputStream;
import javax.crypto.CipherOutputStream;
import org.bouncycastle.util.io.Streams;
import org.bouncycastle.crypto.io.DigestInputStream;
import javax.crypto.CipherInputStream;
import org.bouncycastle.crypto.io.MacOutputStream;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.io.MacInputStream;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.digests.SHA1Digest;
import java.io.OutputStream;
import java.security.UnrecoverableKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.security.KeyStoreException;
import java.util.Enumeration;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.Cipher;
import java.security.spec.EncodedKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.Key;
import java.security.cert.CertificateException;
import java.security.NoSuchProviderException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.io.DataInputStream;
import java.security.cert.CertificateEncodingException;
import java.io.IOException;
import java.io.DataOutputStream;
import java.security.cert.Certificate;
import java.security.SecureRandom;
import java.util.Hashtable;
import org.bouncycastle.jce.interfaces.BCKeyStore;
import java.security.KeyStoreSpi;

public class JDKKeyStore extends KeyStoreSpi implements BCKeyStore
{
    private static final int STORE_VERSION = 1;
    private static final int STORE_SALT_SIZE = 20;
    private static final String STORE_CIPHER = "PBEWithSHAAndTwofish-CBC";
    private static final int KEY_SALT_SIZE = 20;
    private static final int MIN_ITERATIONS = 1024;
    private static final String KEY_CIPHER = "PBEWithSHAAnd3-KeyTripleDES-CBC";
    static final int NULL = 0;
    static final int CERTIFICATE = 1;
    static final int KEY = 2;
    static final int SECRET = 3;
    static final int SEALED = 4;
    static final int KEY_PRIVATE = 0;
    static final int KEY_PUBLIC = 1;
    static final int KEY_SECRET = 2;
    protected Hashtable table;
    protected SecureRandom random;
    
    public JDKKeyStore() {
        this.table = new Hashtable();
        this.random = new SecureRandom();
    }
    
    private void encodeCertificate(final Certificate certificate, final DataOutputStream dataOutputStream) throws IOException {
        try {
            final byte[] encoded = certificate.getEncoded();
            dataOutputStream.writeUTF(certificate.getType());
            dataOutputStream.writeInt(encoded.length);
            dataOutputStream.write(encoded);
        }
        catch (CertificateEncodingException ex) {
            throw new IOException(ex.toString());
        }
    }
    
    private Certificate decodeCertificate(final DataInputStream dataInputStream) throws IOException {
        final String utf = dataInputStream.readUTF();
        final byte[] array = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(array);
        try {
            return CertificateFactory.getInstance(utf, "BC").generateCertificate(new ByteArrayInputStream(array));
        }
        catch (NoSuchProviderException ex) {
            throw new IOException(ex.toString());
        }
        catch (CertificateException ex2) {
            throw new IOException(ex2.toString());
        }
    }
    
    private void encodeKey(final Key key, final DataOutputStream dataOutputStream) throws IOException {
        final byte[] encoded = key.getEncoded();
        if (key instanceof PrivateKey) {
            dataOutputStream.write(0);
        }
        else if (key instanceof PublicKey) {
            dataOutputStream.write(1);
        }
        else {
            dataOutputStream.write(2);
        }
        dataOutputStream.writeUTF(key.getFormat());
        dataOutputStream.writeUTF(key.getAlgorithm());
        dataOutputStream.writeInt(encoded.length);
        dataOutputStream.write(encoded);
    }
    
    private Key decodeKey(final DataInputStream dataInputStream) throws IOException {
        final int read = dataInputStream.read();
        final String utf = dataInputStream.readUTF();
        final String utf2 = dataInputStream.readUTF();
        final byte[] array = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(array);
        EncodedKeySpec keySpec;
        if (utf.equals("PKCS#8") || utf.equals("PKCS8")) {
            keySpec = new PKCS8EncodedKeySpec(array);
        }
        else if (utf.equals("X.509") || utf.equals("X509")) {
            keySpec = new X509EncodedKeySpec(array);
        }
        else {
            if (utf.equals("RAW")) {
                return new SecretKeySpec(array, utf2);
            }
            throw new IOException("Key format " + utf + " not recognised!");
        }
        try {
            switch (read) {
                case 0: {
                    return KeyFactory.getInstance(utf2, "BC").generatePrivate(keySpec);
                }
                case 1: {
                    return KeyFactory.getInstance(utf2, "BC").generatePublic(keySpec);
                }
                case 2: {
                    return SecretKeyFactory.getInstance(utf2, "BC").generateSecret(keySpec);
                }
                default: {
                    throw new IOException("Key type " + read + " not recognised!");
                }
            }
        }
        catch (Exception ex) {
            throw new IOException("Exception creating key: " + ex.toString());
        }
    }
    
    protected Cipher makePBECipher(final String s, final int opmode, final char[] password, final byte[] salt, final int iterationCount) throws IOException {
        try {
            final PBEKeySpec keySpec = new PBEKeySpec(password);
            final SecretKeyFactory instance = SecretKeyFactory.getInstance(s, "BC");
            final PBEParameterSpec params = new PBEParameterSpec(salt, iterationCount);
            final Cipher instance2 = Cipher.getInstance(s, "BC");
            instance2.init(opmode, instance.generateSecret(keySpec), params);
            return instance2;
        }
        catch (Exception obj) {
            throw new IOException("Error initialising store of key store: " + obj);
        }
    }
    
    @Override
    public void setRandom(final SecureRandom random) {
        this.random = random;
    }
    
    @Override
    public Enumeration engineAliases() {
        return this.table.keys();
    }
    
    @Override
    public boolean engineContainsAlias(final String key) {
        return this.table.get(key) != null;
    }
    
    @Override
    public void engineDeleteEntry(final String key) throws KeyStoreException {
        if (this.table.get(key) == null) {
            throw new KeyStoreException("no such entry as " + key);
        }
        this.table.remove(key);
    }
    
    @Override
    public Certificate engineGetCertificate(final String key) {
        final StoreEntry storeEntry = this.table.get(key);
        if (storeEntry != null) {
            if (storeEntry.getType() == 1) {
                return (Certificate)storeEntry.getObject();
            }
            final Certificate[] certificateChain = storeEntry.getCertificateChain();
            if (certificateChain != null) {
                return certificateChain[0];
            }
        }
        return null;
    }
    
    @Override
    public String engineGetCertificateAlias(final Certificate certificate) {
        final Enumeration<StoreEntry> elements = this.table.elements();
        while (elements.hasMoreElements()) {
            final StoreEntry storeEntry = elements.nextElement();
            if (storeEntry.getObject() instanceof Certificate) {
                if (((Certificate)storeEntry.getObject()).equals(certificate)) {
                    return storeEntry.getAlias();
                }
                continue;
            }
            else {
                final Certificate[] certificateChain = storeEntry.getCertificateChain();
                if (certificateChain != null && certificateChain[0].equals(certificate)) {
                    return storeEntry.getAlias();
                }
                continue;
            }
        }
        return null;
    }
    
    @Override
    public Certificate[] engineGetCertificateChain(final String key) {
        final StoreEntry storeEntry = this.table.get(key);
        if (storeEntry != null) {
            return storeEntry.getCertificateChain();
        }
        return null;
    }
    
    @Override
    public Date engineGetCreationDate(final String key) {
        final StoreEntry storeEntry = this.table.get(key);
        if (storeEntry != null) {
            return storeEntry.getDate();
        }
        return null;
    }
    
    @Override
    public Key engineGetKey(final String key, final char[] array) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        final StoreEntry storeEntry = this.table.get(key);
        if (storeEntry == null || storeEntry.getType() == 1) {
            return null;
        }
        return (Key)storeEntry.getObject(array);
    }
    
    @Override
    public boolean engineIsCertificateEntry(final String key) {
        final StoreEntry storeEntry = this.table.get(key);
        return storeEntry != null && storeEntry.getType() == 1;
    }
    
    @Override
    public boolean engineIsKeyEntry(final String key) {
        final StoreEntry storeEntry = this.table.get(key);
        return storeEntry != null && storeEntry.getType() != 1;
    }
    
    @Override
    public void engineSetCertificateEntry(final String key, final Certificate certificate) throws KeyStoreException {
        final StoreEntry storeEntry = this.table.get(key);
        if (storeEntry != null && storeEntry.getType() != 1) {
            throw new KeyStoreException("key store already has a key entry with alias " + key);
        }
        this.table.put(key, new StoreEntry(key, certificate));
    }
    
    @Override
    public void engineSetKeyEntry(final String key, final byte[] array, final Certificate[] array2) throws KeyStoreException {
        this.table.put(key, new StoreEntry(key, array, array2));
    }
    
    @Override
    public void engineSetKeyEntry(final String key, final Key key2, final char[] array, final Certificate[] array2) throws KeyStoreException {
        if (key2 instanceof PrivateKey && array2 == null) {
            throw new KeyStoreException("no certificate chain for private key");
        }
        try {
            this.table.put(key, new StoreEntry(key, key2, array, array2));
        }
        catch (Exception ex) {
            throw new KeyStoreException(ex.toString());
        }
    }
    
    @Override
    public int engineSize() {
        return this.table.size();
    }
    
    protected void loadStore(final InputStream in) throws IOException {
        final DataInputStream dataInputStream = new DataInputStream(in);
        for (int i = dataInputStream.read(); i > 0; i = dataInputStream.read()) {
            final String utf = dataInputStream.readUTF();
            final Date date = new Date(dataInputStream.readLong());
            final int int1 = dataInputStream.readInt();
            Certificate[] array = null;
            if (int1 != 0) {
                array = new Certificate[int1];
                for (int j = 0; j != int1; ++j) {
                    array[j] = this.decodeCertificate(dataInputStream);
                }
            }
            switch (i) {
                case 1: {
                    this.table.put(utf, new StoreEntry(utf, date, 1, this.decodeCertificate(dataInputStream)));
                    break;
                }
                case 2: {
                    this.table.put(utf, new StoreEntry(utf, date, 2, this.decodeKey(dataInputStream), array));
                    break;
                }
                case 3:
                case 4: {
                    final byte[] b = new byte[dataInputStream.readInt()];
                    dataInputStream.readFully(b);
                    this.table.put(utf, new StoreEntry(utf, date, i, b, array));
                    break;
                }
                default: {
                    throw new RuntimeException("Unknown object type in store.");
                }
            }
        }
    }
    
    protected void saveStore(final OutputStream out) throws IOException {
        final Enumeration<StoreEntry> elements = this.table.elements();
        final DataOutputStream dataOutputStream = new DataOutputStream(out);
        while (elements.hasMoreElements()) {
            final StoreEntry storeEntry = elements.nextElement();
            dataOutputStream.write(storeEntry.getType());
            dataOutputStream.writeUTF(storeEntry.getAlias());
            dataOutputStream.writeLong(storeEntry.getDate().getTime());
            final Certificate[] certificateChain = storeEntry.getCertificateChain();
            if (certificateChain == null) {
                dataOutputStream.writeInt(0);
            }
            else {
                dataOutputStream.writeInt(certificateChain.length);
                for (int i = 0; i != certificateChain.length; ++i) {
                    this.encodeCertificate(certificateChain[i], dataOutputStream);
                }
            }
            switch (storeEntry.getType()) {
                case 1: {
                    this.encodeCertificate((Certificate)storeEntry.getObject(), dataOutputStream);
                    continue;
                }
                case 2: {
                    this.encodeKey((Key)storeEntry.getObject(), dataOutputStream);
                    continue;
                }
                case 3:
                case 4: {
                    final byte[] b = (byte[])storeEntry.getObject();
                    dataOutputStream.writeInt(b.length);
                    dataOutputStream.write(b);
                    continue;
                }
                default: {
                    throw new RuntimeException("Unknown object type in store.");
                }
            }
        }
        dataOutputStream.write(0);
    }
    
    @Override
    public void engineLoad(final InputStream in, final char[] array) throws IOException {
        this.table.clear();
        if (in == null) {
            return;
        }
        final DataInputStream dataInputStream = new DataInputStream(in);
        final int int1 = dataInputStream.readInt();
        if (int1 != 1 && int1 != 0) {
            throw new IOException("Wrong version of key store.");
        }
        final byte[] b = new byte[dataInputStream.readInt()];
        dataInputStream.readFully(b);
        final int int2 = dataInputStream.readInt();
        final HMac hMac = new HMac(new SHA1Digest());
        if (array != null && array.length != 0) {
            final byte[] pkcs12PasswordToBytes = PBEParametersGenerator.PKCS12PasswordToBytes(array);
            final PKCS12ParametersGenerator pkcs12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
            pkcs12ParametersGenerator.init(pkcs12PasswordToBytes, b, int2);
            final CipherParameters generateDerivedMacParameters = pkcs12ParametersGenerator.generateDerivedMacParameters(hMac.getMacSize());
            Arrays.fill(pkcs12PasswordToBytes, (byte)0);
            hMac.init(generateDerivedMacParameters);
            this.loadStore(new MacInputStream(dataInputStream, hMac));
            final byte[] array2 = new byte[hMac.getMacSize()];
            hMac.doFinal(array2, 0);
            final byte[] b2 = new byte[hMac.getMacSize()];
            dataInputStream.readFully(b2);
            if (!Arrays.constantTimeAreEqual(array2, b2)) {
                this.table.clear();
                throw new IOException("KeyStore integrity check failed.");
            }
        }
        else {
            this.loadStore(dataInputStream);
            dataInputStream.readFully(new byte[hMac.getMacSize()]);
        }
    }
    
    @Override
    public void engineStore(final OutputStream out, final char[] array) throws IOException {
        final DataOutputStream dataOutputStream = new DataOutputStream(out);
        final byte[] array2 = new byte[20];
        final int v = 1024 + (this.random.nextInt() & 0x3FF);
        this.random.nextBytes(array2);
        dataOutputStream.writeInt(1);
        dataOutputStream.writeInt(array2.length);
        dataOutputStream.write(array2);
        dataOutputStream.writeInt(v);
        final HMac hMac = new HMac(new SHA1Digest());
        final MacOutputStream macOutputStream = new MacOutputStream(dataOutputStream, hMac);
        final PKCS12ParametersGenerator pkcs12ParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
        final byte[] pkcs12PasswordToBytes = PBEParametersGenerator.PKCS12PasswordToBytes(array);
        pkcs12ParametersGenerator.init(pkcs12PasswordToBytes, array2, v);
        hMac.init(pkcs12ParametersGenerator.generateDerivedMacParameters(hMac.getMacSize()));
        for (int i = 0; i != pkcs12PasswordToBytes.length; ++i) {
            pkcs12PasswordToBytes[i] = 0;
        }
        this.saveStore(macOutputStream);
        final byte[] b = new byte[hMac.getMacSize()];
        hMac.doFinal(b, 0);
        dataOutputStream.write(b);
        dataOutputStream.close();
    }
    
    public static class BouncyCastleStore extends JDKKeyStore
    {
        @Override
        public void engineLoad(final InputStream in, final char[] array) throws IOException {
            this.table.clear();
            if (in == null) {
                return;
            }
            final DataInputStream is = new DataInputStream(in);
            final int int1 = is.readInt();
            if (int1 != 1 && int1 != 0) {
                throw new IOException("Wrong version of key store.");
            }
            final byte[] b = new byte[is.readInt()];
            if (b.length != 20) {
                throw new IOException("Key store corrupted.");
            }
            is.readFully(b);
            final int int2 = is.readInt();
            if (int2 < 0 || int2 > 4096) {
                throw new IOException("Key store corrupted.");
            }
            String s;
            if (int1 == 0) {
                s = "OldPBEWithSHAAndTwofish-CBC";
            }
            else {
                s = "PBEWithSHAAndTwofish-CBC";
            }
            final CipherInputStream cipherInputStream = new CipherInputStream(is, this.makePBECipher(s, 2, array, b, int2));
            final SHA1Digest sha1Digest = new SHA1Digest();
            this.loadStore(new DigestInputStream(cipherInputStream, sha1Digest));
            final byte[] array2 = new byte[sha1Digest.getDigestSize()];
            sha1Digest.doFinal(array2, 0);
            final byte[] array3 = new byte[sha1Digest.getDigestSize()];
            Streams.readFully(cipherInputStream, array3);
            if (!Arrays.constantTimeAreEqual(array2, array3)) {
                this.table.clear();
                throw new IOException("KeyStore integrity check failed.");
            }
        }
        
        @Override
        public void engineStore(final OutputStream out, final char[] array) throws IOException {
            final DataOutputStream os = new DataOutputStream(out);
            final byte[] array2 = new byte[20];
            final int v = 1024 + (this.random.nextInt() & 0x3FF);
            this.random.nextBytes(array2);
            os.writeInt(1);
            os.writeInt(array2.length);
            os.write(array2);
            os.writeInt(v);
            final CipherOutputStream cipherOutputStream = new CipherOutputStream(os, this.makePBECipher("PBEWithSHAAndTwofish-CBC", 1, array, array2, v));
            final DigestOutputStream digestOutputStream = new DigestOutputStream(cipherOutputStream, new SHA1Digest());
            this.saveStore(digestOutputStream);
            final Digest digest = digestOutputStream.getDigest();
            final byte[] b = new byte[digest.getDigestSize()];
            digest.doFinal(b, 0);
            cipherOutputStream.write(b);
            cipherOutputStream.close();
        }
    }
    
    private class StoreEntry
    {
        int type;
        String alias;
        Object obj;
        Certificate[] certChain;
        Date date;
        
        StoreEntry(final String alias, final Certificate obj) {
            this.date = new Date();
            this.type = 1;
            this.alias = alias;
            this.obj = obj;
            this.certChain = null;
        }
        
        StoreEntry(final String alias, final byte[] obj, final Certificate[] certChain) {
            this.date = new Date();
            this.type = 3;
            this.alias = alias;
            this.obj = obj;
            this.certChain = certChain;
        }
        
        StoreEntry(final String alias, final Key key, final char[] array, final Certificate[] certChain) throws Exception {
            this.date = new Date();
            this.type = 4;
            this.alias = alias;
            this.certChain = certChain;
            final byte[] array2 = new byte[20];
            JDKKeyStore.this.random.setSeed(System.currentTimeMillis());
            JDKKeyStore.this.random.nextBytes(array2);
            final int v = 1024 + (JDKKeyStore.this.random.nextInt() & 0x3FF);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final DataOutputStream os = new DataOutputStream(out);
            os.writeInt(array2.length);
            os.write(array2);
            os.writeInt(v);
            final DataOutputStream dataOutputStream = new DataOutputStream(new CipherOutputStream(os, JDKKeyStore.this.makePBECipher("PBEWithSHAAnd3-KeyTripleDES-CBC", 1, array, array2, v)));
            JDKKeyStore.this.encodeKey(key, dataOutputStream);
            dataOutputStream.close();
            this.obj = out.toByteArray();
        }
        
        StoreEntry(final String alias, final Date date, final int type, final Object obj) {
            this.date = new Date();
            this.alias = alias;
            this.date = date;
            this.type = type;
            this.obj = obj;
        }
        
        StoreEntry(final String alias, final Date date, final int type, final Object obj, final Certificate[] certChain) {
            this.date = new Date();
            this.alias = alias;
            this.date = date;
            this.type = type;
            this.obj = obj;
            this.certChain = certChain;
        }
        
        int getType() {
            return this.type;
        }
        
        String getAlias() {
            return this.alias;
        }
        
        Object getObject() {
            return this.obj;
        }
        
        Object getObject(final char[] array) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            if ((array == null || array.length == 0) && this.obj instanceof Key) {
                return this.obj;
            }
            if (this.type == 4) {
                final DataInputStream is = new DataInputStream(new ByteArrayInputStream((byte[])this.obj));
                try {
                    final byte[] b = new byte[is.readInt()];
                    is.readFully(b);
                    final CipherInputStream in = new CipherInputStream(is, JDKKeyStore.this.makePBECipher("PBEWithSHAAnd3-KeyTripleDES-CBC", 2, array, b, is.readInt()));
                    try {
                        return JDKKeyStore.this.decodeKey(new DataInputStream(in));
                    }
                    catch (Exception ex) {
                        final DataInputStream is2 = new DataInputStream(new ByteArrayInputStream((byte[])this.obj));
                        byte[] b2 = new byte[is2.readInt()];
                        is2.readFully(b2);
                        int v = is2.readInt();
                        final CipherInputStream in2 = new CipherInputStream(is2, JDKKeyStore.this.makePBECipher("BrokenPBEWithSHAAnd3-KeyTripleDES-CBC", 2, array, b2, v));
                        Key key;
                        try {
                            key = JDKKeyStore.this.decodeKey(new DataInputStream(in2));
                        }
                        catch (Exception ex2) {
                            final DataInputStream is3 = new DataInputStream(new ByteArrayInputStream((byte[])this.obj));
                            b2 = new byte[is3.readInt()];
                            is3.readFully(b2);
                            v = is3.readInt();
                            key = JDKKeyStore.this.decodeKey(new DataInputStream(new CipherInputStream(is3, JDKKeyStore.this.makePBECipher("OldPBEWithSHAAnd3-KeyTripleDES-CBC", 2, array, b2, v))));
                        }
                        if (key != null) {
                            final ByteArrayOutputStream out = new ByteArrayOutputStream();
                            final DataOutputStream os = new DataOutputStream(out);
                            os.writeInt(b2.length);
                            os.write(b2);
                            os.writeInt(v);
                            final DataOutputStream dataOutputStream = new DataOutputStream(new CipherOutputStream(os, JDKKeyStore.this.makePBECipher("PBEWithSHAAnd3-KeyTripleDES-CBC", 1, array, b2, v)));
                            JDKKeyStore.this.encodeKey(key, dataOutputStream);
                            dataOutputStream.close();
                            this.obj = out.toByteArray();
                            return key;
                        }
                        throw new UnrecoverableKeyException("no match");
                    }
                }
                catch (Exception ex3) {
                    throw new UnrecoverableKeyException("no match");
                }
            }
            throw new RuntimeException("forget something!");
        }
        
        Certificate[] getCertificateChain() {
            return this.certChain;
        }
        
        Date getDate() {
            return this.date;
        }
    }
}
