// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.security.spec.KeySpec;

public class ECKeySpec implements KeySpec
{
    private ECParameterSpec spec;
    
    protected ECKeySpec(final ECParameterSpec spec) {
        this.spec = spec;
    }
    
    public ECParameterSpec getParams() {
        return this.spec;
    }
}
