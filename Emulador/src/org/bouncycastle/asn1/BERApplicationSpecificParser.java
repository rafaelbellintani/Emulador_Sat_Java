// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class BERApplicationSpecificParser implements ASN1ApplicationSpecificParser
{
    private final int tag;
    private final ASN1StreamParser parser;
    
    BERApplicationSpecificParser(final int tag, final ASN1StreamParser parser) {
        this.tag = tag;
        this.parser = parser;
    }
    
    @Override
    public DEREncodable readObject() throws IOException {
        return this.parser.readObject();
    }
    
    @Override
    public DERObject getDERObject() {
        try {
            return new BERApplicationSpecific(this.tag, this.parser.readVector());
        }
        catch (IOException ex) {
            throw new ASN1ParsingException(ex.getMessage(), ex);
        }
    }
}
