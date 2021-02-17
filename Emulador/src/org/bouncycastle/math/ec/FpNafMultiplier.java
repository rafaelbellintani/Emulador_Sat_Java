// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.math.BigInteger;

class FpNafMultiplier implements ECMultiplier
{
    @Override
    public ECPoint multiply(final ECPoint ecPoint, final BigInteger bigInteger, final PreCompInfo preCompInfo) {
        final BigInteger multiply = bigInteger.multiply(BigInteger.valueOf(3L));
        final ECPoint negate = ecPoint.negate();
        ECPoint ecPoint2 = ecPoint;
        for (int i = multiply.bitLength() - 2; i > 0; --i) {
            ecPoint2 = ecPoint2.twice();
            final boolean testBit = multiply.testBit(i);
            if (testBit != bigInteger.testBit(i)) {
                ecPoint2 = ecPoint2.add(testBit ? ecPoint : negate);
            }
        }
        return ecPoint2;
    }
}
