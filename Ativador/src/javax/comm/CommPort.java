// 
// Decompiled by Procyon v0.5.36
// 

package javax.comm;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class CommPort
{
    protected String name;
    
    CommPort() {
    }
    
    public String getName() {
        return this.name;
    }
    
    public String toString() {
        return this.name;
    }
    
    public abstract InputStream getInputStream() throws IOException;
    
    public abstract OutputStream getOutputStream() throws IOException;
    
    public void close() {
        try {
            CommPortIdentifier.getPortIdentifier(this).internalClosePort();
        }
        catch (NoSuchPortException ex) {}
    }
    
    public abstract void enableReceiveThreshold(final int p0) throws UnsupportedCommOperationException;
    
    public abstract void disableReceiveThreshold();
    
    public abstract boolean isReceiveThresholdEnabled();
    
    public abstract int getReceiveThreshold();
    
    public abstract void enableReceiveTimeout(final int p0) throws UnsupportedCommOperationException;
    
    public abstract void disableReceiveTimeout();
    
    public abstract boolean isReceiveTimeoutEnabled();
    
    public abstract int getReceiveTimeout();
    
    public abstract void enableReceiveFraming(final int p0) throws UnsupportedCommOperationException;
    
    public abstract void disableReceiveFraming();
    
    public abstract boolean isReceiveFramingEnabled();
    
    public abstract int getReceiveFramingByte();
    
    public abstract void setInputBufferSize(final int p0);
    
    public abstract int getInputBufferSize();
    
    public abstract void setOutputBufferSize(final int p0);
    
    public abstract int getOutputBufferSize();
}
