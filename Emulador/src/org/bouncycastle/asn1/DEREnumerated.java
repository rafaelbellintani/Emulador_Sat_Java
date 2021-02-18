// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import org.bouncycastle.util.Arrays;
import java.io.IOException;
import java.math.BigInteger;

public class DEREnumerated extends ASN1Object
{
    byte[] bytes;
    
    public static DEREnumerated getInstance(final Object o) {
        if (o == null || o instanceof DEREnumerated) {
            return (DEREnumerated)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DEREnumerated(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DEREnumerated getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DEREnumerated(final int n) {
        this.bytes = BigInteger.valueOf(n).toByteArray();
    }
    
    public DEREnumerated(final BigInteger bigInteger) {
        this.bytes = bigInteger.toByteArray();
    }
    
    public DEREnumerated(final byte[] bytes) {
        this.bytes = bytes;
    }
    
    public BigInteger getValue() {
        return new BigInteger(this.bytes);
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(10, this.bytes);
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DEREnumerated && Arrays.areEqual(this.bytes, ((DEREnumerated)derObject).bytes);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }
}
