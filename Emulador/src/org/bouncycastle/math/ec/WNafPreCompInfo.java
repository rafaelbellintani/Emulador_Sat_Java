// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

class WNafPreCompInfo implements PreCompInfo
{
    private ECPoint[] preComp;
    private ECPoint twiceP;
    
    WNafPreCompInfo() {
        this.preComp = null;
        this.twiceP = null;
    }
    
    protected ECPoint[] getPreComp() {
        return this.preComp;
    }
    
    protected void setPreComp(final ECPoint[] preComp) {
        this.preComp = preComp;
    }
    
    protected ECPoint getTwiceP() {
        return this.twiceP;
    }
    
    protected void setTwiceP(final ECPoint twiceP) {
        this.twiceP = twiceP;
    }
}
