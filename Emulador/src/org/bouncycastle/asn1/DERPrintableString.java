// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERPrintableString extends ASN1Object implements DERString
{
    String string;
    
    public static DERPrintableString getInstance(final Object o) {
        if (o == null || o instanceof DERPrintableString) {
            return (DERPrintableString)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERPrintableString(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERPrintableString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERPrintableString(final byte[] array) {
        final char[] value = new char[array.length];
        for (int i = 0; i != value.length; ++i) {
            value[i] = (char)(array[i] & 0xFF);
        }
        this.string = new String(value);
    }
    
    public DERPrintableString(final String s) {
        this(s, false);
    }
    
    public DERPrintableString(final String string, final boolean b) {
        if (b && !isPrintableString(string)) {
            throw new IllegalArgumentException("string contains illegal characters");
        }
        this.string = string;
    }
    
    @Override
    public String getString() {
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
        derOutputStream.writeEncoded(19, this.getOctets());
    }
    
    @Override
    public int hashCode() {
        return this.getString().hashCode();
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERPrintableString && this.getString().equals(((DERPrintableString)derObject).getString());
    }
    
    @Override
    public String toString() {
        return this.string;
    }
    
    public static boolean isPrintableString(final String s) {
        for (int i = s.length() - 1; i >= 0; --i) {
            final char char1 = s.charAt(i);
            if (char1 > '\u007f') {
                return false;
            }
            if ('a' > char1 || char1 > 'z') {
                if ('A' > char1 || char1 > 'Z') {
                    if ('0' > char1 || char1 > '9') {
                        switch (char1) {
                            case 32:
                            case 39:
                            case 40:
                            case 41:
                            case 43:
                            case 44:
                            case 45:
                            case 46:
                            case 47:
                            case 58:
                            case 61:
                            case 63: {
                                break;
                            }
                            default: {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
