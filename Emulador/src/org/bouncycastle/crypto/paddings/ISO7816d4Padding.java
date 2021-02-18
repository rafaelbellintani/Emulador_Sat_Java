// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.paddings;

import org.bouncycastle.crypto.InvalidCipherTextException;
import java.security.SecureRandom;

public class ISO7816d4Padding implements BlockCipherPadding
{
    @Override
    public void init(final SecureRandom secureRandom) throws IllegalArgumentException {
    }
    
    @Override
    public String getPaddingName() {
        return "ISO7816-4";
    }
    
    @Override
    public int addPadding(final byte[] array, int i) {
        final int n = array.length - i;
        array[i] = -128;
        ++i;
        while (i < array.length) {
            array[i] = 0;
            ++i;
        }
        return n;
    }
    
    @Override
    public int padCount(final byte[] array) throws InvalidCipherTextException {
        int n;
        for (n = array.length - 1; n > 0 && array[n] == 0; --n) {}
        if (array[n] != -128) {
            throw new InvalidCipherTextException("pad block corrupted");
        }
        return array.length - n;
    }
}
