// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.OutputStream;

public class TlsOuputStream extends OutputStream
{
    private byte[] buf;
    private TlsProtocolHandler handler;
    
    TlsOuputStream(final TlsProtocolHandler handler) {
        this.buf = new byte[1];
        this.handler = handler;
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        this.handler.writeData(array, n, n2);
    }
    
    @Override
    public void write(final int n) throws IOException {
        this.buf[0] = (byte)n;
        this.write(this.buf, 0, 1);
    }
    
    @Deprecated
    public void cose() throws IOException {
        this.handler.close();
    }
    
    @Override
    public void close() throws IOException {
        this.handler.close();
    }
    
    @Override
    public void flush() throws IOException {
        this.handler.flush();
    }
}
