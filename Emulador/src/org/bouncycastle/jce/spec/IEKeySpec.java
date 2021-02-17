// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.jce.interfaces.IESKey;
import java.security.spec.KeySpec;

public class IEKeySpec implements KeySpec, IESKey
{
    private PublicKey pubKey;
    private PrivateKey privKey;
    
    public IEKeySpec(final PrivateKey privKey, final PublicKey pubKey) {
        this.privKey = privKey;
        this.pubKey = pubKey;
    }
    
    @Override
    public PublicKey getPublic() {
        return this.pubKey;
    }
    
    @Override
    public PrivateKey getPrivate() {
        return this.privKey;
    }
    
    @Override
    public String getAlgorithm() {
        return "IES";
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
