// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.test;

import java.io.PrintStream;
import org.bouncycastle.util.Arrays;

public abstract class SimpleTest implements Test
{
    @Override
    public abstract String getName();
    
    private TestResult success() {
        return SimpleTestResult.successful(this, "Okay");
    }
    
    protected void fail(final String s) {
        throw new TestFailedException(SimpleTestResult.failed(this, s));
    }
    
    protected void fail(final String s, final Throwable t) {
        throw new TestFailedException(SimpleTestResult.failed(this, s, t));
    }
    
    protected void fail(final String s, final Object o, final Object o2) {
        throw new TestFailedException(SimpleTestResult.failed(this, s, o, o2));
    }
    
    protected boolean areEqual(final byte[] array, final byte[] array2) {
        return Arrays.areEqual(array, array2);
    }
    
    @Override
    public TestResult perform() {
        try {
            this.performTest();
            return this.success();
        }
        catch (TestFailedException ex) {
            return ex.getResult();
        }
        catch (Exception obj) {
            return SimpleTestResult.failed(this, "Exception: " + obj, obj);
        }
    }
    
    protected static void runTest(final Test test) {
        runTest(test, System.out);
    }
    
    protected static void runTest(final Test test, final PrintStream s) {
        final TestResult perform = test.perform();
        s.println(perform.toString());
        if (perform.getException() != null) {
            perform.getException().printStackTrace(s);
        }
    }
    
    public abstract void performTest() throws Exception;
}
