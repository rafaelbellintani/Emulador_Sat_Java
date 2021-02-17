// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECField;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;
import org.bouncycastle.math.ec.ECCurve;
import java.security.spec.ECParameterSpec;

public class ECNamedCurveSpec extends ECParameterSpec
{
    private String name;
    
    private static EllipticCurve convertCurve(final ECCurve ecCurve, final byte[] seed) {
        if (ecCurve instanceof ECCurve.Fp) {
            return new EllipticCurve(new ECFieldFp(((ECCurve.Fp)ecCurve).getQ()), ecCurve.getA().toBigInteger(), ecCurve.getB().toBigInteger(), seed);
        }
        final ECCurve.F2m f2m = (ECCurve.F2m)ecCurve;
        if (f2m.isTrinomial()) {
            return new EllipticCurve(new ECFieldF2m(f2m.getM(), new int[] { f2m.getK1() }), ecCurve.getA().toBigInteger(), ecCurve.getB().toBigInteger(), seed);
        }
        return new EllipticCurve(new ECFieldF2m(f2m.getM(), new int[] { f2m.getK3(), f2m.getK2(), f2m.getK1() }), ecCurve.getA().toBigInteger(), ecCurve.getB().toBigInteger(), seed);
    }
    
    private static java.security.spec.ECPoint convertPoint(final ECPoint ecPoint) {
        return new java.security.spec.ECPoint(ecPoint.getX().toBigInteger(), ecPoint.getY().toBigInteger());
    }
    
    public ECNamedCurveSpec(final String name, final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger n) {
        super(convertCurve(ecCurve, null), convertPoint(ecPoint), n, 1);
        this.name = name;
    }
    
    public ECNamedCurveSpec(final String name, final EllipticCurve curve, final java.security.spec.ECPoint g, final BigInteger n) {
        super(curve, g, n, 1);
        this.name = name;
    }
    
    public ECNamedCurveSpec(final String name, final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger n, final BigInteger bigInteger) {
        super(convertCurve(ecCurve, null), convertPoint(ecPoint), n, bigInteger.intValue());
        this.name = name;
    }
    
    public ECNamedCurveSpec(final String name, final EllipticCurve curve, final java.security.spec.ECPoint g, final BigInteger n, final BigInteger bigInteger) {
        super(curve, g, n, bigInteger.intValue());
        this.name = name;
    }
    
    public ECNamedCurveSpec(final String name, final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger n, final BigInteger bigInteger, final byte[] array) {
        super(convertCurve(ecCurve, array), convertPoint(ecPoint), n, bigInteger.intValue());
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
