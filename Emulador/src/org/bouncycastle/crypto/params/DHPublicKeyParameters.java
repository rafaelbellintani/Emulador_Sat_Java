// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class DHPublicKeyParameters extends DHKeyParameters
{
    private BigInteger y;
    
    public DHPublicKeyParameters(final BigInteger y, final DHParameters dhParameters) {
        super(false, dhParameters);
        this.y = y;
    }
    
    public BigInteger getY() {
        return this.y;
    }
    
    @Override
    public int hashCode() {
        return this.y.hashCode() ^ super.hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof DHPublicKeyParameters && ((DHPublicKeyParameters)o).getY().equals(this.y) && super.equals(o);
    }
}
