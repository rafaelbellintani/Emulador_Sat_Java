// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.AsymmetricBlockCipher;

public class RSAEngine implements AsymmetricBlockCipher
{
    private RSACoreEngine core;
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (this.core == null) {
            this.core = new RSACoreEngine();
        }
        this.core.init(b, cipherParameters);
    }
    
    @Override
    public int getInputBlockSize() {
        return this.core.getInputBlockSize();
    }
    
    @Override
    public int getOutputBlockSize() {
        return this.core.getOutputBlockSize();
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) {
        if (this.core == null) {
            throw new IllegalStateException("RSA engine not initialised");
        }
        return this.core.convertOutput(this.core.processBlock(this.core.convertInput(array, n, n2)));
    }
}
