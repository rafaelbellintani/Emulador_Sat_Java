// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class ContentInfo extends ASN1Encodable implements PKCSObjectIdentifiers
{
    private DERObjectIdentifier contentType;
    private DEREncodable content;
    
    public static ContentInfo getInstance(final Object o) {
        if (o instanceof ContentInfo) {
            return (ContentInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ContentInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public ContentInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.contentType = objects.nextElement();
        if (objects.hasMoreElements()) {
            this.content = ((DERTaggedObject)objects.nextElement()).getObject();
        }
    }
    
    public ContentInfo(final DERObjectIdentifier contentType, final DEREncodable content) {
        this.contentType = contentType;
        this.content = content;
    }
    
    public DERObjectIdentifier getContentType() {
        return this.contentType;
    }
    
    public DEREncodable getContent() {
        return this.content;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.contentType);
        if (this.content != null) {
            asn1EncodableVector.add(new BERTaggedObject(0, this.content));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
