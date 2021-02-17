// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

public class ByteQueue
{
    private static final int INITBUFSIZE = 1024;
    private byte[] databuf;
    private int skipped;
    private int available;
    
    public ByteQueue() {
        this.databuf = new byte[1024];
        this.skipped = 0;
        this.available = 0;
    }
    
    public static final int nextTwoPow(int n) {
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n + 1;
    }
    
    public void read(final byte[] array, final int n, final int i, final int n2) {
        if (this.available - n2 < i) {
            throw new TlsRuntimeException("Not enough data to read");
        }
        if (array.length - n < i) {
            throw new TlsRuntimeException("Buffer size of " + array.length + " is too small for a read of " + i + " bytes");
        }
        System.arraycopy(this.databuf, this.skipped + n2, array, n, i);
    }
    
    public void addData(final byte[] array, final int n, final int n2) {
        if (this.skipped + this.available + n2 > this.databuf.length) {
            final byte[] databuf = new byte[nextTwoPow(array.length)];
            System.arraycopy(this.databuf, this.skipped, databuf, 0, this.available);
            this.skipped = 0;
            this.databuf = databuf;
        }
        System.arraycopy(array, n, this.databuf, this.skipped + this.available, n2);
        this.available += n2;
    }
    
    public void removeData(final int i) {
        if (i > this.available) {
            throw new TlsRuntimeException("Cannot remove " + i + " bytes, only got " + this.available);
        }
        this.available -= i;
        this.skipped += i;
        if (this.skipped > this.databuf.length / 2) {
            System.arraycopy(this.databuf, this.skipped, this.databuf, 0, this.available);
            this.skipped = 0;
        }
    }
    
    public int size() {
        return this.available;
    }
}
