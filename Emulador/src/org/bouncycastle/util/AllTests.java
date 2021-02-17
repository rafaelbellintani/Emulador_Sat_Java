// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.textui.TestRunner;

public class AllTests
{
    public static void main(final String[] array) {
        TestRunner.run(suite());
    }
    
    public static Test suite() {
        final TestSuite testSuite = new TestSuite("util tests");
        testSuite.addTestSuite((Class)IPTest.class);
        return (Test)testSuite;
    }
}
