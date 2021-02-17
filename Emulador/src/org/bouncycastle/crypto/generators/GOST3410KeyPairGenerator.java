// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import java.security.SecureRandom;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle.crypto.params.GOST3410PublicKeyParameters;
import java.util.Random;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.GOST3410KeyGenerationParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;

public class GOST3410KeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private static final BigInteger ZERO;
    private GOST3410KeyGenerationParameters param;
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (GOST3410KeyGenerationParameters)keyGenerationParameters;
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final GOST3410Parameters parameters = this.param.getParameters();
        final SecureRandom random = this.param.getRandom();
        final BigInteger q = parameters.getQ();
        final BigInteger p = parameters.getP();
        final BigInteger a = parameters.getA();
        BigInteger exponent;
        do {
            exponent = new BigInteger(256, random);
        } while (exponent.equals(GOST3410KeyPairGenerator.ZERO) || exponent.compareTo(q) >= 0);
        return new AsymmetricCipherKeyPair(new GOST3410PublicKeyParameters(a.modPow(exponent, p), parameters), new GOST3410PrivateKeyParameters(exponent, parameters));
    }
    
    static {
        ZERO = BigInteger.valueOf(0L);
    }
}
