// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.OutputStream;

public abstract class ASN1Generator
{
    protected OutputStream _out;
    
    public ASN1Generator(final OutputStream out) {
        this._out = out;
    }
    
    public abstract OutputStream getRawOutputStream();
}
