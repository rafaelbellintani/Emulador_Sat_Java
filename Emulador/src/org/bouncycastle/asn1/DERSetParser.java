// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERSetParser implements ASN1SetParser
{
    private ASN1StreamParser _parser;
    
    DERSetParser(final ASN1StreamParser parser) {
        this._parser = parser;
    }
    
    @Override
    public DEREncodable readObject() throws IOException {
        return this._parser.readObject();
    }
    
    @Override
    public DERObject getDERObject() {
        try {
            return new DERSet(this._parser.readVector(), false);
        }
        catch (IOException ex) {
            throw new ASN1ParsingException(ex.getMessage(), ex);
        }
    }
}
