// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class PollReqContent extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private PollReqContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static PollReqContent getInstance(final Object o) {
        if (o instanceof PollReqContent) {
            return (PollReqContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PollReqContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERInteger[][] getCertReqIds() {
        final DERInteger[][] array = new DERInteger[this.content.size()][];
        for (int i = 0; i != array.length; ++i) {
            array[i] = this.seqenceToDERIntegerArray((ASN1Sequence)this.content.getObjectAt(i));
        }
        return array;
    }
    
    private DERInteger[] seqenceToDERIntegerArray(final ASN1Sequence asn1Sequence) {
        final DERInteger[] array = new DERInteger[asn1Sequence.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = DERInteger.getInstance(asn1Sequence.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
