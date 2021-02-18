// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class DERUniversalString extends ASN1Object implements DERString
{
    private static final char[] table;
    private byte[] string;
    
    public static DERUniversalString getInstance(final Object o) {
        if (o == null || o instanceof DERUniversalString) {
            return (DERUniversalString)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERUniversalString(((ASN1OctetString)o).getOctets());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERUniversalString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERUniversalString(final byte[] string) {
        this.string = string;
    }
    
    @Override
    public String getString() {
        final StringBuffer sb = new StringBuffer("#");
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ASN1OutputStream asn1OutputStream = new ASN1OutputStream(byteArrayOutputStream);
        try {
            asn1OutputStream.writeObject(this);
        }
        catch (IOException ex) {
            throw new RuntimeException("internal error encoding BitString");
        }
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        for (int i = 0; i != byteArray.length; ++i) {
            sb.append(DERUniversalString.table[byteArray[i] >>> 4 & 0xF]);
            sb.append(DERUniversalString.table[byteArray[i] & 0xF]);
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.getString();
    }
    
    public byte[] getOctets() {
        return this.string;
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(28, this.getOctets());
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERUniversalString && this.getString().equals(((DERUniversalString)derObject).getString());
    }
    
    @Override
    public int hashCode() {
        return this.getString().hashCode();
    }
    
    static {
        table = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
}
