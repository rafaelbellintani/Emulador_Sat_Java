// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.interfaces;

public interface ConfigurableProvider
{
    public static final String THREAD_LOCAL_EC_IMPLICITLY_CA = "threadLocalEcImplicitlyCa";
    public static final String EC_IMPLICITLY_CA = "ecImplicitlyCa";
    
    void setParameter(final String p0, final Object p1);
}
