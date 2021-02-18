// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;

public class ElGamalKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private ElGamalKeyGenerationParameters param;
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (ElGamalKeyGenerationParameters)keyGenerationParameters;
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final DHKeyGeneratorHelper instance = DHKeyGeneratorHelper.INSTANCE;
        final ElGamalParameters parameters = this.param.getParameters();
        final DHParameters dhParameters = new DHParameters(parameters.getP(), parameters.getG(), null, parameters.getL());
        final BigInteger calculatePrivate = instance.calculatePrivate(dhParameters, this.param.getRandom());
        return new AsymmetricCipherKeyPair(new ElGamalPublicKeyParameters(instance.calculatePublic(dhParameters, calculatePrivate), parameters), new ElGamalPrivateKeyParameters(calculatePrivate, parameters));
    }
}
