// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class DSAPublicKeyParameters extends DSAKeyParameters
{
    private BigInteger y;
    
    public DSAPublicKeyParameters(final BigInteger y, final DSAParameters dsaParameters) {
        super(false, dsaParameters);
        this.y = y;
    }
    
    public BigInteger getY() {
        return this.y;
    }
}
