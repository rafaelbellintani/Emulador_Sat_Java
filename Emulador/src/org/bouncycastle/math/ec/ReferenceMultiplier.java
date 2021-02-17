// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.math.BigInteger;

class ReferenceMultiplier implements ECMultiplier
{
    @Override
    public ECPoint multiply(ECPoint twice, final BigInteger bigInteger, final PreCompInfo preCompInfo) {
        ECPoint ecPoint = twice.getCurve().getInfinity();
        for (int bitLength = bigInteger.bitLength(), i = 0; i < bitLength; ++i) {
            if (bigInteger.testBit(i)) {
                ecPoint = ecPoint.add(twice);
            }
            twice = twice.twice();
        }
        return ecPoint;
    }
}
