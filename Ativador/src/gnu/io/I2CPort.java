// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.TooManyListenersException;

abstract class I2CPort extends CommPort
{
    public static final int DATABITS_5 = 5;
    public static final int DATABITS_6 = 6;
    public static final int DATABITS_7 = 7;
    public static final int DATABITS_8 = 8;
    public static final int PARITY_NONE = 0;
    public static final int PARITY_ODD = 1;
    public static final int PARITY_EVEN = 2;
    public static final int PARITY_MARK = 3;
    public static final int PARITY_SPACE = 4;
    public static final int STOPBITS_1 = 1;
    public static final int STOPBITS_1_5 = 0;
    public static final int STOPBITS_2 = 2;
    public static final int FLOWCONTROL_NONE = 0;
    public static final int FLOWCONTROL_RTSCTS_IN = 1;
    public static final int FLOWCONTROL_RTSCTS_OUT = 2;
    public static final int FLOWCONTROL_XONXOFF_IN = 4;
    public static final int FLOWCONTROL_XONXOFF_OUT = 8;
    
    public abstract void setI2CPortParams(final int p0, final int p1, final int p2, final int p3) throws UnsupportedCommOperationException;
    
    public abstract int getBaudRate();
    
    public abstract int getDataBits();
    
    public abstract int getStopBits();
    
    public abstract int getParity();
    
    public abstract void setFlowControlMode(final int p0) throws UnsupportedCommOperationException;
    
    public abstract int getFlowControlMode();
    
    public abstract boolean isDTR();
    
    public abstract void setDTR(final boolean p0);
    
    public abstract void setRTS(final boolean p0);
    
    public abstract boolean isCTS();
    
    public abstract boolean isDSR();
    
    public abstract boolean isCD();
    
    public abstract boolean isRI();
    
    public abstract boolean isRTS();
    
    public abstract void sendBreak(final int p0);
    
    public abstract void addEventListener(final I2CPortEventListener p0) throws TooManyListenersException;
    
    public abstract void removeEventListener();
    
    public abstract void notifyOnDataAvailable(final boolean p0);
    
    public abstract void notifyOnOutputEmpty(final boolean p0);
    
    public abstract void notifyOnCTS(final boolean p0);
    
    public abstract void notifyOnDSR(final boolean p0);
    
    public abstract void notifyOnRingIndicator(final boolean p0);
    
    public abstract void notifyOnCarrierDetect(final boolean p0);
    
    public abstract void notifyOnOverrunError(final boolean p0);
    
    public abstract void notifyOnParityError(final boolean p0);
    
    public abstract void notifyOnFramingError(final boolean p0);
    
    public abstract void notifyOnBreakInterrupt(final boolean p0);
}
