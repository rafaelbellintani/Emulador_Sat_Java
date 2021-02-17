// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.BlockCipher;

public class NullEngine implements BlockCipher
{
    private boolean initialised;
    protected static final int BLOCK_SIZE = 1;
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.initialised = true;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Null";
    }
    
    @Override
    public int getBlockSize() {
        return 1;
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        if (!this.initialised) {
            throw new IllegalStateException("Null engine not initialised");
        }
        if (n + 1 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 1 > array2.length) {
            throw new DataLengthException("output buffer too short");
        }
        for (int i = 0; i < 1; ++i) {
            array2[n2 + i] = array[n + i];
        }
        return 1;
    }
    
    @Override
    public void reset() {
    }
}
