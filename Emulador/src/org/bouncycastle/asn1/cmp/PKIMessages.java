// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKIMessages extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private PKIMessages(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static PKIMessages getInstance(final Object o) {
        if (o instanceof PKIMessages) {
            return (PKIMessages)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PKIMessages((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public PKIMessage[] toPKIMessageArray() {
        final PKIMessage[] array = new PKIMessage[this.content.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = PKIMessage.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
