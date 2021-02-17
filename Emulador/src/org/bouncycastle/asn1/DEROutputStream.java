// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FilterOutputStream;

public class DEROutputStream extends FilterOutputStream implements DERTags
{
    public DEROutputStream(final OutputStream out) {
        super(out);
    }
    
    private void writeLength(final int n) throws IOException {
        if (n > 127) {
            int n2 = 1;
            int n3 = n;
            while ((n3 >>>= 8) != 0) {
                ++n2;
            }
            this.write((byte)(n2 | 0x80));
            for (int i = (n2 - 1) * 8; i >= 0; i -= 8) {
                this.write((byte)(n >> i));
            }
        }
        else {
            this.write((byte)n);
        }
    }
    
    void writeEncoded(final int b, final byte[] array) throws IOException {
        this.write(b);
        this.writeLength(array.length);
        this.write(array);
    }
    
    void writeTag(final int n, int i) throws IOException {
        if (i < 31) {
            this.write(n | i);
        }
        else {
            this.write(n | 0x1F);
            if (i < 128) {
                this.write(i);
            }
            else {
                final byte[] array = new byte[5];
                int length = array.length;
                array[--length] = (byte)(i & 0x7F);
                do {
                    i >>= 7;
                    array[--length] = (byte)((i & 0x7F) | 0x80);
                } while (i > 127);
                this.write(array, length, array.length - length);
            }
        }
    }
    
    void writeEncoded(final int n, final int n2, final byte[] array) throws IOException {
        this.writeTag(n, n2);
        this.writeLength(array.length);
        this.write(array);
    }
    
    protected void writeNull() throws IOException {
        this.write(5);
        this.write(0);
    }
    
    @Override
    public void write(final byte[] b) throws IOException {
        this.out.write(b, 0, b.length);
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b, off, len);
    }
    
    public void writeObject(final Object o) throws IOException {
        if (o == null) {
            this.writeNull();
        }
        else if (o instanceof DERObject) {
            ((DERObject)o).encode(this);
        }
        else {
            if (!(o instanceof DEREncodable)) {
                throw new IOException("object not DEREncodable");
            }
            ((DEREncodable)o).getDERObject().encode(this);
        }
    }
}
