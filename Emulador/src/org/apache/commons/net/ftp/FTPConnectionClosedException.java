// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp;

import java.io.IOException;

public class FTPConnectionClosedException extends IOException
{
    public FTPConnectionClosedException() {
    }
    
    public FTPConnectionClosedException(final String message) {
        super(message);
    }
}
