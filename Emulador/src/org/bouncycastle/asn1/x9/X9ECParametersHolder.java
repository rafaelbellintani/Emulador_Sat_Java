// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x9;

public abstract class X9ECParametersHolder
{
    private X9ECParameters params;
    
    public X9ECParameters getParameters() {
        if (this.params == null) {
            this.params = this.createParameters();
        }
        return this.params;
    }
    
    protected abstract X9ECParameters createParameters();
}
