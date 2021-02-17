// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.io;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.Signer;
import java.io.FilterOutputStream;

public class SignerOutputStream extends FilterOutputStream
{
    protected Signer signer;
    
    public SignerOutputStream(final OutputStream out, final Signer signer) {
        super(out);
        this.signer = signer;
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.signer.update((byte)n);
        this.out.write(n);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.signer.update(b, off, len);
        this.out.write(b, off, len);
    }
    
    public Signer getSigner() {
        return this.signer;
    }
}
