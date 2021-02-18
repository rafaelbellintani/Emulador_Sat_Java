// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertConfirmContent extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private CertConfirmContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static CertConfirmContent getInstance(final Object o) {
        if (o instanceof CertConfirmContent) {
            return (CertConfirmContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertConfirmContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public CertStatus[] toCertStatusArray() {
        final CertStatus[] array = new CertStatus[this.content.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = CertStatus.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
