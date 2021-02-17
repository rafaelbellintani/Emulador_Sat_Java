// 
// Decompiled by Procyon v0.5.36
// 

package javax.comm;

import java.util.TooManyListenersException;

public abstract class ParallelPort extends CommPort
{
    public static final int LPT_MODE_ANY = 0;
    public static final int LPT_MODE_SPP = 1;
    public static final int LPT_MODE_PS2 = 2;
    public static final int LPT_MODE_EPP = 3;
    public static final int LPT_MODE_ECP = 4;
    public static final int LPT_MODE_NIBBLE = 5;
    
    public abstract void addEventListener(final ParallelPortEventListener p0) throws TooManyListenersException;
    
    public abstract void removeEventListener();
    
    public abstract void notifyOnError(final boolean p0);
    
    public abstract void notifyOnBuffer(final boolean p0);
    
    public abstract int getOutputBufferFree();
    
    public abstract boolean isPaperOut();
    
    public abstract boolean isPrinterBusy();
    
    public abstract boolean isPrinterSelected();
    
    public abstract boolean isPrinterTimedOut();
    
    public abstract boolean isPrinterError();
    
    public abstract void restart();
    
    public abstract void suspend();
    
    public abstract int getMode();
    
    public abstract int setMode(final int p0) throws UnsupportedCommOperationException;
}
