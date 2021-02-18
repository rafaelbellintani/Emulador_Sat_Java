// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.varia;

import org.apache.log4j.helpers.LogLog;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;

class HUPNode implements Runnable
{
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    ExternallyRolledFileAppender er;
    
    public HUPNode(final Socket socket, final ExternallyRolledFileAppender er) {
        this.socket = socket;
        this.er = er;
        try {
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void run() {
        try {
            final String line = this.dis.readUTF();
            LogLog.debug("Got external roll over signal.");
            if ("RollOver".equals(line)) {
                synchronized (this.er) {
                    this.er.rollOver();
                }
                this.dos.writeUTF("OK");
            }
            else {
                this.dos.writeUTF("Expecting [RollOver] string.");
            }
            this.dos.close();
        }
        catch (Exception e) {
            LogLog.error("Unexpected exception. Exiting HUPNode.", e);
        }
    }
}
