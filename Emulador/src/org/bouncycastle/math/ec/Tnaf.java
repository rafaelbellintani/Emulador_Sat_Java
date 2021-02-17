// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.math.BigInteger;

class Tnaf
{
    private static final BigInteger MINUS_ONE;
    private static final BigInteger MINUS_TWO;
    private static final BigInteger MINUS_THREE;
    public static final byte WIDTH = 4;
    public static final byte POW_2_WIDTH = 16;
    public static final ZTauElement[] alpha0;
    public static final byte[][] alpha0Tnaf;
    public static final ZTauElement[] alpha1;
    public static final byte[][] alpha1Tnaf;
    
    public static BigInteger norm(final byte b, final ZTauElement zTauElement) {
        final BigInteger multiply = zTauElement.u.multiply(zTauElement.u);
        final BigInteger multiply2 = zTauElement.u.multiply(zTauElement.v);
        final BigInteger shiftLeft = zTauElement.v.multiply(zTauElement.v).shiftLeft(1);
        BigInteger bigInteger;
        if (b == 1) {
            bigInteger = multiply.add(multiply2).add(shiftLeft);
        }
        else {
            if (b != -1) {
                throw new IllegalArgumentException("mu must be 1 or -1");
            }
            bigInteger = multiply.subtract(multiply2).add(shiftLeft);
        }
        return bigInteger;
    }
    
    public static SimpleBigDecimal norm(final byte b, final SimpleBigDecimal simpleBigDecimal, final SimpleBigDecimal simpleBigDecimal2) {
        final SimpleBigDecimal multiply = simpleBigDecimal.multiply(simpleBigDecimal);
        final SimpleBigDecimal multiply2 = simpleBigDecimal.multiply(simpleBigDecimal2);
        final SimpleBigDecimal shiftLeft = simpleBigDecimal2.multiply(simpleBigDecimal2).shiftLeft(1);
        SimpleBigDecimal simpleBigDecimal3;
        if (b == 1) {
            simpleBigDecimal3 = multiply.add(multiply2).add(shiftLeft);
        }
        else {
            if (b != -1) {
                throw new IllegalArgumentException("mu must be 1 or -1");
            }
            simpleBigDecimal3 = multiply.subtract(multiply2).add(shiftLeft);
        }
        return simpleBigDecimal3;
    }
    
    public static ZTauElement round(final SimpleBigDecimal simpleBigDecimal, final SimpleBigDecimal simpleBigDecimal2, final byte b) {
        if (simpleBigDecimal2.getScale() != simpleBigDecimal.getScale()) {
            throw new IllegalArgumentException("lambda0 and lambda1 do not have same scale");
        }
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        final BigInteger round = simpleBigDecimal.round();
        final BigInteger round2 = simpleBigDecimal2.round();
        final SimpleBigDecimal subtract = simpleBigDecimal.subtract(round);
        final SimpleBigDecimal subtract2 = simpleBigDecimal2.subtract(round2);
        final SimpleBigDecimal add = subtract.add(subtract);
        SimpleBigDecimal simpleBigDecimal3;
        if (b == 1) {
            simpleBigDecimal3 = add.add(subtract2);
        }
        else {
            simpleBigDecimal3 = add.subtract(subtract2);
        }
        final SimpleBigDecimal add2 = subtract2.add(subtract2).add(subtract2);
        final SimpleBigDecimal add3 = add2.add(subtract2);
        SimpleBigDecimal simpleBigDecimal4;
        SimpleBigDecimal simpleBigDecimal5;
        if (b == 1) {
            simpleBigDecimal4 = subtract.subtract(add2);
            simpleBigDecimal5 = subtract.add(add3);
        }
        else {
            simpleBigDecimal4 = subtract.add(add2);
            simpleBigDecimal5 = subtract.subtract(add3);
        }
        int n = 0;
        byte b2 = 0;
        if (simpleBigDecimal3.compareTo(ECConstants.ONE) >= 0) {
            if (simpleBigDecimal4.compareTo(Tnaf.MINUS_ONE) < 0) {
                b2 = b;
            }
            else {
                n = 1;
            }
        }
        else if (simpleBigDecimal5.compareTo(ECConstants.TWO) >= 0) {
            b2 = b;
        }
        if (simpleBigDecimal3.compareTo(Tnaf.MINUS_ONE) < 0) {
            if (simpleBigDecimal4.compareTo(ECConstants.ONE) >= 0) {
                b2 = (byte)(-b);
            }
            else {
                n = -1;
            }
        }
        else if (simpleBigDecimal5.compareTo(Tnaf.MINUS_TWO) < 0) {
            b2 = (byte)(-b);
        }
        return new ZTauElement(round.add(BigInteger.valueOf(n)), round2.add(BigInteger.valueOf(b2)));
    }
    
    public static SimpleBigDecimal approximateDivisionByN(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final byte b, final int n, final int n2) {
        final int n3 = (n + 5) / 2 + n2;
        final BigInteger multiply = bigInteger2.multiply(bigInteger.shiftRight(n - n3 - 2 + b));
        final BigInteger add = multiply.add(bigInteger3.multiply(multiply.shiftRight(n)));
        BigInteger bigInteger4 = add.shiftRight(n3 - n2);
        if (add.testBit(n3 - n2 - 1)) {
            bigInteger4 = bigInteger4.add(ECConstants.ONE);
        }
        return new SimpleBigDecimal(bigInteger4, n2);
    }
    
    public static byte[] tauAdicNaf(final byte b, final ZTauElement zTauElement) {
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        final int bitLength = norm(b, zTauElement).bitLength();
        final byte[] array = new byte[(bitLength > 30) ? (bitLength + 4) : 34];
        int n = 0;
        int n2 = 0;
        BigInteger bigInteger3;
        for (BigInteger bigInteger = zTauElement.u, bigInteger2 = zTauElement.v; !bigInteger.equals(ECConstants.ZERO) || !bigInteger2.equals(ECConstants.ZERO); bigInteger2 = bigInteger3.shiftRight(1).negate(), ++n) {
            if (bigInteger.testBit(0)) {
                array[n] = (byte)ECConstants.TWO.subtract(bigInteger.subtract(bigInteger2.shiftLeft(1)).mod(ECConstants.FOUR)).intValue();
                if (array[n] == 1) {
                    bigInteger = bigInteger.clearBit(0);
                }
                else {
                    bigInteger = bigInteger.add(ECConstants.ONE);
                }
                n2 = n;
            }
            else {
                array[n] = 0;
            }
            bigInteger3 = bigInteger;
            final BigInteger shiftRight = bigInteger.shiftRight(1);
            if (b == 1) {
                bigInteger = bigInteger2.add(shiftRight);
            }
            else {
                bigInteger = bigInteger2.subtract(shiftRight);
            }
        }
        final byte[] array2 = new byte[++n2];
        System.arraycopy(array, 0, array2, 0, n2);
        return array2;
    }
    
    public static ECPoint.F2m tau(final ECPoint.F2m f2m) {
        if (f2m.isInfinity()) {
            return f2m;
        }
        return new ECPoint.F2m(f2m.getCurve(), f2m.getX().square(), f2m.getY().square(), f2m.isCompressed());
    }
    
    public static byte getMu(final ECCurve.F2m f2m) {
        final BigInteger bigInteger = f2m.getA().toBigInteger();
        byte b;
        if (bigInteger.equals(ECConstants.ZERO)) {
            b = -1;
        }
        else {
            if (!bigInteger.equals(ECConstants.ONE)) {
                throw new IllegalArgumentException("No Koblitz curve (ABC), TNAF multiplication not possible");
            }
            b = 1;
        }
        return b;
    }
    
    public static BigInteger[] getLucas(final byte b, final int n, final boolean b2) {
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        BigInteger bigInteger;
        BigInteger bigInteger2;
        if (b2) {
            bigInteger = ECConstants.TWO;
            bigInteger2 = BigInteger.valueOf(b);
        }
        else {
            bigInteger = ECConstants.ZERO;
            bigInteger2 = ECConstants.ONE;
        }
        for (int i = 1; i < n; ++i) {
            BigInteger negate;
            if (b == 1) {
                negate = bigInteger2;
            }
            else {
                negate = bigInteger2.negate();
            }
            final BigInteger subtract = negate.subtract(bigInteger.shiftLeft(1));
            bigInteger = bigInteger2;
            bigInteger2 = subtract;
        }
        return new BigInteger[] { bigInteger, bigInteger2 };
    }
    
    public static BigInteger getTw(final byte b, final int bit) {
        if (bit != 4) {
            final BigInteger[] lucas = getLucas(b, bit, false);
            final BigInteger setBit = ECConstants.ZERO.setBit(bit);
            return ECConstants.TWO.multiply(lucas[0]).multiply(lucas[1].modInverse(setBit)).mod(setBit);
        }
        if (b == 1) {
            return BigInteger.valueOf(6L);
        }
        return BigInteger.valueOf(10L);
    }
    
    public static BigInteger[] getSi(final ECCurve.F2m f2m) {
        if (!f2m.isKoblitz()) {
            throw new IllegalArgumentException("si is defined for Koblitz curves only");
        }
        final int m = f2m.getM();
        final int intValue = f2m.getA().toBigInteger().intValue();
        final byte mu = f2m.getMu();
        final int intValue2 = f2m.getH().intValue();
        final BigInteger[] lucas = getLucas(mu, m + 3 - intValue, false);
        BigInteger bigInteger;
        BigInteger bigInteger2;
        if (mu == 1) {
            bigInteger = ECConstants.ONE.subtract(lucas[1]);
            bigInteger2 = ECConstants.ONE.subtract(lucas[0]);
        }
        else {
            if (mu != -1) {
                throw new IllegalArgumentException("mu must be 1 or -1");
            }
            bigInteger = ECConstants.ONE.add(lucas[1]);
            bigInteger2 = ECConstants.ONE.add(lucas[0]);
        }
        final BigInteger[] array = new BigInteger[2];
        if (intValue2 == 2) {
            array[0] = bigInteger.shiftRight(1);
            array[1] = bigInteger2.shiftRight(1).negate();
        }
        else {
            if (intValue2 != 4) {
                throw new IllegalArgumentException("h (Cofactor) must be 2 or 4");
            }
            array[0] = bigInteger.shiftRight(2);
            array[1] = bigInteger2.shiftRight(2).negate();
        }
        return array;
    }
    
    public static ZTauElement partModReduction(final BigInteger bigInteger, final int n, final byte b, final BigInteger[] array, final byte b2, final byte b3) {
        BigInteger bigInteger2;
        if (b2 == 1) {
            bigInteger2 = array[0].add(array[1]);
        }
        else {
            bigInteger2 = array[0].subtract(array[1]);
        }
        final BigInteger bigInteger3 = getLucas(b2, n, true)[1];
        final ZTauElement round = round(approximateDivisionByN(bigInteger, array[0], bigInteger3, b, n, b3), approximateDivisionByN(bigInteger, array[1], bigInteger3, b, n, b3), b2);
        return new ZTauElement(bigInteger.subtract(bigInteger2.multiply(round.u)).subtract(BigInteger.valueOf(2L).multiply(array[1]).multiply(round.v)), array[1].multiply(round.u).subtract(array[0].multiply(round.v)));
    }
    
    public static ECPoint.F2m multiplyRTnaf(final ECPoint.F2m f2m, final BigInteger bigInteger) {
        final ECCurve.F2m f2m2 = (ECCurve.F2m)f2m.getCurve();
        return multiplyTnaf(f2m, partModReduction(bigInteger, f2m2.getM(), (byte)f2m2.getA().toBigInteger().intValue(), f2m2.getSi(), f2m2.getMu(), (byte)10));
    }
    
    public static ECPoint.F2m multiplyTnaf(final ECPoint.F2m f2m, final ZTauElement zTauElement) {
        return multiplyFromTnaf(f2m, tauAdicNaf(((ECCurve.F2m)f2m.getCurve()).getMu(), zTauElement));
    }
    
    public static ECPoint.F2m multiplyFromTnaf(final ECPoint.F2m f2m, final byte[] array) {
        ECPoint.F2m f2m2 = (ECPoint.F2m)((ECCurve.F2m)f2m.getCurve()).getInfinity();
        for (int i = array.length - 1; i >= 0; --i) {
            f2m2 = tau(f2m2);
            if (array[i] == 1) {
                f2m2 = f2m2.addSimple(f2m);
            }
            else if (array[i] == -1) {
                f2m2 = f2m2.subtractSimple(f2m);
            }
        }
        return f2m2;
    }
    
    public static byte[] tauAdicWNaf(final byte b, final ZTauElement zTauElement, final byte b2, final BigInteger bigInteger, final BigInteger val, final ZTauElement[] array) {
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        final int bitLength = norm(b, zTauElement).bitLength();
        final byte[] array2 = new byte[(bitLength > 30) ? (bitLength + 4 + b2) : (34 + b2)];
        final BigInteger shiftRight = bigInteger.shiftRight(1);
        BigInteger bigInteger2 = zTauElement.u;
        BigInteger bigInteger3 = zTauElement.v;
        BigInteger bigInteger4;
        for (int n = 0; !bigInteger2.equals(ECConstants.ZERO) || !bigInteger3.equals(ECConstants.ZERO); bigInteger3 = bigInteger4.shiftRight(1).negate(), ++n) {
            if (bigInteger2.testBit(0)) {
                final BigInteger mod = bigInteger2.add(bigInteger3.multiply(val)).mod(bigInteger);
                byte b3;
                if (mod.compareTo(shiftRight) >= 0) {
                    b3 = (byte)mod.subtract(bigInteger).intValue();
                }
                else {
                    b3 = (byte)mod.intValue();
                }
                array2[n] = b3;
                boolean b4 = true;
                if (b3 < 0) {
                    b4 = false;
                    b3 = (byte)(-b3);
                }
                if (b4) {
                    bigInteger2 = bigInteger2.subtract(array[b3].u);
                    bigInteger3 = bigInteger3.subtract(array[b3].v);
                }
                else {
                    bigInteger2 = bigInteger2.add(array[b3].u);
                    bigInteger3 = bigInteger3.add(array[b3].v);
                }
            }
            else {
                array2[n] = 0;
            }
            bigInteger4 = bigInteger2;
            if (b == 1) {
                bigInteger2 = bigInteger3.add(bigInteger2.shiftRight(1));
            }
            else {
                bigInteger2 = bigInteger3.subtract(bigInteger2.shiftRight(1));
            }
        }
        return array2;
    }
    
    public static ECPoint.F2m[] getPreComp(final ECPoint.F2m f2m, final byte b) {
        final ECPoint.F2m[] array = new ECPoint.F2m[16];
        array[1] = f2m;
        byte[][] array2;
        if (b == 0) {
            array2 = Tnaf.alpha0Tnaf;
        }
        else {
            array2 = Tnaf.alpha1Tnaf;
        }
        for (int length = array2.length, i = 3; i < length; i += 2) {
            array[i] = multiplyFromTnaf(f2m, array2[i]);
        }
        return array;
    }
    
    static {
        MINUS_ONE = ECConstants.ONE.negate();
        MINUS_TWO = ECConstants.TWO.negate();
        MINUS_THREE = ECConstants.THREE.negate();
        alpha0 = new ZTauElement[] { null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(Tnaf.MINUS_THREE, Tnaf.MINUS_ONE), null, new ZTauElement(Tnaf.MINUS_ONE, Tnaf.MINUS_ONE), null, new ZTauElement(ECConstants.ONE, Tnaf.MINUS_ONE), null };
        alpha0Tnaf = new byte[][] { null, { 1 }, null, { -1, 0, 1 }, null, { 1, 0, 1 }, null, { -1, 0, 0, 1 } };
        alpha1 = new ZTauElement[] { null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(Tnaf.MINUS_THREE, ECConstants.ONE), null, new ZTauElement(Tnaf.MINUS_ONE, ECConstants.ONE), null, new ZTauElement(ECConstants.ONE, ECConstants.ONE), null };
        alpha1Tnaf = new byte[][] { null, { 1 }, null, { -1, 0, 1 }, null, { 1, 0, 1 }, null, { -1, 0, 0, -1 } };
    }
}
