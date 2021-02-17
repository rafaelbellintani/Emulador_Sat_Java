// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import org.bouncycastle.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DERApplicationSpecific extends ASN1Object
{
    private final boolean isConstructed;
    private final int tag;
    private final byte[] octets;
    
    DERApplicationSpecific(final boolean isConstructed, final int tag, final byte[] octets) {
        this.isConstructed = isConstructed;
        this.tag = tag;
        this.octets = octets;
    }
    
    public DERApplicationSpecific(final int n, final byte[] array) {
        this(false, n, array);
    }
    
    public DERApplicationSpecific(final int n, final DEREncodable derEncodable) throws IOException {
        this(true, n, derEncodable);
    }
    
    public DERApplicationSpecific(final boolean isConstructed, final int tag, final DEREncodable derEncodable) throws IOException {
        final byte[] derEncoded = derEncodable.getDERObject().getDEREncoded();
        this.isConstructed = isConstructed;
        this.tag = tag;
        if (isConstructed) {
            this.octets = derEncoded;
        }
        else {
            final int lengthOfLength = this.getLengthOfLength(derEncoded);
            final byte[] octets = new byte[derEncoded.length - lengthOfLength];
            System.arraycopy(derEncoded, lengthOfLength, octets, 0, octets.length);
            this.octets = octets;
        }
    }
    
    public DERApplicationSpecific(final int tag, final ASN1EncodableVector asn1EncodableVector) {
        this.tag = tag;
        this.isConstructed = true;
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i != asn1EncodableVector.size(); ++i) {
            try {
                byteArrayOutputStream.write(((ASN1Encodable)asn1EncodableVector.get(i)).getEncoded());
            }
            catch (IOException obj) {
                throw new ASN1ParsingException("malformed object: " + obj, obj);
            }
        }
        this.octets = byteArrayOutputStream.toByteArray();
    }
    
    private int getLengthOfLength(final byte[] array) {
        int n;
        for (n = 2; (array[n - 1] & 0x80) != 0x0; ++n) {}
        return n;
    }
    
    public boolean isConstructed() {
        return this.isConstructed;
    }
    
    public byte[] getContents() {
        return this.octets;
    }
    
    public int getApplicationTag() {
        return this.tag;
    }
    
    public DERObject getObject() throws IOException {
        return new ASN1InputStream(this.getContents()).readObject();
    }
    
    public DERObject getObject(final int n) throws IOException {
        if (n >= 31) {
            throw new IOException("unsupported tag number");
        }
        final byte[] encoded = this.getEncoded();
        final byte[] replaceTagNumber = this.replaceTagNumber(n, encoded);
        if ((encoded[0] & 0x20) != 0x0) {
            final byte[] array = replaceTagNumber;
            final int n2 = 0;
            array[n2] |= 0x20;
        }
        return new ASN1InputStream(replaceTagNumber).readObject();
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        int n = 64;
        if (this.isConstructed) {
            n |= 0x20;
        }
        derOutputStream.writeEncoded(n, this.tag, this.octets);
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        if (!(derObject instanceof DERApplicationSpecific)) {
            return false;
        }
        final DERApplicationSpecific derApplicationSpecific = (DERApplicationSpecific)derObject;
        return this.isConstructed == derApplicationSpecific.isConstructed && this.tag == derApplicationSpecific.tag && Arrays.areEqual(this.octets, derApplicationSpecific.octets);
    }
    
    @Override
    public int hashCode() {
        return (this.isConstructed ? 1 : 0) ^ this.tag ^ Arrays.hashCode(this.octets);
    }
    
    private byte[] replaceTagNumber(final int n, final byte[] array) throws IOException {
        final int n2 = array[0] & 0x1F;
        int n3 = 1;
        if (n2 == 31) {
            int n4 = 0;
            int n5 = array[n3++] & 0xFF;
            if ((n5 & 0x7F) == 0x0) {
                throw new ASN1ParsingException("corrupted stream - invalid high tag number found");
            }
            while (n5 >= 0 && (n5 & 0x80) != 0x0) {
                n4 = (n4 | (n5 & 0x7F)) << 7;
                n5 = (array[n3++] & 0xFF);
            }
        }
        final byte[] array2 = new byte[array.length - n3 + 1];
        System.arraycopy(array, n3, array2, 1, array2.length - 1);
        array2[0] = (byte)n;
        return array2;
    }
}
