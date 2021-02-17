// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public class DEROctetString extends ASN1OctetString
{
    public DEROctetString(final byte[] array) {
        super(array);
    }
    
    public DEROctetString(final DEREncodable derEncodable) {
        super(derEncodable);
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(4, this.string);
    }
    
    static void encode(final DEROutputStream derOutputStream, final byte[] array) throws IOException {
        derOutputStream.writeEncoded(4, array);
    }
}
