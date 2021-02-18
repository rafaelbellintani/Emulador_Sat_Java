// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto;

public class StreamBlockCipher implements StreamCipher
{
    private BlockCipher cipher;
    private byte[] oneByte;
    
    public StreamBlockCipher(final BlockCipher cipher) {
        this.oneByte = new byte[1];
        if (cipher.getBlockSize() != 1) {
            throw new IllegalArgumentException("block cipher block size != 1.");
        }
        this.cipher = cipher;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        this.cipher.init(b, cipherParameters);
    }
    
    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName();
    }
    
    @Override
    public byte returnByte(final byte b) {
        this.oneByte[0] = b;
        this.cipher.processBlock(this.oneByte, 0, this.oneByte, 0);
        return this.oneByte[0];
    }
    
    @Override
    public void processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
        if (n3 + n2 > array2.length) {
            throw new DataLengthException("output buffer too small in processBytes()");
        }
        for (int i = 0; i != n2; ++i) {
            this.cipher.processBlock(array, n + i, array2, n3 + i);
        }
    }
    
    @Override
    public void reset() {
        this.cipher.reset();
    }
}
