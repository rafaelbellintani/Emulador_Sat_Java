// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class CRLAnnContent extends ASN1Encodable
{
    private ASN1Sequence content;
    
    private CRLAnnContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static CRLAnnContent getInstance(final Object o) {
        if (o instanceof CRLAnnContent) {
            return (CRLAnnContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CRLAnnContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public CertificateList[] toCertificateListArray() {
        final CertificateList[] array = new CertificateList[this.content.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = CertificateList.getInstance(this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.content;
    }
}
