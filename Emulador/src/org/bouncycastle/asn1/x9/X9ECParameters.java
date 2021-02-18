// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECCurve;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class X9ECParameters extends ASN1Encodable implements X9ObjectIdentifiers
{
    private static final BigInteger ONE;
    private X9FieldID fieldID;
    private ECCurve curve;
    private ECPoint g;
    private BigInteger n;
    private BigInteger h;
    private byte[] seed;
    
    public X9ECParameters(final ASN1Sequence asn1Sequence) {
        if (!(asn1Sequence.getObjectAt(0) instanceof DERInteger) || !((DERInteger)asn1Sequence.getObjectAt(0)).getValue().equals(X9ECParameters.ONE)) {
            throw new IllegalArgumentException("bad version in X9ECParameters");
        }
        final X9Curve x9Curve = new X9Curve(new X9FieldID((ASN1Sequence)asn1Sequence.getObjectAt(1)), (ASN1Sequence)asn1Sequence.getObjectAt(2));
        this.curve = x9Curve.getCurve();
        this.g = new X9ECPoint(this.curve, (ASN1OctetString)asn1Sequence.getObjectAt(3)).getPoint();
        this.n = ((DERInteger)asn1Sequence.getObjectAt(4)).getValue();
        this.seed = x9Curve.getSeed();
        if (asn1Sequence.size() == 6) {
            this.h = ((DERInteger)asn1Sequence.getObjectAt(5)).getValue();
        }
    }
    
    public X9ECParameters(final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger) {
        this(ecCurve, ecPoint, bigInteger, X9ECParameters.ONE, null);
    }
    
    public X9ECParameters(final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger, final BigInteger bigInteger2) {
        this(ecCurve, ecPoint, bigInteger, bigInteger2, null);
    }
    
    public X9ECParameters(final ECCurve curve, final ECPoint g, final BigInteger n, final BigInteger h, final byte[] seed) {
        this.curve = curve;
        this.g = g;
        this.n = n;
        this.h = h;
        this.seed = seed;
        if (curve instanceof ECCurve.Fp) {
            this.fieldID = new X9FieldID(((ECCurve.Fp)curve).getQ());
        }
        else if (curve instanceof ECCurve.F2m) {
            final ECCurve.F2m f2m = (ECCurve.F2m)curve;
            this.fieldID = new X9FieldID(f2m.getM(), f2m.getK1(), f2m.getK2(), f2m.getK3());
        }
    }
    
    public ECCurve getCurve() {
        return this.curve;
    }
    
    public ECPoint getG() {
        return this.g;
    }
    
    public BigInteger getN() {
        return this.n;
    }
    
    public BigInteger getH() {
        if (this.h == null) {
            return X9ECParameters.ONE;
        }
        return this.h;
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(1));
        asn1EncodableVector.add(this.fieldID);
        asn1EncodableVector.add(new X9Curve(this.curve, this.seed));
        asn1EncodableVector.add(new X9ECPoint(this.g));
        asn1EncodableVector.add(new DERInteger(this.n));
        if (this.h != null) {
            asn1EncodableVector.add(new DERInteger(this.h));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
}
