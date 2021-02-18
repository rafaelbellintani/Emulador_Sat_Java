// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.test;

public interface TestResult
{
    boolean isSuccessful();
    
    Throwable getException();
    
    String toString();
}
