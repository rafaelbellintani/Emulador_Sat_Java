// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.symmetric;

import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.engines.SEEDWrapEngine;
import org.bouncycastle.jce.provider.WrapCipherSpi;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.jce.provider.JCEKeyGenerator;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.jce.provider.JCEBlockCipher;
import org.bouncycastle.jce.provider.JDKAlgorithmParameters;
import javax.crypto.spec.IvParameterSpec;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.jce.provider.JDKAlgorithmParameterGenerator;

public final class SEED
{
    private SEED() {
    }
    
    public static class AlgParamGen extends JDKAlgorithmParameterGenerator
    {
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for SEED parameter generation.");
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
                instance = AlgorithmParameters.getInstance("SEED", "BC");
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
            return "SEED IV";
        }
    }
    
    public static class CBC extends JCEBlockCipher
    {
        public CBC() {
            super(new CBCBlockCipher(new SEEDEngine()), 128);
        }
    }
    
    public static class ECB extends JCEBlockCipher
    {
        public ECB() {
            super(new SEEDEngine());
        }
    }
    
    public static class KeyGen extends JCEKeyGenerator
    {
        public KeyGen() {
            super("SEED", 128, new CipherKeyGenerator());
        }
    }
    
    public static class Wrap extends WrapCipherSpi
    {
        public Wrap() {
            super(new SEEDWrapEngine());
        }
    }
}
