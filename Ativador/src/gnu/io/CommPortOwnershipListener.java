// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.EventListener;

public interface CommPortOwnershipListener extends EventListener
{
    public static final int PORT_OWNED = 1;
    public static final int PORT_UNOWNED = 2;
    public static final int PORT_OWNERSHIP_REQUESTED = 3;
    
    void ownershipChange(final int p0);
}
