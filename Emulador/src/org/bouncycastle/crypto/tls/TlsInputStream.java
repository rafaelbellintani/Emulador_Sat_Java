// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.InputStream;

public class TlsInputStream extends InputStream
{
    private byte[] buf;
    private TlsProtocolHandler handler;
    
    TlsInputStream(final TlsProtocolHandler handler) {
        this.buf = new byte[1];
        this.handler = null;
        this.handler = handler;
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        return this.handler.readApplicationData(array, n, n2);
    }
    
    @Override
    public int read() throws IOException {
        if (this.read(this.buf) < 0) {
            return -1;
        }
        return this.buf[0] & 0xFF;
    }
    
    @Override
    public void close() throws IOException {
        this.handler.close();
    }
}
