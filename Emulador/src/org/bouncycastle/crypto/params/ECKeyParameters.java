// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.params;

public class ECKeyParameters extends AsymmetricKeyParameter
{
    ECDomainParameters params;
    
    protected ECKeyParameters(final boolean b, final ECDomainParameters params) {
        super(b);
        this.params = params;
    }
    
    public ECDomainParameters getParameters() {
        return this.params;
    }
}
