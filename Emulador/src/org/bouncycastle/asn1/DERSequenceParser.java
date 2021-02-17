// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERSequenceParser implements ASN1SequenceParser
{
    private ASN1StreamParser _parser;
    
    DERSequenceParser(final ASN1StreamParser parser) {
        this._parser = parser;
    }
    
    @Override
    public DEREncodable readObject() throws IOException {
        return this._parser.readObject();
    }
    
    @Override
    public DERObject getDERObject() {
        try {
            return new DERSequence(this._parser.readVector());
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }
}
