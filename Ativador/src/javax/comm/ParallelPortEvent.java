// 
// Decompiled by Procyon v0.5.36
// 

package javax.comm;

import java.util.EventObject;

public class ParallelPortEvent extends EventObject
{
    public int eventType;
    private boolean oldValue;
    private boolean newValue;
    public static final int PAR_EV_ERROR = 1;
    public static final int PAR_EV_BUFFER = 2;
    
    public ParallelPortEvent(final ParallelPort source, final int eventType, final boolean b, final boolean newValue) {
        super(source);
        this.eventType = eventType;
        this.oldValue = this.oldValue;
        this.newValue = newValue;
    }
    
    public int getEventType() {
        return this.eventType;
    }
    
    public boolean getNewValue() {
        return this.newValue;
    }
    
    public boolean getOldValue() {
        return this.oldValue;
    }
}
