// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERBMPString extends ASN1Object implements DERString
{
    String string;
    
    public static DERBMPString getInstance(final Object o) {
        if (o == null || o instanceof DERBMPString) {
            return (DERBMPString)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERBMPString(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERBMPString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERBMPString(final byte[] array) {
        final char[] value = new char[array.length / 2];
        for (int i = 0; i != value.length; ++i) {
            value[i] = (char)(array[2 * i] << 8 | (array[2 * i + 1] & 0xFF));
        }
        this.string = new String(value);
    }
    
    public DERBMPString(final String string) {
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
    public int hashCode() {
        return this.getString().hashCode();
    }
    
    protected boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERBMPString && this.getString().equals(((DERBMPString)derObject).getString());
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        final char[] charArray = this.string.toCharArray();
        final byte[] array = new byte[charArray.length * 2];
        for (int i = 0; i != charArray.length; ++i) {
            array[2 * i] = (byte)(charArray[i] >> 8);
            array[2 * i + 1] = (byte)charArray[i];
        }
        derOutputStream.writeEncoded(30, array);
    }
}
