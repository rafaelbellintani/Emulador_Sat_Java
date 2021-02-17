// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class DERSequenceGenerator extends DERGenerator
{
    private final ByteArrayOutputStream _bOut;
    
    public DERSequenceGenerator(final OutputStream outputStream) throws IOException {
        super(outputStream);
        this._bOut = new ByteArrayOutputStream();
    }
    
    public DERSequenceGenerator(final OutputStream outputStream, final int n, final boolean b) throws IOException {
        super(outputStream, n, b);
        this._bOut = new ByteArrayOutputStream();
    }
    
    public void addObject(final DEREncodable derEncodable) throws IOException {
        derEncodable.getDERObject().encode(new DEROutputStream(this._bOut));
    }
    
    @Override
    public OutputStream getRawOutputStream() {
        return this._bOut;
    }
    
    public void close() throws IOException {
        this.writeDEREncoded(48, this._bOut.toByteArray());
    }
}
