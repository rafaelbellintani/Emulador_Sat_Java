// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import javax.crypto.spec.DESedeKeySpec;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.CipherParameters;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import java.security.spec.KeySpec;
import org.bouncycastle.asn1.DERObjectIdentifier;
import javax.crypto.SecretKeyFactorySpi;

public class JCESecretKeyFactory extends SecretKeyFactorySpi implements PBE
{
    protected String algName;
    protected DERObjectIdentifier algOid;
    
    protected JCESecretKeyFactory(final String algName, final DERObjectIdentifier algOid) {
        this.algName = algName;
        this.algOid = algOid;
    }
    
    @Override
    protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof SecretKeySpec) {
            return (SecretKey)keySpec;
        }
        throw new InvalidKeySpecException("Invalid KeySpec");
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final SecretKey secretKey, final Class clazz) throws InvalidKeySpecException {
        if (clazz == null) {
            throw new InvalidKeySpecException("keySpec parameter is null");
        }
        if (secretKey == null) {
            throw new InvalidKeySpecException("key parameter is null");
        }
        if (SecretKeySpec.class.isAssignableFrom(clazz)) {
            return new SecretKeySpec(secretKey.getEncoded(), this.algName);
        }
        try {
            return (KeySpec)clazz.getConstructor(byte[].class).newInstance(secretKey.getEncoded());
        }
        catch (Exception ex) {
            throw new InvalidKeySpecException(ex.toString());
        }
    }
    
    @Override
    protected SecretKey engineTranslateKey(final SecretKey secretKey) throws InvalidKeyException {
        if (secretKey == null) {
            throw new InvalidKeyException("key parameter is null");
        }
        if (!secretKey.getAlgorithm().equalsIgnoreCase(this.algName)) {
            throw new InvalidKeyException("Key not of type " + this.algName + ".");
        }
        return new SecretKeySpec(secretKey.getEncoded(), this.algName);
    }
    
    public static class DES extends JCESecretKeyFactory
    {
        public DES() {
            super("DES", null);
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof DESKeySpec) {
                return new SecretKeySpec(((DESKeySpec)keySpec).getKey(), "DES");
            }
            return super.engineGenerateSecret(keySpec);
        }
    }
    
    public static class DESPBEKeyFactory extends JCESecretKeyFactory
    {
        private boolean forCipher;
        private int scheme;
        private int digest;
        private int keySize;
        private int ivSize;
        
        public DESPBEKeyFactory(final String s, final DERObjectIdentifier derObjectIdentifier, final boolean forCipher, final int scheme, final int digest, final int keySize, final int ivSize) {
            super(s, derObjectIdentifier);
            this.forCipher = forCipher;
            this.scheme = scheme;
            this.digest = digest;
            this.keySize = keySize;
            this.ivSize = ivSize;
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (!(keySpec instanceof PBEKeySpec)) {
                throw new InvalidKeySpecException("Invalid KeySpec");
            }
            final PBEKeySpec pbeKeySpec = (PBEKeySpec)keySpec;
            if (pbeKeySpec.getSalt() == null) {
                return new JCEPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pbeKeySpec, null);
            }
            CipherParameters cipherParameters;
            if (this.forCipher) {
                cipherParameters = Util.makePBEParameters(pbeKeySpec, this.scheme, this.digest, this.keySize, this.ivSize);
            }
            else {
                cipherParameters = Util.makePBEMacParameters(pbeKeySpec, this.scheme, this.digest, this.keySize);
            }
            if (cipherParameters instanceof ParametersWithIV) {
                DESParameters.setOddParity(((KeyParameter)((ParametersWithIV)cipherParameters).getParameters()).getKey());
            }
            else {
                DESParameters.setOddParity(((KeyParameter)cipherParameters).getKey());
            }
            return new JCEPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pbeKeySpec, cipherParameters);
        }
    }
    
    public static class DESede extends JCESecretKeyFactory
    {
        public DESede() {
            super("DESede", null);
        }
        
        @Override
        protected KeySpec engineGetKeySpec(final SecretKey secretKey, final Class clazz) throws InvalidKeySpecException {
            if (clazz == null) {
                throw new InvalidKeySpecException("keySpec parameter is null");
            }
            if (secretKey == null) {
                throw new InvalidKeySpecException("key parameter is null");
            }
            if (SecretKeySpec.class.isAssignableFrom(clazz)) {
                return new SecretKeySpec(secretKey.getEncoded(), this.algName);
            }
            if (DESedeKeySpec.class.isAssignableFrom(clazz)) {
                final byte[] encoded = secretKey.getEncoded();
                try {
                    if (encoded.length == 16) {
                        final byte[] key = new byte[24];
                        System.arraycopy(encoded, 0, key, 0, 16);
                        System.arraycopy(encoded, 0, key, 16, 8);
                        return new DESedeKeySpec(key);
                    }
                    return new DESedeKeySpec(encoded);
                }
                catch (Exception ex) {
                    throw new InvalidKeySpecException(ex.toString());
                }
            }
            throw new InvalidKeySpecException("Invalid KeySpec");
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof DESedeKeySpec) {
                return new SecretKeySpec(((DESedeKeySpec)keySpec).getKey(), "DESede");
            }
            return super.engineGenerateSecret(keySpec);
        }
    }
    
    public static class PBEKeyFactory extends JCESecretKeyFactory
    {
        private boolean forCipher;
        private int scheme;
        private int digest;
        private int keySize;
        private int ivSize;
        
        public PBEKeyFactory(final String s, final DERObjectIdentifier derObjectIdentifier, final boolean forCipher, final int scheme, final int digest, final int keySize, final int ivSize) {
            super(s, derObjectIdentifier);
            this.forCipher = forCipher;
            this.scheme = scheme;
            this.digest = digest;
            this.keySize = keySize;
            this.ivSize = ivSize;
        }
        
        @Override
        protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
            if (!(keySpec instanceof PBEKeySpec)) {
                throw new InvalidKeySpecException("Invalid KeySpec");
            }
            final PBEKeySpec pbeKeySpec = (PBEKeySpec)keySpec;
            if (pbeKeySpec.getSalt() == null) {
                return new JCEPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pbeKeySpec, null);
            }
            CipherParameters cipherParameters;
            if (this.forCipher) {
                cipherParameters = Util.makePBEParameters(pbeKeySpec, this.scheme, this.digest, this.keySize, this.ivSize);
            }
            else {
                cipherParameters = Util.makePBEMacParameters(pbeKeySpec, this.scheme, this.digest, this.keySize);
            }
            return new JCEPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pbeKeySpec, cipherParameters);
        }
    }
    
    public static class PBEWithMD2AndDES extends DESPBEKeyFactory
    {
        public PBEWithMD2AndDES() {
            super("PBEwithMD2andDES", PKCSObjectIdentifiers.pbeWithMD2AndDES_CBC, true, 0, 5, 64, 64);
        }
    }
    
    public static class PBEWithMD2AndRC2 extends PBEKeyFactory
    {
        public PBEWithMD2AndRC2() {
            super("PBEwithMD2andRC2", PKCSObjectIdentifiers.pbeWithMD2AndRC2_CBC, true, 0, 5, 64, 64);
        }
    }
    
    public static class PBEWithMD5And128BitAESCBCOpenSSL extends PBEKeyFactory
    {
        public PBEWithMD5And128BitAESCBCOpenSSL() {
            super("PBEWithMD5And128BitAES-CBC-OpenSSL", null, true, 3, 0, 128, 128);
        }
    }
    
    public static class PBEWithMD5And192BitAESCBCOpenSSL extends PBEKeyFactory
    {
        public PBEWithMD5And192BitAESCBCOpenSSL() {
            super("PBEWithMD5And192BitAES-CBC-OpenSSL", null, true, 3, 0, 192, 128);
        }
    }
    
    public static class PBEWithMD5And256BitAESCBCOpenSSL extends PBEKeyFactory
    {
        public PBEWithMD5And256BitAESCBCOpenSSL() {
            super("PBEWithMD5And256BitAES-CBC-OpenSSL", null, true, 3, 0, 256, 128);
        }
    }
    
    public static class PBEWithMD5AndDES extends DESPBEKeyFactory
    {
        public PBEWithMD5AndDES() {
            super("PBEwithMD5andDES", PKCSObjectIdentifiers.pbeWithMD5AndDES_CBC, true, 0, 0, 64, 64);
        }
    }
    
    public static class PBEWithMD5AndRC2 extends PBEKeyFactory
    {
        public PBEWithMD5AndRC2() {
            super("PBEwithMD5andRC2", PKCSObjectIdentifiers.pbeWithMD5AndRC2_CBC, true, 0, 0, 64, 64);
        }
    }
    
    public static class PBEWithRIPEMD160 extends PBEKeyFactory
    {
        public PBEWithRIPEMD160() {
            super("PBEwithHmacRIPEMD160", null, false, 2, 2, 160, 0);
        }
    }
    
    public static class PBEWithSHA extends PBEKeyFactory
    {
        public PBEWithSHA() {
            super("PBEwithHmacSHA", null, false, 2, 1, 160, 0);
        }
    }
    
    public static class PBEWithSHA1AndDES extends PBEKeyFactory
    {
        public PBEWithSHA1AndDES() {
            super("PBEwithSHA1andDES", PKCSObjectIdentifiers.pbeWithSHA1AndDES_CBC, true, 0, 1, 64, 64);
        }
    }
    
    public static class PBEWithSHA1AndRC2 extends PBEKeyFactory
    {
        public PBEWithSHA1AndRC2() {
            super("PBEwithSHA1andRC2", PKCSObjectIdentifiers.pbeWithSHA1AndRC2_CBC, true, 0, 1, 64, 64);
        }
    }
    
    public static class PBEWithSHA256And128BitAESBC extends PBEKeyFactory
    {
        public PBEWithSHA256And128BitAESBC() {
            super("PBEWithSHA256And128BitAES-CBC-BC", null, true, 2, 4, 128, 128);
        }
    }
    
    public static class PBEWithSHA256And192BitAESBC extends PBEKeyFactory
    {
        public PBEWithSHA256And192BitAESBC() {
            super("PBEWithSHA256And192BitAES-CBC-BC", null, true, 2, 4, 192, 128);
        }
    }
    
    public static class PBEWithSHA256And256BitAESBC extends PBEKeyFactory
    {
        public PBEWithSHA256And256BitAESBC() {
            super("PBEWithSHA256And256BitAES-CBC-BC", null, true, 2, 4, 256, 128);
        }
    }
    
    public static class PBEWithSHAAnd128BitAESBC extends PBEKeyFactory
    {
        public PBEWithSHAAnd128BitAESBC() {
            super("PBEWithSHA1And128BitAES-CBC-BC", null, true, 2, 1, 128, 128);
        }
    }
    
    public static class PBEWithSHAAnd128BitRC2 extends PBEKeyFactory
    {
        public PBEWithSHAAnd128BitRC2() {
            super("PBEwithSHAand128BitRC2-CBC", PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC, true, 2, 1, 128, 64);
        }
    }
    
    public static class PBEWithSHAAnd128BitRC4 extends PBEKeyFactory
    {
        public PBEWithSHAAnd128BitRC4() {
            super("PBEWithSHAAnd128BitRC4", PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC4, true, 2, 1, 128, 0);
        }
    }
    
    public static class PBEWithSHAAnd192BitAESBC extends PBEKeyFactory
    {
        public PBEWithSHAAnd192BitAESBC() {
            super("PBEWithSHA1And192BitAES-CBC-BC", null, true, 2, 1, 192, 128);
        }
    }
    
    public static class PBEWithSHAAnd256BitAESBC extends PBEKeyFactory
    {
        public PBEWithSHAAnd256BitAESBC() {
            super("PBEWithSHA1And256BitAES-CBC-BC", null, true, 2, 1, 256, 128);
        }
    }
    
    public static class PBEWithSHAAnd40BitRC2 extends PBEKeyFactory
    {
        public PBEWithSHAAnd40BitRC2() {
            super("PBEwithSHAand40BitRC2-CBC", PKCSObjectIdentifiers.pbewithSHAAnd40BitRC2_CBC, true, 2, 1, 40, 64);
        }
    }
    
    public static class PBEWithSHAAnd40BitRC4 extends PBEKeyFactory
    {
        public PBEWithSHAAnd40BitRC4() {
            super("PBEWithSHAAnd128BitRC4", PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC4, true, 2, 1, 40, 0);
        }
    }
    
    public static class PBEWithSHAAndDES2Key extends PBEKeyFactory
    {
        public PBEWithSHAAndDES2Key() {
            super("PBEwithSHAandDES2Key-CBC", PKCSObjectIdentifiers.pbeWithSHAAnd2_KeyTripleDES_CBC, true, 2, 1, 128, 64);
        }
    }
    
    public static class PBEWithSHAAndDES3Key extends PBEKeyFactory
    {
        public PBEWithSHAAndDES3Key() {
            super("PBEwithSHAandDES3Key-CBC", PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC, true, 2, 1, 192, 64);
        }
    }
    
    public static class PBEWithSHAAndTwofish extends PBEKeyFactory
    {
        public PBEWithSHAAndTwofish() {
            super("PBEwithSHAandTwofish-CBC", null, true, 2, 1, 256, 128);
        }
    }
    
    public static class PBEWithTiger extends PBEKeyFactory
    {
        public PBEWithTiger() {
            super("PBEwithHmacTiger", null, false, 2, 3, 192, 0);
        }
    }
}
