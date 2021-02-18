// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

public class StreamParsingException extends Exception
{
    Throwable _e;
    
    public StreamParsingException(final String message, final Throwable e) {
        super(message);
        this._e = e;
    }
    
    @Override
    public Throwable getCause() {
        return this._e;
    }
}
