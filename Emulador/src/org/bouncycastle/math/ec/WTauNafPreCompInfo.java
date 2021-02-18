// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

class WTauNafPreCompInfo implements PreCompInfo
{
    private ECPoint.F2m[] preComp;
    
    WTauNafPreCompInfo(final ECPoint.F2m[] preComp) {
        this.preComp = null;
        this.preComp = preComp;
    }
    
    protected ECPoint.F2m[] getPreComp() {
        return this.preComp;
    }
}
