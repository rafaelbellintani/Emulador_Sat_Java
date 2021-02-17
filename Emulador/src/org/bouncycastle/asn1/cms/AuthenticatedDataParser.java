// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1SetParser;
import org.bouncycastle.asn1.ASN1TaggedObjectParser;
import java.io.IOException;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1SequenceParser;

public class AuthenticatedDataParser
{
    private ASN1SequenceParser seq;
    private DERInteger version;
    private DEREncodable nextObject;
    private boolean originatorInfoCalled;
    
    public AuthenticatedDataParser(final ASN1SequenceParser seq) throws IOException {
        this.seq = seq;
        this.version = (DERInteger)seq.readObject();
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public OriginatorInfo getOriginatorInfo() throws IOException {
        this.originatorInfoCalled = true;
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject instanceof ASN1TaggedObjectParser && ((ASN1TaggedObjectParser)this.nextObject).getTagNo() == 0) {
            final ASN1SequenceParser asn1SequenceParser = (ASN1SequenceParser)((ASN1TaggedObjectParser)this.nextObject).getObjectParser(16, false);
            this.nextObject = null;
            return OriginatorInfo.getInstance(asn1SequenceParser.getDERObject());
        }
        return null;
    }
    
    public ASN1SetParser getRecipientInfos() throws IOException {
        if (!this.originatorInfoCalled) {
            this.getOriginatorInfo();
        }
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        final ASN1SetParser asn1SetParser = (ASN1SetParser)this.nextObject;
        this.nextObject = null;
        return asn1SetParser;
    }
    
    public AlgorithmIdentifier getMacAlgorithm() throws IOException {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject != null) {
            final ASN1SequenceParser asn1SequenceParser = (ASN1SequenceParser)this.nextObject;
            this.nextObject = null;
            return AlgorithmIdentifier.getInstance(asn1SequenceParser.getDERObject());
        }
        return null;
    }
    
    public ContentInfoParser getEnapsulatedContentInfo() throws IOException {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject != null) {
            final ASN1SequenceParser asn1SequenceParser = (ASN1SequenceParser)this.nextObject;
            this.nextObject = null;
            return new ContentInfoParser(asn1SequenceParser);
        }
        return null;
    }
    
    public ASN1SetParser getAuthAttrs() throws IOException {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject instanceof ASN1TaggedObjectParser) {
            final DEREncodable nextObject = this.nextObject;
            this.nextObject = null;
            return (ASN1SetParser)((ASN1TaggedObjectParser)nextObject).getObjectParser(17, false);
        }
        return null;
    }
    
    public ASN1OctetString getMac() throws IOException {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        final DEREncodable nextObject = this.nextObject;
        this.nextObject = null;
        return ASN1OctetString.getInstance(nextObject.getDERObject());
    }
    
    public ASN1SetParser getUnauthAttrs() throws IOException {
        if (this.nextObject == null) {
            this.nextObject = this.seq.readObject();
        }
        if (this.nextObject != null) {
            final DEREncodable nextObject = this.nextObject;
            this.nextObject = null;
            return (ASN1SetParser)((ASN1TaggedObjectParser)nextObject).getObjectParser(17, false);
        }
        return null;
    }
}
