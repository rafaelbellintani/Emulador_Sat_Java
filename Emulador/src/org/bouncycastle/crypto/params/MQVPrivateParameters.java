// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class MQVPrivateParameters implements CipherParameters
{
    private ECPrivateKeyParameters staticPrivateKey;
    private ECPrivateKeyParameters ephemeralPrivateKey;
    private ECPublicKeyParameters ephemeralPublicKey;
    
    public MQVPrivateParameters(final ECPrivateKeyParameters ecPrivateKeyParameters, final ECPrivateKeyParameters ecPrivateKeyParameters2) {
        this(ecPrivateKeyParameters, ecPrivateKeyParameters2, null);
    }
    
    public MQVPrivateParameters(final ECPrivateKeyParameters staticPrivateKey, final ECPrivateKeyParameters ephemeralPrivateKey, final ECPublicKeyParameters ephemeralPublicKey) {
        this.staticPrivateKey = staticPrivateKey;
        this.ephemeralPrivateKey = ephemeralPrivateKey;
        this.ephemeralPublicKey = ephemeralPublicKey;
    }
    
    public ECPrivateKeyParameters getStaticPrivateKey() {
        return this.staticPrivateKey;
    }
    
    public ECPrivateKeyParameters getEphemeralPrivateKey() {
        return this.ephemeralPrivateKey;
    }
    
    public ECPublicKeyParameters getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }
}
