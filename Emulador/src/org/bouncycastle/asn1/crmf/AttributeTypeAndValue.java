// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class AttributeTypeAndValue extends ASN1Encodable
{
    private DERObjectIdentifier type;
    private ASN1Encodable value;
    
    private AttributeTypeAndValue(final ASN1Sequence asn1Sequence) {
        this.type = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.value = (ASN1Encodable)asn1Sequence.getObjectAt(1);
    }
    
    public static AttributeTypeAndValue getInstance(final Object o) {
        if (o instanceof AttributeTypeAndValue) {
            return (AttributeTypeAndValue)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AttributeTypeAndValue((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERObjectIdentifier getType() {
        return this.type;
    }
    
    public ASN1Encodable getValue() {
        return this.value;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.type);
        asn1EncodableVector.add(this.value);
        return new DERSequence(asn1EncodableVector);
    }
}
