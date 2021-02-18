// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.ocsp;

public class OCSPException extends Exception
{
    Exception e;
    
    public OCSPException(final String message) {
        super(message);
    }
    
    public OCSPException(final String message, final Exception e) {
        super(message);
        this.e = e;
    }
    
    public Exception getUnderlyingException() {
        return this.e;
    }
    
    @Override
    public Throwable getCause() {
        return this.e;
    }
}
