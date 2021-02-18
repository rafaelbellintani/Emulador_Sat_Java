// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import java.util.Random;
import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;

public class ECKeyPairGenerator implements AsymmetricCipherKeyPairGenerator, ECConstants
{
    ECDomainParameters params;
    SecureRandom random;
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        final ECKeyGenerationParameters ecKeyGenerationParameters = (ECKeyGenerationParameters)keyGenerationParameters;
        this.random = ecKeyGenerationParameters.getRandom();
        this.params = ecKeyGenerationParameters.getDomainParameters();
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final BigInteger n = this.params.getN();
        final int bitLength = n.bitLength();
        BigInteger bigInteger;
        do {
            bigInteger = new BigInteger(bitLength, this.random);
        } while (bigInteger.equals(ECKeyPairGenerator.ZERO) || bigInteger.compareTo(n) >= 0);
        return new AsymmetricCipherKeyPair(new ECPublicKeyParameters(this.params.getG().multiply(bigInteger), this.params), new ECPrivateKeyParameters(bigInteger, this.params));
    }
}
