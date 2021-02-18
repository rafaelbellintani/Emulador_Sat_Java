// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.test;

public class TestFailedException extends RuntimeException
{
    private TestResult _result;
    
    public TestFailedException(final TestResult result) {
        this._result = result;
    }
    
    public TestResult getResult() {
        return this._result;
    }
}
