// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.security.PublicKey;
import java.security.PrivateKey;
import org.bouncycastle.jce.interfaces.MQVPrivateKey;
import java.security.spec.KeySpec;

public class MQVPrivateKeySpec implements KeySpec, MQVPrivateKey
{
    private PrivateKey staticPrivateKey;
    private PrivateKey ephemeralPrivateKey;
    private PublicKey ephemeralPublicKey;
    
    public MQVPrivateKeySpec(final PrivateKey privateKey, final PrivateKey privateKey2) {
        this(privateKey, privateKey2, null);
    }
    
    public MQVPrivateKeySpec(final PrivateKey staticPrivateKey, final PrivateKey ephemeralPrivateKey, final PublicKey ephemeralPublicKey) {
        this.staticPrivateKey = staticPrivateKey;
        this.ephemeralPrivateKey = ephemeralPrivateKey;
        this.ephemeralPublicKey = ephemeralPublicKey;
    }
    
    @Override
    public PrivateKey getStaticPrivateKey() {
        return this.staticPrivateKey;
    }
    
    @Override
    public PrivateKey getEphemeralPrivateKey() {
        return this.ephemeralPrivateKey;
    }
    
    @Override
    public PublicKey getEphemeralPublicKey() {
        return this.ephemeralPublicKey;
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
