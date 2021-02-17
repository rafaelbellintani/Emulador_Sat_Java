// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.EventObject;

public class ParallelPortEvent extends EventObject
{
    public static final int PAR_EV_ERROR = 1;
    public static final int PAR_EV_BUFFER = 2;
    private boolean OldValue;
    private boolean NewValue;
    private int eventType;
    
    public ParallelPortEvent(final ParallelPort source, final int eventType, final boolean oldValue, final boolean newValue) {
        super(source);
        this.OldValue = oldValue;
        this.NewValue = newValue;
        this.eventType = eventType;
    }
    
    public int getEventType() {
        return this.eventType;
    }
    
    public boolean getNewValue() {
        return this.NewValue;
    }
    
    public boolean getOldValue() {
        return this.OldValue;
    }
}
