// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.modes.gcm;

public interface GCMMultiplier
{
    void init(final byte[] p0);
    
    void multiplyH(final byte[] p0);
}
