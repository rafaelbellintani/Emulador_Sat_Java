// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class BERNull extends DERNull
{
    public static final BERNull INSTANCE;
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        if (derOutputStream instanceof ASN1OutputStream || derOutputStream instanceof BEROutputStream) {
            derOutputStream.write(5);
        }
        else {
            super.encode(derOutputStream);
        }
    }
    
    static {
        INSTANCE = new BERNull();
    }
}
