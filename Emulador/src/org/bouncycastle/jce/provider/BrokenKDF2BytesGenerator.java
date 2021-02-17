// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.DerivationFunction;

public class BrokenKDF2BytesGenerator implements DerivationFunction
{
    private Digest digest;
    private byte[] shared;
    private byte[] iv;
    
    public BrokenKDF2BytesGenerator(final Digest digest) {
        this.digest = digest;
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        if (!(derivationParameters instanceof KDFParameters)) {
            throw new IllegalArgumentException("KDF parameters required for KDF2Generator");
        }
        final KDFParameters kdfParameters = (KDFParameters)derivationParameters;
        this.shared = kdfParameters.getSharedSecret();
        this.iv = kdfParameters.getIV();
    }
    
    @Override
    public Digest getDigest() {
        return this.digest;
    }
    
    @Override
    public int generateBytes(final byte[] array, int n, final int n2) throws DataLengthException, IllegalArgumentException {
        if (array.length - n2 < n) {
            throw new DataLengthException("output buffer too small");
        }
        final long n3 = n2 * 8;
        if (n3 > this.digest.getDigestSize() * 8 * 29L) {
            new IllegalArgumentException("Output length to large");
        }
        final int n4 = (int)(n3 / this.digest.getDigestSize());
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        for (int i = 1; i <= n4; ++i) {
            this.digest.update(this.shared, 0, this.shared.length);
            this.digest.update((byte)(i & 0xFF));
            this.digest.update((byte)(i >> 8 & 0xFF));
            this.digest.update((byte)(i >> 16 & 0xFF));
            this.digest.update((byte)(i >> 24 & 0xFF));
            this.digest.update(this.iv, 0, this.iv.length);
            this.digest.doFinal(array2, 0);
            if (n2 - n > array2.length) {
                System.arraycopy(array2, 0, array, n, array2.length);
                n += array2.length;
            }
            else {
                System.arraycopy(array2, 0, array, n, n2 - n);
            }
        }
        this.digest.reset();
        return n2;
    }
}
