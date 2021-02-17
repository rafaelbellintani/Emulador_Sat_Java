// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERBoolean extends ASN1Object
{
    byte value;
    public static final DERBoolean FALSE;
    public static final DERBoolean TRUE;
    
    public static DERBoolean getInstance(final Object o) {
        if (o == null || o instanceof DERBoolean) {
            return (DERBoolean)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERBoolean(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERBoolean getInstance(final boolean b) {
        return b ? DERBoolean.TRUE : DERBoolean.FALSE;
    }
    
    public static DERBoolean getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERBoolean(final byte[] array) {
        this.value = array[0];
    }
    
    public DERBoolean(final boolean b) {
        this.value = (byte)(b ? -1 : 0);
    }
    
    public boolean isTrue() {
        return this.value != 0;
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(1, new byte[] { this.value });
    }
    
    protected boolean asn1Equals(final DERObject derObject) {
        return derObject != null && derObject instanceof DERBoolean && this.value == ((DERBoolean)derObject).value;
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return (this.value != 0) ? "TRUE" : "FALSE";
    }
    
    static {
        FALSE = new DERBoolean(false);
        TRUE = new DERBoolean(true);
    }
}
