// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto;

import java.security.SecureRandom;

public class CipherKeyGenerator
{
    protected SecureRandom random;
    protected int strength;
    
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.random = keyGenerationParameters.getRandom();
        this.strength = (keyGenerationParameters.getStrength() + 7) / 8;
    }
    
    public byte[] generateKey() {
        final byte[] bytes = new byte[this.strength];
        this.random.nextBytes(bytes);
        return bytes;
    }
}
