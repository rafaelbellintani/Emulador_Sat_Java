// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.Mac;

public class CMac implements Mac
{
    private static final byte CONSTANT_128 = -121;
    private static final byte CONSTANT_64 = 27;
    private byte[] ZEROES;
    private byte[] mac;
    private byte[] buf;
    private int bufOff;
    private BlockCipher cipher;
    private int macSize;
    private byte[] L;
    private byte[] Lu;
    private byte[] Lu2;
    
    public CMac(final BlockCipher blockCipher) {
        this(blockCipher, blockCipher.getBlockSize() * 8);
    }
    
    public CMac(final BlockCipher blockCipher, final int n) {
        if (n % 8 != 0) {
            throw new IllegalArgumentException("MAC size must be multiple of 8");
        }
        if (n > blockCipher.getBlockSize() * 8) {
            throw new IllegalArgumentException("MAC size must be less or equal to " + blockCipher.getBlockSize() * 8);
        }
        if (blockCipher.getBlockSize() != 8 && blockCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("Block size must be either 64 or 128 bits");
        }
        this.cipher = new CBCBlockCipher(blockCipher);
        this.macSize = n / 8;
        this.mac = new byte[blockCipher.getBlockSize()];
        this.buf = new byte[blockCipher.getBlockSize()];
        this.ZEROES = new byte[blockCipher.getBlockSize()];
        this.bufOff = 0;
    }
    
    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName();
    }
    
    private byte[] doubleLu(final byte[] array) {
        final int n = (array[0] & 0xFF) >> 7;
        final byte[] array2 = new byte[array.length];
        for (int i = 0; i < array.length - 1; ++i) {
            array2[i] = (byte)((array[i] << 1) + ((array[i + 1] & 0xFF) >> 7));
        }
        array2[array.length - 1] = (byte)(array[array.length - 1] << 1);
        if (n == 1) {
            final byte[] array3 = array2;
            final int n2 = array.length - 1;
            array3[n2] ^= (byte)((array.length == 16) ? -121 : 27);
        }
        return array2;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.reset();
        this.cipher.init(true, cipherParameters);
        this.L = new byte[this.ZEROES.length];
        this.cipher.processBlock(this.ZEROES, 0, this.L, 0);
        this.Lu = this.doubleLu(this.L);
        this.Lu2 = this.doubleLu(this.Lu);
        this.cipher.init(true, cipherParameters);
    }
    
    @Override
    public int getMacSize() {
        return this.macSize;
    }
    
    @Override
    public void update(final byte b) {
        if (this.bufOff == this.buf.length) {
            this.cipher.processBlock(this.buf, 0, this.mac, 0);
            this.bufOff = 0;
        }
        this.buf[this.bufOff++] = b;
    }
    
    @Override
    public void update(final byte[] array, int n, int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        final int blockSize = this.cipher.getBlockSize();
        final int n2 = blockSize - this.bufOff;
        if (i > n2) {
            System.arraycopy(array, n, this.buf, this.bufOff, n2);
            this.cipher.processBlock(this.buf, 0, this.mac, 0);
            this.bufOff = 0;
            for (i -= n2, n += n2; i > blockSize; i -= blockSize, n += blockSize) {
                this.cipher.processBlock(array, n, this.mac, 0);
            }
        }
        System.arraycopy(array, n, this.buf, this.bufOff, i);
        this.bufOff += i;
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        byte[] array2;
        if (this.bufOff == this.cipher.getBlockSize()) {
            array2 = this.Lu;
        }
        else {
            new ISO7816d4Padding().addPadding(this.buf, this.bufOff);
            array2 = this.Lu2;
        }
        for (int i = 0; i < this.mac.length; ++i) {
            final byte[] buf = this.buf;
            final int n2 = i;
            buf[n2] ^= array2[i];
        }
        this.cipher.processBlock(this.buf, 0, this.mac, 0);
        System.arraycopy(this.mac, 0, array, n, this.macSize);
        this.reset();
        return this.macSize;
    }
    
    @Override
    public void reset() {
        for (int i = 0; i < this.buf.length; ++i) {
            this.buf[i] = 0;
        }
        this.bufOff = 0;
        this.cipher.reset();
    }
}
