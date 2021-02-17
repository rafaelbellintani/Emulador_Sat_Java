// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class Controls extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private Controls(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static Controls getInstance(final Object o) {
        if (o instanceof Controls) {
            return (Controls)o;
        }
        if (o instanceof ASN1Sequence) {
            return new Controls((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public AttributeTypeAndValue[] toAttributeTypeAndValueArray() {
        final AttributeTypeAndValue[] array = new AttributeTypeAndValue[this.content.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = AttributeTypeAndValue.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
