// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

public class GOST3410KeyParameters extends AsymmetricKeyParameter
{
    private GOST3410Parameters params;
    
    public GOST3410KeyParameters(final boolean b, final GOST3410Parameters params) {
        super(b);
        this.params = params;
    }
    
    public GOST3410Parameters getParameters() {
        return this.params;
    }
}
