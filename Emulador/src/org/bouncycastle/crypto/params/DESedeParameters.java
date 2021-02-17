// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

public class DESedeParameters extends DESParameters
{
    public static final int DES_EDE_KEY_LENGTH = 24;
    
    public DESedeParameters(final byte[] array) {
        super(array);
        if (isWeakKey(array, 0, array.length)) {
            throw new IllegalArgumentException("attempt to create weak DESede key");
        }
    }
    
    public static boolean isWeakKey(final byte[] array, final int n, final int n2) {
        for (int i = n; i < n2; i += 8) {
            if (DESParameters.isWeakKey(array, i)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isWeakKey(final byte[] array, final int n) {
        return isWeakKey(array, n, array.length - n);
    }
}
