// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.symmetric;

import org.bouncycastle.crypto.engines.AESWrapEngine;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.RFC3211WrapEngine;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.jce.provider.WrapCipherSpi;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.jce.provider.JCEKeyGenerator;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.jce.provider.JCEBlockCipher;
import org.bouncycastle.jce.provider.JDKAlgorithmParameters;
import javax.crypto.spec.IvParameterSpec;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.jce.provider.JDKAlgorithmParameterGenerator;

public final class AES
{
    private AES() {
    }
    
    public static class AlgParamGen extends JDKAlgorithmParameterGenerator
    {
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for AES parameter generation.");
        }
        
        @Override
        protected AlgorithmParameters engineGenerateParameters() {
            final byte[] array = new byte[16];
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            this.random.nextBytes(array);
            AlgorithmParameters instance;
            try {
                instance = AlgorithmParameters.getInstance("AES", "BC");
                instance.init(new IvParameterSpec(array));
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
            return instance;
        }
    }
    
    public static class AlgParams extends IVAlgorithmParameters
    {
        @Override
        protected String engineToString() {
            return "AES IV";
        }
    }
    
    public static class CBC extends JCEBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new AESFastEngine()), 128);
        }
    }
    
    public static class CFB extends JCEBlockCipher
    {
        public CFB() {
            super(new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 128)), 128);
        }
    }
    
    public static class ECB extends JCEBlockCipher
    {
        public ECB() {
            super(new AESFastEngine());
        }
    }
    
    public static class KeyGen extends JCEKeyGenerator
    {
        public KeyGen() {
            this(192);
        }
        
        public KeyGen(final int n) {
            super("AES", n, new CipherKeyGenerator());
        }
    }
    
    public static class KeyGen128 extends KeyGen
    {
        public KeyGen128() {
            super(128);
        }
    }
    
    public static class KeyGen192 extends KeyGen
    {
        public KeyGen192() {
            super(192);
        }
    }
    
    public static class KeyGen256 extends KeyGen
    {
        public KeyGen256() {
            super(256);
        }
    }
    
    public static class OFB extends JCEBlockCipher
    {
        public OFB() {
            super(new BufferedBlockCipher(new OFBBlockCipher(new AESFastEngine(), 128)), 128);
        }
    }
    
    public static class RFC3211Wrap extends WrapCipherSpi
    {
        public RFC3211Wrap() {
            super(new RFC3211WrapEngine(new AESEngine()), 16);
        }
    }
    
    public static class Wrap extends WrapCipherSpi
    {
        public Wrap() {
            super(new AESWrapEngine());
        }
    }
}
