// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class MQVPublicParameters implements CipherParameters
{
    private ECPublicKeyParameters staticPublicKey;
    private ECPublicKeyParameters ephemeralPublicKey;
    
    public MQVPublicParameters(final ECPublicKeyParameters staticPublicKey, final ECPublicKeyParameters ephemeralPublicKey) {
        this.staticPublicKey = staticPublicKey;
        this.ephemeralPublicKey = ephemeralPublicKey;
    }
    
    public ECPublicKeyParameters getStaticPublicKey() {
        return this.staticPublicKey;
    }
    
    public ECPublicKeyParameters getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
    }
}
