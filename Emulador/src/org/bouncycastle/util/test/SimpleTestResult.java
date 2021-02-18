// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util.test;

public class SimpleTestResult implements TestResult
{
    private static final String SEPARATOR;
    private boolean success;
    private String message;
    private Throwable exception;
    
    public SimpleTestResult(final boolean success, final String message) {
        this.success = success;
        this.message = message;
    }
    
    public SimpleTestResult(final boolean success, final String message, final Throwable exception) {
        this.success = success;
        this.message = message;
        this.exception = exception;
    }
    
    public static TestResult successful(final Test test, final String str) {
        return new SimpleTestResult(true, test.getName() + ": " + str);
    }
    
    public static TestResult failed(final Test test, final String str) {
        return new SimpleTestResult(false, test.getName() + ": " + str);
    }
    
    public static TestResult failed(final Test test, final String str, final Throwable t) {
        return new SimpleTestResult(false, test.getName() + ": " + str, t);
    }
    
    public static TestResult failed(final Test test, final String str, final Object obj, final Object obj2) {
        return failed(test, str + SimpleTestResult.SEPARATOR + "Expected: " + obj + SimpleTestResult.SEPARATOR + "Found   : " + obj2);
    }
    
    public static String failedMessage(final String str, final String str2, final String str3, final String str4) {
        final StringBuffer sb = new StringBuffer(str);
        sb.append(" failing ").append(str2);
        sb.append(SimpleTestResult.SEPARATOR).append("    expected: ").append(str3);
        sb.append(SimpleTestResult.SEPARATOR).append("    got     : ").append(str4);
        return sb.toString();
    }
    
    @Override
    public boolean isSuccessful() {
        return this.success;
    }
    
    @Override
    public String toString() {
        return this.message;
    }
    
    @Override
    public Throwable getException() {
        return this.exception;
    }
    
    static {
        SEPARATOR = System.getProperty("line.separator");
    }
}
