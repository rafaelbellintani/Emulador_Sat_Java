// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public abstract class ASN1Encodable implements DEREncodable
{
    public static final String DER = "DER";
    public static final String BER = "BER";
    
    public byte[] getEncoded() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ASN1OutputStream(byteArrayOutputStream).writeObject(this);
        return byteArrayOutputStream.toByteArray();
    }
    
    public byte[] getEncoded(final String s) throws IOException {
        if (s.equals("DER")) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new DEROutputStream(byteArrayOutputStream).writeObject(this);
            return byteArrayOutputStream.toByteArray();
        }
        return this.getEncoded();
    }
    
    public byte[] getDEREncoded() {
        try {
            return this.getEncoded("DER");
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public int hashCode() {
        return this.toASN1Object().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof DEREncodable && this.toASN1Object().equals(((DEREncodable)o).getDERObject()));
    }
    
    @Override
    public DERObject getDERObject() {
        return this.toASN1Object();
    }
    
    public abstract DERObject toASN1Object();
}
