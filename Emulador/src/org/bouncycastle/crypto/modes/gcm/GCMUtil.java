// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.crypto.util.Pack;

abstract class GCMUtil
{
    static int[] asInts(final byte[] array) {
        return new int[] { Pack.bigEndianToInt(array, 0), Pack.bigEndianToInt(array, 4), Pack.bigEndianToInt(array, 8), Pack.bigEndianToInt(array, 12) };
    }
    
    static void multiplyP(final int[] array) {
        final boolean b = (array[3] & 0x1) != 0x0;
        shiftRight(array);
        if (b) {
            final int n = 0;
            array[n] ^= 0xE1000000;
        }
    }
    
    static void multiplyP8(final int[] array) {
        for (int i = 8; i != 0; --i) {
            multiplyP(array);
        }
    }
    
    static void shiftRight(final byte[] array) {
        int n = 0;
        int n2 = 0;
        while (true) {
            final int n3 = array[n] & 0xFF;
            array[n] = (byte)(n3 >>> 1 | n2);
            if (++n == 16) {
                break;
            }
            n2 = (n3 & 0x1) << 7;
        }
    }
    
    static void shiftRight(final int[] array) {
        int n = 0;
        int n2 = 0;
        while (true) {
            final int n3 = array[n];
            array[n] = (n3 >>> 1 | n2);
            if (++n == 4) {
                break;
            }
            n2 = n3 << 31;
        }
    }
    
    static void xor(final byte[] array, final byte[] array2) {
        for (int i = 15; i >= 0; --i) {
            final int n = i;
            array[n] ^= array2[i];
        }
    }
    
    static void xor(final int[] array, final int[] array2) {
        for (int i = 3; i >= 0; --i) {
            final int n = i;
            array[n] ^= array2[i];
        }
    }
}
