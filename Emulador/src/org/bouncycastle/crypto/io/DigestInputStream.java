// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.io;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.Digest;
import java.io.FilterInputStream;

public class DigestInputStream extends FilterInputStream
{
    protected Digest digest;
    
    public DigestInputStream(final InputStream in, final Digest digest) {
        super(in);
        this.digest = digest;
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        if (read >= 0) {
            this.digest.update((byte)read);
        }
        return read;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int read = this.in.read(b, off, len);
        if (read > 0) {
            this.digest.update(b, off, read);
        }
        return read;
    }
    
    public Digest getDigest() {
        return this.digest;
    }
}
