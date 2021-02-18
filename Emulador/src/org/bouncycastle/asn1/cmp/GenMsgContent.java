// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class GenMsgContent extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private GenMsgContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static GenMsgContent getInstance(final Object o) {
        if (o instanceof GenMsgContent) {
            return (GenMsgContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new GenMsgContent((ASN1Sequence)o);
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
