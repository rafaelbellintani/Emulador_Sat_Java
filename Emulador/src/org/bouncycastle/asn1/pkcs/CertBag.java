// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertBag extends ASN1Encodable
{
    ASN1Sequence seq;
    DERObjectIdentifier certId;
    DERObject certValue;
    
    public CertBag(final ASN1Sequence seq) {
        this.seq = seq;
        this.certId = (DERObjectIdentifier)seq.getObjectAt(0);
        this.certValue = ((DERTaggedObject)seq.getObjectAt(1)).getObject();
    }
    
    public CertBag(final DERObjectIdentifier certId, final DERObject certValue) {
        this.certId = certId;
        this.certValue = certValue;
    }
    
    public DERObjectIdentifier getCertId() {
        return this.certId;
    }
    
    public DERObject getCertValue() {
        return this.certValue;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certId);
        asn1EncodableVector.add(new DERTaggedObject(0, this.certValue));
        return new DERSequence(asn1EncodableVector);
    }
}
