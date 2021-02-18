// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.digests;

import org.bouncycastle.crypto.ExtendedDigest;

public abstract class GeneralDigest implements ExtendedDigest
{
    private static final int BYTE_LENGTH = 64;
    private byte[] xBuf;
    private int xBufOff;
    private long byteCount;
    
    protected GeneralDigest() {
        this.xBuf = new byte[4];
        this.xBufOff = 0;
    }
    
    protected GeneralDigest(final GeneralDigest generalDigest) {
        this.xBuf = new byte[generalDigest.xBuf.length];
        System.arraycopy(generalDigest.xBuf, 0, this.xBuf, 0, generalDigest.xBuf.length);
        this.xBufOff = generalDigest.xBufOff;
        this.byteCount = generalDigest.byteCount;
    }
    
    @Override
    public void update(final byte b) {
        this.xBuf[this.xBufOff++] = b;
        if (this.xBufOff == this.xBuf.length) {
            this.processWord(this.xBuf, 0);
            this.xBufOff = 0;
        }
        ++this.byteCount;
    }
    
    @Override
    public void update(final byte[] array, int n, int i) {
        while (this.xBufOff != 0 && i > 0) {
            this.update(array[n]);
            ++n;
            --i;
        }
        while (i > this.xBuf.length) {
            this.processWord(array, n);
            n += this.xBuf.length;
            i -= this.xBuf.length;
            this.byteCount += this.xBuf.length;
        }
        while (i > 0) {
            this.update(array[n]);
            ++n;
            --i;
        }
    }
    
    public void finish() {
        final long n = this.byteCount << 3;
        this.update((byte)(-128));
        while (this.xBufOff != 0) {
            this.update((byte)0);
        }
        this.processLength(n);
        this.processBlock();
    }
    
    @Override
    public void reset() {
        this.byteCount = 0L;
        this.xBufOff = 0;
        for (int i = 0; i < this.xBuf.length; ++i) {
            this.xBuf[i] = 0;
        }
    }
    
    @Override
    public int getByteLength() {
        return 64;
    }
    
    protected abstract void processWord(final byte[] p0, final int p1);
    
    protected abstract void processLength(final long p0);
    
    protected abstract void processBlock();
}
