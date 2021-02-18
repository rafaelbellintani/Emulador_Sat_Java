// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.nntp;

import java.io.IOException;

public final class NNTPConnectionClosedException extends IOException
{
    public NNTPConnectionClosedException() {
    }
    
    public NNTPConnectionClosedException(final String message) {
        super(message);
    }
}
