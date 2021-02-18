// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.cert.CRLException;

class ExtCRLException extends CRLException
{
    Throwable cause;
    
    ExtCRLException(final String message, final Throwable cause) {
        super(message);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
