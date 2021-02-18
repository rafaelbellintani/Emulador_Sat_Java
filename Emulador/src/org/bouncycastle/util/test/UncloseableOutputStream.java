// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FilterOutputStream;

public class UncloseableOutputStream extends FilterOutputStream
{
    public UncloseableOutputStream(final OutputStream out) {
        super(out);
    }
    
    @Override
    public void close() {
        throw new RuntimeException("close() called on UncloseableOutputStream");
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b, off, len);
    }
}
