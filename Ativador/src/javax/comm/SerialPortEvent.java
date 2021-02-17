// 
// Decompiled by Procyon v0.5.36
// 

package javax.comm;

import java.util.EventObject;

public class SerialPortEvent extends EventObject
{
    public int eventType;
    private boolean oldValue;
    private boolean newValue;
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
    
    public SerialPortEvent(final SerialPort source, final int eventType, final boolean b, final boolean newValue) {
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
