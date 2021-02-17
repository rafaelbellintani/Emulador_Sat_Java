// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.io;

import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.Mac;
import java.io.FilterOutputStream;

public class MacOutputStream extends FilterOutputStream
{
    protected Mac mac;
    
    public MacOutputStream(final OutputStream out, final Mac mac) {
        super(out);
        this.mac = mac;
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.mac.update((byte)n);
        this.out.write(n);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.mac.update(b, off, len);
        this.out.write(b, off, len);
    }
    
    public Mac getMac() {
        return this.mac;
    }
}
