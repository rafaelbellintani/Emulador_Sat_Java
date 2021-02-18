// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.tls;

public class TlsRuntimeException extends RuntimeException
{
    Throwable e;
    
    public TlsRuntimeException(final String message, final Throwable e) {
        super(message);
        this.e = e;
    }
    
    public TlsRuntimeException(final String message) {
        super(message);
    }
    
    @Override
    public Throwable getCause() {
        return this.e;
    }
}
