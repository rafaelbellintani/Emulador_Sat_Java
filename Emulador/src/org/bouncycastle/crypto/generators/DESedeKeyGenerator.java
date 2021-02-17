// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.DESedeParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class DESedeKeyGenerator extends DESKeyGenerator
{
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.random = keyGenerationParameters.getRandom();
        this.strength = (keyGenerationParameters.getStrength() + 7) / 8;
        if (this.strength == 0 || this.strength == 21) {
            this.strength = 24;
        }
        else if (this.strength == 14) {
            this.strength = 16;
        }
        else if (this.strength != 24 && this.strength != 16) {
            throw new IllegalArgumentException("DESede key must be 192 or 128 bits long.");
        }
    }
    
    @Override
    public byte[] generateKey() {
        final byte[] array = new byte[this.strength];
        do {
            this.random.nextBytes(array);
            DESParameters.setOddParity(array);
        } while (DESedeParameters.isWeakKey(array, 0, array.length));
        return array;
    }
}
