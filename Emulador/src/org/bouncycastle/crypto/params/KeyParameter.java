// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class KeyParameter implements CipherParameters
{
    private byte[] key;
    
    public KeyParameter(final byte[] array) {
        this(array, 0, array.length);
    }
    
    public KeyParameter(final byte[] array, final int n, final int n2) {
        System.arraycopy(array, n, this.key = new byte[n2], 0, n2);
    }
    
    public byte[] getKey() {
        return this.key;
    }
}
