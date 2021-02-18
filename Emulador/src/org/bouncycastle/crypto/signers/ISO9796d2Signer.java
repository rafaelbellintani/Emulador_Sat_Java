// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.signers;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.SignerWithRecovery;

public class ISO9796d2Signer implements SignerWithRecovery
{
    public static final int TRAILER_IMPLICIT = 188;
    public static final int TRAILER_RIPEMD160 = 12748;
    public static final int TRAILER_RIPEMD128 = 13004;
    public static final int TRAILER_SHA1 = 13260;
    private Digest digest;
    private AsymmetricBlockCipher cipher;
    private int trailer;
    private int keyBits;
    private byte[] block;
    private byte[] mBuf;
    private int messageLength;
    private boolean fullMessage;
    private byte[] recoveredMessage;
    
    public ISO9796d2Signer(final AsymmetricBlockCipher cipher, final Digest digest, final boolean b) {
        this.cipher = cipher;
        this.digest = digest;
        if (b) {
            this.trailer = 188;
        }
        else if (digest instanceof SHA1Digest) {
            this.trailer = 13260;
        }
        else if (digest instanceof RIPEMD160Digest) {
            this.trailer = 12748;
        }
        else {
            if (!(digest instanceof RIPEMD128Digest)) {
                throw new IllegalArgumentException("no valid trailer for digest");
            }
            this.trailer = 13004;
        }
    }
    
    public ISO9796d2Signer(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest) {
        this(asymmetricBlockCipher, digest, false);
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        final RSAKeyParameters rsaKeyParameters = (RSAKeyParameters)cipherParameters;
        this.cipher.init(b, rsaKeyParameters);
        this.keyBits = rsaKeyParameters.getModulus().bitLength();
        this.block = new byte[(this.keyBits + 7) / 8];
        if (this.trailer == 188) {
            this.mBuf = new byte[this.block.length - this.digest.getDigestSize() - 2];
        }
        else {
            this.mBuf = new byte[this.block.length - this.digest.getDigestSize() - 3];
        }
        this.reset();
    }
    
    private boolean isSameAs(final byte[] array, final byte[] array2) {
        if (this.messageLength > this.mBuf.length) {
            if (this.mBuf.length > array2.length) {
                return false;
            }
            for (int i = 0; i != this.mBuf.length; ++i) {
                if (array[i] != array2[i]) {
                    return false;
                }
            }
        }
        else {
            if (this.messageLength != array2.length) {
                return false;
            }
            for (int j = 0; j != array2.length; ++j) {
                if (array[j] != array2[j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void clearBlock(final byte[] array) {
        for (int i = 0; i != array.length; ++i) {
            array[i] = 0;
        }
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
        if (this.messageLength < this.mBuf.length) {
            this.mBuf[this.messageLength] = b;
        }
        ++this.messageLength;
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
        if (this.messageLength < this.mBuf.length) {
            for (int n3 = 0; n3 < n2 && n3 + this.messageLength < this.mBuf.length; ++n3) {
                this.mBuf[this.messageLength + n3] = array[n + n3];
            }
        }
        this.messageLength += n2;
    }
    
    @Override
    public void reset() {
        this.digest.reset();
        this.messageLength = 0;
        this.clearBlock(this.mBuf);
        if (this.recoveredMessage != null) {
            this.clearBlock(this.recoveredMessage);
        }
        this.recoveredMessage = null;
        this.fullMessage = false;
    }
    
    @Override
    public byte[] generateSignature() throws CryptoException {
        final int digestSize = this.digest.getDigestSize();
        int n;
        int n2;
        if (this.trailer == 188) {
            n = 8;
            n2 = this.block.length - digestSize - 1;
            this.digest.doFinal(this.block, n2);
            this.block[this.block.length - 1] = -68;
        }
        else {
            n = 16;
            n2 = this.block.length - digestSize - 2;
            this.digest.doFinal(this.block, n2);
            this.block[this.block.length - 2] = (byte)(this.trailer >>> 8);
            this.block[this.block.length - 1] = (byte)this.trailer;
        }
        final int n3 = (digestSize + this.messageLength) * 8 + n + 4 - this.keyBits;
        byte b;
        int n5;
        if (n3 > 0) {
            final int n4 = this.messageLength - (n3 + 7) / 8;
            b = 96;
            n5 = n2 - n4;
            System.arraycopy(this.mBuf, 0, this.block, n5, n4);
        }
        else {
            b = 64;
            n5 = n2 - this.messageLength;
            System.arraycopy(this.mBuf, 0, this.block, n5, this.messageLength);
        }
        if (n5 - 1 > 0) {
            for (int i = n5 - 1; i != 0; --i) {
                this.block[i] = -69;
            }
            final byte[] block = this.block;
            final int n6 = n5 - 1;
            block[n6] ^= 0x1;
            this.block[0] = 11;
            final byte[] block2 = this.block;
            final int n7 = 0;
            block2[n7] |= b;
        }
        else {
            this.block[0] = 10;
            final byte[] block3 = this.block;
            final int n8 = 0;
            block3[n8] |= b;
        }
        final byte[] processBlock = this.cipher.processBlock(this.block, 0, this.block.length);
        this.clearBlock(this.mBuf);
        this.clearBlock(this.block);
        return processBlock;
    }
    
    @Override
    public boolean verifySignature(final byte[] array) {
        byte[] processBlock;
        try {
            processBlock = this.cipher.processBlock(array, 0, array.length);
        }
        catch (Exception ex) {
            return false;
        }
        if (((processBlock[0] & 0xC0) ^ 0x40) != 0x0) {
            this.clearBlock(this.mBuf);
            this.clearBlock(processBlock);
            return false;
        }
        if (((processBlock[processBlock.length - 1] & 0xF) ^ 0xC) != 0x0) {
            this.clearBlock(this.mBuf);
            this.clearBlock(processBlock);
            return false;
        }
        int n;
        if (((processBlock[processBlock.length - 1] & 0xFF) ^ 0xBC) == 0x0) {
            n = 1;
        }
        else {
            switch ((processBlock[processBlock.length - 2] & 0xFF) << 8 | (processBlock[processBlock.length - 1] & 0xFF)) {
                case 12748: {
                    if (!(this.digest instanceof RIPEMD160Digest)) {
                        throw new IllegalStateException("signer should be initialised with RIPEMD160");
                    }
                    break;
                }
                case 13260: {
                    if (!(this.digest instanceof SHA1Digest)) {
                        throw new IllegalStateException("signer should be initialised with SHA1");
                    }
                    break;
                }
                case 13004: {
                    if (!(this.digest instanceof RIPEMD128Digest)) {
                        throw new IllegalStateException("signer should be initialised with RIPEMD128");
                    }
                    break;
                }
                default: {
                    throw new IllegalArgumentException("unrecognised hash in signature");
                }
            }
            n = 2;
        }
        int n2;
        for (n2 = 0; n2 != processBlock.length && ((processBlock[n2] & 0xF) ^ 0xA) != 0x0; ++n2) {}
        ++n2;
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        final int n3 = processBlock.length - n - array2.length;
        if (n3 - n2 <= 0) {
            this.clearBlock(this.mBuf);
            this.clearBlock(processBlock);
            return false;
        }
        if ((processBlock[0] & 0x20) == 0x0) {
            this.fullMessage = true;
            this.digest.reset();
            this.digest.update(processBlock, n2, n3 - n2);
            this.digest.doFinal(array2, 0);
            for (int i = 0; i != array2.length; ++i) {
                final byte[] array3 = processBlock;
                final int n4 = n3 + i;
                array3[n4] ^= array2[i];
                if (processBlock[n3 + i] != 0) {
                    this.clearBlock(this.mBuf);
                    this.clearBlock(processBlock);
                    return false;
                }
            }
            System.arraycopy(processBlock, n2, this.recoveredMessage = new byte[n3 - n2], 0, this.recoveredMessage.length);
        }
        else {
            this.fullMessage = false;
            this.digest.doFinal(array2, 0);
            for (int j = 0; j != array2.length; ++j) {
                final byte[] array4 = processBlock;
                final int n5 = n3 + j;
                array4[n5] ^= array2[j];
                if (processBlock[n3 + j] != 0) {
                    this.clearBlock(this.mBuf);
                    this.clearBlock(processBlock);
                    return false;
                }
            }
            System.arraycopy(processBlock, n2, this.recoveredMessage = new byte[n3 - n2], 0, this.recoveredMessage.length);
        }
        if (this.messageLength != 0 && !this.isSameAs(this.mBuf, this.recoveredMessage)) {
            this.clearBlock(this.mBuf);
            this.clearBlock(processBlock);
            return false;
        }
        this.clearBlock(this.mBuf);
        this.clearBlock(processBlock);
        return true;
    }
    
    @Override
    public boolean hasFullMessage() {
        return this.fullMessage;
    }
    
    @Override
    public byte[] getRecoveredMessage() {
        return this.recoveredMessage;
    }
}
