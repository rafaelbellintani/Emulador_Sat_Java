// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERInteger;

public class CompressedDataParser
{
    private DERInteger _version;
    private AlgorithmIdentifier _compressionAlgorithm;
    private ContentInfoParser _encapContentInfo;
    
    public CompressedDataParser(final ASN1SequenceParser asn1SequenceParser) throws IOException {
        this._version = (DERInteger)asn1SequenceParser.readObject();
        this._compressionAlgorithm = AlgorithmIdentifier.getInstance(asn1SequenceParser.readObject().getDERObject());
        this._encapContentInfo = new ContentInfoParser((ASN1SequenceParser)asn1SequenceParser.readObject());
    }
    
    public DERInteger getVersion() {
        return this._version;
    }
    
    public AlgorithmIdentifier getCompressionAlgorithmIdentifier() {
        return this._compressionAlgorithm;
    }
    
    public ContentInfoParser getEncapContentInfo() {
        return this._encapContentInfo;
    }
}
