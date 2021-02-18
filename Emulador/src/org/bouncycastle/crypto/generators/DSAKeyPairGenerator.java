// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.util.BigIntegers;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;

public class DSAKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private static final BigInteger ONE;
    private DSAKeyGenerationParameters param;
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (DSAKeyGenerationParameters)keyGenerationParameters;
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final DSAParameters parameters = this.param.getParameters();
        final BigInteger generatePrivateKey = generatePrivateKey(parameters.getQ(), this.param.getRandom());
        return new AsymmetricCipherKeyPair(new DSAPublicKeyParameters(calculatePublicKey(parameters.getP(), parameters.getG(), generatePrivateKey), parameters), new DSAPrivateKeyParameters(generatePrivateKey, parameters));
    }
    
    private static BigInteger generatePrivateKey(final BigInteger bigInteger, final SecureRandom secureRandom) {
        return BigIntegers.createRandomInRange(DSAKeyPairGenerator.ONE, bigInteger.subtract(DSAKeyPairGenerator.ONE), secureRandom);
    }
    
    private static BigInteger calculatePublicKey(final BigInteger m, final BigInteger bigInteger, final BigInteger exponent) {
        return bigInteger.modPow(exponent, m);
    }
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
}
