// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodable;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;
import org.bouncycastle.asn1.DERObjectIdentifier;

public class ContentInfoParser
{
    private DERObjectIdentifier contentType;
    private ASN1TaggedObjectParser content;
    
    public ContentInfoParser(final ASN1SequenceParser asn1SequenceParser) throws IOException {
        this.contentType = (DERObjectIdentifier)asn1SequenceParser.readObject();
        this.content = (ASN1TaggedObjectParser)asn1SequenceParser.readObject();
    }
    
    public DERObjectIdentifier getContentType() {
        return this.contentType;
    }
    
    public DEREncodable getContent(final int n) throws IOException {
        if (this.content != null) {
            return this.content.getObjectParser(n, true);
        }
        return null;
    }
}
