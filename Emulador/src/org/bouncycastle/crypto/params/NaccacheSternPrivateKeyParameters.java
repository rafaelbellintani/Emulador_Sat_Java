// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import java.util.Vector;
import java.math.BigInteger;

public class NaccacheSternPrivateKeyParameters extends NaccacheSternKeyParameters
{
    private BigInteger phi_n;
    private Vector smallPrimes;
    
    public NaccacheSternPrivateKeyParameters(final BigInteger bigInteger, final BigInteger bigInteger2, final int n, final Vector smallPrimes, final BigInteger phi_n) {
        super(true, bigInteger, bigInteger2, n);
        this.smallPrimes = smallPrimes;
        this.phi_n = phi_n;
    }
    
    public BigInteger getPhi_n() {
        return this.phi_n;
    }
    
    public Vector getSmallPrimes() {
        return this.smallPrimes;
    }
}
