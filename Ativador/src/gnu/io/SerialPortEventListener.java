// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.EventListener;

public interface SerialPortEventListener extends EventListener
{
    void serialEvent(final SerialPortEvent p0);
}
