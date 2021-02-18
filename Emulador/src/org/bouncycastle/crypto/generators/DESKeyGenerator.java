// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.CipherKeyGenerator;

public class DESKeyGenerator extends CipherKeyGenerator
{
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        super.init(keyGenerationParameters);
        if (this.strength == 0 || this.strength == 7) {
            this.strength = 8;
        }
        else if (this.strength != 8) {
            throw new IllegalArgumentException("DES key must be 64 bits long.");
        }
    }
    
    @Override
    public byte[] generateKey() {
        final byte[] array = new byte[8];
        do {
            this.random.nextBytes(array);
            DESParameters.setOddParity(array);
        } while (DESParameters.isWeakKey(array, 0));
        return array;
    }
}
