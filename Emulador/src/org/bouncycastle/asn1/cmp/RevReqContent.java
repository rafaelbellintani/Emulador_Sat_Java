// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class RevReqContent extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private RevReqContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static RevReqContent getInstance(final Object o) {
        if (o instanceof RevReqContent) {
            return (RevReqContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RevReqContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public RevDetails[] toRevDetailsArray() {
        final RevDetails[] array = new RevDetails[this.content.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = RevDetails.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
