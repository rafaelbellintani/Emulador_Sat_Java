// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net;

import java.net.SocketException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

public abstract class SocketClient
{
    public static final String NETASCII_EOL = "\r\n";
    private static final SocketFactory __DEFAULT_SOCKET_FACTORY;
    private static final ServerSocketFactory __DEFAULT_SERVER_SOCKET_FACTORY;
    protected int _timeout_;
    protected Socket _socket_;
    protected int _defaultPort_;
    protected InputStream _input_;
    protected OutputStream _output_;
    protected SocketFactory _socketFactory_;
    protected ServerSocketFactory _serverSocketFactory_;
    private static final int DEFAULT_CONNECT_TIMEOUT = 0;
    protected int connectTimeout;
    
    public SocketClient() {
        this.connectTimeout = 0;
        this._socket_ = null;
        this._input_ = null;
        this._output_ = null;
        this._timeout_ = 0;
        this._defaultPort_ = 0;
        this._socketFactory_ = SocketClient.__DEFAULT_SOCKET_FACTORY;
        this._serverSocketFactory_ = SocketClient.__DEFAULT_SERVER_SOCKET_FACTORY;
    }
    
    protected void _connectAction_() throws IOException {
        this._socket_.setSoTimeout(this._timeout_);
        this._input_ = this._socket_.getInputStream();
        this._output_ = this._socket_.getOutputStream();
    }
    
    public void connect(final InetAddress host, final int port) throws SocketException, IOException {
        (this._socket_ = this._socketFactory_.createSocket()).connect(new InetSocketAddress(host, port), this.connectTimeout);
        this._connectAction_();
    }
    
    public void connect(final String hostname, final int port) throws SocketException, IOException {
        (this._socket_ = this._socketFactory_.createSocket()).connect(new InetSocketAddress(hostname, port), this.connectTimeout);
        this._connectAction_();
    }
    
    public void connect(final InetAddress host, final int port, final InetAddress localAddr, final int localPort) throws SocketException, IOException {
        (this._socket_ = this._socketFactory_.createSocket()).bind(new InetSocketAddress(localAddr, localPort));
        this._socket_.connect(new InetSocketAddress(host, port), this.connectTimeout);
        this._connectAction_();
    }
    
    public void connect(final String hostname, final int port, final InetAddress localAddr, final int localPort) throws SocketException, IOException {
        this._socket_ = this._socketFactory_.createSocket(hostname, port, localAddr, localPort);
        this._connectAction_();
    }
    
    public void connect(final InetAddress host) throws SocketException, IOException {
        this.connect(host, this._defaultPort_);
    }
    
    public void connect(final String hostname) throws SocketException, IOException {
        this.connect(hostname, this._defaultPort_);
    }
    
    public void disconnect() throws IOException {
        if (this._socket_ != null) {
            this._socket_.close();
        }
        if (this._input_ != null) {
            this._input_.close();
        }
        if (this._output_ != null) {
            this._output_.close();
        }
        if (this._socket_ != null) {
            this._socket_ = null;
        }
        this._input_ = null;
        this._output_ = null;
    }
    
    public boolean isConnected() {
        return this._socket_ != null && this._socket_.isConnected();
    }
    
    public void setDefaultPort(final int port) {
        this._defaultPort_ = port;
    }
    
    public int getDefaultPort() {
        return this._defaultPort_;
    }
    
    public void setDefaultTimeout(final int timeout) {
        this._timeout_ = timeout;
    }
    
    public int getDefaultTimeout() {
        return this._timeout_;
    }
    
    public void setSoTimeout(final int timeout) throws SocketException {
        this._socket_.setSoTimeout(timeout);
    }
    
    public void setSendBufferSize(final int size) throws SocketException {
        this._socket_.setSendBufferSize(size);
    }
    
    public void setReceiveBufferSize(final int size) throws SocketException {
        this._socket_.setReceiveBufferSize(size);
    }
    
    public int getSoTimeout() throws SocketException {
        return this._socket_.getSoTimeout();
    }
    
    public void setTcpNoDelay(final boolean on) throws SocketException {
        this._socket_.setTcpNoDelay(on);
    }
    
    public boolean getTcpNoDelay() throws SocketException {
        return this._socket_.getTcpNoDelay();
    }
    
    public void setSoLinger(final boolean on, final int val) throws SocketException {
        this._socket_.setSoLinger(on, val);
    }
    
    public int getSoLinger() throws SocketException {
        return this._socket_.getSoLinger();
    }
    
    public int getLocalPort() {
        return this._socket_.getLocalPort();
    }
    
    public InetAddress getLocalAddress() {
        return this._socket_.getLocalAddress();
    }
    
    public int getRemotePort() {
        return this._socket_.getPort();
    }
    
    public InetAddress getRemoteAddress() {
        return this._socket_.getInetAddress();
    }
    
    public boolean verifyRemote(final Socket socket) {
        final InetAddress host1 = socket.getInetAddress();
        final InetAddress host2 = this.getRemoteAddress();
        return host1.equals(host2);
    }
    
    public void setSocketFactory(final SocketFactory factory) {
        if (factory == null) {
            this._socketFactory_ = SocketClient.__DEFAULT_SOCKET_FACTORY;
        }
        else {
            this._socketFactory_ = factory;
        }
    }
    
    public void setServerSocketFactory(final ServerSocketFactory factory) {
        if (factory == null) {
            this._serverSocketFactory_ = SocketClient.__DEFAULT_SERVER_SOCKET_FACTORY;
        }
        else {
            this._serverSocketFactory_ = factory;
        }
    }
    
    public void setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
    
    public int getConnectTimeout() {
        return this.connectTimeout;
    }
    
    static {
        __DEFAULT_SOCKET_FACTORY = SocketFactory.getDefault();
        __DEFAULT_SERVER_SOCKET_FACTORY = ServerSocketFactory.getDefault();
    }
}
