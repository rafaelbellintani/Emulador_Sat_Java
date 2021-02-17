// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import java.security.SecureRandom;
import java.math.BigInteger;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class RSAKeyGenerationParameters extends KeyGenerationParameters
{
    private BigInteger publicExponent;
    private int certainty;
    
    public RSAKeyGenerationParameters(final BigInteger publicExponent, final SecureRandom secureRandom, final int n, final int certainty) {
        super(secureRandom, n);
        if (n < 12) {
            throw new IllegalArgumentException("key strength too small");
        }
        if (!publicExponent.testBit(0)) {
            throw new IllegalArgumentException("public exponent cannot be even");
        }
        this.publicExponent = publicExponent;
        this.certainty = certainty;
    }
    
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
    
    public int getCertainty() {
        return this.certainty;
    }
}
