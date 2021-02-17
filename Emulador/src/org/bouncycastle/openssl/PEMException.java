// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.openssl;

import java.io.IOException;

public class PEMException extends IOException
{
    Exception underlying;
    
    public PEMException(final String message) {
        super(message);
    }
    
    public PEMException(final String message, final Exception underlying) {
        super(message);
        this.underlying = underlying;
    }
    
    public Exception getUnderlyingException() {
        return this.underlying;
    }
    
    @Override
    public Throwable getCause() {
        return this.underlying;
    }
}
