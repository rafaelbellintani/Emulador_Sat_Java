// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class Attribute extends ASN1Encodable
{
    private DERObjectIdentifier attrType;
    private ASN1Set attrValues;
    
    public static Attribute getInstance(final Object o) {
        if (o == null || o instanceof Attribute) {
            return (Attribute)o;
        }
        if (o instanceof ASN1Sequence) {
            return new Attribute((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public Attribute(final ASN1Sequence asn1Sequence) {
        this.attrType = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.attrValues = (ASN1Set)asn1Sequence.getObjectAt(1);
    }
    
    public Attribute(final DERObjectIdentifier attrType, final ASN1Set attrValues) {
        this.attrType = attrType;
        this.attrValues = attrValues;
    }
    
    public DERObjectIdentifier getAttrType() {
        return this.attrType;
    }
    
    public ASN1Set getAttrValues() {
        return this.attrValues;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.attrType);
        asn1EncodableVector.add(this.attrValues);
        return new DERSequence(asn1EncodableVector);
    }
}
