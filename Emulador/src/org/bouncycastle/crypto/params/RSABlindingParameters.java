// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;

public class RSABlindingParameters implements CipherParameters
{
    private RSAKeyParameters publicKey;
    private BigInteger blindingFactor;
    
    public RSABlindingParameters(final RSAKeyParameters publicKey, final BigInteger blindingFactor) {
        if (publicKey instanceof RSAPrivateCrtKeyParameters) {
            throw new IllegalArgumentException("RSA parameters should be for a public key");
        }
        this.publicKey = publicKey;
        this.blindingFactor = blindingFactor;
    }
    
    public RSAKeyParameters getPublicKey() {
        return this.publicKey;
    }
    
    public BigInteger getBlindingFactor() {
        return this.blindingFactor;
    }
}
