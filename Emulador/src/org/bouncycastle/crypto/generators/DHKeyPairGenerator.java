// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;

public class DHKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private DHKeyGenerationParameters param;
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (DHKeyGenerationParameters)keyGenerationParameters;
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final DHKeyGeneratorHelper instance = DHKeyGeneratorHelper.INSTANCE;
        final DHParameters parameters = this.param.getParameters();
        final BigInteger calculatePrivate = instance.calculatePrivate(parameters, this.param.getRandom());
        return new AsymmetricCipherKeyPair(new DHPublicKeyParameters(instance.calculatePublic(parameters, calculatePrivate), parameters), new DHPrivateKeyParameters(calculatePrivate, parameters));
    }
}
