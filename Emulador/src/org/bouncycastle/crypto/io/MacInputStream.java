// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.io;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.Mac;
import java.io.FilterInputStream;

public class MacInputStream extends FilterInputStream
{
    protected Mac mac;
    
    public MacInputStream(final InputStream in, final Mac mac) {
        super(in);
        this.mac = mac;
    }
    
    @Override
    public int read() throws IOException {
        final int read = this.in.read();
        if (read >= 0) {
            this.mac.update((byte)read);
        }
        return read;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        final int read = this.in.read(b, off, len);
        if (read >= 0) {
            this.mac.update(b, off, read);
        }
        return read;
    }
    
    public Mac getMac() {
        return this.mac;
    }
}
