// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.generators.DESedeKeyGenerator;
import org.bouncycastle.crypto.generators.DESKeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import java.security.InvalidParameterException;
import org.bouncycastle.crypto.KeyGenerationParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.CipherKeyGenerator;
import javax.crypto.KeyGeneratorSpi;

public class JCEKeyGenerator extends KeyGeneratorSpi
{
    protected String algName;
    protected int keySize;
    protected int defaultKeySize;
    protected CipherKeyGenerator engine;
    protected boolean uninitialised;
    
    protected JCEKeyGenerator(final String algName, final int n, final CipherKeyGenerator engine) {
        this.uninitialised = true;
        this.algName = algName;
        this.defaultKeySize = n;
        this.keySize = n;
        this.engine = engine;
    }
    
    @Override
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("Not Implemented");
    }
    
    @Override
    protected void engineInit(final SecureRandom secureRandom) {
        if (secureRandom != null) {
            this.engine.init(new KeyGenerationParameters(secureRandom, this.defaultKeySize));
            this.uninitialised = false;
        }
    }
    
    @Override
    protected void engineInit(final int n, final SecureRandom secureRandom) {
        try {
            this.engine.init(new KeyGenerationParameters(secureRandom, n));
            this.uninitialised = false;
        }
        catch (IllegalArgumentException ex) {
            throw new InvalidParameterException(ex.getMessage());
        }
    }
    
    @Override
    protected SecretKey engineGenerateKey() {
        if (this.uninitialised) {
            this.engine.init(new KeyGenerationParameters(new SecureRandom(), this.defaultKeySize));
            this.uninitialised = false;
        }
        return new SecretKeySpec(this.engine.generateKey(), this.algName);
    }
    
    public static class Blowfish extends JCEKeyGenerator
    {
        public Blowfish() {
            super("Blowfish", 128, new CipherKeyGenerator());
        }
    }
    
    public static class CAST6 extends JCEKeyGenerator
    {
        public CAST6() {
            super("CAST6", 256, new CipherKeyGenerator());
        }
    }
    
    public static class DES extends JCEKeyGenerator
    {
        public DES() {
            super("DES", 64, new DESKeyGenerator());
        }
    }
    
    public static class DESede extends JCEKeyGenerator
    {
        private boolean keySizeSet;
        
        public DESede() {
            super("DESede", 192, new DESedeKeyGenerator());
            this.keySizeSet = false;
        }
        
        @Override
        protected void engineInit(final int n, final SecureRandom secureRandom) {
            super.engineInit(n, secureRandom);
            this.keySizeSet = true;
        }
        
        @Override
        protected SecretKey engineGenerateKey() {
            if (this.uninitialised) {
                this.engine.init(new KeyGenerationParameters(new SecureRandom(), this.defaultKeySize));
                this.uninitialised = false;
            }
            if (!this.keySizeSet) {
                final byte[] generateKey = this.engine.generateKey();
                System.arraycopy(generateKey, 0, generateKey, 16, 8);
                return new SecretKeySpec(generateKey, this.algName);
            }
            return new SecretKeySpec(this.engine.generateKey(), this.algName);
        }
    }
    
    public static class DESede3 extends JCEKeyGenerator
    {
        public DESede3() {
            super("DESede3", 192, new DESedeKeyGenerator());
        }
    }
    
    public static class GOST28147 extends JCEKeyGenerator
    {
        public GOST28147() {
            super("GOST28147", 256, new CipherKeyGenerator());
        }
    }
    
    public static class HC128 extends JCEKeyGenerator
    {
        public HC128() {
            super("HC128", 128, new CipherKeyGenerator());
        }
    }
    
    public static class HC256 extends JCEKeyGenerator
    {
        public HC256() {
            super("HC256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class HMACSHA1 extends JCEKeyGenerator
    {
        public HMACSHA1() {
            super("HMACSHA1", 160, new CipherKeyGenerator());
        }
    }
    
    public static class HMACSHA224 extends JCEKeyGenerator
    {
        public HMACSHA224() {
            super("HMACSHA224", 224, new CipherKeyGenerator());
        }
    }
    
    public static class HMACSHA256 extends JCEKeyGenerator
    {
        public HMACSHA256() {
            super("HMACSHA256", 256, new CipherKeyGenerator());
        }
    }
    
    public static class HMACSHA384 extends JCEKeyGenerator
    {
        public HMACSHA384() {
            super("HMACSHA384", 384, new CipherKeyGenerator());
        }
    }
    
    public static class HMACSHA512 extends JCEKeyGenerator
    {
        public HMACSHA512() {
            super("HMACSHA512", 512, new CipherKeyGenerator());
        }
    }
    
    public static class HMACTIGER extends JCEKeyGenerator
    {
        public HMACTIGER() {
            super("HMACTIGER", 192, new CipherKeyGenerator());
        }
    }
    
    public static class MD2HMAC extends JCEKeyGenerator
    {
        public MD2HMAC() {
            super("HMACMD2", 128, new CipherKeyGenerator());
        }
    }
    
    public static class MD4HMAC extends JCEKeyGenerator
    {
        public MD4HMAC() {
            super("HMACMD4", 128, new CipherKeyGenerator());
        }
    }
    
    public static class MD5HMAC extends JCEKeyGenerator
    {
        public MD5HMAC() {
            super("HMACMD5", 128, new CipherKeyGenerator());
        }
    }
    
    public static class RC2 extends JCEKeyGenerator
    {
        public RC2() {
            super("RC2", 128, new CipherKeyGenerator());
        }
    }
    
    public static class RC4 extends JCEKeyGenerator
    {
        public RC4() {
            super("RC4", 128, new CipherKeyGenerator());
        }
    }
    
    public static class RC5 extends JCEKeyGenerator
    {
        public RC5() {
            super("RC5", 128, new CipherKeyGenerator());
        }
    }
    
    public static class RC564 extends JCEKeyGenerator
    {
        public RC564() {
            super("RC5-64", 256, new CipherKeyGenerator());
        }
    }
    
    public static class RC6 extends JCEKeyGenerator
    {
        public RC6() {
            super("RC6", 256, new CipherKeyGenerator());
        }
    }
    
    public static class RIPEMD128HMAC extends JCEKeyGenerator
    {
        public RIPEMD128HMAC() {
            super("HMACRIPEMD128", 128, new CipherKeyGenerator());
        }
    }
    
    public static class RIPEMD160HMAC extends JCEKeyGenerator
    {
        public RIPEMD160HMAC() {
            super("HMACRIPEMD160", 160, new CipherKeyGenerator());
        }
    }
    
    public static class Rijndael extends JCEKeyGenerator
    {
        public Rijndael() {
            super("Rijndael", 192, new CipherKeyGenerator());
        }
    }
    
    public static class Salsa20 extends JCEKeyGenerator
    {
        public Salsa20() {
            super("Salsa20", 128, new CipherKeyGenerator());
        }
    }
    
    public static class Serpent extends JCEKeyGenerator
    {
        public Serpent() {
            super("Serpent", 192, new CipherKeyGenerator());
        }
    }
    
    public static class Skipjack extends JCEKeyGenerator
    {
        public Skipjack() {
            super("SKIPJACK", 80, new CipherKeyGenerator());
        }
    }
    
    public static class TEA extends JCEKeyGenerator
    {
        public TEA() {
            super("TEA", 128, new CipherKeyGenerator());
        }
    }
    
    public static class Twofish extends JCEKeyGenerator
    {
        public Twofish() {
            super("Twofish", 256, new CipherKeyGenerator());
        }
    }
    
    public static class VMPC extends JCEKeyGenerator
    {
        public VMPC() {
            super("VMPC", 128, new CipherKeyGenerator());
        }
    }
    
    public static class VMPCKSA3 extends JCEKeyGenerator
    {
        public VMPCKSA3() {
            super("VMPC-KSA3", 128, new CipherKeyGenerator());
        }
    }
    
    public static class XTEA extends JCEKeyGenerator
    {
        public XTEA() {
            super("XTEA", 128, new CipherKeyGenerator());
        }
    }
}
