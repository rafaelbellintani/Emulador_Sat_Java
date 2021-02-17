// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.ASN1Encodable;

public class X509Attribute extends ASN1Encodable
{
    Attribute attr;
    
    X509Attribute(final ASN1Encodable asn1Encodable) {
        this.attr = Attribute.getInstance(asn1Encodable);
    }
    
    public X509Attribute(final String s, final ASN1Encodable asn1Encodable) {
        this.attr = new Attribute(new DERObjectIdentifier(s), new DERSet(asn1Encodable));
    }
    
    public X509Attribute(final String s, final ASN1EncodableVector asn1EncodableVector) {
        this.attr = new Attribute(new DERObjectIdentifier(s), new DERSet(asn1EncodableVector));
    }
    
    public String getOID() {
        return this.attr.getAttrType().getId();
    }
    
    public ASN1Encodable[] getValues() {
        final ASN1Set attrValues = this.attr.getAttrValues();
        final ASN1Encodable[] array = new ASN1Encodable[attrValues.size()];
        for (int i = 0; i != attrValues.size(); ++i) {
            array[i] = (ASN1Encodable)attrValues.getObjectAt(i);
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.attr.toASN1Object();
    }
}
