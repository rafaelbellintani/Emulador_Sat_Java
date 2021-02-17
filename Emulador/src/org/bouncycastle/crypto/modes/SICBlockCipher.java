// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.BlockCipher;

public class SICBlockCipher implements BlockCipher
{
    private final BlockCipher cipher;
    private final int blockSize;
    private byte[] IV;
    private byte[] counter;
    private byte[] counterOut;
    
    public SICBlockCipher(final BlockCipher cipher) {
        this.cipher = cipher;
        this.blockSize = this.cipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.counter = new byte[this.blockSize];
        this.counterOut = new byte[this.blockSize];
    }
    
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            System.arraycopy(parametersWithIV.getIV(), 0, this.IV, 0, this.IV.length);
            this.reset();
            this.cipher.init(true, parametersWithIV.getParameters());
            return;
        }
        throw new IllegalArgumentException("SIC mode requires ParametersWithIV");
    }
    
    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/SIC";
    }
    
    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
        for (int i = 0; i < this.counterOut.length; ++i) {
            array2[n2 + i] = (byte)(this.counterOut[i] ^ array[n + i]);
        }
        int n3 = 1;
        for (int j = this.counter.length - 1; j >= 0; --j) {
            final int n4 = (this.counter[j] & 0xFF) + n3;
            if (n4 > 255) {
                n3 = 1;
            }
            else {
                n3 = 0;
            }
            this.counter[j] = (byte)n4;
        }
        return this.counter.length;
    }
    
    @Override
    public void reset() {
        System.arraycopy(this.IV, 0, this.counter, 0, this.counter.length);
        this.cipher.reset();
    }
}
