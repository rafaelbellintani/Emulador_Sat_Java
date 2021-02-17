// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.asymmetric.ec;

import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import java.security.spec.ECPoint;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import java.math.BigInteger;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECField;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;
import org.bouncycastle.math.ec.ECCurve;

public class EC5Util
{
    public static EllipticCurve convertCurve(final ECCurve ecCurve, final byte[] array) {
        if (ecCurve instanceof ECCurve.Fp) {
            return new EllipticCurve(new ECFieldFp(((ECCurve.Fp)ecCurve).getQ()), ecCurve.getA().toBigInteger(), ecCurve.getB().toBigInteger(), null);
        }
        final ECCurve.F2m f2m = (ECCurve.F2m)ecCurve;
        if (f2m.isTrinomial()) {
            return new EllipticCurve(new ECFieldF2m(f2m.getM(), new int[] { f2m.getK1() }), ecCurve.getA().toBigInteger(), ecCurve.getB().toBigInteger(), null);
        }
        return new EllipticCurve(new ECFieldF2m(f2m.getM(), new int[] { f2m.getK3(), f2m.getK2(), f2m.getK1() }), ecCurve.getA().toBigInteger(), ecCurve.getB().toBigInteger(), null);
    }
    
    public static ECCurve convertCurve(final EllipticCurve ellipticCurve) {
        final ECField field = ellipticCurve.getField();
        final BigInteger a = ellipticCurve.getA();
        final BigInteger b = ellipticCurve.getB();
        if (field instanceof ECFieldFp) {
            return new ECCurve.Fp(((ECFieldFp)field).getP(), a, b);
        }
        final ECFieldF2m ecFieldF2m = (ECFieldF2m)field;
        final int m = ecFieldF2m.getM();
        final int[] convertMidTerms = ECUtil.convertMidTerms(ecFieldF2m.getMidTermsOfReductionPolynomial());
        return new ECCurve.F2m(m, convertMidTerms[0], convertMidTerms[1], convertMidTerms[2], a, b);
    }
    
    public static java.security.spec.ECParameterSpec convertSpec(final EllipticCurve curve, final ECParameterSpec ecParameterSpec) {
        if (ecParameterSpec instanceof ECNamedCurveParameterSpec) {
            return new ECNamedCurveSpec(((ECNamedCurveParameterSpec)ecParameterSpec).getName(), curve, new ECPoint(ecParameterSpec.getG().getX().toBigInteger(), ecParameterSpec.getG().getY().toBigInteger()), ecParameterSpec.getN(), ecParameterSpec.getH());
        }
        return new java.security.spec.ECParameterSpec(curve, new ECPoint(ecParameterSpec.getG().getX().toBigInteger(), ecParameterSpec.getG().getY().toBigInteger()), ecParameterSpec.getN(), ecParameterSpec.getH().intValue());
    }
    
    public static ECParameterSpec convertSpec(final java.security.spec.ECParameterSpec ecParameterSpec, final boolean b) {
        final ECCurve convertCurve = convertCurve(ecParameterSpec.getCurve());
        return new ECParameterSpec(convertCurve, convertPoint(convertCurve, ecParameterSpec.getGenerator(), b), ecParameterSpec.getOrder(), BigInteger.valueOf(ecParameterSpec.getCofactor()), ecParameterSpec.getCurve().getSeed());
    }
    
    public static org.bouncycastle.math.ec.ECPoint convertPoint(final java.security.spec.ECParameterSpec ecParameterSpec, final ECPoint ecPoint, final boolean b) {
        return convertPoint(convertCurve(ecParameterSpec.getCurve()), ecPoint, b);
    }
    
    public static org.bouncycastle.math.ec.ECPoint convertPoint(final ECCurve ecCurve, final ECPoint ecPoint, final boolean b) {
        return ecCurve.createPoint(ecPoint.getAffineX(), ecPoint.getAffineY(), b);
    }
}
