// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.IESWithCipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.crypto.params.IESParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.BasicAgreement;

public class IESEngine
{
    BasicAgreement agree;
    DerivationFunction kdf;
    Mac mac;
    BufferedBlockCipher cipher;
    byte[] macBuf;
    boolean forEncryption;
    CipherParameters privParam;
    CipherParameters pubParam;
    IESParameters param;
    
    public IESEngine(final BasicAgreement agree, final DerivationFunction kdf, final Mac mac) {
        this.agree = agree;
        this.kdf = kdf;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = null;
    }
    
    public IESEngine(final BasicAgreement agree, final DerivationFunction kdf, final Mac mac, final BufferedBlockCipher cipher) {
        this.agree = agree;
        this.kdf = kdf;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = cipher;
    }
    
    public void init(final boolean forEncryption, final CipherParameters privParam, final CipherParameters pubParam, final CipherParameters cipherParameters) {
        this.forEncryption = forEncryption;
        this.privParam = privParam;
        this.pubParam = pubParam;
        this.param = (IESParameters)cipherParameters;
    }
    
    private byte[] decryptBlock(final byte[] array, int n, int n2, final byte[] array2) throws InvalidCipherTextException {
        final KDFParameters kdfParameters = new KDFParameters(array2, this.param.getDerivationV());
        final int macKeySize = this.param.getMacKeySize();
        this.kdf.init(kdfParameters);
        n2 -= this.mac.getMacSize();
        byte[] array3;
        KeyParameter keyParameter;
        if (this.cipher == null) {
            final byte[] generateKdfBytes = this.generateKdfBytes(kdfParameters, n2 + macKeySize / 8);
            array3 = new byte[n2];
            for (int i = 0; i != n2; ++i) {
                array3[i] = (byte)(array[n + i] ^ generateKdfBytes[i]);
            }
            keyParameter = new KeyParameter(generateKdfBytes, n2, macKeySize / 8);
        }
        else {
            final int cipherKeySize = ((IESWithCipherParameters)this.param).getCipherKeySize();
            final byte[] generateKdfBytes2 = this.generateKdfBytes(kdfParameters, cipherKeySize / 8 + macKeySize / 8);
            this.cipher.init(false, new KeyParameter(generateKdfBytes2, 0, cipherKeySize / 8));
            final byte[] array4 = new byte[this.cipher.getOutputSize(n2)];
            final int processBytes = this.cipher.processBytes(array, n, n2, array4, 0);
            final int n3 = processBytes + this.cipher.doFinal(array4, processBytes);
            array3 = new byte[n3];
            System.arraycopy(array4, 0, array3, 0, n3);
            keyParameter = new KeyParameter(generateKdfBytes2, cipherKeySize / 8, macKeySize / 8);
        }
        final byte[] encodingV = this.param.getEncodingV();
        this.mac.init(keyParameter);
        this.mac.update(array, n, n2);
        this.mac.update(encodingV, 0, encodingV.length);
        this.mac.doFinal(this.macBuf, 0);
        n += n2;
        for (int j = 0; j < this.macBuf.length; ++j) {
            if (this.macBuf[j] != array[n + j]) {
                throw new InvalidCipherTextException("Mac codes failed to equal.");
            }
        }
        return array3;
    }
    
    private byte[] encryptBlock(final byte[] array, final int n, final int n2, final byte[] array2) throws InvalidCipherTextException {
        final KDFParameters kdfParameters = new KDFParameters(array2, this.param.getDerivationV());
        final int macKeySize = this.param.getMacKeySize();
        byte[] array3;
        int n3;
        KeyParameter keyParameter;
        if (this.cipher == null) {
            final byte[] generateKdfBytes = this.generateKdfBytes(kdfParameters, n2 + macKeySize / 8);
            array3 = new byte[n2 + this.mac.getMacSize()];
            n3 = n2;
            for (int i = 0; i != n2; ++i) {
                array3[i] = (byte)(array[n + i] ^ generateKdfBytes[i]);
            }
            keyParameter = new KeyParameter(generateKdfBytes, n2, macKeySize / 8);
        }
        else {
            final int cipherKeySize = ((IESWithCipherParameters)this.param).getCipherKeySize();
            final byte[] generateKdfBytes2 = this.generateKdfBytes(kdfParameters, cipherKeySize / 8 + macKeySize / 8);
            this.cipher.init(true, new KeyParameter(generateKdfBytes2, 0, cipherKeySize / 8));
            final byte[] array4 = new byte[this.cipher.getOutputSize(n2)];
            final int processBytes = this.cipher.processBytes(array, n, n2, array4, 0);
            final int n4 = processBytes + this.cipher.doFinal(array4, processBytes);
            array3 = new byte[n4 + this.mac.getMacSize()];
            n3 = n4;
            System.arraycopy(array4, 0, array3, 0, n4);
            keyParameter = new KeyParameter(generateKdfBytes2, cipherKeySize / 8, macKeySize / 8);
        }
        final byte[] encodingV = this.param.getEncodingV();
        this.mac.init(keyParameter);
        this.mac.update(array3, 0, n3);
        this.mac.update(encodingV, 0, encodingV.length);
        this.mac.doFinal(array3, n3);
        return array3;
    }
    
    private byte[] generateKdfBytes(final KDFParameters kdfParameters, final int n) {
        final byte[] array = new byte[n];
        this.kdf.init(kdfParameters);
        this.kdf.generateBytes(array, 0, array.length);
        return array;
    }
    
    public byte[] processBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        this.agree.init(this.privParam);
        final BigInteger calculateAgreement = this.agree.calculateAgreement(this.pubParam);
        if (this.forEncryption) {
            return this.encryptBlock(array, n, n2, calculateAgreement.toByteArray());
        }
        return this.decryptBlock(array, n, n2, calculateAgreement.toByteArray());
    }
}
