// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERT61String extends ASN1Object implements DERString
{
    String string;
    
    public static DERT61String getInstance(final Object o) {
        if (o == null || o instanceof DERT61String) {
            return (DERT61String)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERT61String(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERT61String getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERT61String(final byte[] array) {
        final char[] value = new char[array.length];
        for (int i = 0; i != value.length; ++i) {
            value[i] = (char)(array[i] & 0xFF);
        }
        this.string = new String(value);
    }
    
    public DERT61String(final String string) {
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
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(20, this.getOctets());
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
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERT61String && this.getString().equals(((DERT61String)derObject).getString());
    }
    
    @Override
    public int hashCode() {
        return this.getString().hashCode();
    }
}
