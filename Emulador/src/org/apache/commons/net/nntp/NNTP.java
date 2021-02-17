// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.nntp;

import org.apache.commons.net.ProtocolCommandListener;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.apache.commons.net.MalformedServerReplyException;
import org.apache.commons.net.ProtocolCommandSupport;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import org.apache.commons.net.SocketClient;

public class NNTP extends SocketClient
{
    public static final int DEFAULT_PORT = 119;
    private static final String __DEFAULT_ENCODING = "ISO-8859-1";
    private StringBuffer __commandBuffer;
    boolean _isAllowedToPost;
    int _replyCode;
    String _replyString;
    protected BufferedReader _reader_;
    protected BufferedWriter _writer_;
    protected ProtocolCommandSupport _commandSupport_;
    
    public NNTP() {
        this.setDefaultPort(119);
        this.__commandBuffer = new StringBuffer();
        this._replyString = null;
        this._reader_ = null;
        this._writer_ = null;
        this._isAllowedToPost = false;
        this._commandSupport_ = new ProtocolCommandSupport(this);
    }
    
    private void __getReply() throws IOException {
        this._replyString = this._reader_.readLine();
        if (this._replyString == null) {
            throw new NNTPConnectionClosedException("Connection closed without indication.");
        }
        if (this._replyString.length() < 3) {
            throw new MalformedServerReplyException("Truncated server reply: " + this._replyString);
        }
        try {
            this._replyCode = Integer.parseInt(this._replyString.substring(0, 3));
        }
        catch (NumberFormatException e) {
            throw new MalformedServerReplyException("Could not parse response code.\nServer Reply: " + this._replyString);
        }
        if (this._commandSupport_.getListenerCount() > 0) {
            this._commandSupport_.fireReplyReceived(this._replyCode, this._replyString + "\r\n");
        }
        if (this._replyCode == 400) {
            throw new NNTPConnectionClosedException("NNTP response 400 received.  Server closed connection.");
        }
    }
    
    @Override
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        this._reader_ = new BufferedReader(new InputStreamReader(this._input_, "ISO-8859-1"));
        this._writer_ = new BufferedWriter(new OutputStreamWriter(this._output_, "ISO-8859-1"));
        this.__getReply();
        this._isAllowedToPost = (this._replyCode == 200);
    }
    
    public void addProtocolCommandListener(final ProtocolCommandListener listener) {
        this._commandSupport_.addProtocolCommandListener(listener);
    }
    
    public void removeProtocolCommandListener(final ProtocolCommandListener listener) {
        this._commandSupport_.removeProtocolCommandListener(listener);
    }
    
    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        this._reader_ = null;
        this._writer_ = null;
        this._replyString = null;
        this._isAllowedToPost = false;
    }
    
    public boolean isAllowedToPost() {
        return this._isAllowedToPost;
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
        this._writer_.write(message = this.__commandBuffer.toString());
        this._writer_.flush();
        if (this._commandSupport_.getListenerCount() > 0) {
            this._commandSupport_.fireCommandSent(command, message);
        }
        this.__getReply();
        return this._replyCode;
    }
    
    public int sendCommand(final int command, final String args) throws IOException {
        return this.sendCommand(NNTPCommand._commands[command], args);
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
    
    public String getReplyString() {
        return this._replyString;
    }
    
    public int article(final String messageId) throws IOException {
        return this.sendCommand(0, messageId);
    }
    
    public int article(final int articleNumber) throws IOException {
        return this.sendCommand(0, Integer.toString(articleNumber));
    }
    
    public int article() throws IOException {
        return this.sendCommand(0);
    }
    
    public int body(final String messageId) throws IOException {
        return this.sendCommand(1, messageId);
    }
    
    public int body(final int articleNumber) throws IOException {
        return this.sendCommand(1, Integer.toString(articleNumber));
    }
    
    public int body() throws IOException {
        return this.sendCommand(1);
    }
    
    public int head(final String messageId) throws IOException {
        return this.sendCommand(3, messageId);
    }
    
    public int head(final int articleNumber) throws IOException {
        return this.sendCommand(3, Integer.toString(articleNumber));
    }
    
    public int head() throws IOException {
        return this.sendCommand(3);
    }
    
    public int stat(final String messageId) throws IOException {
        return this.sendCommand(14, messageId);
    }
    
    public int stat(final int articleNumber) throws IOException {
        return this.sendCommand(14, Integer.toString(articleNumber));
    }
    
    public int stat() throws IOException {
        return this.sendCommand(14);
    }
    
    public int group(final String newsgroup) throws IOException {
        return this.sendCommand(2, newsgroup);
    }
    
    public int help() throws IOException {
        return this.sendCommand(4);
    }
    
    public int ihave(final String messageId) throws IOException {
        return this.sendCommand(5, messageId);
    }
    
    public int last() throws IOException {
        return this.sendCommand(6);
    }
    
    public int list() throws IOException {
        return this.sendCommand(7);
    }
    
    public int next() throws IOException {
        return this.sendCommand(10);
    }
    
    public int newgroups(final String date, final String time, final boolean GMT, final String distributions) throws IOException {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(date);
        buffer.append(' ');
        buffer.append(time);
        if (GMT) {
            buffer.append(' ');
            buffer.append("GMT");
        }
        if (distributions != null) {
            buffer.append(" <");
            buffer.append(distributions);
            buffer.append('>');
        }
        return this.sendCommand(8, buffer.toString());
    }
    
    public int newnews(final String newsgroups, final String date, final String time, final boolean GMT, final String distributions) throws IOException {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(newsgroups);
        buffer.append(' ');
        buffer.append(date);
        buffer.append(' ');
        buffer.append(time);
        if (GMT) {
            buffer.append(' ');
            buffer.append("GMT");
        }
        if (distributions != null) {
            buffer.append(" <");
            buffer.append(distributions);
            buffer.append('>');
        }
        return this.sendCommand(9, buffer.toString());
    }
    
    public int post() throws IOException {
        return this.sendCommand(11);
    }
    
    public int quit() throws IOException {
        return this.sendCommand(12);
    }
    
    public int authinfoUser(final String username) throws IOException {
        final String userParameter = "USER " + username;
        return this.sendCommand(15, userParameter);
    }
    
    public int authinfoPass(final String password) throws IOException {
        final String passParameter = "PASS " + password;
        return this.sendCommand(15, passParameter);
    }
    
    public int xover(final String selectedArticles) throws IOException {
        return this.sendCommand(16, selectedArticles);
    }
    
    public int xhdr(final String header, final String selectedArticles) throws IOException {
        final StringBuffer command = new StringBuffer(header);
        command.append(" ");
        command.append(selectedArticles);
        return this.sendCommand(17, command.toString());
    }
    
    public int listActive(final String wildmat) throws IOException {
        final StringBuffer command = new StringBuffer("ACTIVE ");
        command.append(wildmat);
        return this.sendCommand(7, command.toString());
    }
}
