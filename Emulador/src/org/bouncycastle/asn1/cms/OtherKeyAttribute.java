// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class OtherKeyAttribute extends ASN1Encodable
{
    private DERObjectIdentifier keyAttrId;
    private DEREncodable keyAttr;
    
    public static OtherKeyAttribute getInstance(final Object o) {
        if (o == null || o instanceof OtherKeyAttribute) {
            return (OtherKeyAttribute)o;
        }
        if (o instanceof ASN1Sequence) {
            return new OtherKeyAttribute((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public OtherKeyAttribute(final ASN1Sequence asn1Sequence) {
        this.keyAttrId = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.keyAttr = asn1Sequence.getObjectAt(1);
    }
    
    public OtherKeyAttribute(final DERObjectIdentifier keyAttrId, final DEREncodable keyAttr) {
        this.keyAttrId = keyAttrId;
        this.keyAttr = keyAttr;
    }
    
    public DERObjectIdentifier getKeyAttrId() {
        return this.keyAttrId;
    }
    
    public DEREncodable getKeyAttr() {
        return this.keyAttr;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.keyAttrId);
        asn1EncodableVector.add(this.keyAttr);
        return new DERSequence(asn1EncodableVector);
    }
}
