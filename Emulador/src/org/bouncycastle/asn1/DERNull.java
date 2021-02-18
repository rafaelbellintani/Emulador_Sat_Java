// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DERNull extends ASN1Null
{
    public static final DERNull INSTANCE;
    byte[] zeroBytes;
    
    public DERNull() {
        this.zeroBytes = new byte[0];
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(5, this.zeroBytes);
    }
    
    static {
        INSTANCE = new DERNull();
    }
}
