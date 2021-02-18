// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.smtp;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import org.apache.commons.net.ProtocolCommandListener;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.InputStreamReader;
import org.apache.commons.net.MalformedServerReplyException;
import java.io.IOException;
import org.apache.commons.net.ProtocolCommandSupport;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import org.apache.commons.net.SocketClient;

public class SMTP extends SocketClient
{
    public static final int DEFAULT_PORT = 25;
    private static final String __DEFAULT_ENCODING = "ISO-8859-1";
    private String encoding;
    private StringBuffer __commandBuffer;
    BufferedReader _reader;
    BufferedWriter _writer;
    int _replyCode;
    ArrayList<String> _replyLines;
    boolean _newReplyString;
    String _replyString;
    protected ProtocolCommandSupport _commandSupport_;
    
    public SMTP() {
        this.encoding = "ISO-8859-1";
        this.setDefaultPort(25);
        this.__commandBuffer = new StringBuffer();
        this._replyLines = new ArrayList<String>();
        this._newReplyString = false;
        this._replyString = null;
        this._commandSupport_ = new ProtocolCommandSupport(this);
    }
    
    public SMTP(final String encoding) {
        this();
        this.encoding = encoding;
    }
    
    private int __sendCommand(final String command, final String args, final boolean includeSpace) throws IOException {
        this.__commandBuffer.setLength(0);
        this.__commandBuffer.append(command);
        if (args != null) {
            if (includeSpace) {
                this.__commandBuffer.append(' ');
            }
            this.__commandBuffer.append(args);
        }
        this.__commandBuffer.append("\r\n");
        final String message;
        this._writer.write(message = this.__commandBuffer.toString());
        this._writer.flush();
        if (this._commandSupport_.getListenerCount() > 0) {
            this._commandSupport_.fireCommandSent(command, message);
        }
        this.__getReply();
        return this._replyCode;
    }
    
    private int __sendCommand(final int command, final String args, final boolean includeSpace) throws IOException {
        return this.__sendCommand(SMTPCommand._commands[command], args, includeSpace);
    }
    
    private void __getReply() throws IOException {
        this._newReplyString = true;
        this._replyLines.clear();
        String line = this._reader.readLine();
        if (line == null) {
            throw new SMTPConnectionClosedException("Connection closed without indication.");
        }
        final int length = line.length();
        if (length < 3) {
            throw new MalformedServerReplyException("Truncated server reply: " + line);
        }
        try {
            final String code = line.substring(0, 3);
            this._replyCode = Integer.parseInt(code);
        }
        catch (NumberFormatException e) {
            throw new MalformedServerReplyException("Could not parse response code.\nServer Reply: " + line);
        }
        this._replyLines.add(line);
        if (length > 3 && line.charAt(3) == '-') {
            do {
                line = this._reader.readLine();
                if (line == null) {
                    throw new SMTPConnectionClosedException("Connection closed without indication.");
                }
                this._replyLines.add(line);
            } while (line.length() < 4 || line.charAt(3) == '-' || !Character.isDigit(line.charAt(0)));
        }
        if (this._commandSupport_.getListenerCount() > 0) {
            this._commandSupport_.fireReplyReceived(this._replyCode, this.getReplyString());
        }
        if (this._replyCode == 421) {
            throw new SMTPConnectionClosedException("SMTP response 421 received.  Server closed connection.");
        }
    }
    
    @Override
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        this._reader = new BufferedReader(new InputStreamReader(this._input_, this.encoding));
        this._writer = new BufferedWriter(new OutputStreamWriter(this._output_, this.encoding));
        this.__getReply();
    }
    
    public void addProtocolCommandListener(final ProtocolCommandListener listener) {
        this._commandSupport_.addProtocolCommandListener(listener);
    }
    
    public void removeProtocolCommandistener(final ProtocolCommandListener listener) {
        this._commandSupport_.removeProtocolCommandListener(listener);
    }
    
    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        this._reader = null;
        this._writer = null;
        this._replyString = null;
        this._replyLines.clear();
        this._newReplyString = false;
    }
    
    public int sendCommand(final String command, final String args) throws IOException {
        return this.__sendCommand(command, args, true);
    }
    
    public int sendCommand(final int command, final String args) throws IOException {
        return this.sendCommand(SMTPCommand._commands[command], args);
    }
    
    public int sendCommand(final String command) throws IOException {
        return this.sendCommand(command, null);
    }
    
    public int sendCommand(final int command) throws IOException {
        return this.sendCommand(command, null);
    }
    
    public int getReplyCode() {
        return this._replyCode;
    }
    
    public int getReply() throws IOException {
        this.__getReply();
        return this._replyCode;
    }
    
    public String[] getReplyStrings() {
        final String[] lines = new String[this._replyLines.size()];
        this._replyLines.addAll(Arrays.asList(lines));
        return lines;
    }
    
    public String getReplyString() {
        if (!this._newReplyString) {
            return this._replyString;
        }
        final StringBuilder buffer = new StringBuilder();
        for (final String line : this._replyLines) {
            buffer.append(line);
            buffer.append("\r\n");
        }
        this._newReplyString = false;
        return this._replyString = buffer.toString();
    }
    
    public int helo(final String hostname) throws IOException {
        return this.sendCommand(0, hostname);
    }
    
    public int mail(final String reversePath) throws IOException {
        return this.__sendCommand(1, reversePath, false);
    }
    
    public int rcpt(final String forwardPath) throws IOException {
        return this.__sendCommand(2, forwardPath, false);
    }
    
    public int data() throws IOException {
        return this.sendCommand(3);
    }
    
    public int send(final String reversePath) throws IOException {
        return this.sendCommand(4, reversePath);
    }
    
    public int soml(final String reversePath) throws IOException {
        return this.sendCommand(5, reversePath);
    }
    
    public int saml(final String reversePath) throws IOException {
        return this.sendCommand(6, reversePath);
    }
    
    public int rset() throws IOException {
        return this.sendCommand(7);
    }
    
    public int vrfy(final String user) throws IOException {
        return this.sendCommand(8, user);
    }
    
    public int expn(final String name) throws IOException {
        return this.sendCommand(9, name);
    }
    
    public int help() throws IOException {
        return this.sendCommand(10);
    }
    
    public int help(final String command) throws IOException {
        return this.sendCommand(10, command);
    }
    
    public int noop() throws IOException {
        return this.sendCommand(11);
    }
    
    public int turn() throws IOException {
        return this.sendCommand(12);
    }
    
    public int quit() throws IOException {
        return this.sendCommand(13);
    }
}
