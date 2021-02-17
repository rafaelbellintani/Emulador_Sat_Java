// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.io;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.Signer;
import java.io.FilterInputStream;

public class SignerInputStream extends FilterInputStream
{
    protected Signer signer;
    
    public SignerInputStream(final InputStream in, final Signer signer) {
        super(in);
        this.signer = signer;
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        if (read >= 0) {
            this.signer.update((byte)read);
        }
        return read;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int read = this.in.read(b, off, len);
        if (read > 0) {
            this.signer.update(b, off, read);
        }
        return read;
    }
    
    public Signer getSigner() {
        return this.signer;
    }
}
