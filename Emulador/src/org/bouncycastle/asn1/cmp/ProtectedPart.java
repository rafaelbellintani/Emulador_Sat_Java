// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class ProtectedPart extends ASN1Encodable
{
    private PKIHeader header;
    private PKIBody body;
    
    private ProtectedPart(final ASN1Sequence asn1Sequence) {
        this.header = PKIHeader.getInstance(asn1Sequence.getObjectAt(0));
        this.body = PKIBody.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public static ProtectedPart getInstance(final Object o) {
        if (o instanceof ProtectedPart) {
            return (ProtectedPart)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ProtectedPart((ASN1Sequence)o);
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
        return new DERSequence(asn1EncodableVector);
    }
}
