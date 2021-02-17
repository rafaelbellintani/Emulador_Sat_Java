// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

public final class Arrays
{
    private Arrays() {
    }
    
    public static boolean areEqual(final boolean[] array, final boolean[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null || array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean areEqual(final byte[] array, final byte[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null || array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean constantTimeAreEqual(final byte[] array, final byte[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null || array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        int n = 0;
        for (int i = 0; i != array.length; ++i) {
            n |= (array[i] ^ array2[i]);
        }
        return n == 0;
    }
    
    public static boolean areEqual(final int[] array, final int[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null || array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static void fill(final byte[] array, final byte b) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = b;
        }
    }
    
    public static void fill(final long[] array, final long n) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = n;
        }
    }
    
    public static void fill(final short[] array, final short n) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = n;
        }
    }
    
    public static int hashCode(final byte[] array) {
        if (array == null) {
            return 0;
        }
        int length = array.length;
        int n = length + 1;
        while (--length >= 0) {
            n = (n * 257 ^ array[length]);
        }
        return n;
    }
    
    public static byte[] clone(final byte[] array) {
        if (array == null) {
            return null;
        }
        final byte[] array2 = new byte[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static int[] clone(final int[] array) {
        if (array == null) {
            return null;
        }
        final int[] array2 = new int[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
}
