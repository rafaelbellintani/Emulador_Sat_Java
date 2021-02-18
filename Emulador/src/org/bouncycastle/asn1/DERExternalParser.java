// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERExternalParser implements DEREncodable
{
    private ASN1StreamParser _parser;
    
    public DERExternalParser(final ASN1StreamParser parser) {
        this._parser = parser;
    }
    
    public DEREncodable readObject() throws IOException {
        return this._parser.readObject();
    }
    
    @Override
    public DERObject getDERObject() {
        try {
            return new DERExternal(this._parser.readVector());
        }
        catch (IOException ex) {
            throw new ASN1ParsingException("unable to get DER object", ex);
        }
        catch (IllegalArgumentException ex2) {
            throw new ASN1ParsingException("unable to get DER object", ex2);
        }
    }
}
