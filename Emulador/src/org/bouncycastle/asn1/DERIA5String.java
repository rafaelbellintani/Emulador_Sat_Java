// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERIA5String extends ASN1Object implements DERString
{
    String string;
    
    public static DERIA5String getInstance(final Object o) {
        if (o == null || o instanceof DERIA5String) {
            return (DERIA5String)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERIA5String(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERIA5String getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERIA5String(final byte[] array) {
        final char[] value = new char[array.length];
        for (int i = 0; i != value.length; ++i) {
            value[i] = (char)(array[i] & 0xFF);
        }
        this.string = new String(value);
    }
    
    public DERIA5String(final String s) {
        this(s, false);
    }
    
    public DERIA5String(final String string, final boolean b) {
        if (string == null) {
            throw new NullPointerException("string cannot be null");
        }
        if (b && !isIA5String(string)) {
            throw new IllegalArgumentException("string contains illegal characters");
        }
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
        derOutputStream.writeEncoded(22, this.getOctets());
    }
    
    @Override
    public int hashCode() {
        return this.getString().hashCode();
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERIA5String && this.getString().equals(((DERIA5String)derObject).getString());
    }
    
    public static boolean isIA5String(final String s) {
        for (int i = s.length() - 1; i >= 0; --i) {
            if (s.charAt(i) > '\u007f') {
                return false;
            }
        }
        return true;
    }
}
