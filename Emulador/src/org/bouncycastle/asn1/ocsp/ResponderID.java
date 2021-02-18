// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class ResponderID extends ASN1Encodable implements ASN1Choice
{
    private DEREncodable value;
    
    public ResponderID(final ASN1OctetString value) {
        this.value = value;
    }
    
    public ResponderID(final X509Name value) {
        this.value = value;
    }
    
    public static ResponderID getInstance(final Object o) {
        if (o == null || o instanceof ResponderID) {
            return (ResponderID)o;
        }
        if (o instanceof DEROctetString) {
            return new ResponderID((ASN1OctetString)o);
        }
        if (!(o instanceof ASN1TaggedObject)) {
            return new ResponderID(X509Name.getInstance(o));
        }
        final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)o;
        if (asn1TaggedObject.getTagNo() == 1) {
            return new ResponderID(X509Name.getInstance(asn1TaggedObject, true));
        }
        return new ResponderID(ASN1OctetString.getInstance(asn1TaggedObject, true));
    }
    
    public static ResponderID getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    @Override
    public DERObject toASN1Object() {
        if (this.value instanceof ASN1OctetString) {
            return new DERTaggedObject(true, 2, this.value);
        }
        return new DERTaggedObject(true, 1, this.value);
    }
}
