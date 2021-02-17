// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.BlockCipher;

public class GOFBBlockCipher implements BlockCipher
{
    private byte[] IV;
    private byte[] ofbV;
    private byte[] ofbOutV;
    private final int blockSize;
    private final BlockCipher cipher;
    boolean firstStep;
    int N3;
    int N4;
    static final int C1 = 16843012;
    static final int C2 = 16843009;
    
    public GOFBBlockCipher(final BlockCipher cipher) {
        this.firstStep = true;
        this.cipher = cipher;
        this.blockSize = cipher.getBlockSize();
        if (this.blockSize != 8) {
            throw new IllegalArgumentException("GCTR only for 64 bit block ciphers");
        }
        this.IV = new byte[cipher.getBlockSize()];
        this.ofbV = new byte[cipher.getBlockSize()];
        this.ofbOutV = new byte[cipher.getBlockSize()];
    }
    
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.firstStep = true;
        this.N3 = 0;
        this.N4 = 0;
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
        return this.cipher.getAlgorithmName() + "/GCTR";
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
        if (this.firstStep) {
            this.firstStep = false;
            this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
            this.N3 = this.bytesToint(this.ofbOutV, 0);
            this.N4 = this.bytesToint(this.ofbOutV, 4);
        }
        this.N3 += 16843009;
        this.N4 += 16843012;
        this.intTobytes(this.N3, this.ofbV, 0);
        this.intTobytes(this.N4, this.ofbV, 4);
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
    
    private int bytesToint(final byte[] array, final int n) {
        return (array[n + 3] << 24 & 0xFF000000) + (array[n + 2] << 16 & 0xFF0000) + (array[n + 1] << 8 & 0xFF00) + (array[n] & 0xFF);
    }
    
    private void intTobytes(final int n, final byte[] array, final int n2) {
        array[n2 + 3] = (byte)(n >>> 24);
        array[n2 + 2] = (byte)(n >>> 16);
        array[n2 + 1] = (byte)(n >>> 8);
        array[n2] = (byte)n;
    }
}
