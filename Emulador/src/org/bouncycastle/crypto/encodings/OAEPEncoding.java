// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.encodings;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.digests.SHA1Digest;
import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.AsymmetricBlockCipher;

public class OAEPEncoding implements AsymmetricBlockCipher
{
    private byte[] defHash;
    private Digest hash;
    private Digest mgf1Hash;
    private AsymmetricBlockCipher engine;
    private SecureRandom random;
    private boolean forEncryption;
    
    public OAEPEncoding(final AsymmetricBlockCipher asymmetricBlockCipher) {
        this(asymmetricBlockCipher, new SHA1Digest(), null);
    }
    
    public OAEPEncoding(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest) {
        this(asymmetricBlockCipher, digest, null);
    }
    
    public OAEPEncoding(final AsymmetricBlockCipher asymmetricBlockCipher, final Digest digest, final byte[] array) {
        this(asymmetricBlockCipher, digest, digest, array);
    }
    
    public OAEPEncoding(final AsymmetricBlockCipher engine, final Digest hash, final Digest mgf1Hash, final byte[] array) {
        this.engine = engine;
        this.hash = hash;
        this.mgf1Hash = mgf1Hash;
        this.defHash = new byte[hash.getDigestSize()];
        if (array != null) {
            hash.update(array, 0, array.length);
        }
        hash.doFinal(this.defHash, 0);
    }
    
    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            this.random = ((ParametersWithRandom)cipherParameters).getRandom();
        }
        else {
            this.random = new SecureRandom();
        }
        this.engine.init(forEncryption, cipherParameters);
        this.forEncryption = forEncryption;
    }
    
    @Override
    public int getInputBlockSize() {
        final int inputBlockSize = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            return inputBlockSize - 1 - 2 * this.defHash.length;
        }
        return inputBlockSize;
    }
    
    @Override
    public int getOutputBlockSize() {
        final int outputBlockSize = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return outputBlockSize;
        }
        return outputBlockSize - 1 - 2 * this.defHash.length;
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (this.forEncryption) {
            return this.encodeBlock(array, n, n2);
        }
        return this.decodeBlock(array, n, n2);
    }
    
    public byte[] encodeBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        final byte[] array2 = new byte[this.getInputBlockSize() + 1 + 2 * this.defHash.length];
        System.arraycopy(array, n, array2, array2.length - n2, n2);
        array2[array2.length - n2 - 1] = 1;
        System.arraycopy(this.defHash, 0, array2, this.defHash.length, this.defHash.length);
        final byte[] bytes = new byte[this.defHash.length];
        this.random.nextBytes(bytes);
        final byte[] maskGeneratorFunction1 = this.maskGeneratorFunction1(bytes, 0, bytes.length, array2.length - this.defHash.length);
        for (int i = this.defHash.length; i != array2.length; ++i) {
            final byte[] array3 = array2;
            final int n3 = i;
            array3[n3] ^= maskGeneratorFunction1[i - this.defHash.length];
        }
        System.arraycopy(bytes, 0, array2, 0, this.defHash.length);
        final byte[] maskGeneratorFunction2 = this.maskGeneratorFunction1(array2, this.defHash.length, array2.length - this.defHash.length, this.defHash.length);
        for (int j = 0; j != this.defHash.length; ++j) {
            final byte[] array4 = array2;
            final int n4 = j;
            array4[n4] ^= maskGeneratorFunction2[j];
        }
        return this.engine.processBlock(array2, 0, array2.length);
    }
    
    public byte[] decodeBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        final byte[] processBlock = this.engine.processBlock(array, n, n2);
        byte[] array2;
        if (processBlock.length < this.engine.getOutputBlockSize()) {
            array2 = new byte[this.engine.getOutputBlockSize()];
            System.arraycopy(processBlock, 0, array2, array2.length - processBlock.length, processBlock.length);
        }
        else {
            array2 = processBlock;
        }
        if (array2.length < 2 * this.defHash.length + 1) {
            throw new InvalidCipherTextException("data too short");
        }
        final byte[] maskGeneratorFunction1 = this.maskGeneratorFunction1(array2, this.defHash.length, array2.length - this.defHash.length, this.defHash.length);
        for (int i = 0; i != this.defHash.length; ++i) {
            final byte[] array3 = array2;
            final int n3 = i;
            array3[n3] ^= maskGeneratorFunction1[i];
        }
        final byte[] maskGeneratorFunction2 = this.maskGeneratorFunction1(array2, 0, this.defHash.length, array2.length - this.defHash.length);
        for (int j = this.defHash.length; j != array2.length; ++j) {
            final byte[] array4 = array2;
            final int n4 = j;
            array4[n4] ^= maskGeneratorFunction2[j - this.defHash.length];
        }
        for (int k = 0; k != this.defHash.length; ++k) {
            if (this.defHash[k] != array2[this.defHash.length + k]) {
                throw new InvalidCipherTextException("data hash wrong");
            }
        }
        int l;
        for (l = 2 * this.defHash.length; l != array2.length && array2[l] == 0; ++l) {}
        if (l >= array2.length - 1 || array2[l] != 1) {
            throw new InvalidCipherTextException("data start wrong " + l);
        }
        ++l;
        final byte[] array5 = new byte[array2.length - l];
        System.arraycopy(array2, l, array5, 0, array5.length);
        return array5;
    }
    
    private void ItoOSP(final int n, final byte[] array) {
        array[0] = (byte)(n >>> 24);
        array[1] = (byte)(n >>> 16);
        array[2] = (byte)(n >>> 8);
        array[3] = (byte)(n >>> 0);
    }
    
    private byte[] maskGeneratorFunction1(final byte[] array, final int n, final int n2, final int n3) {
        final byte[] array2 = new byte[n3];
        final byte[] array3 = new byte[this.mgf1Hash.getDigestSize()];
        final byte[] array4 = new byte[4];
        int n4 = 0;
        this.hash.reset();
        do {
            this.ItoOSP(n4, array4);
            this.mgf1Hash.update(array, n, n2);
            this.mgf1Hash.update(array4, 0, array4.length);
            this.mgf1Hash.doFinal(array3, 0);
            System.arraycopy(array3, 0, array2, n4 * array3.length, array3.length);
        } while (++n4 < n3 / array3.length);
        if (n4 * array3.length < n3) {
            this.ItoOSP(n4, array4);
            this.mgf1Hash.update(array, n, n2);
            this.mgf1Hash.update(array4, 0, array4.length);
            this.mgf1Hash.doFinal(array3, 0);
            System.arraycopy(array3, 0, array2, n4 * array3.length, array2.length - n4 * array3.length);
        }
        return array2;
    }
}
