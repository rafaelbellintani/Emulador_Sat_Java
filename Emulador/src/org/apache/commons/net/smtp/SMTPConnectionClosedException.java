// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.smtp;

import java.io.IOException;

public final class SMTPConnectionClosedException extends IOException
{
    public SMTPConnectionClosedException() {
    }
    
    public SMTPConnectionClosedException(final String message) {
        super(message);
    }
}
