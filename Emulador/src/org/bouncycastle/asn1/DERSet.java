// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class DERSet extends ASN1Set
{
    public DERSet() {
    }
    
    public DERSet(final DEREncodable derEncodable) {
        this.addObject(derEncodable);
    }
    
    public DERSet(final DEREncodableVector derEncodableVector) {
        this(derEncodableVector, true);
    }
    
    public DERSet(final ASN1Encodable[] array) {
        for (int i = 0; i != array.length; ++i) {
            this.addObject(array[i]);
        }
        this.sort();
    }
    
    DERSet(final DEREncodableVector derEncodableVector, final boolean b) {
        for (int i = 0; i != derEncodableVector.size(); ++i) {
            this.addObject(derEncodableVector.get(i));
        }
        if (b) {
            this.sort();
        }
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DEROutputStream derOutputStream2 = new DEROutputStream(byteArrayOutputStream);
        final Enumeration objects = this.getObjects();
        while (objects.hasMoreElements()) {
            derOutputStream2.writeObject(objects.nextElement());
        }
        derOutputStream2.close();
        derOutputStream.writeEncoded(49, byteArrayOutputStream.toByteArray());
    }
}
