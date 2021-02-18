// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.math.BigInteger;

public class ElGamalPrivateKeySpec extends ElGamalKeySpec
{
    private BigInteger x;
    
    public ElGamalPrivateKeySpec(final BigInteger x, final ElGamalParameterSpec elGamalParameterSpec) {
        super(elGamalParameterSpec);
        this.x = x;
    }
    
    public BigInteger getX() {
        return this.x;
    }
}
