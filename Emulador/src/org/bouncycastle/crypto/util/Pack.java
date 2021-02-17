// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.util;

public abstract class Pack
{
    public static int bigEndianToInt(final byte[] array, int n) {
        return array[n] << 24 | (array[++n] & 0xFF) << 16 | (array[++n] & 0xFF) << 8 | (array[++n] & 0xFF);
    }
    
    public static void intToBigEndian(final int n, final byte[] array, int n2) {
        array[n2] = (byte)(n >>> 24);
        array[++n2] = (byte)(n >>> 16);
        array[++n2] = (byte)(n >>> 8);
        array[++n2] = (byte)n;
    }
    
    public static long bigEndianToLong(final byte[] array, final int n) {
        return ((long)bigEndianToInt(array, n) & 0xFFFFFFFFL) << 32 | ((long)bigEndianToInt(array, n + 4) & 0xFFFFFFFFL);
    }
    
    public static void longToBigEndian(final long n, final byte[] array, final int n2) {
        intToBigEndian((int)(n >>> 32), array, n2);
        intToBigEndian((int)(n & 0xFFFFFFFFL), array, n2 + 4);
    }
}
