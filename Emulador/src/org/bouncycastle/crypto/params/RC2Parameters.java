// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class RC2Parameters implements CipherParameters
{
    private byte[] key;
    private int bits;
    
    public RC2Parameters(final byte[] array) {
        this(array, (array.length > 128) ? 1024 : (array.length * 8));
    }
    
    public RC2Parameters(final byte[] array, final int bits) {
        this.key = new byte[array.length];
        this.bits = bits;
        System.arraycopy(array, 0, this.key, 0, array.length);
    }
    
    public byte[] getKey() {
        return this.key;
    }
    
    public int getEffectiveKeyBits() {
        return this.bits;
    }
}
