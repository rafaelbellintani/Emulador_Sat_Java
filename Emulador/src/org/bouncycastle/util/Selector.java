// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

public interface Selector extends Cloneable
{
    boolean match(final Object p0);
    
    Object clone();
}
