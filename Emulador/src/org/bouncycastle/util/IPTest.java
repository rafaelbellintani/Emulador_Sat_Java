// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

import junit.framework.TestCase;

public class IPTest extends TestCase
{
    private static final String[] validIP4v;
    private static final String[] invalidIP4v;
    private static final String[] validIP6v;
    private static final String[] invalidIP6v;
    
    private void testIP(final String[] array, final String[] array2) {
        for (int i = 0; i < array.length; ++i) {
            if (!IPAddress.isValid(array[i])) {
                fail("Valid input string not accepted: " + array[i] + ".");
            }
        }
        for (int j = 0; j < array2.length; ++j) {
            if (IPAddress.isValid(array2[j])) {
                fail("Invalid input string accepted: " + array2[j] + ".");
            }
        }
    }
    
    public String getName() {
        return "IPTest";
    }
    
    public void testIPv4() {
        this.testIP(IPTest.validIP4v, IPTest.invalidIP4v);
    }
    
    public void testIPv6() {
        this.testIP(IPTest.validIP6v, IPTest.invalidIP6v);
    }
    
    static {
        validIP4v = new String[] { "0.0.0.0", "255.255.255.255", "192.168.0.0" };
        invalidIP4v = new String[] { "0.0.0.0.1", "256.255.255.255", "1", "A.B.C", "1:.4.6.5" };
        validIP6v = new String[] { "0:0:0:0:0:0:0:0", "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF", "0:1:2:3:FFFF:5:FFFF:1" };
        invalidIP6v = new String[] { "0.0.0.0:1", "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFFF" };
    }
}
