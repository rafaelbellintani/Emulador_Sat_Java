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
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class DSAParameter extends ASN1Encodable
{
    DERInteger p;
    DERInteger q;
    DERInteger g;
    
    public static DSAParameter getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static DSAParameter getInstance(final Object o) {
        if (o == null || o instanceof DSAParameter) {
            return (DSAParameter)o;
        }
        if (o instanceof ASN1Sequence) {
            return new DSAParameter((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid DSAParameter: " + o.getClass().getName());
    }
    
    public DSAParameter(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        this.p = new DERInteger(bigInteger);
        this.q = new DERInteger(bigInteger2);
        this.g = new DERInteger(bigInteger3);
    }
    
    public DSAParameter(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 3) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        this.p = DERInteger.getInstance(objects.nextElement());
        this.q = DERInteger.getInstance(objects.nextElement());
        this.g = DERInteger.getInstance(objects.nextElement());
    }
    
    public BigInteger getP() {
        return this.p.getPositiveValue();
    }
    
    public BigInteger getQ() {
        return this.q.getPositiveValue();
    }
    
    public BigInteger getG() {
        return this.g.getPositiveValue();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.q);
        asn1EncodableVector.add(this.g);
        return new DERSequence(asn1EncodableVector);
    }
}
