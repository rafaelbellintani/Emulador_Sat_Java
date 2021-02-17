// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.BlockCipher;

public class OFBBlockCipher implements BlockCipher
{
    private byte[] IV;
    private byte[] ofbV;
    private byte[] ofbOutV;
    private final int blockSize;
    private final BlockCipher cipher;
    
    public OFBBlockCipher(final BlockCipher cipher, final int n) {
        this.cipher = cipher;
        this.blockSize = n / 8;
        this.IV = new byte[cipher.getBlockSize()];
        this.ofbV = new byte[cipher.getBlockSize()];
        this.ofbOutV = new byte[cipher.getBlockSize()];
    }
    
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            final byte[] iv = parametersWithIV.getIV();
            if (iv.length < this.IV.length) {
                System.arraycopy(iv, 0, this.IV, this.IV.length - iv.length, iv.length);
                for (int i = 0; i < this.IV.length - iv.length; ++i) {
                    this.IV[i] = 0;
                }
            }
            else {
                System.arraycopy(iv, 0, this.IV, 0, this.IV.length);
            }
            this.reset();
            this.cipher.init(true, parametersWithIV.getParameters());
        }
        else {
            this.reset();
            this.cipher.init(true, cipherParameters);
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/OFB" + this.blockSize * 8;
    }
    
    @Override
    public int getBlockSize() {
        return this.blockSize;
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        if (n + this.blockSize > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + this.blockSize > array2.length) {
            throw new DataLengthException("output buffer too short");
        }
        this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
        for (int i = 0; i < this.blockSize; ++i) {
            array2[n2 + i] = (byte)(this.ofbOutV[i] ^ array[n + i]);
        }
        System.arraycopy(this.ofbV, this.blockSize, this.ofbV, 0, this.ofbV.length - this.blockSize);
        System.arraycopy(this.ofbOutV, 0, this.ofbV, this.ofbV.length - this.blockSize, this.blockSize);
        return this.blockSize;
    }
    
    @Override
    public void reset() {
        System.arraycopy(this.IV, 0, this.ofbV, 0, this.IV.length);
        this.cipher.reset();
    }
}
