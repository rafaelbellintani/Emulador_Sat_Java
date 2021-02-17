// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Encodable;

public class ContentIdentifier extends ASN1Encodable
{
    ASN1OctetString value;
    
    public static ContentIdentifier getInstance(final Object o) {
        if (o == null || o instanceof ContentIdentifier) {
            return (ContentIdentifier)o;
        }
        if (o instanceof ASN1OctetString) {
            return new ContentIdentifier((ASN1OctetString)o);
        }
        throw new IllegalArgumentException("unknown object in 'ContentIdentifier' factory : " + o.getClass().getName() + ".");
    }
    
    public ContentIdentifier(final ASN1OctetString value) {
        this.value = value;
    }
    
    public ContentIdentifier(final byte[] array) {
        this(new DEROctetString(array));
    }
    
    public ASN1OctetString getValue() {
        return this.value;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.value;
    }
}
