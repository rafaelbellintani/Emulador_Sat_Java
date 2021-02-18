// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement.srp;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import java.security.SecureRandom;
import java.math.BigInteger;

public class SRP6Server
{
    protected BigInteger N;
    protected BigInteger g;
    protected BigInteger v;
    protected SecureRandom random;
    protected Digest digest;
    protected BigInteger A;
    protected BigInteger b;
    protected BigInteger B;
    protected BigInteger u;
    protected BigInteger S;
    
    public void init(final BigInteger n, final BigInteger g, final BigInteger v, final Digest digest, final SecureRandom random) {
        this.N = n;
        this.g = g;
        this.v = v;
        this.random = random;
        this.digest = digest;
    }
    
    public BigInteger generateServerCredentials() {
        final BigInteger calculateK = SRP6Util.calculateK(this.digest, this.N, this.g);
        this.b = this.selectPrivateValue();
        return this.B = calculateK.multiply(this.v).mod(this.N).add(this.g.modPow(this.b, this.N)).mod(this.N);
    }
    
    public BigInteger calculateSecret(final BigInteger bigInteger) throws CryptoException {
        this.A = SRP6Util.validatePublicValue(this.N, bigInteger);
        this.u = SRP6Util.calculateU(this.digest, this.N, this.A, this.B);
        return this.S = this.calculateS();
    }
    
    protected BigInteger selectPrivateValue() {
        return SRP6Util.generatePrivateValue(this.digest, this.N, this.g, this.random);
    }
    
    private BigInteger calculateS() {
        return this.v.modPow(this.u, this.N).multiply(this.A).mod(this.N).modPow(this.b, this.N);
    }
}
