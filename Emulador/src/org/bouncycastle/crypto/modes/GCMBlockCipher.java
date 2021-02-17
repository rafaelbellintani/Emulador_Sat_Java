// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.util.Pack;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.modes.gcm.Tables8kGCMMultiplier;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.BlockCipher;

public class GCMBlockCipher implements AEADBlockCipher
{
    private static final int BLOCK_SIZE = 16;
    private static final byte[] ZEROES;
    private BlockCipher cipher;
    private GCMMultiplier multiplier;
    private boolean forEncryption;
    private int macSize;
    private byte[] nonce;
    private byte[] A;
    private KeyParameter keyParam;
    private byte[] H;
    private byte[] initS;
    private byte[] J0;
    private byte[] bufBlock;
    private byte[] macBlock;
    private byte[] S;
    private byte[] counter;
    private int bufOff;
    private long totalLength;
    
    public GCMBlockCipher(final BlockCipher blockCipher) {
        this(blockCipher, null);
    }
    
    public GCMBlockCipher(final BlockCipher cipher, GCMMultiplier multiplier) {
        if (cipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
        if (multiplier == null) {
            multiplier = new Tables8kGCMMultiplier();
        }
        this.cipher = cipher;
        this.multiplier = multiplier;
    }
    
    @Override
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/GCM";
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        this.macBlock = null;
        if (cipherParameters instanceof AEADParameters) {
            final AEADParameters aeadParameters = (AEADParameters)cipherParameters;
            this.nonce = aeadParameters.getNonce();
            this.A = aeadParameters.getAssociatedText();
            final int macSize = aeadParameters.getMacSize();
            if (macSize < 96 || macSize > 128 || macSize % 8 != 0) {
                throw new IllegalArgumentException("Invalid value for MAC size: " + macSize);
            }
            this.macSize = macSize / 8;
            this.keyParam = aeadParameters.getKey();
        }
        else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to GCM");
            }
            final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.A = null;
            this.macSize = 16;
            this.keyParam = (KeyParameter)parametersWithIV.getParameters();
        }
        this.bufBlock = new byte[forEncryption ? 16 : (16 + this.macSize)];
        if (this.nonce == null || this.nonce.length < 1) {
            throw new IllegalArgumentException("IV must be at least 1 byte");
        }
        if (this.A == null) {
            this.A = new byte[0];
        }
        this.cipher.init(true, this.keyParam);
        this.H = new byte[16];
        this.cipher.processBlock(GCMBlockCipher.ZEROES, 0, this.H, 0);
        this.multiplier.init(this.H);
        this.initS = this.gHASH(this.A);
        if (this.nonce.length == 12) {
            this.J0 = new byte[16];
            System.arraycopy(this.nonce, 0, this.J0, 0, this.nonce.length);
            this.J0[15] = 1;
        }
        else {
            this.J0 = this.gHASH(this.nonce);
            final byte[] array = new byte[16];
            packLength(this.nonce.length * 8L, array, 8);
            xor(this.J0, array);
            this.multiplier.multiplyH(this.J0);
        }
        this.S = Arrays.clone(this.initS);
        this.counter = Arrays.clone(this.J0);
        this.bufOff = 0;
        this.totalLength = 0L;
    }
    
    @Override
    public byte[] getMac() {
        return Arrays.clone(this.macBlock);
    }
    
    @Override
    public int getOutputSize(final int n) {
        if (this.forEncryption) {
            return n + this.bufOff + this.macSize;
        }
        return n + this.bufOff - this.macSize;
    }
    
    @Override
    public int getUpdateOutputSize(final int n) {
        return (n + this.bufOff) / 16 * 16;
    }
    
    @Override
    public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException {
        return this.process(b, array, n);
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
        int n4 = 0;
        for (int i = 0; i != n2; ++i) {
            this.bufBlock[this.bufOff++] = array[n + i];
            if (this.bufOff == this.bufBlock.length) {
                this.gCTRBlock(this.bufBlock, 16, array2, n3 + n4);
                if (!this.forEncryption) {
                    System.arraycopy(this.bufBlock, 16, this.bufBlock, 0, this.macSize);
                }
                this.bufOff = this.bufBlock.length - 16;
                n4 += 16;
            }
        }
        return n4;
    }
    
    private int process(final byte b, final byte[] array, final int n) throws DataLengthException {
        this.bufBlock[this.bufOff++] = b;
        if (this.bufOff == this.bufBlock.length) {
            this.gCTRBlock(this.bufBlock, 16, array, n);
            if (!this.forEncryption) {
                System.arraycopy(this.bufBlock, 16, this.bufBlock, 0, this.macSize);
            }
            this.bufOff = this.bufBlock.length - 16;
            return 16;
        }
        return 0;
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) throws IllegalStateException, InvalidCipherTextException {
        int bufOff = this.bufOff;
        if (!this.forEncryption) {
            if (bufOff < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            bufOff -= this.macSize;
        }
        if (bufOff > 0) {
            final byte[] array2 = new byte[16];
            System.arraycopy(this.bufBlock, 0, array2, 0, bufOff);
            this.gCTRBlock(array2, bufOff, array, n);
        }
        final byte[] array3 = new byte[16];
        packLength(this.A.length * 8L, array3, 0);
        packLength(this.totalLength * 8L, array3, 8);
        xor(this.S, array3);
        this.multiplier.multiplyH(this.S);
        final byte[] array4 = new byte[16];
        this.cipher.processBlock(this.J0, 0, array4, 0);
        xor(array4, this.S);
        int n2 = bufOff;
        System.arraycopy(array4, 0, this.macBlock = new byte[this.macSize], 0, this.macSize);
        if (this.forEncryption) {
            System.arraycopy(this.macBlock, 0, array, n + this.bufOff, this.macSize);
            n2 += this.macSize;
        }
        else {
            final byte[] array5 = new byte[this.macSize];
            System.arraycopy(this.bufBlock, bufOff, array5, 0, this.macSize);
            if (!Arrays.constantTimeAreEqual(this.macBlock, array5)) {
                throw new InvalidCipherTextException("mac check in GCM failed");
            }
        }
        this.reset(false);
        return n2;
    }
    
    @Override
    public void reset() {
        this.reset(true);
    }
    
    private void reset(final boolean b) {
        this.S = Arrays.clone(this.initS);
        this.counter = Arrays.clone(this.J0);
        this.bufOff = 0;
        this.totalLength = 0L;
        if (this.bufBlock != null) {
            Arrays.fill(this.bufBlock, (byte)0);
        }
        if (b) {
            this.macBlock = null;
        }
        this.cipher.reset();
    }
    
    private void gCTRBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        for (int n3 = 15; n3 >= 12 && (this.counter[n3] = (byte)(this.counter[n3] + 1 & 0xFF)) == 0; --n3) {}
        final byte[] array3 = new byte[16];
        this.cipher.processBlock(this.counter, 0, array3, 0);
        byte[] array4;
        if (this.forEncryption) {
            System.arraycopy(GCMBlockCipher.ZEROES, n, array3, n, 16 - n);
            array4 = array3;
        }
        else {
            array4 = array;
        }
        for (int i = n - 1; i >= 0; --i) {
            final byte[] array5 = array3;
            final int n4 = i;
            array5[n4] ^= array[i];
            array2[n2 + i] = array3[i];
        }
        xor(this.S, array4);
        this.multiplier.multiplyH(this.S);
        this.totalLength += n;
    }
    
    private byte[] gHASH(final byte[] array) {
        final byte[] array2 = new byte[16];
        for (int i = 0; i < array.length; i += 16) {
            final byte[] array3 = new byte[16];
            System.arraycopy(array, i, array3, 0, Math.min(array.length - i, 16));
            xor(array2, array3);
            this.multiplier.multiplyH(array2);
        }
        return array2;
    }
    
    private static void xor(final byte[] array, final byte[] array2) {
        for (int i = 15; i >= 0; --i) {
            final int n = i;
            array[n] ^= array2[i];
        }
    }
    
    private static void packLength(final long n, final byte[] array, final int n2) {
        Pack.intToBigEndian((int)(n >>> 32), array, n2);
        Pack.intToBigEndian((int)n, array, n2 + 4);
    }
    
    static {
        ZEROES = new byte[16];
    }
}
