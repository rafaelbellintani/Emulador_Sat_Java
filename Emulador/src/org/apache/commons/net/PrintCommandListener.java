// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net;

import java.io.PrintWriter;

public class PrintCommandListener implements ProtocolCommandListener
{
    private PrintWriter __writer;
    
    public PrintCommandListener(final PrintWriter writer) {
        this.__writer = writer;
    }
    
    public void protocolCommandSent(final ProtocolCommandEvent event) {
        this.__writer.print(event.getMessage());
        this.__writer.flush();
    }
    
    public void protocolReplyReceived(final ProtocolCommandEvent event) {
        this.__writer.print(event.getMessage());
        this.__writer.flush();
    }
}
