// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.i18n.filter;

public class TrustedInput
{
    protected Object input;
    
    public TrustedInput(final Object input) {
        this.input = input;
    }
    
    public Object getInput() {
        return this.input;
    }
    
    @Override
    public String toString() {
        return this.input.toString();
    }
}
