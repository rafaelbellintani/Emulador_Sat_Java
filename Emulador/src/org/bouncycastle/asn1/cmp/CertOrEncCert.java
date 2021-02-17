// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.crmf.EncryptedValue;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertOrEncCert extends ASN1Encodable implements ASN1Choice
{
    private CMPCertificate certificate;
    private EncryptedValue encryptedCert;
    
    private CertOrEncCert(final ASN1TaggedObject asn1TaggedObject) {
        if (asn1TaggedObject.getTagNo() == 0) {
            this.certificate = CMPCertificate.getInstance(asn1TaggedObject.getObject());
        }
        else {
            if (asn1TaggedObject.getTagNo() != 1) {
                throw new IllegalArgumentException("unknown tag: " + asn1TaggedObject.getTagNo());
            }
            this.encryptedCert = EncryptedValue.getInstance(asn1TaggedObject.getObject());
        }
    }
    
    public static CertOrEncCert getInstance(final Object o) {
        if (o instanceof CertOrEncCert) {
            return (CertOrEncCert)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new CertOrEncCert((ASN1TaggedObject)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public CMPCertificate getCertificate() {
        return this.certificate;
    }
    
    public EncryptedValue getEncryptedCert() {
        return this.encryptedCert;
    }
    
    @Override
    public DERObject toASN1Object() {
        if (this.certificate != null) {
            return new DERTaggedObject(true, 0, this.certificate);
        }
        return new DERTaggedObject(true, 1, this.encryptedCert);
    }
}
