// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.telnet;

public class SimpleOptionHandler extends TelnetOptionHandler
{
    public SimpleOptionHandler(final int optcode, final boolean initlocal, final boolean initremote, final boolean acceptlocal, final boolean acceptremote) {
        super(optcode, initlocal, initremote, acceptlocal, acceptremote);
    }
    
    public SimpleOptionHandler(final int optcode) {
        super(optcode, false, false, false, false);
    }
    
    @Override
    public int[] answerSubnegotiation(final int[] suboptionData, final int suboptionLength) {
        return null;
    }
    
    @Override
    public int[] startSubnegotiationLocal() {
        return null;
    }
    
    @Override
    public int[] startSubnegotiationRemote() {
        return null;
    }
}
