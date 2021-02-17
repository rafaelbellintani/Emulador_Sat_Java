// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.util.Arrays;

public class BasicGCMMultiplier implements GCMMultiplier
{
    private byte[] H;
    
    @Override
    public void init(final byte[] array) {
        this.H = Arrays.clone(array);
    }
    
    @Override
    public void multiplyH(final byte[] array) {
        final byte[] array2 = new byte[16];
        for (int i = 0; i < 16; ++i) {
            final byte b = this.H[i];
            for (int j = 7; j >= 0; --j) {
                if ((b & 1 << j) != 0x0) {
                    GCMUtil.xor(array2, array);
                }
                final boolean b2 = (array[15] & 0x1) != 0x0;
                GCMUtil.shiftRight(array);
                if (b2) {
                    final int n = 0;
                    array[n] ^= 0xFFFFFFE1;
                }
            }
        }
        System.arraycopy(array2, 0, array, 0, 16);
    }
}
