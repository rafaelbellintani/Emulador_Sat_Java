// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

public class GOST3410PrivateKeySpec implements KeySpec
{
    private BigInteger x;
    private BigInteger p;
    private BigInteger q;
    private BigInteger a;
    
    public GOST3410PrivateKeySpec(final BigInteger x, final BigInteger p4, final BigInteger q, final BigInteger a) {
        this.x = x;
        this.p = p4;
        this.q = q;
        this.a = a;
    }
    
    public BigInteger getX() {
        return this.x;
    }
    
    public BigInteger getP() {
        return this.p;
    }
    
    public BigInteger getQ() {
        return this.q;
    }
    
    public BigInteger getA() {
        return this.a;
    }
}
