// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.io;

import java.util.Iterator;
import java.util.EventListener;
import org.apache.commons.net.util.ListenerList;

public class CopyStreamAdapter implements CopyStreamListener
{
    private ListenerList internalListeners;
    
    public CopyStreamAdapter() {
        this.internalListeners = new ListenerList();
    }
    
    public void bytesTransferred(final CopyStreamEvent event) {
        this.bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
    }
    
    public void bytesTransferred(final long totalBytesTransferred, final int bytesTransferred, final long streamSize) {
        final CopyStreamEvent event = new CopyStreamEvent(this, totalBytesTransferred, bytesTransferred, streamSize);
        for (final EventListener listener : this.internalListeners) {
            ((CopyStreamListener)listener).bytesTransferred(event);
        }
    }
    
    public void addCopyStreamListener(final CopyStreamListener listener) {
        this.internalListeners.addListener(listener);
    }
    
    public void removeCopyStreamListener(final CopyStreamListener listener) {
        this.internalListeners.removeListener(listener);
    }
}
