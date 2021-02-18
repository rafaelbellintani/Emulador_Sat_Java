// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.telnet;

import org.apache.commons.net.io.ToNetASCIIOutputStream;
import java.io.BufferedInputStream;
import org.apache.commons.net.io.FromNetASCIIInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class TelnetClient extends Telnet
{
    private InputStream __input;
    private OutputStream __output;
    protected boolean readerThread;
    
    public TelnetClient() {
        super("VT100");
        this.readerThread = true;
        this.__input = null;
        this.__output = null;
    }
    
    public TelnetClient(final String termtype) {
        super(termtype);
        this.readerThread = true;
        this.__input = null;
        this.__output = null;
    }
    
    void _flushOutputStream() throws IOException {
        this._output_.flush();
    }
    
    void _closeOutputStream() throws IOException {
        this._output_.close();
    }
    
    @Override
    protected void _connectAction_() throws IOException {
        super._connectAction_();
        InputStream input;
        if (FromNetASCIIInputStream.isConversionRequired()) {
            input = new FromNetASCIIInputStream(this._input_);
        }
        else {
            input = this._input_;
        }
        final TelnetInputStream tmp = new TelnetInputStream(input, this, this.readerThread);
        if (this.readerThread) {
            tmp._start();
        }
        this.__input = new BufferedInputStream(tmp);
        this.__output = new ToNetASCIIOutputStream(new TelnetOutputStream(this));
    }
    
    @Override
    public void disconnect() throws IOException {
        if (this.__input != null) {
            this.__input.close();
        }
        if (this.__output != null) {
            this.__output.close();
        }
        super.disconnect();
    }
    
    public OutputStream getOutputStream() {
        return this.__output;
    }
    
    public InputStream getInputStream() {
        return this.__input;
    }
    
    public boolean getLocalOptionState(final int option) {
        return this._stateIsWill(option) && this._requestedWill(option);
    }
    
    public boolean getRemoteOptionState(final int option) {
        return this._stateIsDo(option) && this._requestedDo(option);
    }
    
    public boolean sendAYT(final long timeout) throws IOException, IllegalArgumentException, InterruptedException {
        return this._sendAYT(timeout);
    }
    
    public void addOptionHandler(final TelnetOptionHandler opthand) throws InvalidTelnetOptionException {
        super.addOptionHandler(opthand);
    }
    
    public void deleteOptionHandler(final int optcode) throws InvalidTelnetOptionException {
        super.deleteOptionHandler(optcode);
    }
    
    public void registerSpyStream(final OutputStream spystream) {
        super._registerSpyStream(spystream);
    }
    
    public void stopSpyStream() {
        super._stopSpyStream();
    }
    
    @Override
    public void registerNotifHandler(final TelnetNotificationHandler notifhand) {
        super.registerNotifHandler(notifhand);
    }
    
    @Override
    public void unregisterNotifHandler() {
        super.unregisterNotifHandler();
    }
    
    public void setReaderThread(final boolean flag) {
        this.readerThread = flag;
    }
    
    public boolean getReaderThread() {
        return this.readerThread;
    }
}
