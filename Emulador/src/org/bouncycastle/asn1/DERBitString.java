// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.util.Arrays;
import java.io.IOException;

public class DERBitString extends ASN1Object implements DERString
{
    private static final char[] table;
    protected byte[] data;
    protected int padBits;
    
    protected static int getPadBits(final int n) {
        int n2 = 0;
        for (int i = 3; i >= 0; --i) {
            if (i != 0) {
                if (n >> i * 8 != 0) {
                    n2 = (n >> i * 8 & 0xFF);
                    break;
                }
            }
            else if (n != 0) {
                n2 = (n & 0xFF);
                break;
            }
        }
        if (n2 == 0) {
            return 7;
        }
        int n3 = 1;
        while (((n2 <<= 1) & 0xFF) != 0x0) {
            ++n3;
        }
        return 8 - n3;
    }
    
    protected static byte[] getBytes(final int n) {
        int n2 = 4;
        for (int n3 = 3; n3 >= 1 && (n & 255 << n3 * 8) == 0x0; --n3) {
            --n2;
        }
        final byte[] array = new byte[n2];
        for (int i = 0; i < n2; ++i) {
            array[i] = (byte)(n >> i * 8 & 0xFF);
        }
        return array;
    }
    
    public static DERBitString getInstance(final Object o) {
        if (o == null || o instanceof DERBitString) {
            return (DERBitString)o;
        }
        if (o instanceof ASN1OctetString) {
            final byte[] octets = ((ASN1OctetString)o).getOctets();
            final byte b = octets[0];
            final byte[] array = new byte[octets.length - 1];
            System.arraycopy(octets, 1, array, 0, octets.length - 1);
            return new DERBitString(array, b);
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERBitString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    protected DERBitString(final byte b, final int padBits) {
        (this.data = new byte[1])[0] = b;
        this.padBits = padBits;
    }
    
    public DERBitString(final byte[] data, final int padBits) {
        this.data = data;
        this.padBits = padBits;
    }
    
    public DERBitString(final byte[] array) {
        this(array, 0);
    }
    
    public DERBitString(final DEREncodable derEncodable) {
        try {
            this.data = derEncodable.getDERObject().getEncoded("DER");
            this.padBits = 0;
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("Error processing object : " + ex.toString());
        }
    }
    
    public byte[] getBytes() {
        return this.data;
    }
    
    public int getPadBits() {
        return this.padBits;
    }
    
    public int intValue() {
        int n = 0;
        for (int n2 = 0; n2 != this.data.length && n2 != 4; ++n2) {
            n |= (this.data[n2] & 0xFF) << 8 * n2;
        }
        return n;
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        final byte[] array = new byte[this.getBytes().length + 1];
        array[0] = (byte)this.getPadBits();
        System.arraycopy(this.getBytes(), 0, array, 1, array.length - 1);
        derOutputStream.writeEncoded(3, array);
    }
    
    @Override
    public int hashCode() {
        return this.padBits ^ Arrays.hashCode(this.data);
    }
    
    protected boolean asn1Equals(final DERObject derObject) {
        if (!(derObject instanceof DERBitString)) {
            return false;
        }
        final DERBitString derBitString = (DERBitString)derObject;
        return this.padBits == derBitString.padBits && Arrays.areEqual(this.data, derBitString.data);
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
            sb.append(DERBitString.table[byteArray[i] >>> 4 & 0xF]);
            sb.append(DERBitString.table[byteArray[i] & 0xF]);
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.getString();
    }
    
    static {
        table = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    }
}
