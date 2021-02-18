// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1Object extends DERObject
{
    public static ASN1Object fromByteArray(final byte[] array) throws IOException {
        return (ASN1Object)new ASN1InputStream(array).readObject();
    }
    
    @Override
    public final boolean equals(final Object o) {
        return this == o || (o instanceof DEREncodable && this.asn1Equals(((DEREncodable)o).getDERObject()));
    }
    
    @Override
    public abstract int hashCode();
    
    @Override
    abstract void encode(final DEROutputStream p0) throws IOException;
    
    abstract boolean asn1Equals(final DERObject p0);
}
