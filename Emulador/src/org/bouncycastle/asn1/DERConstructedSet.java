// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class DERConstructedSet extends ASN1Set
{
    public DERConstructedSet() {
    }
    
    public DERConstructedSet(final DEREncodable derEncodable) {
        this.addObject(derEncodable);
    }
    
    public DERConstructedSet(final DEREncodableVector derEncodableVector) {
        for (int i = 0; i != derEncodableVector.size(); ++i) {
            this.addObject(derEncodableVector.get(i));
        }
    }
    
    public void addObject(final DEREncodable derEncodable) {
        super.addObject(derEncodable);
    }
    
    public int getSize() {
        return this.size();
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
