// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.signers;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSABlindingParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;

public class PSSSigner implements Signer
{
    public static final byte TRAILER_IMPLICIT = -68;
    private Digest contentDigest;
    private Digest mgfDigest;
    private AsymmetricBlockCipher cipher;
    private SecureRandom random;
    private int hLen;
    private int sLen;
    private int emBits;
    private byte[] salt;
    private byte[] mDash;
    private byte[] block;
    private byte trailer;
    
    public PSSSigner(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final int n) {
        this(asymmetricBlockCipher, digest, n, (byte)(-68));
    }
    
    public PSSSigner(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final int n, final byte b) {
        this(asymmetricBlockCipher, digest, digest, n, b);
    }
    
    public PSSSigner(final AsymmetricBlockCipher cipher, final Digest contentDigest, final Digest mgfDigest, final int sLen, final byte trailer) {
        this.cipher = cipher;
        this.contentDigest = contentDigest;
        this.mgfDigest = mgfDigest;
        this.hLen = mgfDigest.getDigestSize();
        this.sLen = sLen;
        this.salt = new byte[sLen];
        this.mDash = new byte[8 + sLen + this.hLen];
        this.trailer = trailer;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        CipherParameters parameters;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            parameters = parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
        }
        else {
            parameters = cipherParameters;
            if (b) {
                this.random = new SecureRandom();
            }
        }
        this.cipher.init(b, parameters);
        RSAKeyParameters publicKey;
        if (parameters instanceof RSABlindingParameters) {
            publicKey = ((RSABlindingParameters)parameters).getPublicKey();
        }
        else {
            publicKey = (RSAKeyParameters)parameters;
        }
        this.emBits = publicKey.getModulus().bitLength() - 1;
        if (this.emBits < 8 * this.hLen + 8 * this.sLen + 9) {
            throw new IllegalArgumentException("key too small for specified hash and salt lengths");
        }
        this.block = new byte[(this.emBits + 7) / 8];
        this.reset();
    }
    
    private void clearBlock(final byte[] array) {
        for (int i = 0; i != array.length; ++i) {
            array[i] = 0;
        }
    }
    
    @Override
    public void update(final byte b) {
        this.contentDigest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.contentDigest.update(array, n, n2);
    }
    
    @Override
    public void reset() {
        this.contentDigest.reset();
    }
    
    @Override
    public byte[] generateSignature() throws CryptoException, DataLengthException {
        this.contentDigest.doFinal(this.mDash, this.mDash.length - this.hLen - this.sLen);
        if (this.sLen != 0) {
            this.random.nextBytes(this.salt);
            System.arraycopy(this.salt, 0, this.mDash, this.mDash.length - this.sLen, this.sLen);
        }
        final byte[] array = new byte[this.hLen];
        this.mgfDigest.update(this.mDash, 0, this.mDash.length);
        this.mgfDigest.doFinal(array, 0);
        this.block[this.block.length - this.sLen - 1 - this.hLen - 1] = 1;
        System.arraycopy(this.salt, 0, this.block, this.block.length - this.sLen - this.hLen - 1, this.sLen);
        final byte[] maskGeneratorFunction1 = this.maskGeneratorFunction1(array, 0, array.length, this.block.length - this.hLen - 1);
        for (int i = 0; i != maskGeneratorFunction1.length; ++i) {
            final byte[] block = this.block;
            final int n = i;
            block[n] ^= maskGeneratorFunction1[i];
        }
        final byte[] block2 = this.block;
        final int n2 = 0;
        block2[n2] &= (byte)(255 >> this.block.length * 8 - this.emBits);
        System.arraycopy(array, 0, this.block, this.block.length - this.hLen - 1, this.hLen);
        this.block[this.block.length - 1] = this.trailer;
        final byte[] processBlock = this.cipher.processBlock(this.block, 0, this.block.length);
        this.clearBlock(this.block);
        return processBlock;
    }
    
    @Override
    public boolean verifySignature(final byte[] array) {
        this.contentDigest.doFinal(this.mDash, this.mDash.length - this.hLen - this.sLen);
        try {
            final byte[] processBlock = this.cipher.processBlock(array, 0, array.length);
            System.arraycopy(processBlock, 0, this.block, this.block.length - processBlock.length, processBlock.length);
        }
        catch (Exception ex) {
            return false;
        }
        if (this.block[this.block.length - 1] != this.trailer) {
            this.clearBlock(this.block);
            return false;
        }
        final byte[] maskGeneratorFunction1 = this.maskGeneratorFunction1(this.block, this.block.length - this.hLen - 1, this.hLen, this.block.length - this.hLen - 1);
        for (int i = 0; i != maskGeneratorFunction1.length; ++i) {
            final byte[] block = this.block;
            final int n = i;
            block[n] ^= maskGeneratorFunction1[i];
        }
        final byte[] block2 = this.block;
        final int n2 = 0;
        block2[n2] &= (byte)(255 >> this.block.length * 8 - this.emBits);
        for (int j = 0; j != this.block.length - this.hLen - this.sLen - 2; ++j) {
            if (this.block[j] != 0) {
                this.clearBlock(this.block);
                return false;
            }
        }
        if (this.block[this.block.length - this.hLen - this.sLen - 2] != 1) {
            this.clearBlock(this.block);
            return false;
        }
        System.arraycopy(this.block, this.block.length - this.sLen - this.hLen - 1, this.mDash, this.mDash.length - this.sLen, this.sLen);
        this.mgfDigest.update(this.mDash, 0, this.mDash.length);
        this.mgfDigest.doFinal(this.mDash, this.mDash.length - this.hLen);
        int n3 = this.block.length - this.hLen - 1;
        for (int k = this.mDash.length - this.hLen; k != this.mDash.length; ++k) {
            if ((this.block[n3] ^ this.mDash[k]) != 0x0) {
                this.clearBlock(this.mDash);
                this.clearBlock(this.block);
                return false;
            }
            ++n3;
        }
        this.clearBlock(this.mDash);
        this.clearBlock(this.block);
        return true;
    }
    
    private void ItoOSP(final int n, final byte[] array) {
        array[0] = (byte)(n >>> 24);
        array[1] = (byte)(n >>> 16);
        array[2] = (byte)(n >>> 8);
        array[3] = (byte)(n >>> 0);
    }
    
    private byte[] maskGeneratorFunction1(final byte[] array, final int n, final int n2, final int n3) {
        final byte[] array2 = new byte[n3];
        final byte[] array3 = new byte[this.hLen];
        final byte[] array4 = new byte[4];
        int i = 0;
        this.mgfDigest.reset();
        while (i < n3 / this.hLen) {
            this.ItoOSP(i, array4);
            this.mgfDigest.update(array, n, n2);
            this.mgfDigest.update(array4, 0, array4.length);
            this.mgfDigest.doFinal(array3, 0);
            System.arraycopy(array3, 0, array2, i * this.hLen, this.hLen);
            ++i;
        }
        if (i * this.hLen < n3) {
            this.ItoOSP(i, array4);
            this.mgfDigest.update(array, n, n2);
            this.mgfDigest.update(array4, 0, array4.length);
            this.mgfDigest.doFinal(array3, 0);
            System.arraycopy(array3, 0, array2, i * this.hLen, array2.length - i * this.hLen);
        }
        return array2;
    }
}
