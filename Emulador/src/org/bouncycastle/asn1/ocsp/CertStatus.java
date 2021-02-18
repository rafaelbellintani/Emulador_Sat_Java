// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertStatus extends ASN1Encodable implements ASN1Choice
{
    private int tagNo;
    private DEREncodable value;
    
    public CertStatus() {
        this.tagNo = 0;
        this.value = new DERNull();
    }
    
    public CertStatus(final RevokedInfo value) {
        this.tagNo = 1;
        this.value = value;
    }
    
    public CertStatus(final int tagNo, final DEREncodable value) {
        this.tagNo = tagNo;
        this.value = value;
    }
    
    public CertStatus(final ASN1TaggedObject asn1TaggedObject) {
        this.tagNo = asn1TaggedObject.getTagNo();
        switch (asn1TaggedObject.getTagNo()) {
            case 0: {
                this.value = new DERNull();
                break;
            }
            case 1: {
                this.value = RevokedInfo.getInstance(asn1TaggedObject, false);
                break;
            }
            case 2: {
                this.value = new DERNull();
                break;
            }
        }
    }
    
    public static CertStatus getInstance(final Object o) {
        if (o == null || o instanceof CertStatus) {
            return (CertStatus)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new CertStatus((ASN1TaggedObject)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public static CertStatus getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public int getTagNo() {
        return this.tagNo;
    }
    
    public DEREncodable getStatus() {
        return this.value;
    }
    
    @Override
    public DERObject toASN1Object() {
        return new DERTaggedObject(false, this.tagNo, this.value);
    }
}
