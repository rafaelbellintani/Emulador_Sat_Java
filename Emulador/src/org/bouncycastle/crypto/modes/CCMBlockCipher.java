// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.AEADParameters;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.BlockCipher;

public class CCMBlockCipher implements AEADBlockCipher
{
    private BlockCipher cipher;
    private int blockSize;
    private boolean forEncryption;
    private byte[] nonce;
    private byte[] associatedText;
    private int macSize;
    private CipherParameters keyParam;
    private byte[] macBlock;
    private ByteArrayOutputStream data;
    
    public CCMBlockCipher(final BlockCipher cipher) {
        this.data = new ByteArrayOutputStream();
        this.cipher = cipher;
        this.blockSize = cipher.getBlockSize();
        this.macBlock = new byte[this.blockSize];
        if (this.blockSize != 16) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
    }
    
    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        if (cipherParameters instanceof AEADParameters) {
            final AEADParameters aeadParameters = (AEADParameters)cipherParameters;
            this.nonce = aeadParameters.getNonce();
            this.associatedText = aeadParameters.getAssociatedText();
            this.macSize = aeadParameters.getMacSize() / 8;
            this.keyParam = aeadParameters.getKey();
        }
        else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to CCM");
            }
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.associatedText = null;
            this.macSize = this.macBlock.length / 2;
            this.keyParam = parametersWithIV.getParameters();
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CCM";
    }
    
    @Override
    public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        this.data.write(b);
        return 0;
    }
    
    @Override
    public int processBytes(final byte[] b, final int off, final int len, final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        this.data.write(b, off, len);
        return 0;
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) throws IllegalStateException, InvalidCipherTextException {
        final byte[] byteArray = this.data.toByteArray();
        final byte[] processPacket = this.processPacket(byteArray, 0, byteArray.length);
        System.arraycopy(processPacket, 0, array, n, processPacket.length);
        this.reset();
        return processPacket.length;
    }
    
    @Override
    public void reset() {
        this.cipher.reset();
        this.data.reset();
    }
    
    @Override
    public byte[] getMac() {
        final byte[] array = new byte[this.macSize];
        System.arraycopy(this.macBlock, 0, array, 0, array.length);
        return array;
    }
    
    @Override
    public int getUpdateOutputSize(final int n) {
        return 0;
    }
    
    @Override
    public int getOutputSize(final int n) {
        if (this.forEncryption) {
            return this.data.size() + n + this.macSize;
        }
        return this.data.size() + n - this.macSize;
    }
    
    public byte[] processPacket(final byte[] array, final int n, final int n2) throws IllegalStateException, InvalidCipherTextException {
        if (this.keyParam == null) {
            throw new IllegalStateException("CCM cipher unitialized.");
        }
        final SICBlockCipher sicBlockCipher = new SICBlockCipher(this.cipher);
        final byte[] array2 = new byte[this.blockSize];
        array2[0] = (byte)(15 - this.nonce.length - 1 & 0x7);
        System.arraycopy(this.nonce, 0, array2, 1, this.nonce.length);
        sicBlockCipher.init(this.forEncryption, new ParametersWithIV(this.keyParam, array2));
        byte[] array3;
        if (this.forEncryption) {
            int i = n;
            int n3 = 0;
            array3 = new byte[n2 + this.macSize];
            this.calculateMac(array, n, n2, this.macBlock);
            sicBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
            while (i < n2 - this.blockSize) {
                sicBlockCipher.processBlock(array, i, array3, n3);
                n3 += this.blockSize;
                i += this.blockSize;
            }
            final byte[] array4 = new byte[this.blockSize];
            System.arraycopy(array, i, array4, 0, n2 - i);
            sicBlockCipher.processBlock(array4, 0, array4, 0);
            System.arraycopy(array4, 0, array3, n3, n2 - i);
            final int n4 = n3 + (n2 - i);
            System.arraycopy(this.macBlock, 0, array3, n4, array3.length - n4);
        }
        else {
            int n5 = n;
            int j = 0;
            array3 = new byte[n2 - this.macSize];
            System.arraycopy(array, n + n2 - this.macSize, this.macBlock, 0, this.macSize);
            sicBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
            for (int k = this.macSize; k != this.macBlock.length; ++k) {
                this.macBlock[k] = 0;
            }
            while (j < array3.length - this.blockSize) {
                sicBlockCipher.processBlock(array, n5, array3, j);
                j += this.blockSize;
                n5 += this.blockSize;
            }
            final byte[] array5 = new byte[this.blockSize];
            System.arraycopy(array, n5, array5, 0, array3.length - j);
            sicBlockCipher.processBlock(array5, 0, array5, 0);
            System.arraycopy(array5, 0, array3, j, array3.length - j);
            final byte[] array6 = new byte[this.blockSize];
            this.calculateMac(array3, 0, array3.length, array6);
            if (!Arrays.constantTimeAreEqual(this.macBlock, array6)) {
                throw new InvalidCipherTextException("mac check in CCM failed");
            }
        }
        return array3;
    }
    
    private int calculateMac(final byte[] array, final int n, final int n2, final byte[] array2) {
        final CBCBlockCipherMac cbcBlockCipherMac = new CBCBlockCipherMac(this.cipher, this.macSize * 8);
        cbcBlockCipherMac.init(this.keyParam);
        final byte[] array3 = new byte[16];
        if (this.hasAssociatedText()) {
            final byte[] array4 = array3;
            final int n3 = 0;
            array4[n3] |= 0x40;
        }
        final byte[] array5 = array3;
        final int n4 = 0;
        array5[n4] |= (byte)(((cbcBlockCipherMac.getMacSize() - 2) / 2 & 0x7) << 3);
        final byte[] array6 = array3;
        final int n5 = 0;
        array6[n5] |= (byte)(15 - this.nonce.length - 1 & 0x7);
        System.arraycopy(this.nonce, 0, array3, 1, this.nonce.length);
        for (int i = n2, n6 = 1; i > 0; i >>>= 8, ++n6) {
            array3[array3.length - n6] = (byte)(i & 0xFF);
        }
        cbcBlockCipherMac.update(array3, 0, array3.length);
        if (this.hasAssociatedText()) {
            int n7;
            if (this.associatedText.length < 65280) {
                cbcBlockCipherMac.update((byte)(this.associatedText.length >> 8));
                cbcBlockCipherMac.update((byte)this.associatedText.length);
                n7 = 2;
            }
            else {
                cbcBlockCipherMac.update((byte)(-1));
                cbcBlockCipherMac.update((byte)(-2));
                cbcBlockCipherMac.update((byte)(this.associatedText.length >> 24));
                cbcBlockCipherMac.update((byte)(this.associatedText.length >> 16));
                cbcBlockCipherMac.update((byte)(this.associatedText.length >> 8));
                cbcBlockCipherMac.update((byte)this.associatedText.length);
                n7 = 6;
            }
            cbcBlockCipherMac.update(this.associatedText, 0, this.associatedText.length);
            final int n8 = (n7 + this.associatedText.length) % 16;
            if (n8 != 0) {
                for (int j = 0; j != 16 - n8; ++j) {
                    cbcBlockCipherMac.update((byte)0);
                }
            }
        }
        cbcBlockCipherMac.update(array, n, n2);
        return cbcBlockCipherMac.doFinal(array2, 0);
    }
    
    private boolean hasAssociatedText() {
        return this.associatedText != null && this.associatedText.length != 0;
    }
}
