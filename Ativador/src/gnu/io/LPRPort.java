// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.TooManyListenersException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class LPRPort extends ParallelPort
{
    private static final boolean debug = false;
    private int fd;
    private final ParallelOutputStream out;
    private final ParallelInputStream in;
    private int lprmode;
    private int timeout;
    private int threshold;
    private ParallelPortEventListener PPEventListener;
    private MonitorThread monThread;
    
    private static native void Initialize();
    
    public LPRPort(final String name) throws PortInUseException {
        this.out = new ParallelOutputStream();
        this.in = new ParallelInputStream();
        this.lprmode = 0;
        this.timeout = 0;
        this.threshold = 1;
        this.fd = this.open(name);
        this.name = name;
    }
    
    private synchronized native int open(final String p0) throws PortInUseException;
    
    public OutputStream getOutputStream() {
        return this.out;
    }
    
    public InputStream getInputStream() {
        return this.in;
    }
    
    public int getMode() {
        return this.lprmode;
    }
    
    public int setMode(final int n) throws UnsupportedCommOperationException {
        try {
            this.setLPRMode(n);
        }
        catch (UnsupportedCommOperationException ex) {
            ex.printStackTrace();
            return -1;
        }
        this.lprmode = n;
        return 0;
    }
    
    public void restart() {
        System.out.println("restart() is not implemented");
    }
    
    public void suspend() {
        System.out.println("suspend() is not implemented");
    }
    
    public native boolean setLPRMode(final int p0) throws UnsupportedCommOperationException;
    
    public native boolean isPaperOut();
    
    public native boolean isPrinterBusy();
    
    public native boolean isPrinterError();
    
    public native boolean isPrinterSelected();
    
    public native boolean isPrinterTimedOut();
    
    private native void nativeClose();
    
    public synchronized void close() {
        if (this.fd < 0) {
            return;
        }
        this.nativeClose();
        super.close();
        this.removeEventListener();
        this.fd = 0;
        Runtime.getRuntime().gc();
    }
    
    public void enableReceiveFraming(final int n) throws UnsupportedCommOperationException {
        throw new UnsupportedCommOperationException("Not supported");
    }
    
    public void disableReceiveFraming() {
    }
    
    public boolean isReceiveFramingEnabled() {
        return false;
    }
    
    public int getReceiveFramingByte() {
        return 0;
    }
    
    public void enableReceiveTimeout(final int timeout) {
        if (timeout > 0) {
            this.timeout = timeout;
        }
        else {
            this.timeout = 0;
        }
    }
    
    public void disableReceiveTimeout() {
        this.timeout = 0;
    }
    
    public boolean isReceiveTimeoutEnabled() {
        return this.timeout > 0;
    }
    
    public int getReceiveTimeout() {
        return this.timeout;
    }
    
    public void enableReceiveThreshold(final int threshold) {
        if (threshold > 1) {
            this.threshold = threshold;
        }
        else {
            this.threshold = 1;
        }
    }
    
    public void disableReceiveThreshold() {
        this.threshold = 1;
    }
    
    public int getReceiveThreshold() {
        return this.threshold;
    }
    
    public boolean isReceiveThresholdEnabled() {
        return this.threshold > 1;
    }
    
    public native void setInputBufferSize(final int p0);
    
    public native int getInputBufferSize();
    
    public native void setOutputBufferSize(final int p0);
    
    public native int getOutputBufferSize();
    
    public native int getOutputBufferFree();
    
    protected native void writeByte(final int p0) throws IOException;
    
    protected native void writeArray(final byte[] p0, final int p1, final int p2) throws IOException;
    
    protected native void drain() throws IOException;
    
    protected native int nativeavailable() throws IOException;
    
    protected native int readByte() throws IOException;
    
    protected native int readArray(final byte[] p0, final int p1, final int p2) throws IOException;
    
    native void eventLoop();
    
    public boolean checkMonitorThread() {
        return this.monThread == null || this.monThread.isInterrupted();
    }
    
    public synchronized boolean sendEvent(final int i, final boolean b) {
        if (this.fd == 0 || this.PPEventListener == null || this.monThread == null) {
            return true;
        }
        switch (i) {
            case 2: {
                if (this.monThread.monBuffer) {
                    break;
                }
                return false;
            }
            case 1: {
                if (this.monThread.monError) {
                    break;
                }
                return false;
            }
            default: {
                System.err.println("unknown event:" + i);
                return false;
            }
        }
        final ParallelPortEvent parallelPortEvent = new ParallelPortEvent(this, i, !b, b);
        if (this.PPEventListener != null) {
            this.PPEventListener.parallelEvent(parallelPortEvent);
        }
        if (this.fd == 0 || this.PPEventListener == null || this.monThread == null) {
            return true;
        }
        try {
            Thread.sleep(50L);
        }
        catch (Exception ex) {}
        return false;
    }
    
    public synchronized void addEventListener(final ParallelPortEventListener ppEventListener) throws TooManyListenersException {
        if (this.PPEventListener != null) {
            throw new TooManyListenersException();
        }
        this.PPEventListener = ppEventListener;
        (this.monThread = new MonitorThread()).start();
    }
    
    public synchronized void removeEventListener() {
        this.PPEventListener = null;
        if (this.monThread != null) {
            this.monThread.interrupt();
            this.monThread = null;
        }
    }
    
    public synchronized void notifyOnError(final boolean b) {
        System.out.println("notifyOnError is not implemented yet");
        this.monThread.monError = b;
    }
    
    public synchronized void notifyOnBuffer(final boolean b) {
        System.out.println("notifyOnBuffer is not implemented yet");
        this.monThread.monBuffer = b;
    }
    
    protected void finalize() {
        if (this.fd > 0) {
            this.close();
        }
    }
    
    static {
        System.loadLibrary("rxtxParallel");
        Initialize();
    }
    
    class ParallelOutputStream extends OutputStream
    {
        public synchronized void write(final int n) throws IOException {
            if (LPRPort.this.fd == 0) {
                throw new IOException();
            }
            LPRPort.this.writeByte(n);
        }
        
        public synchronized void write(final byte[] array) throws IOException {
            if (LPRPort.this.fd == 0) {
                throw new IOException();
            }
            LPRPort.this.writeArray(array, 0, array.length);
        }
        
        public synchronized void write(final byte[] array, final int n, final int n2) throws IOException {
            if (LPRPort.this.fd == 0) {
                throw new IOException();
            }
            LPRPort.this.writeArray(array, n, n2);
        }
        
        public synchronized void flush() throws IOException {
            if (LPRPort.this.fd == 0) {
                throw new IOException();
            }
        }
    }
    
    class ParallelInputStream extends InputStream
    {
        public int read() throws IOException {
            if (LPRPort.this.fd == 0) {
                throw new IOException();
            }
            return LPRPort.this.readByte();
        }
        
        public int read(final byte[] array) throws IOException {
            if (LPRPort.this.fd == 0) {
                throw new IOException();
            }
            return LPRPort.this.readArray(array, 0, array.length);
        }
        
        public int read(final byte[] array, final int n, final int n2) throws IOException {
            if (LPRPort.this.fd == 0) {
                throw new IOException();
            }
            return LPRPort.this.readArray(array, n, n2);
        }
        
        public int available() throws IOException {
            if (LPRPort.this.fd == 0) {
                throw new IOException();
            }
            return LPRPort.this.nativeavailable();
        }
    }
    
    class MonitorThread extends Thread
    {
        private boolean monError;
        private boolean monBuffer;
        
        MonitorThread() {
            this.monError = false;
            this.monBuffer = false;
        }
        
        public void run() {
            LPRPort.this.eventLoop();
            yield();
        }
    }
}
