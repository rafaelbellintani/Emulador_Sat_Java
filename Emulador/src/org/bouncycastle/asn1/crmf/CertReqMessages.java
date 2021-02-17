// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertReqMessages extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private CertReqMessages(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static CertReqMessages getInstance(final Object o) {
        if (o instanceof CertReqMessages) {
            return (CertReqMessages)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertReqMessages((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public CertReqMsg[] toCertReqMsgArray() {
        final CertReqMsg[] array = new CertReqMsg[this.content.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = CertReqMsg.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
