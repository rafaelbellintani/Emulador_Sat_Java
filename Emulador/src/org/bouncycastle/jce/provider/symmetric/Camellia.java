// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.symmetric;

import org.bouncycastle.crypto.engines.CamelliaWrapEngine;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.RFC3211WrapEngine;
import org.bouncycastle.jce.provider.WrapCipherSpi;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.jce.provider.JCEKeyGenerator;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.engines.CamelliaEngine;
import org.bouncycastle.jce.provider.JCEBlockCipher;
import org.bouncycastle.jce.provider.JDKAlgorithmParameters;
import javax.crypto.spec.IvParameterSpec;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.jce.provider.JDKAlgorithmParameterGenerator;

public final class Camellia
{
    private Camellia() {
    }
    
    public static class AlgParamGen extends JDKAlgorithmParameterGenerator
    {
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for Camellia parameter generation.");
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
                instance = AlgorithmParameters.getInstance("Camellia", "BC");
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
            return "Camellia IV";
        }
    }
    
    public static class CBC extends JCEBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new CamelliaEngine()), 128);
        }
    }
    
    public static class ECB extends JCEBlockCipher
    {
        public ECB() {
            super(new CamelliaEngine());
        }
    }
    
    public static class KeyGen extends JCEKeyGenerator
    {
        public KeyGen() {
            this(256);
        }
        
        public KeyGen(final int n) {
            super("Camellia", n, new CipherKeyGenerator());
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
    
    public static class RFC3211Wrap extends WrapCipherSpi
    {
        public RFC3211Wrap() {
            super(new RFC3211WrapEngine(new CamelliaEngine()), 16);
        }
    }
    
    public static class Wrap extends WrapCipherSpi
    {
        public Wrap() {
            super(new CamelliaWrapEngine());
        }
    }
}
