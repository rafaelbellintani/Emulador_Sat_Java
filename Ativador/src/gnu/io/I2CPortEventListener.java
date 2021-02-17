// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.EventListener;

public interface I2CPortEventListener extends EventListener
{
    void I2CEvent(final I2CPortEvent p0);
}
