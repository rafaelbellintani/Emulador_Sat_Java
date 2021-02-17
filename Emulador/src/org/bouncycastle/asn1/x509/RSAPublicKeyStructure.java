// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class RSAPublicKeyStructure extends ASN1Encodable
{
    private BigInteger modulus;
    private BigInteger publicExponent;
    
    public static RSAPublicKeyStructure getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static RSAPublicKeyStructure getInstance(final Object o) {
        if (o == null || o instanceof RSAPublicKeyStructure) {
            return (RSAPublicKeyStructure)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RSAPublicKeyStructure((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid RSAPublicKeyStructure: " + o.getClass().getName());
    }
    
    public RSAPublicKeyStructure(final BigInteger modulus, final BigInteger publicExponent) {
        this.modulus = modulus;
        this.publicExponent = publicExponent;
    }
    
    public RSAPublicKeyStructure(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        this.modulus = DERInteger.getInstance(objects.nextElement()).getPositiveValue();
        this.publicExponent = DERInteger.getInstance(objects.nextElement()).getPositiveValue();
    }
    
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(this.getModulus()));
        asn1EncodableVector.add(new DERInteger(this.getPublicExponent()));
        return new DERSequence(asn1EncodableVector);
    }
}
