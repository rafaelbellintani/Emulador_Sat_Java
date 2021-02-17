// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class RecordStream
{
    private TlsProtocolHandler handler;
    private InputStream is;
    private OutputStream os;
    protected CombinedHash hash1;
    protected CombinedHash hash2;
    protected TlsCipherSuite readSuite;
    protected TlsCipherSuite writeSuite;
    
    protected RecordStream(final TlsProtocolHandler handler, final InputStream is, final OutputStream os) {
        this.readSuite = null;
        this.writeSuite = null;
        this.handler = handler;
        this.is = is;
        this.os = os;
        this.hash1 = new CombinedHash();
        this.hash2 = new CombinedHash();
        this.readSuite = new TlsNullCipherSuite();
        this.writeSuite = this.readSuite;
    }
    
    public void readData() throws IOException {
        final short uint8 = TlsUtils.readUint8(this.is);
        TlsUtils.checkVersion(this.is, this.handler);
        final byte[] decodeAndVerify = this.decodeAndVerify(uint8, this.is, TlsUtils.readUint16(this.is));
        this.handler.processData(uint8, decodeAndVerify, 0, decodeAndVerify.length);
    }
    
    protected byte[] decodeAndVerify(final short n, final InputStream inputStream, final int n2) throws IOException {
        final byte[] array = new byte[n2];
        TlsUtils.readFully(array, inputStream);
        return this.readSuite.decodeCiphertext(n, array, 0, array.length, this.handler);
    }
    
    protected void writeMessage(final short n, final byte[] array, final int n2, final int n3) throws IOException {
        if (n == 22) {
            this.hash1.update(array, n2, n3);
            this.hash2.update(array, n2, n3);
        }
        final byte[] encodePlaintext = this.writeSuite.encodePlaintext(n, array, n2, n3);
        final byte[] b = new byte[encodePlaintext.length + 5];
        TlsUtils.writeUint8(n, b, 0);
        TlsUtils.writeUint8((short)3, b, 1);
        TlsUtils.writeUint8((short)1, b, 2);
        TlsUtils.writeUint16(encodePlaintext.length, b, 3);
        System.arraycopy(encodePlaintext, 0, b, 5, encodePlaintext.length);
        this.os.write(b);
        this.os.flush();
    }
    
    protected void close() throws IOException {
        IOException ex = null;
        try {
            this.is.close();
        }
        catch (IOException ex2) {
            ex = ex2;
        }
        try {
            this.os.close();
        }
        catch (IOException ex3) {
            ex = ex3;
        }
        if (ex != null) {
            throw ex;
        }
    }
    
    protected void flush() throws IOException {
        this.os.flush();
    }
}
