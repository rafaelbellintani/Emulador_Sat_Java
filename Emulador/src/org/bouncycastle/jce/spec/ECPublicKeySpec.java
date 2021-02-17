// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import org.bouncycastle.math.ec.ECPoint;

public class ECPublicKeySpec extends ECKeySpec
{
    private ECPoint q;
    
    public ECPublicKeySpec(final ECPoint q, final ECParameterSpec ecParameterSpec) {
        super(ecParameterSpec);
        this.q = q;
    }
    
    public ECPoint getQ() {
        return this.q;
    }
}
