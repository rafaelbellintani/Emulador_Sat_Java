// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.DatagramSocket;

public class DefaultDatagramSocketFactory implements DatagramSocketFactory
{
    public DatagramSocket createDatagramSocket() throws SocketException {
        return new DatagramSocket();
    }
    
    public DatagramSocket createDatagramSocket(final int port) throws SocketException {
        return new DatagramSocket(port);
    }
    
    public DatagramSocket createDatagramSocket(final int port, final InetAddress laddr) throws SocketException {
        return new DatagramSocket(port, laddr);
    }
}
