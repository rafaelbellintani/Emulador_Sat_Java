// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithSBox implements CipherParameters
{
    private CipherParameters parameters;
    private byte[] sBox;
    
    public ParametersWithSBox(final CipherParameters parameters, final byte[] sBox) {
        this.parameters = parameters;
        this.sBox = sBox;
    }
    
    public byte[] getSBox() {
        return this.sBox;
    }
    
    public CipherParameters getParameters() {
        return this.parameters;
    }
}
