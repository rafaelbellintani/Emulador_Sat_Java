// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

public interface CommDriver
{
    CommPort getCommPort(final String p0, final int p1);
    
    void initialize();
}
