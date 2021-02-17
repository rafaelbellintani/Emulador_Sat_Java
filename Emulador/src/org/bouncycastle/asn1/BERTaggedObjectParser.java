// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.InputStream;

public class BERTaggedObjectParser implements ASN1TaggedObjectParser
{
    private int _baseTag;
    private int _tagNumber;
    private InputStream _contentStream;
    private boolean _indefiniteLength;
    
    protected BERTaggedObjectParser(final int baseTag, final int tagNumber, final InputStream contentStream) {
        this._baseTag = baseTag;
        this._tagNumber = tagNumber;
        this._contentStream = contentStream;
        this._indefiniteLength = (contentStream instanceof IndefiniteLengthInputStream);
    }
    
    public boolean isConstructed() {
        return (this._baseTag & 0x20) != 0x0;
    }
    
    @Override
    public int getTagNo() {
        return this._tagNumber;
    }
    
    @Override
    public DEREncodable getObjectParser(final int n, final boolean b) throws IOException {
        if (b) {
            return new ASN1StreamParser(this._contentStream).readObject();
        }
        switch (n) {
            case 17: {
                if (this._indefiniteLength) {
                    return new BERSetParser(new ASN1StreamParser(this._contentStream));
                }
                return new DERSetParser(new ASN1StreamParser(this._contentStream));
            }
            case 16: {
                if (this._indefiniteLength) {
                    return new BERSequenceParser(new ASN1StreamParser(this._contentStream));
                }
                return new DERSequenceParser(new ASN1StreamParser(this._contentStream));
            }
            case 4: {
                if (this._indefiniteLength || this.isConstructed()) {
                    return new BEROctetStringParser(new ASN1StreamParser(this._contentStream));
                }
                return new DEROctetStringParser((DefiniteLengthInputStream)this._contentStream);
            }
            default: {
                throw new RuntimeException("implicit tagging not implemented");
            }
        }
    }
    
    private ASN1EncodableVector rLoadVector(final InputStream inputStream) {
        try {
            return new ASN1StreamParser(inputStream).readVector();
        }
        catch (IOException ex) {
            throw new ASN1ParsingException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public DERObject getDERObject() {
        if (this._indefiniteLength) {
            final ASN1EncodableVector rLoadVector = this.rLoadVector(this._contentStream);
            return (rLoadVector.size() == 1) ? new BERTaggedObject(true, this._tagNumber, rLoadVector.get(0)) : new BERTaggedObject(false, this._tagNumber, BERFactory.createSequence(rLoadVector));
        }
        if (this.isConstructed()) {
            final ASN1EncodableVector rLoadVector2 = this.rLoadVector(this._contentStream);
            return (rLoadVector2.size() == 1) ? new DERTaggedObject(true, this._tagNumber, rLoadVector2.get(0)) : new DERTaggedObject(false, this._tagNumber, DERFactory.createSequence(rLoadVector2));
        }
        try {
            return new DERTaggedObject(false, this._tagNumber, new DEROctetString(((DefiniteLengthInputStream)this._contentStream).toByteArray()));
        }
        catch (IOException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }
}
