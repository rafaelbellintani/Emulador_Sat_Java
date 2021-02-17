// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement.srp;

import org.bouncycastle.crypto.CryptoException;
import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import java.math.BigInteger;

public class SRP6Client
{
    protected BigInteger N;
    protected BigInteger g;
    protected BigInteger a;
    protected BigInteger A;
    protected BigInteger B;
    protected BigInteger x;
    protected BigInteger u;
    protected BigInteger S;
    protected Digest digest;
    protected SecureRandom random;
    
    public void init(final BigInteger n, final BigInteger g, final Digest digest, final SecureRandom random) {
        this.N = n;
        this.g = g;
        this.digest = digest;
        this.random = random;
    }
    
    public BigInteger generateClientCredentials(final byte[] array, final byte[] array2, final byte[] array3) {
        this.x = SRP6Util.calculateX(this.digest, this.N, array, array2, array3);
        this.a = this.selectPrivateValue();
        return this.A = this.g.modPow(this.a, this.N);
    }
    
    public BigInteger calculateSecret(final BigInteger bigInteger) throws CryptoException {
        this.B = SRP6Util.validatePublicValue(this.N, bigInteger);
        this.u = SRP6Util.calculateU(this.digest, this.N, this.A, this.B);
        return this.S = this.calculateS();
    }
    
    protected BigInteger selectPrivateValue() {
        return SRP6Util.generatePrivateValue(this.digest, this.N, this.g, this.random);
    }
    
    private BigInteger calculateS() {
        return this.B.subtract(this.g.modPow(this.x, this.N).multiply(SRP6Util.calculateK(this.digest, this.N, this.g)).mod(this.N)).mod(this.N).modPow(this.u.multiply(this.x).add(this.a), this.N);
    }
}
