// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import org.bouncycastle.util.Arrays;

public class DSAValidationParameters
{
    private byte[] seed;
    private int counter;
    
    public DSAValidationParameters(final byte[] seed, final int counter) {
        this.seed = seed;
        this.counter = counter;
    }
    
    public int getCounter() {
        return this.counter;
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
    
    @Override
    public int hashCode() {
        return this.counter ^ Arrays.hashCode(this.seed);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DSAValidationParameters)) {
            return false;
        }
        final DSAValidationParameters dsaValidationParameters = (DSAValidationParameters)o;
        return dsaValidationParameters.counter == this.counter && Arrays.areEqual(this.seed, dsaValidationParameters.seed);
    }
}
