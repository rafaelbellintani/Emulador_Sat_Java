// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class BERSequenceGenerator extends BERGenerator
{
    public BERSequenceGenerator(final OutputStream outputStream) throws IOException {
        super(outputStream);
        this.writeBERHeader(48);
    }
    
    public BERSequenceGenerator(final OutputStream outputStream, final int n, final boolean b) throws IOException {
        super(outputStream, n, b);
        this.writeBERHeader(48);
    }
    
    public void addObject(final DEREncodable derEncodable) throws IOException {
        derEncodable.getDERObject().encode(new BEROutputStream(this._out));
    }
    
    public void close() throws IOException {
        this.writeBEREnd();
    }
}
