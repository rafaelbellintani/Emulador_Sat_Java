// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.math.BigInteger;

class WTauNafMultiplier implements ECMultiplier
{
    @Override
    public ECPoint multiply(final ECPoint ecPoint, final BigInteger bigInteger, final PreCompInfo preCompInfo) {
        if (!(ecPoint instanceof ECPoint.F2m)) {
            throw new IllegalArgumentException("Only ECPoint.F2m can be used in WTauNafMultiplier");
        }
        final ECPoint.F2m f2m = (ECPoint.F2m)ecPoint;
        final ECCurve.F2m f2m2 = (ECCurve.F2m)f2m.getCurve();
        final int m = f2m2.getM();
        final byte byteValue = f2m2.getA().toBigInteger().byteValue();
        final byte mu = f2m2.getMu();
        return this.multiplyWTnaf(f2m, Tnaf.partModReduction(bigInteger, m, byteValue, f2m2.getSi(), mu, (byte)10), preCompInfo, byteValue, mu);
    }
    
    private ECPoint.F2m multiplyWTnaf(final ECPoint.F2m f2m, final ZTauElement zTauElement, final PreCompInfo preCompInfo, final byte b, final byte b2) {
        ZTauElement[] array;
        if (b == 0) {
            array = Tnaf.alpha0;
        }
        else {
            array = Tnaf.alpha1;
        }
        return multiplyFromWTnaf(f2m, Tnaf.tauAdicWNaf(b2, zTauElement, (byte)4, BigInteger.valueOf(16L), Tnaf.getTw(b2, 4), array), preCompInfo);
    }
    
    private static ECPoint.F2m multiplyFromWTnaf(final ECPoint.F2m f2m, final byte[] array, final PreCompInfo preCompInfo) {
        final byte byteValue = ((ECCurve.F2m)f2m.getCurve()).getA().toBigInteger().byteValue();
        ECPoint.F2m[] array2;
        if (preCompInfo == null || !(preCompInfo instanceof WTauNafPreCompInfo)) {
            array2 = Tnaf.getPreComp(f2m, byteValue);
            f2m.setPreCompInfo(new WTauNafPreCompInfo(array2));
        }
        else {
            array2 = ((WTauNafPreCompInfo)preCompInfo).getPreComp();
        }
        ECPoint.F2m f2m2 = (ECPoint.F2m)f2m.getCurve().getInfinity();
        for (int i = array.length - 1; i >= 0; --i) {
            f2m2 = Tnaf.tau(f2m2);
            if (array[i] != 0) {
                if (array[i] > 0) {
                    f2m2 = f2m2.addSimple(array2[array[i]]);
                }
                else {
                    f2m2 = f2m2.subtractSimple(array2[-array[i]]);
                }
            }
        }
        return f2m2;
    }
}
