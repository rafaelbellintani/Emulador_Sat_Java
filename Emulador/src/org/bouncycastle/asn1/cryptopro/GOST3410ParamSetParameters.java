// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cryptopro;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class GOST3410ParamSetParameters extends ASN1Encodable
{
    int keySize;
    DERInteger p;
    DERInteger q;
    DERInteger a;
    
    public static GOST3410ParamSetParameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static GOST3410ParamSetParameters getInstance(final Object o) {
        if (o == null || o instanceof GOST3410ParamSetParameters) {
            return (GOST3410ParamSetParameters)o;
        }
        if (o instanceof ASN1Sequence) {
            return new GOST3410ParamSetParameters((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid GOST3410Parameter: " + o.getClass().getName());
    }
    
    public GOST3410ParamSetParameters(final int keySize, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        this.keySize = keySize;
        this.p = new DERInteger(bigInteger);
        this.q = new DERInteger(bigInteger2);
        this.a = new DERInteger(bigInteger3);
    }
    
    public GOST3410ParamSetParameters(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.keySize = objects.nextElement().getValue().intValue();
        this.p = objects.nextElement();
        this.q = objects.nextElement();
        this.a = objects.nextElement();
    }
    
    @Deprecated
    public int getLKeySize() {
        return this.keySize;
    }
    
    public int getKeySize() {
        return this.keySize;
    }
    
    public BigInteger getP() {
        return this.p.getPositiveValue();
    }
    
    public BigInteger getQ() {
        return this.q.getPositiveValue();
    }
    
    public BigInteger getA() {
        return this.a.getPositiveValue();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(this.keySize));
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.q);
        asn1EncodableVector.add(this.a);
        return new DERSequence(asn1EncodableVector);
    }
}
