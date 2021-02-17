// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.util;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.EventListener;
import java.io.Serializable;

public class ListenerList implements Serializable, Iterable<EventListener>
{
    private final CopyOnWriteArrayList<EventListener> __listeners;
    
    public ListenerList() {
        this.__listeners = new CopyOnWriteArrayList<EventListener>();
    }
    
    public void addListener(final EventListener listener) {
        this.__listeners.add(listener);
    }
    
    public void removeListener(final EventListener listener) {
        this.__listeners.remove(listener);
    }
    
    public int getListenerCount() {
        return this.__listeners.size();
    }
    
    public Iterator<EventListener> iterator() {
        return this.__listeners.iterator();
    }
}
