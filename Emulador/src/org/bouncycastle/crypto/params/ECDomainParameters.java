// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECConstants;

public class ECDomainParameters implements ECConstants
{
    ECCurve curve;
    byte[] seed;
    ECPoint G;
    BigInteger n;
    BigInteger h;
    
    public ECDomainParameters(final ECCurve curve, final ECPoint g, final BigInteger n) {
        this.curve = curve;
        this.G = g;
        this.n = n;
        this.h = ECDomainParameters.ONE;
        this.seed = null;
    }
    
    public ECDomainParameters(final ECCurve curve, final ECPoint g, final BigInteger n, final BigInteger h) {
        this.curve = curve;
        this.G = g;
        this.n = n;
        this.h = h;
        this.seed = null;
    }
    
    public ECDomainParameters(final ECCurve curve, final ECPoint g, final BigInteger n, final BigInteger h, final byte[] seed) {
        this.curve = curve;
        this.G = g;
        this.n = n;
        this.h = h;
        this.seed = seed;
    }
    
    public ECCurve getCurve() {
        return this.curve;
    }
    
    public ECPoint getG() {
        return this.G;
    }
    
    public BigInteger getN() {
        return this.n;
    }
    
    public BigInteger getH() {
        return this.h;
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
}
