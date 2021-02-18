// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class DSAPrivateKeyParameters extends DSAKeyParameters
{
    private BigInteger x;
    
    public DSAPrivateKeyParameters(final BigInteger x, final DSAParameters dsaParameters) {
        super(true, dsaParameters);
        this.x = x;
    }
    
    public BigInteger getX() {
        return this.x;
    }
}
