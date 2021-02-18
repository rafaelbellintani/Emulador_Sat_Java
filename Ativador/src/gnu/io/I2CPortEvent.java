// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.EventObject;

public class I2CPortEvent extends EventObject
{
    public static final int DATA_AVAILABLE = 1;
    public static final int OUTPUT_BUFFER_EMPTY = 2;
    public static final int CTS = 3;
    public static final int DSR = 4;
    public static final int RI = 5;
    public static final int CD = 6;
    public static final int OE = 7;
    public static final int PE = 8;
    public static final int FE = 9;
    public static final int BI = 10;
    private boolean OldValue;
    private boolean NewValue;
    private int eventType;
    
    public I2CPortEvent(final I2CPort source, final int eventType, final boolean oldValue, final boolean newValue) {
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
