// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.io;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.Digest;
import java.io.FilterOutputStream;

public class DigestOutputStream extends FilterOutputStream
{
    protected Digest digest;
    
    public DigestOutputStream(final OutputStream out, final Digest digest) {
        super(out);
        this.digest = digest;
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.digest.update((byte)n);
        this.out.write(n);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.digest.update(b, off, len);
        this.out.write(b, off, len);
    }
    
    public Digest getDigest() {
        return this.digest;
    }
}
