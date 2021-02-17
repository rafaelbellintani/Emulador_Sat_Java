// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;

public class ASN1OutputStream extends DEROutputStream
{
    public ASN1OutputStream(final OutputStream outputStream) {
        super(outputStream);
    }
    
    @Override
    public void writeObject(final Object o) throws IOException {
        if (o == null) {
            this.writeNull();
        }
        else if (o instanceof DERObject) {
            ((DERObject)o).encode(this);
        }
        else {
            if (!(o instanceof DEREncodable)) {
                throw new IOException("object not ASN1Encodable");
            }
            ((DEREncodable)o).getDERObject().encode(this);
        }
    }
}
