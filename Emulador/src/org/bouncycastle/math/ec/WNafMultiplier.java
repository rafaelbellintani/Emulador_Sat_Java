// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.math.BigInteger;

class WNafMultiplier implements ECMultiplier
{
    public byte[] windowNaf(final byte b, BigInteger bigInteger) {
        final byte[] array = new byte[bigInteger.bitLength() + 1];
        final short n = (short)(1 << b);
        final BigInteger value = BigInteger.valueOf(n);
        int n2 = 0;
        int n3 = 0;
        while (bigInteger.signum() > 0) {
            if (bigInteger.testBit(0)) {
                final BigInteger mod = bigInteger.mod(value);
                if (mod.testBit(b - 1)) {
                    array[n2] = (byte)(mod.intValue() - n);
                }
                else {
                    array[n2] = (byte)mod.intValue();
                }
                bigInteger = bigInteger.subtract(BigInteger.valueOf(array[n2]));
                n3 = n2;
            }
            else {
                array[n2] = 0;
            }
            bigInteger = bigInteger.shiftRight(1);
            ++n2;
        }
        final byte[] array2 = new byte[++n3];
        System.arraycopy(array, 0, array2, 0, n3);
        return array2;
    }
    
    @Override
    public ECPoint multiply(final ECPoint ecPoint, final BigInteger bigInteger, final PreCompInfo preCompInfo) {
        WNafPreCompInfo preCompInfo2;
        if (preCompInfo != null && preCompInfo instanceof WNafPreCompInfo) {
            preCompInfo2 = (WNafPreCompInfo)preCompInfo;
        }
        else {
            preCompInfo2 = new WNafPreCompInfo();
        }
        final int bitLength = bigInteger.bitLength();
        byte b;
        int n;
        if (bitLength < 13) {
            b = 2;
            n = 1;
        }
        else if (bitLength < 41) {
            b = 3;
            n = 2;
        }
        else if (bitLength < 121) {
            b = 4;
            n = 4;
        }
        else if (bitLength < 337) {
            b = 5;
            n = 8;
        }
        else if (bitLength < 897) {
            b = 6;
            n = 16;
        }
        else if (bitLength < 2305) {
            b = 7;
            n = 32;
        }
        else {
            b = 8;
            n = 127;
        }
        int length = 1;
        ECPoint[] preComp = preCompInfo2.getPreComp();
        ECPoint twiceP = preCompInfo2.getTwiceP();
        if (preComp == null) {
            preComp = new ECPoint[] { ecPoint };
        }
        else {
            length = preComp.length;
        }
        if (twiceP == null) {
            twiceP = ecPoint.twice();
        }
        if (length < n) {
            final ECPoint[] array = preComp;
            preComp = new ECPoint[n];
            System.arraycopy(array, 0, preComp, 0, length);
            for (int i = length; i < n; ++i) {
                preComp[i] = twiceP.add(preComp[i - 1]);
            }
        }
        final byte[] windowNaf = this.windowNaf(b, bigInteger);
        final int length2 = windowNaf.length;
        ECPoint ecPoint2 = ecPoint.getCurve().getInfinity();
        for (int j = length2 - 1; j >= 0; --j) {
            ecPoint2 = ecPoint2.twice();
            if (windowNaf[j] != 0) {
                if (windowNaf[j] > 0) {
                    ecPoint2 = ecPoint2.add(preComp[(windowNaf[j] - 1) / 2]);
                }
                else {
                    ecPoint2 = ecPoint2.subtract(preComp[(-windowNaf[j] - 1) / 2]);
                }
            }
        }
        preCompInfo2.setPreComp(preComp);
        preCompInfo2.setTwiceP(twiceP);
        ecPoint.setPreCompInfo(preCompInfo2);
        return ecPoint2;
    }
}
