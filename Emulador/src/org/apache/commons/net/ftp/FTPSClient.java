// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSocket;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import java.net.Socket;
import javax.net.ssl.SSLContext;

public class FTPSClient extends FTPClient
{
    public static String KEYSTORE_ALGORITHM;
    public static String TRUSTSTORE_ALGORITHM;
    public static String PROVIDER;
    public static String STORE_TYPE;
    private static final String[] PROT_COMMAND_VALUE;
    private static final String DEFAULT_PROT = "C";
    private static final String DEFAULT_PROTOCOL = "TLS";
    private boolean isImplicit;
    private String protocol;
    private String auth;
    private SSLContext context;
    private Socket planeSocket;
    private boolean isCreation;
    private boolean isClientMode;
    private boolean isNeedClientAuth;
    private boolean isWantClientAuth;
    private String[] suites;
    private String[] protocols;
    private TrustManager trustManager;
    private KeyManager keyManager;
    
    public FTPSClient() throws NoSuchAlgorithmException {
        this.protocol = "TLS";
        this.auth = "TLS";
        this.isCreation = true;
        this.isClientMode = true;
        this.isNeedClientAuth = false;
        this.isWantClientAuth = false;
        this.suites = null;
        this.protocols = null;
        this.trustManager = new FTPSTrustManager();
        this.protocol = "TLS";
        this.isImplicit = false;
    }
    
    public FTPSClient(final boolean isImplicit) throws NoSuchAlgorithmException {
        this.protocol = "TLS";
        this.auth = "TLS";
        this.isCreation = true;
        this.isClientMode = true;
        this.isNeedClientAuth = false;
        this.isWantClientAuth = false;
        this.suites = null;
        this.protocols = null;
        this.trustManager = new FTPSTrustManager();
        this.protocol = "TLS";
        this.isImplicit = isImplicit;
    }
    
    public FTPSClient(final String protocol) throws NoSuchAlgorithmException {
        this.protocol = "TLS";
        this.auth = "TLS";
        this.isCreation = true;
        this.isClientMode = true;
        this.isNeedClientAuth = false;
        this.isWantClientAuth = false;
        this.suites = null;
        this.protocols = null;
        this.trustManager = new FTPSTrustManager();
        this.protocol = protocol;
        this.isImplicit = false;
    }
    
    public FTPSClient(final String protocol, final boolean isImplicit) throws NoSuchAlgorithmException {
        this.protocol = "TLS";
        this.auth = "TLS";
        this.isCreation = true;
        this.isClientMode = true;
        this.isNeedClientAuth = false;
        this.isWantClientAuth = false;
        this.suites = null;
        this.protocols = null;
        this.trustManager = new FTPSTrustManager();
        this.protocol = protocol;
        this.isImplicit = isImplicit;
    }
    
    public void setAuthValue(final String auth) {
        this.auth = auth;
    }
    
    public String getAuthValue() {
        return this.auth;
    }
    
    @Override
    protected void _connectAction_() throws IOException {
        if (this.isImplicit) {
            this.sslNegotiation();
        }
        super._connectAction_();
        if (!this.isImplicit) {
            this.execAUTH();
            this.sslNegotiation();
        }
    }
    
    private void execAUTH() throws SSLException, IOException {
        final int replyCode = this.sendCommand(FTPSCommand._commands[0], this.auth);
        if (334 != replyCode) {
            if (234 != replyCode) {
                throw new SSLException(this.getReplyString());
            }
        }
    }
    
    private void initSslContext() throws IOException {
        if (this.context == null) {
            try {
                (this.context = SSLContext.getInstance(this.protocol)).init(new KeyManager[] { this.getKeyManager() }, new TrustManager[] { this.getTrustManager() }, null);
            }
            catch (KeyManagementException e) {
                final IOException ioe = new IOException("Could not initialize SSL context");
                ioe.initCause(e);
                throw ioe;
            }
            catch (NoSuchAlgorithmException e2) {
                final IOException ioe = new IOException("Could not initialize SSL context");
                ioe.initCause(e2);
                throw ioe;
            }
        }
    }
    
    private void sslNegotiation() throws IOException {
        this.planeSocket = this._socket_;
        this.initSslContext();
        final SSLSocketFactory ssf = this.context.getSocketFactory();
        final String ip = this._socket_.getInetAddress().getHostAddress();
        final int port = this._socket_.getPort();
        final SSLSocket socket = (SSLSocket)ssf.createSocket(this._socket_, ip, port, true);
        socket.setEnableSessionCreation(this.isCreation);
        socket.setUseClientMode(this.isClientMode);
        if (!this.isClientMode) {
            socket.setNeedClientAuth(this.isNeedClientAuth);
            socket.setWantClientAuth(this.isWantClientAuth);
        }
        if (this.protocols != null) {
            socket.setEnabledProtocols(this.protocols);
        }
        if (this.suites != null) {
            socket.setEnabledCipherSuites(this.suites);
        }
        socket.startHandshake();
        this._socket_ = socket;
        this._controlInput_ = new BufferedReader(new InputStreamReader(socket.getInputStream(), this.getControlEncoding()));
        this._controlOutput_ = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), this.getControlEncoding()));
    }
    
    private KeyManager getKeyManager() {
        return this.keyManager;
    }
    
    public void setKeyManager(final KeyManager keyManager) {
        this.keyManager = keyManager;
    }
    
    public void setEnabledSessionCreation(final boolean isCreation) {
        this.isCreation = isCreation;
    }
    
    public boolean getEnableSessionCreation() {
        return this._socket_ instanceof SSLSocket && ((SSLSocket)this._socket_).getEnableSessionCreation();
    }
    
    public void setNeedClientAuth(final boolean isNeedClientAuth) {
        this.isNeedClientAuth = isNeedClientAuth;
    }
    
    public boolean getNeedClientAuth() {
        return this._socket_ instanceof SSLSocket && ((SSLSocket)this._socket_).getNeedClientAuth();
    }
    
    public void setWantClientAuth(final boolean isWantClientAuth) {
        this.isWantClientAuth = isWantClientAuth;
    }
    
    public boolean getWantClientAuth() {
        return this._socket_ instanceof SSLSocket && ((SSLSocket)this._socket_).getWantClientAuth();
    }
    
    public void setUseClientMode(final boolean isClientMode) {
        this.isClientMode = isClientMode;
    }
    
    public boolean getUseClientMode() {
        return this._socket_ instanceof SSLSocket && ((SSLSocket)this._socket_).getUseClientMode();
    }
    
    public void setEnabledCipherSuites(final String[] cipherSuites) {
        System.arraycopy(cipherSuites, 0, this.suites = new String[cipherSuites.length], 0, cipherSuites.length);
    }
    
    public String[] getEnabledCipherSuites() {
        if (this._socket_ instanceof SSLSocket) {
            return ((SSLSocket)this._socket_).getEnabledCipherSuites();
        }
        return null;
    }
    
    public void setEnabledProtocols(final String[] protocolVersions) {
        System.arraycopy(protocolVersions, 0, this.protocols = new String[protocolVersions.length], 0, protocolVersions.length);
    }
    
    public String[] getEnabledProtocols() {
        if (this._socket_ instanceof SSLSocket) {
            return ((SSLSocket)this._socket_).getEnabledProtocols();
        }
        return null;
    }
    
    public void execPBSZ(final long pbsz) throws SSLException, IOException {
        if (pbsz < 0L || 4294967295L < pbsz) {
            throw new IllegalArgumentException();
        }
        if (200 != this.sendCommand(FTPSCommand._commands[2], String.valueOf(pbsz))) {
            throw new SSLException(this.getReplyString());
        }
    }
    
    public void execPROT(String prot) throws SSLException, IOException {
        if (prot == null) {
            prot = "C";
        }
        if (!this.checkPROTValue(prot)) {
            throw new IllegalArgumentException();
        }
        if (200 != this.sendCommand(FTPSCommand._commands[3], prot)) {
            throw new SSLException(this.getReplyString());
        }
        if ("C".equals(prot)) {
            this.setSocketFactory(null);
            this.setServerSocketFactory(null);
        }
        else {
            this.setSocketFactory(new FTPSSocketFactory(this.context));
            this.initSslContext();
            final SSLServerSocketFactory ssf = this.context.getServerSocketFactory();
            this.setServerSocketFactory(ssf);
        }
    }
    
    private boolean checkPROTValue(final String prot) {
        for (int p = 0; p < FTPSClient.PROT_COMMAND_VALUE.length; ++p) {
            if (FTPSClient.PROT_COMMAND_VALUE[p].equals(prot)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int sendCommand(final String command, final String args) throws IOException {
        final int repCode = super.sendCommand(command, args);
        if (FTPSCommand._commands[4].equals(command)) {
            if (200 != repCode) {
                throw new SSLException(this.getReplyString());
            }
            this._socket_ = this.planeSocket;
            this.setSocketFactory(null);
        }
        return repCode;
    }
    
    @Override
    protected Socket _openDataConnection_(final int command, final String arg) throws IOException {
        final Socket socket = super._openDataConnection_(command, arg);
        if (socket != null && socket instanceof SSLSocket) {
            final SSLSocket sslSocket = (SSLSocket)socket;
            sslSocket.setUseClientMode(this.isClientMode);
            sslSocket.setEnableSessionCreation(this.isCreation);
            if (!this.isClientMode) {
                sslSocket.setNeedClientAuth(this.isNeedClientAuth);
                sslSocket.setWantClientAuth(this.isWantClientAuth);
            }
            if (this.suites != null) {
                sslSocket.setEnabledCipherSuites(this.suites);
            }
            if (this.protocols != null) {
                sslSocket.setEnabledProtocols(this.protocols);
            }
            sslSocket.startHandshake();
        }
        return socket;
    }
    
    public TrustManager getTrustManager() {
        return this.trustManager;
    }
    
    public void setTrustManager(final TrustManager trustManager) {
        this.trustManager = trustManager;
    }
    
    static {
        PROT_COMMAND_VALUE = new String[] { "C", "E", "S", "P" };
    }
}
