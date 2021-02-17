// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKIMessage extends ASN1Encodable
{
    private PKIHeader header;
    private PKIBody body;
    private DERBitString protection;
    private ASN1Sequence extraCerts;
    
    private PKIMessage(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.header = PKIHeader.getInstance(objects.nextElement());
        this.body = PKIBody.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            if (asn1TaggedObject.getTagNo() == 0) {
                this.protection = DERBitString.getInstance(asn1TaggedObject, true);
            }
            else {
                this.extraCerts = ASN1Sequence.getInstance(asn1TaggedObject, true);
            }
        }
    }
    
    public static PKIMessage getInstance(final Object o) {
        if (o instanceof PKIMessage) {
            return (PKIMessage)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PKIMessage((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public PKIHeader getHeader() {
        return this.header;
    }
    
    public PKIBody getBody() {
        return this.body;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.header);
        asn1EncodableVector.add(this.body);
        this.addOptional(asn1EncodableVector, 0, this.protection);
        this.addOptional(asn1EncodableVector, 1, this.extraCerts);
        return new DERSequence(asn1EncodableVector);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
}
