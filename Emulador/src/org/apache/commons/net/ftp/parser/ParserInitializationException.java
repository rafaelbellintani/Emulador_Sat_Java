// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp.parser;

public class ParserInitializationException extends RuntimeException
{
    private final Throwable rootCause;
    
    public ParserInitializationException(final String message) {
        super(message);
        this.rootCause = null;
    }
    
    public ParserInitializationException(final String message, final Throwable rootCause) {
        super(message);
        this.rootCause = rootCause;
    }
    
    public Throwable getRootCause() {
        return this.rootCause;
    }
}
