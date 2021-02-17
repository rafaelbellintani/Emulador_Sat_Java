// 
// Decompiled by Procyon v0.5.36
// 

package javax.comm;

public interface CommDriver
{
    void initialize();
    
    CommPort getCommPort(final String p0, final int p1);
}
