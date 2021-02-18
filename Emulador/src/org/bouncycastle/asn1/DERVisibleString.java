// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERVisibleString extends ASN1Object implements DERString
{
    String string;
    
    public static DERVisibleString getInstance(final Object o) {
        if (o == null || o instanceof DERVisibleString) {
            return (DERVisibleString)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERVisibleString(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERVisibleString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERVisibleString(final byte[] array) {
        final char[] value = new char[array.length];
        for (int i = 0; i != value.length; ++i) {
            value[i] = (char)(array[i] & 0xFF);
        }
        this.string = new String(value);
    }
    
    public DERVisibleString(final String string) {
        this.string = string;
    }
    
    @Override
    public String getString() {
        return this.string;
    }
    
    @Override
    public String toString() {
        return this.string;
    }
    
    public byte[] getOctets() {
        final char[] charArray = this.string.toCharArray();
        final byte[] array = new byte[charArray.length];
        for (int i = 0; i != charArray.length; ++i) {
            array[i] = (byte)charArray[i];
        }
        return array;
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(26, this.getOctets());
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERVisibleString && this.getString().equals(((DERVisibleString)derObject).getString());
    }
    
    @Override
    public int hashCode() {
        return this.getString().hashCode();
    }
}
