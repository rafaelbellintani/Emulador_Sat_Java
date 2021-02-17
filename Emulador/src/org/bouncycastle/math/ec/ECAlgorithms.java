// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.math.BigInteger;

public class ECAlgorithms
{
    public static ECPoint sumOfTwoMultiplies(final ECPoint ecPoint, final BigInteger bigInteger, final ECPoint ecPoint2, final BigInteger bigInteger2) {
        if (!ecPoint.getCurve().equals(ecPoint2.getCurve())) {
            throw new IllegalArgumentException("P and Q must be on same curve");
        }
        return implShamirsTrick(ecPoint, bigInteger, ecPoint2, bigInteger2);
    }
    
    public static ECPoint shamirsTrick(final ECPoint ecPoint, final BigInteger bigInteger, final ECPoint ecPoint2, final BigInteger bigInteger2) {
        if (!ecPoint.getCurve().equals(ecPoint2.getCurve())) {
            throw new IllegalArgumentException("P and Q must be on same curve");
        }
        return implShamirsTrick(ecPoint, bigInteger, ecPoint2, bigInteger2);
    }
    
    private static ECPoint implShamirsTrick(final ECPoint ecPoint, final BigInteger bigInteger, final ECPoint ecPoint2, final BigInteger bigInteger2) {
        final int max = Math.max(bigInteger.bitLength(), bigInteger2.bitLength());
        final ECPoint add = ecPoint.add(ecPoint2);
        ECPoint ecPoint3 = ecPoint.getCurve().getInfinity();
        for (int i = max - 1; i >= 0; --i) {
            ecPoint3 = ecPoint3.twice();
            if (bigInteger.testBit(i)) {
                if (bigInteger2.testBit(i)) {
                    ecPoint3 = ecPoint3.add(add);
                }
                else {
                    ecPoint3 = ecPoint3.add(ecPoint);
                }
            }
            else if (bigInteger2.testBit(i)) {
                ecPoint3 = ecPoint3.add(ecPoint2);
            }
        }
        return ecPoint3;
    }
}
