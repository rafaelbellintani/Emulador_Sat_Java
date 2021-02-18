// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import org.bouncycastle.util.Arrays;
import java.io.IOException;
import java.math.BigInteger;

public class DERInteger extends ASN1Object
{
    byte[] bytes;
    
    public static DERInteger getInstance(final Object o) {
        if (o == null || o instanceof DERInteger) {
            return (DERInteger)o;
        }
        if (o instanceof ASN1OctetString) {
            return new DERInteger(((ASN1OctetString)o).getOctets());
        }
        if (o instanceof ASN1TaggedObject) {
            return getInstance(((ASN1TaggedObject)o).getObject());
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static DERInteger getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public DERInteger(final int n) {
        this.bytes = BigInteger.valueOf(n).toByteArray();
    }
    
    public DERInteger(final BigInteger bigInteger) {
        this.bytes = bigInteger.toByteArray();
    }
    
    public DERInteger(final byte[] bytes) {
        this.bytes = bytes;
    }
    
    public BigInteger getValue() {
        return new BigInteger(this.bytes);
    }
    
    public BigInteger getPositiveValue() {
        return new BigInteger(1, this.bytes);
    }
    
    @Override
    void encode(final DEROutputStream derOutputStream) throws IOException {
        derOutputStream.writeEncoded(2, this.bytes);
    }
    
    @Override
    public int hashCode() {
        int n = 0;
        for (int i = 0; i != this.bytes.length; ++i) {
            n ^= (this.bytes[i] & 0xFF) << i % 4;
        }
        return n;
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof DERInteger && Arrays.areEqual(this.bytes, ((DERInteger)derObject).bytes);
    }
    
    @Override
    public String toString() {
        return this.getValue().toString();
    }
}
