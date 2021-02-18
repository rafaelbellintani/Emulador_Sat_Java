// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;

public class DEROctetStringParser implements ASN1OctetStringParser
{
    private DefiniteLengthInputStream stream;
    
    DEROctetStringParser(final DefiniteLengthInputStream stream) {
        this.stream = stream;
    }
    
    @Override
    public InputStream getOctetStream() {
        return this.stream;
    }
    
    @Override
    public DERObject getDERObject() {
        try {
            return new DEROctetString(this.stream.toByteArray());
        }
        catch (IOException ex) {
            throw new ASN1ParsingException("IOException converting stream to byte array: " + ex.getMessage(), ex);
        }
    }
}
