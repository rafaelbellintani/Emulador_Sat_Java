// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement.srp;

import org.bouncycastle.crypto.Digest;
import java.math.BigInteger;

public class SRP6VerifierGenerator
{
    protected BigInteger N;
    protected BigInteger g;
    protected Digest digest;
    
    public void init(final BigInteger n, final BigInteger g, final Digest digest) {
        this.N = n;
        this.g = g;
        this.digest = digest;
    }
    
    public BigInteger generateVerifier(final byte[] array, final byte[] array2, final byte[] array3) {
        return this.g.modPow(SRP6Util.calculateX(this.digest, this.N, array, array2, array3), this.N);
    }
}
