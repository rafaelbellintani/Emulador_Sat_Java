// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class POPODecKeyRespContent extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private POPODecKeyRespContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static POPODecKeyRespContent getInstance(final Object o) {
        if (o instanceof POPODecKeyRespContent) {
            return (POPODecKeyRespContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new POPODecKeyRespContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERInteger[] toDERIntegerArray() {
        final DERInteger[] array = new DERInteger[this.content.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = DERInteger.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
