// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERSequence extends DERSequence
{
    public BERSequence() {
    }
    
    public BERSequence(final DEREncodable derEncodable) {
        super(derEncodable);
    }
    
    public BERSequence(final DEREncodableVector derEncodableVector) {
        super(derEncodableVector);
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        if (derOutputStream instanceof ASN1OutputStream || derOutputStream instanceof BEROutputStream) {
            derOutputStream.write(48);
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
