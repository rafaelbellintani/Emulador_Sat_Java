// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKIConfirmContent extends ASN1Encodable
{
    private ASN1Null val;
    
    private PKIConfirmContent(final ASN1Null val) {
        this.val = val;
    }
    
    public static PKIConfirmContent getInstance(final Object o) {
        if (o instanceof PKIConfirmContent) {
            return (PKIConfirmContent)o;
        }
        if (o instanceof ASN1Null) {
            return new PKIConfirmContent((ASN1Null)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.val;
    }
}
