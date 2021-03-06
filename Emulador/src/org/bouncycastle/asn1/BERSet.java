// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERSet extends DERSet
{
    public BERSet() {
    }
    
    public BERSet(final DEREncodable derEncodable) {
        super(derEncodable);
    }
    
    public BERSet(final DEREncodableVector derEncodableVector) {
        super(derEncodableVector, false);
    }
    
    BERSet(final DEREncodableVector derEncodableVector, final boolean b) {
        super(derEncodableVector, b);
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        if (derOutputStream instanceof ASN1OutputStream || derOutputStream instanceof BEROutputStream) {
            derOutputStream.write(49);
            derOutputStream.write(128);
            final Enumeration objects = this.getObjects();
            while (objects.hasMoreElements()) {
                derOutputStream.writeObject(objects.nextElement());
            }
            derOutputStream.write(0);
            derOutputStream.write(0);
        }
        else {
            super.encode(derOutputStream);
        }
    }
}
