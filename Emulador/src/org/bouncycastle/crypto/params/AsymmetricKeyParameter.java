// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class AsymmetricKeyParameter implements CipherParameters
{
    boolean privateKey;
    
    public AsymmetricKeyParameter(final boolean privateKey) {
        this.privateKey = privateKey;
    }
    
    public boolean isPrivate() {
        return this.privateKey;
    }
}
