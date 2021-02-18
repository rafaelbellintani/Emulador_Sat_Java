// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.security.PublicKey;
import org.bouncycastle.jce.interfaces.MQVPublicKey;
import java.security.spec.KeySpec;

public class MQVPublicKeySpec implements KeySpec, MQVPublicKey
{
    private PublicKey staticKey;
    private PublicKey ephemeralKey;
    
    public MQVPublicKeySpec(final PublicKey staticKey, final PublicKey ephemeralKey) {
        this.staticKey = staticKey;
        this.ephemeralKey = ephemeralKey;
    }
    
    @Override
    public PublicKey getStaticKey() {
        return this.staticKey;
    }
    
    @Override
    public PublicKey getEphemeralKey() {
        return this.ephemeralKey;
    }
    
    @Override
    public String getAlgorithm() {
        return "ECMQV";
    }
    
    @Override
    public String getFormat() {
        return null;
    }
    
    @Override
    public byte[] getEncoded() {
        return null;
    }
}
