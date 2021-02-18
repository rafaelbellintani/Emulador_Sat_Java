// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.exception;

import java.io.IOException;

public class ExtIOException extends IOException implements ExtException
{
    private Throwable cause;
    
    public ExtIOException(final String message, final Throwable cause) {
        super(message);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
