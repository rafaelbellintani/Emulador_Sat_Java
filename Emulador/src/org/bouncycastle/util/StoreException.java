// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

public class StoreException extends RuntimeException
{
    private Throwable _e;
    
    public StoreException(final String message, final Throwable e) {
        super(message);
        this._e = e;
    }
    
    @Override
    public Throwable getCause() {
        return this._e;
    }
}
