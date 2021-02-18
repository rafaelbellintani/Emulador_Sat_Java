// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto;

public class AsymmetricCipherKeyPair
{
    private CipherParameters publicParam;
    private CipherParameters privateParam;
    
    public AsymmetricCipherKeyPair(final CipherParameters publicParam, final CipherParameters privateParam) {
        this.publicParam = publicParam;
        this.privateParam = privateParam;
    }
    
    public CipherParameters getPublic() {
        return this.publicParam;
    }
    
    public CipherParameters getPrivate() {
        return this.privateParam;
    }
}
