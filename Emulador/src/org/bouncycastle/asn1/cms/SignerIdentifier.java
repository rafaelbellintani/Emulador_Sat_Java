// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class SignerIdentifier extends ASN1Encodable implements ASN1Choice
{
    private DEREncodable id;
    
    public SignerIdentifier(final IssuerAndSerialNumber id) {
        this.id = id;
    }
    
    public SignerIdentifier(final ASN1OctetString asn1OctetString) {
        this.id = new DERTaggedObject(false, 0, asn1OctetString);
    }
    
    public SignerIdentifier(final DERObject id) {
        this.id = id;
    }
    
    public static SignerIdentifier getInstance(final Object o) {
        if (o == null || o instanceof SignerIdentifier) {
            return (SignerIdentifier)o;
        }
        if (o instanceof IssuerAndSerialNumber) {
            return new SignerIdentifier((IssuerAndSerialNumber)o);
        }
        if (o instanceof ASN1OctetString) {
            return new SignerIdentifier((ASN1OctetString)o);
        }
        if (o instanceof DERObject) {
            return new SignerIdentifier((DERObject)o);
        }
        throw new IllegalArgumentException("Illegal object in SignerIdentifier: " + o.getClass().getName());
    }
    
    public boolean isTagged() {
        return this.id instanceof ASN1TaggedObject;
    }
    
    public DEREncodable getId() {
        if (this.id instanceof ASN1TaggedObject) {
            return ASN1OctetString.getInstance((ASN1TaggedObject)this.id, false);
        }
        return this.id;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.id.getDERObject();
    }
}
