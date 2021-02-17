// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKCS12PBEParams extends ASN1Encodable
{
    DERInteger iterations;
    ASN1OctetString iv;
    
    public PKCS12PBEParams(final byte[] array, final int n) {
        this.iv = new DEROctetString(array);
        this.iterations = new DERInteger(n);
    }
    
    public PKCS12PBEParams(final ASN1Sequence asn1Sequence) {
        this.iv = (ASN1OctetString)asn1Sequence.getObjectAt(0);
        this.iterations = (DERInteger)asn1Sequence.getObjectAt(1);
    }
    
    public static PKCS12PBEParams getInstance(final Object o) {
        if (o instanceof PKCS12PBEParams) {
            return (PKCS12PBEParams)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PKCS12PBEParams((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public BigInteger getIterations() {
        return this.iterations.getValue();
    }
    
    public byte[] getIV() {
        return this.iv.getOctets();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.iv);
        asn1EncodableVector.add(this.iterations);
        return new DERSequence(asn1EncodableVector);
    }
}
