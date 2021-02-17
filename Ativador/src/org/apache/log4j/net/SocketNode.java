// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.log4j.net;

import java.io.IOException;
import java.net.SocketException;
import java.io.EOFException;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import java.io.InputStream;
import java.io.BufferedInputStream;
import org.apache.log4j.Logger;
import java.io.ObjectInputStream;
import org.apache.log4j.spi.LoggerRepository;
import java.net.Socket;

public class SocketNode implements Runnable
{
    Socket socket;
    LoggerRepository hierarchy;
    ObjectInputStream ois;
    static Logger logger;
    
    public SocketNode(final Socket socket, final LoggerRepository hierarchy) {
        this.socket = socket;
        this.hierarchy = hierarchy;
        try {
            this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        }
        catch (Exception e) {
            SocketNode.logger.error("Could not open ObjectInputStream to " + socket, e);
        }
    }
    
    public void run() {
        try {
            if (this.ois != null) {
                while (true) {
                    final LoggingEvent event = (LoggingEvent)this.ois.readObject();
                    final Logger remoteLogger = this.hierarchy.getLogger(event.getLoggerName());
                    if (event.getLevel().isGreaterOrEqual(remoteLogger.getEffectiveLevel())) {
                        remoteLogger.callAppenders(event);
                    }
                }
            }
        }
        catch (EOFException e4) {
            SocketNode.logger.info("Caught java.io.EOFException closing conneciton.");
        }
        catch (SocketException e5) {
            SocketNode.logger.info("Caught java.net.SocketException closing conneciton.");
        }
        catch (IOException e) {
            SocketNode.logger.info("Caught java.io.IOException: " + e);
            SocketNode.logger.info("Closing connection.");
        }
        catch (Exception e2) {
            SocketNode.logger.error("Unexpected exception. Closing conneciton.", e2);
        }
        finally {
            if (this.ois != null) {
                try {
                    this.ois.close();
                }
                catch (Exception e3) {
                    SocketNode.logger.info("Could not close connection.", e3);
                }
            }
            if (this.socket != null) {
                try {
                    this.socket.close();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    static {
        SocketNode.logger = Logger.getLogger(SocketNode.class);
    }
}
