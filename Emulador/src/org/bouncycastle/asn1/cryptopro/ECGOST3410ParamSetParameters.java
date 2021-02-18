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

public class ECGOST3410ParamSetParameters extends ASN1Encodable
{
    DERInteger p;
    DERInteger q;
    DERInteger a;
    DERInteger b;
    DERInteger x;
    DERInteger y;
    
    public static ECGOST3410ParamSetParameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static ECGOST3410ParamSetParameters getInstance(final Object o) {
        if (o == null || o instanceof ECGOST3410ParamSetParameters) {
            return (ECGOST3410ParamSetParameters)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ECGOST3410ParamSetParameters((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid GOST3410Parameter: " + o.getClass().getName());
    }
    
    public ECGOST3410ParamSetParameters(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4, final int n, final BigInteger bigInteger5) {
        this.a = new DERInteger(bigInteger);
        this.b = new DERInteger(bigInteger2);
        this.p = new DERInteger(bigInteger3);
        this.q = new DERInteger(bigInteger4);
        this.x = new DERInteger(n);
        this.y = new DERInteger(bigInteger5);
    }
    
    public ECGOST3410ParamSetParameters(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.a = objects.nextElement();
        this.b = objects.nextElement();
        this.p = objects.nextElement();
        this.q = objects.nextElement();
        this.x = objects.nextElement();
        this.y = objects.nextElement();
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
        asn1EncodableVector.add(this.a);
        asn1EncodableVector.add(this.b);
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.q);
        asn1EncodableVector.add(this.x);
        asn1EncodableVector.add(this.y);
        return new DERSequence(asn1EncodableVector);
    }
}
