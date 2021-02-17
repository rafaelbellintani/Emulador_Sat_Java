// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.io;

import java.io.IOException;

public class CopyStreamException extends IOException
{
    private long totalBytesTransferred;
    private IOException ioException;
    
    public CopyStreamException(final String message, final long bytesTransferred, final IOException exception) {
        super(message);
        this.totalBytesTransferred = bytesTransferred;
        this.ioException = exception;
    }
    
    public long getTotalBytesTransferred() {
        return this.totalBytesTransferred;
    }
    
    public IOException getIOException() {
        return this.ioException;
    }
}
