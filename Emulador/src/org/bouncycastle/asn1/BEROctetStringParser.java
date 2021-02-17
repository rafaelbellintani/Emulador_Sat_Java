// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import org.bouncycastle.util.io.Streams;
import java.io.InputStream;

public class BEROctetStringParser implements ASN1OctetStringParser
{
    private ASN1StreamParser _parser;
    
    BEROctetStringParser(final ASN1StreamParser parser) {
        this._parser = parser;
    }
    
    @Deprecated
    protected BEROctetStringParser(final ASN1ObjectParser asn1ObjectParser) {
        this._parser = asn1ObjectParser._aIn;
    }
    
    @Override
    public InputStream getOctetStream() {
        return new ConstructedOctetStream(this._parser);
    }
    
    @Override
    public DERObject getDERObject() {
        try {
            return new BERConstructedOctetString(Streams.readAll(this.getOctetStream()));
        }
        catch (IOException ex) {
            throw new ASN1ParsingException("IOException converting stream to byte array: " + ex.getMessage(), ex);
        }
    }
}
