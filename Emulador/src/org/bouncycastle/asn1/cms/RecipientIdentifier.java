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

public class RecipientIdentifier extends ASN1Encodable implements ASN1Choice
{
    private DEREncodable id;
    
    public RecipientIdentifier(final IssuerAndSerialNumber id) {
        this.id = id;
    }
    
    public RecipientIdentifier(final ASN1OctetString asn1OctetString) {
        this.id = new DERTaggedObject(false, 0, asn1OctetString);
    }
    
    public RecipientIdentifier(final DERObject id) {
        this.id = id;
    }
    
    public static RecipientIdentifier getInstance(final Object o) {
        if (o == null || o instanceof RecipientIdentifier) {
            return (RecipientIdentifier)o;
        }
        if (o instanceof IssuerAndSerialNumber) {
            return new RecipientIdentifier((IssuerAndSerialNumber)o);
        }
        if (o instanceof ASN1OctetString) {
            return new RecipientIdentifier((ASN1OctetString)o);
        }
        if (o instanceof DERObject) {
            return new RecipientIdentifier((DERObject)o);
        }
        throw new IllegalArgumentException("Illegal object in RecipientIdentifier: " + o.getClass().getName());
    }
    
    public boolean isTagged() {
        return this.id instanceof ASN1TaggedObject;
    }
    
    public DEREncodable getId() {
        if (this.id instanceof ASN1TaggedObject) {
            return ASN1OctetString.getInstance((ASN1TaggedObject)this.id, false);
        }
        return IssuerAndSerialNumber.getInstance(this.id);
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.id.getDERObject();
    }
}
