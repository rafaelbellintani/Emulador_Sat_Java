// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import java.io.IOException;
import org.apache.log4j.helpers.LogLog;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Vector;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.AppenderSkeleton;

public class TelnetAppender extends AppenderSkeleton
{
    private SocketHandler sh;
    private int port;
    
    public TelnetAppender() {
        this.port = 23;
    }
    
    public boolean requiresLayout() {
        return true;
    }
    
    public void activateOptions() {
        try {
            (this.sh = new SocketHandler(this.port)).start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        super.activateOptions();
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    public void close() {
        if (this.sh != null) {
            this.sh.close();
            try {
                this.sh.join();
            }
            catch (InterruptedException ex) {}
        }
    }
    
    protected void append(final LoggingEvent event) {
        this.sh.send(super.layout.format(event));
        if (super.layout.ignoresThrowable()) {
            final String[] s = event.getThrowableStrRep();
            if (s != null) {
                for (int len = s.length, i = 0; i < len; ++i) {
                    this.sh.send(s[i]);
                    this.sh.send(Layout.LINE_SEP);
                }
            }
        }
    }
    
    protected class SocketHandler extends Thread
    {
        private Vector writers;
        private Vector connections;
        private ServerSocket serverSocket;
        private int MAX_CONNECTIONS;
        
        public void finalize() {
            this.close();
        }
        
        public void close() {
            final Enumeration e = this.connections.elements();
            while (e.hasMoreElements()) {
                try {
                    e.nextElement().close();
                }
                catch (Exception ex) {}
            }
            try {
                this.serverSocket.close();
            }
            catch (Exception ex2) {}
        }
        
        public void send(final String message) {
            final Enumeration ce = this.connections.elements();
            final Enumeration e = this.writers.elements();
            while (e.hasMoreElements()) {
                final Socket sock = ce.nextElement();
                final PrintWriter writer = e.nextElement();
                writer.print(message);
                if (writer.checkError()) {
                    this.connections.remove(sock);
                    this.writers.remove(writer);
                }
            }
        }
        
        public void run() {
            while (!this.serverSocket.isClosed()) {
                try {
                    final Socket newClient = this.serverSocket.accept();
                    final PrintWriter pw = new PrintWriter(newClient.getOutputStream());
                    if (this.connections.size() < this.MAX_CONNECTIONS) {
                        this.connections.addElement(newClient);
                        this.writers.addElement(pw);
                        pw.print("TelnetAppender v1.0 (" + this.connections.size() + " active connections)\r\n\r\n");
                        pw.flush();
                    }
                    else {
                        pw.print("Too many connections.\r\n");
                        pw.flush();
                        newClient.close();
                    }
                    continue;
                }
                catch (Exception e) {
                    if (!this.serverSocket.isClosed()) {
                        LogLog.error("Encountered error while in SocketHandler loop.", e);
                    }
                }
                break;
            }
            try {
                this.serverSocket.close();
            }
            catch (IOException ex) {}
        }
        
        public SocketHandler(final int port) throws IOException {
            this.writers = new Vector();
            this.connections = new Vector();
            this.MAX_CONNECTIONS = 20;
            this.serverSocket = new ServerSocket(port);
            this.setName("TelnetAppender-" + this.getName() + "-" + port);
        }
    }
}
