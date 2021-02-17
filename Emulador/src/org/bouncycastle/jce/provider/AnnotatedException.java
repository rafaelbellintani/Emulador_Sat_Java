// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.jce.exception.ExtException;

public class AnnotatedException extends Exception implements ExtException
{
    private Throwable _underlyingException;
    
    AnnotatedException(final String message, final Throwable underlyingException) {
        super(message);
        this._underlyingException = underlyingException;
    }
    
    AnnotatedException(final String s) {
        this(s, null);
    }
    
    Throwable getUnderlyingException() {
        return this._underlyingException;
    }
    
    @Override
    public Throwable getCause() {
        return this._underlyingException;
    }
}
