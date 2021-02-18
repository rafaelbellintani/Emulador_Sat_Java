// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;

public class ShortenedDigest implements ExtendedDigest
{
    private ExtendedDigest baseDigest;
    private int length;
    
    public ShortenedDigest(final ExtendedDigest baseDigest, final int length) {
        if (baseDigest == null) {
            throw new IllegalArgumentException("baseDigest must not be null");
        }
        if (length > baseDigest.getDigestSize()) {
            throw new IllegalArgumentException("baseDigest output not large enough to support length");
        }
        this.baseDigest = baseDigest;
        this.length = length;
    }
    
    @Override
    public String getAlgorithmName() {
        return this.baseDigest.getAlgorithmName() + "(" + this.length * 8 + ")";
    }
    
    @Override
    public int getDigestSize() {
        return this.length;
    }
    
    @Override
    public void update(final byte b) {
        this.baseDigest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.baseDigest.update(array, n, n2);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final byte[] array2 = new byte[this.baseDigest.getDigestSize()];
        this.baseDigest.doFinal(array2, 0);
        System.arraycopy(array2, 0, array, n, this.length);
        return this.length;
    }
    
    @Override
    public void reset() {
        this.baseDigest.reset();
    }
    
    @Override
    public int getByteLength() {
        return this.baseDigest.getByteLength();
    }
}
