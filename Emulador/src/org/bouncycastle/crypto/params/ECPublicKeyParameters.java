// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import org.bouncycastle.math.ec.ECPoint;

public class ECPublicKeyParameters extends ECKeyParameters
{
    ECPoint Q;
    
    public ECPublicKeyParameters(final ECPoint q, final ECDomainParameters ecDomainParameters) {
        super(false, ecDomainParameters);
        this.Q = q;
    }
    
    public ECPoint getQ() {
        return this.Q;
    }
}
