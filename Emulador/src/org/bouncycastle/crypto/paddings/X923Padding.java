// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.paddings;

import org.bouncycastle.crypto.InvalidCipherTextException;
import java.security.SecureRandom;

public class X923Padding implements BlockCipherPadding
{
    SecureRandom random;
    
    public X923Padding() {
        this.random = null;
    }
    
    @Override
    public void init(final SecureRandom random) throws IllegalArgumentException {
        this.random = random;
    }
    
    @Override
    public String getPaddingName() {
        return "X9.23";
    }
    
    @Override
    public int addPadding(final byte[] array, int i) {
        final byte b = (byte)(array.length - i);
        while (i < array.length - 1) {
            if (this.random == null) {
                array[i] = 0;
            }
            else {
                array[i] = (byte)this.random.nextInt();
            }
            ++i;
        }
        return array[i] = b;
    }
    
    @Override
    public int padCount(final byte[] array) throws InvalidCipherTextException {
        final int n = array[array.length - 1] & 0xFF;
        if (n > array.length) {
            throw new InvalidCipherTextException("pad block corrupted");
        }
        return n;
    }
}
