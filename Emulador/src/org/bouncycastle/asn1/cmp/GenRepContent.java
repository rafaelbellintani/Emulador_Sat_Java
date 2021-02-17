// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class GenRepContent extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private GenRepContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static GenRepContent getInstance(final Object o) {
        if (o instanceof GenRepContent) {
            return (GenRepContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new GenRepContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public InfoTypeAndValue[] toInfoTypeAndValueArray() {
        final InfoTypeAndValue[] array = new InfoTypeAndValue[this.content.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = InfoTypeAndValue.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
