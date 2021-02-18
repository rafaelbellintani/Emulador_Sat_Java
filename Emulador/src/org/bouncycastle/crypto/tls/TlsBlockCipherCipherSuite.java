// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.BlockCipher;

public class TlsBlockCipherCipherSuite extends TlsCipherSuite
{
    private BlockCipher encryptCipher;
    private BlockCipher decryptCipher;
    private Digest writeDigest;
    private Digest readDigest;
    private int cipherKeySize;
    private short keyExchange;
    private TlsMac writeMac;
    private TlsMac readMac;
    
    protected TlsBlockCipherCipherSuite(final BlockCipher encryptCipher, final BlockCipher decryptCipher, final Digest writeDigest, final Digest readDigest, final int cipherKeySize, final short keyExchange) {
        this.encryptCipher = encryptCipher;
        this.decryptCipher = decryptCipher;
        this.writeDigest = writeDigest;
        this.readDigest = readDigest;
        this.cipherKeySize = cipherKeySize;
        this.keyExchange = keyExchange;
    }
    
    @Override
    protected void init(final byte[] array, final byte[] array2, final byte[] array3) {
        final byte[] array4 = new byte[2 * this.cipherKeySize + 2 * this.writeDigest.getDigestSize() + 2 * this.encryptCipher.getBlockSize()];
        final byte[] array5 = new byte[array2.length + array3.length];
        System.arraycopy(array2, 0, array5, array3.length, array2.length);
        System.arraycopy(array3, 0, array5, 0, array3.length);
        TlsUtils.PRF(array, TlsUtils.toByteArray("key expansion"), array5, array4);
        final int n = 0;
        this.writeMac = new TlsMac(this.writeDigest, array4, n, this.writeDigest.getDigestSize());
        final int n2 = n + this.writeDigest.getDigestSize();
        this.readMac = new TlsMac(this.readDigest, array4, n2, this.readDigest.getDigestSize());
        final int n3 = n2 + this.readDigest.getDigestSize();
        this.initCipher(true, this.encryptCipher, array4, this.cipherKeySize, n3, n3 + this.cipherKeySize * 2);
        final int n4 = n3 + this.cipherKeySize;
        this.initCipher(false, this.decryptCipher, array4, this.cipherKeySize, n4, n4 + this.cipherKeySize + this.decryptCipher.getBlockSize());
    }
    
    private void initCipher(final boolean b, final BlockCipher blockCipher, final byte[] array, final int n, final int n2, final int n3) {
        blockCipher.init(b, new ParametersWithIV(new KeyParameter(array, n2, n), array, n3, blockCipher.getBlockSize()));
    }
    
    @Override
    protected byte[] encodePlaintext(final short n, final byte[] array, final int n2, final int n3) {
        final int blockSize = this.encryptCipher.getBlockSize();
        final int n4 = blockSize - (n3 + this.writeMac.getSize() + 1) % blockSize;
        final int n5 = n3 + this.writeMac.getSize() + n4 + 1;
        final byte[] array2 = new byte[n5];
        System.arraycopy(array, n2, array2, 0, n3);
        final byte[] calculateMac = this.writeMac.calculateMac(n, array, n2, n3);
        System.arraycopy(calculateMac, 0, array2, n3, calculateMac.length);
        final int n6 = n3 + calculateMac.length;
        for (int i = 0; i <= n4; ++i) {
            array2[i + n6] = (byte)n4;
        }
        for (int j = 0; j < n5; j += blockSize) {
            this.encryptCipher.processBlock(array2, j, array2, j);
        }
        return array2;
    }
    
    @Override
    protected byte[] decodeCiphertext(final short n, final byte[] array, final int n2, final int n3, final TlsProtocolHandler tlsProtocolHandler) throws IOException {
        final int blockSize = this.decryptCipher.getBlockSize();
        boolean b = false;
        for (int i = 0; i < n3; i += blockSize) {
            this.decryptCipher.processBlock(array, i + n2, array, i + n2);
        }
        byte b2 = array[n2 + n3 - 1];
        if (n2 + n3 - 1 - b2 < 0) {
            b = true;
            b2 = 0;
        }
        else {
            for (byte b3 = 0; b3 <= b2; ++b3) {
                if (array[n2 + n3 - 1 - b3] != b2) {
                    b = true;
                }
            }
        }
        final int n4 = n3 - this.readMac.getSize() - b2 - 1;
        final byte[] calculateMac = this.readMac.calculateMac(n, array, n2, n4);
        for (int j = 0; j < calculateMac.length; ++j) {
            if (array[n2 + n4 + j] != calculateMac[j]) {
                b = true;
            }
        }
        if (b) {
            tlsProtocolHandler.failWithError((short)2, (short)20);
        }
        final byte[] array2 = new byte[n4];
        System.arraycopy(array, n2, array2, 0, n4);
        return array2;
    }
    
    @Override
    protected short getKeyExchangeAlgorithm() {
        return this.keyExchange;
    }
}
