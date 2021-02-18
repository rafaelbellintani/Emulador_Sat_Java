// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.pop3;

import java.util.Enumeration;
import org.apache.commons.net.ProtocolCommandListener;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.apache.commons.net.MalformedServerReplyException;
import java.io.EOFException;
import org.apache.commons.net.ProtocolCommandSupport;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.apache.commons.net.SocketClient;

public class POP3 extends SocketClient
{
    public static final int DEFAULT_PORT = 110;
    public static final int DISCONNECTED_STATE = -1;
    public static final int AUTHORIZATION_STATE = 0;
    public static final int TRANSACTION_STATE = 1;
    public static final int UPDATE_STATE = 2;
    static final String _OK = "+OK";
    static final String _ERROR = "-ERR";
    private static final String __DEFAULT_ENCODING = "ISO-8859-1";
    private int __popState;
    private BufferedWriter __writer;
    private StringBuffer __commandBuffer;
    BufferedReader _reader;
    int _replyCode;
    String _lastReplyLine;
    Vector<String> _replyLines;
    protected ProtocolCommandSupport _commandSupport_;
    
    public POP3() {
        this.setDefaultPort(110);
        this.__commandBuffer = new StringBuffer();
        this.__popState = -1;
        this._reader = null;
        this.__writer = null;
        this._replyLines = new Vector<String>();
        this._commandSupport_ = new ProtocolCommandSupport(this);
    }
    
    private void __getReply() throws IOException {
        this._replyLines.setSize(0);
        final String line = this._reader.readLine();
        if (line == null) {
            throw new EOFException("Connection closed without indication.");
        }
        if (line.startsWith("+OK")) {
            this._replyCode = 0;
        }
        else {
            if (!line.startsWith("-ERR")) {
                throw new MalformedServerReplyException("Received invalid POP3 protocol response from server.");
            }
            this._replyCode = 1;
        }
        this._replyLines.addElement(line);
        this._lastReplyLine = line;
        if (this._commandSupport_.getListenerCount() > 0) {
            this._commandSupport_.fireReplyReceived(this._replyCode, this.getReplyString());
        }
    }
    
    @Override
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        this._reader = new BufferedReader(new InputStreamReader(this._input_, "ISO-8859-1"));
        this.__writer = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
        this.__getReply();
        this.setState(0);
    }
    
    public void addProtocolCommandListener(final ProtocolCommandListener listener) {
        this._commandSupport_.addProtocolCommandListener(listener);
    }
    
    public void removeProtocolCommandistener(final ProtocolCommandListener listener) {
        this._commandSupport_.removeProtocolCommandListener(listener);
    }
    
    public void setState(final int state) {
        this.__popState = state;
    }
    
    public int getState() {
        return this.__popState;
    }
    
    public void getAdditionalReply() throws IOException {
        for (String line = this._reader.readLine(); line != null; line = this._reader.readLine()) {
            this._replyLines.addElement(line);
            if (line.equals(".")) {
                break;
            }
        }
    }
    
    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        this._reader = null;
        this.__writer = null;
        this._lastReplyLine = null;
        this._replyLines.setSize(0);
        this.setState(-1);
    }
    
    public int sendCommand(final String command, final String args) throws IOException {
        this.__commandBuffer.setLength(0);
        this.__commandBuffer.append(command);
        if (args != null) {
            this.__commandBuffer.append(' ');
            this.__commandBuffer.append(args);
        }
        this.__commandBuffer.append("\r\n");
        final String message;
        this.__writer.write(message = this.__commandBuffer.toString());
        this.__writer.flush();
        if (this._commandSupport_.getListenerCount() > 0) {
            this._commandSupport_.fireCommandSent(command, message);
        }
        this.__getReply();
        return this._replyCode;
    }
    
    public int sendCommand(final String command) throws IOException {
        return this.sendCommand(command, null);
    }
    
    public int sendCommand(final int command, final String args) throws IOException {
        return this.sendCommand(POP3Command._commands[command], args);
    }
    
    public int sendCommand(final int command) throws IOException {
        return this.sendCommand(POP3Command._commands[command], null);
    }
    
    public String[] getReplyStrings() {
        final String[] lines = new String[this._replyLines.size()];
        this._replyLines.copyInto(lines);
        return lines;
    }
    
    public String getReplyString() {
        final StringBuffer buffer = new StringBuffer(256);
        final Enumeration<String> en = this._replyLines.elements();
        while (en.hasMoreElements()) {
            buffer.append(en.nextElement());
            buffer.append("\r\n");
        }
        return buffer.toString();
    }
}
