// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class RSAKeyParameters extends AsymmetricKeyParameter
{
    private BigInteger modulus;
    private BigInteger exponent;
    
    public RSAKeyParameters(final boolean b, final BigInteger modulus, final BigInteger exponent) {
        super(b);
        this.modulus = modulus;
        this.exponent = exponent;
    }
    
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    public BigInteger getExponent() {
        return this.exponent;
    }
}
