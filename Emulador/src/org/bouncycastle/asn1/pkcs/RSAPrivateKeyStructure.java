// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class RSAPrivateKeyStructure extends ASN1Encodable
{
    private int version;
    private BigInteger modulus;
    private BigInteger publicExponent;
    private BigInteger privateExponent;
    private BigInteger prime1;
    private BigInteger prime2;
    private BigInteger exponent1;
    private BigInteger exponent2;
    private BigInteger coefficient;
    private ASN1Sequence otherPrimeInfos;
    
    public static RSAPrivateKeyStructure getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static RSAPrivateKeyStructure getInstance(final Object o) {
        if (o instanceof RSAPrivateKeyStructure) {
            return (RSAPrivateKeyStructure)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RSAPrivateKeyStructure((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public RSAPrivateKeyStructure(final BigInteger modulus, final BigInteger publicExponent, final BigInteger privateExponent, final BigInteger prime1, final BigInteger prime2, final BigInteger exponent1, final BigInteger exponent2, final BigInteger coefficient) {
        this.otherPrimeInfos = null;
        this.version = 0;
        this.modulus = modulus;
        this.publicExponent = publicExponent;
        this.privateExponent = privateExponent;
        this.prime1 = prime1;
        this.prime2 = prime2;
        this.exponent1 = exponent1;
        this.exponent2 = exponent2;
        this.coefficient = coefficient;
    }
    
    public RSAPrivateKeyStructure(final ASN1Sequence asn1Sequence) {
        this.otherPrimeInfos = null;
        final Enumeration objects = asn1Sequence.getObjects();
        final BigInteger value = objects.nextElement().getValue();
        if (value.intValue() != 0 && value.intValue() != 1) {
            throw new IllegalArgumentException("wrong version for RSA private key");
        }
        this.version = value.intValue();
        this.modulus = objects.nextElement().getValue();
        this.publicExponent = objects.nextElement().getValue();
        this.privateExponent = objects.nextElement().getValue();
        this.prime1 = objects.nextElement().getValue();
        this.prime2 = objects.nextElement().getValue();
        this.exponent1 = objects.nextElement().getValue();
        this.exponent2 = objects.nextElement().getValue();
        this.coefficient = objects.nextElement().getValue();
        if (objects.hasMoreElements()) {
            this.otherPrimeInfos = (ASN1Sequence)objects.nextElement();
        }
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
    
    public BigInteger getPrivateExponent() {
        return this.privateExponent;
    }
    
    public BigInteger getPrime1() {
        return this.prime1;
    }
    
    public BigInteger getPrime2() {
        return this.prime2;
    }
    
    public BigInteger getExponent1() {
        return this.exponent1;
    }
    
    public BigInteger getExponent2() {
        return this.exponent2;
    }
    
    public BigInteger getCoefficient() {
        return this.coefficient;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(this.version));
        asn1EncodableVector.add(new DERInteger(this.getModulus()));
        asn1EncodableVector.add(new DERInteger(this.getPublicExponent()));
        asn1EncodableVector.add(new DERInteger(this.getPrivateExponent()));
        asn1EncodableVector.add(new DERInteger(this.getPrime1()));
        asn1EncodableVector.add(new DERInteger(this.getPrime2()));
        asn1EncodableVector.add(new DERInteger(this.getExponent1()));
        asn1EncodableVector.add(new DERInteger(this.getExponent2()));
        asn1EncodableVector.add(new DERInteger(this.getCoefficient()));
        if (this.otherPrimeInfos != null) {
            asn1EncodableVector.add(this.otherPrimeInfos);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
