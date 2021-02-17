// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.ASN1Encodable;

public class ContentHints extends ASN1Encodable
{
    private DERUTF8String contentDescription;
    private DERObjectIdentifier contentType;
    
    public static ContentHints getInstance(final Object o) {
        if (o == null || o instanceof ContentHints) {
            return (ContentHints)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ContentHints((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in 'ContentHints' factory : " + o.getClass().getName() + ".");
    }
    
    private ContentHints(final ASN1Sequence asn1Sequence) {
        final DEREncodable object = asn1Sequence.getObjectAt(0);
        if (object.getDERObject() instanceof DERUTF8String) {
            this.contentDescription = DERUTF8String.getInstance(object);
            this.contentType = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        }
        else {
            this.contentType = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        }
    }
    
    public ContentHints(final DERObjectIdentifier contentType) {
        this.contentType = contentType;
        this.contentDescription = null;
    }
    
    public ContentHints(final DERObjectIdentifier contentType, final DERUTF8String contentDescription) {
        this.contentType = contentType;
        this.contentDescription = contentDescription;
    }
    
    public DERObjectIdentifier getContentType() {
        return this.contentType;
    }
    
    public DERUTF8String getContentDescription() {
        return this.contentDescription;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.contentDescription != null) {
            asn1EncodableVector.add(this.contentDescription);
        }
        asn1EncodableVector.add(this.contentType);
        return new DERSequence(asn1EncodableVector);
    }
}
