// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodable;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;

public class EncryptedContentInfoParser
{
    private DERObjectIdentifier _contentType;
    private AlgorithmIdentifier _contentEncryptionAlgorithm;
    private ASN1TaggedObjectParser _encryptedContent;
    
    public EncryptedContentInfoParser(final ASN1SequenceParser asn1SequenceParser) throws IOException {
        this._contentType = (DERObjectIdentifier)asn1SequenceParser.readObject();
        this._contentEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1SequenceParser.readObject().getDERObject());
        this._encryptedContent = (ASN1TaggedObjectParser)asn1SequenceParser.readObject();
    }
    
    public DERObjectIdentifier getContentType() {
        return this._contentType;
    }
    
    public AlgorithmIdentifier getContentEncryptionAlgorithm() {
        return this._contentEncryptionAlgorithm;
    }
    
    public DEREncodable getEncryptedContent(final int n) throws IOException {
        return this._encryptedContent.getObjectParser(n, false);
    }
}
