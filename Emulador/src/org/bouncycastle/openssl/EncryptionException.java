// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.openssl;

import java.io.IOException;

public class EncryptionException extends IOException
{
    private Throwable cause;
    
    public EncryptionException(final String message) {
        super(message);
    }
    
    public EncryptionException(final String message, final Throwable cause) {
        super(message);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
