// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.security.spec.KeySpec;

public class ElGamalKeySpec implements KeySpec
{
    private ElGamalParameterSpec spec;
    
    public ElGamalKeySpec(final ElGamalParameterSpec spec) {
        this.spec = spec;
    }
    
    public ElGamalParameterSpec getParams() {
        return this.spec;
    }
}
