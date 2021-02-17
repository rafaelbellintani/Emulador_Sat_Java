// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.comm;

import java.io.OutputStream;
import java.io.InputStream;
import javax.comm.ParallelPortEvent;
import java.util.TooManyListenersException;
import javax.comm.UnsupportedCommOperationException;
import java.io.IOException;
import javax.comm.ParallelPortEventListener;
import javax.comm.ParallelPort;

class SolarisParallel extends ParallelPort
{
    private int port_mode;
    private ParallelPortEventListener listener;
    private int rcv_timeout;
    private int rcv_threshold;
    private int framing_byte;
    private int ibuffer_size;
    private int obuffer_size;
    private int deviceType;
    public static final int LPT_MODE_ANY = 0;
    public static final int LPT_MODE_SPP = 1;
    public static final int LPT_MODE_PS2 = 2;
    public static final int LPT_MODE_EPP = 3;
    public static final int LPT_MODE_ECP = 4;
    private static final int STATUS_PaperOut = 1;
    private static final int STATUS_PrtBusy = 2;
    private static final int STATUS_PrtSelected = 4;
    private static final int STATUS_PrtTimedOut = 8;
    private static final int STATUS_PrtError = 16;
    private SPInputStream inputStream;
    private SPOutputStream outputStream;
    private int notificationMask;
    private static final int NOTIFY_ERR = 1;
    private static final int NOTIFY_BUF = 2;
    private int port_fd;
    
    public SolarisParallel(final String name) throws IOException {
        this.port_mode = 1;
        this.listener = null;
        this.rcv_timeout = 0;
        this.rcv_threshold = 0;
        this.framing_byte = -1;
        this.ibuffer_size = 1024;
        this.obuffer_size = 1024;
        this.deviceType = 0;
        this.inputStream = null;
        this.outputStream = null;
        this.notificationMask = 0;
        this.port_fd = 0;
        this.name = name;
        if (this.port_fd != 0) {
            return;
        }
        this.port_fd = this.openNativePort(name);
        if (this.port_fd < 0) {
            throw new IOException("port can not be opened.");
        }
    }
    
    private int getDeviceType() {
        return this.deviceType;
    }
    
    private void setDeviceType(final int deviceType) {
        this.deviceType = deviceType;
    }
    
    public int getOutputBufferFree() {
        return 512;
    }
    
    public int setMode(final int n) throws UnsupportedCommOperationException {
        switch (n) {
            case 0: {
                this.port_mode = 1;
                break;
            }
            case 1: {
                this.port_mode = 1;
                break;
            }
            case 2: {
                this.port_mode = 2;
                break;
            }
            default: {
                throw new UnsupportedCommOperationException("mode not supported");
            }
        }
        return this.port_mode;
    }
    
    public int getMode() {
        return this.port_mode;
    }
    
    public void suspend() {
    }
    
    public void restart() {
    }
    
    private native void setNativeTimeout(final int p0, final int p1);
    
    public boolean isPaperOut() {
        if (this.port_fd > 0) {
            return this.getStatusFlags(this.port_fd, 1);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isPrinterBusy() {
        if (this.port_fd > 0) {
            return this.getStatusFlags(this.port_fd, 2);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isPrinterSelected() {
        if (this.port_fd > 0) {
            return this.getStatusFlags(this.port_fd, 4);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isPrinterTimedOut() {
        if (this.port_fd > 0) {
            return this.getStatusFlags(this.port_fd, 8);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isPrinterError() {
        if (this.port_fd > 0) {
            return this.getStatusFlags(this.port_fd, 16);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    private native boolean getStatusFlags(final int p0, final int p1);
    
    public void addEventListener(final ParallelPortEventListener listener) throws TooManyListenersException {
        if (this.port_fd <= 0) {
            throw new IllegalStateException("This port is closed");
        }
        if (this.listener != null) {
            throw new TooManyListenersException("only one listener is allowed");
        }
        this.listener = listener;
    }
    
    public void removeEventListener() {
        this.listener = null;
    }
    
    private void raiseEvent(final int n, final boolean b, final boolean b2) {
        if (this.listener != null && (this.notificationMask & n) > 0) {
            int n2 = 0;
            switch (n) {
                case 1: {
                    n2 = 1;
                    break;
                }
                case 2: {
                    n2 = 2;
                    break;
                }
                default: {
                    return;
                }
            }
            this.listener.parallelEvent(new ParallelPortEvent(this, n2, b, b2));
        }
    }
    
    public void notifyOnError(final boolean b) {
        this.notificationMask &= 0xFFFFFFFE;
        if (b) {
            this.notificationMask |= 0x1;
        }
    }
    
    public void notifyOnBuffer(final boolean b) {
        this.notificationMask &= 0xFFFFFFFD;
        if (b) {
            this.notificationMask |= 0x2;
        }
    }
    
    public InputStream getInputStream() throws IOException {
        if (this.port_fd <= 0) {
            throw new IllegalStateException("This port is closed");
        }
        if (this.port_mode == 2) {
            if (this.inputStream == null) {
                this.inputStream = new SPInputStream();
            }
            return this.inputStream;
        }
        throw new IOException("This port is not in bidirectional mode");
    }
    
    public OutputStream getOutputStream() throws IOException {
        if (this.port_fd > 0) {
            if (this.outputStream == null) {
                this.outputStream = new SPOutputStream(this);
            }
            return this.outputStream;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public void close() {
        try {
            if (this.port_fd <= 0) {
                throw new IllegalStateException("This port is closed");
            }
            this.closeNativePort(this.port_fd);
            this.port_fd = 0;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        super.close();
    }
    
    private native int openNativePort(final String p0);
    
    private native void closeNativePort(final int p0) throws IOException;
    
    private native int available(final int p0) throws IOException;
    
    private native byte readByte(final int p0) throws IOException;
    
    private native int read(final int p0, final byte[] p1, final int p2, final int p3) throws IOException;
    
    private native int write(final int p0, final byte[] p1, final int p2, final int p3) throws IOException;
    
    public void enableReceiveThreshold(final int n) throws UnsupportedCommOperationException {
        if (this.port_mode == 2) {
            throw new UnsupportedCommOperationException("Receive threshold is not supported in Solaris parallel port");
        }
        throw new IllegalStateException("This port is not in bidirectional mode");
    }
    
    public void disableReceiveThreshold() {
        if (this.port_mode == 2) {
            this.rcv_threshold = 0;
            return;
        }
        throw new IllegalStateException("This port is not in bidirectional mode");
    }
    
    public boolean isReceiveThresholdEnabled() {
        return false;
    }
    
    public int getReceiveThreshold() {
        return this.rcv_threshold;
    }
    
    public void enableReceiveTimeout(final int rcv_timeout) {
        if (this.port_mode != 2) {
            throw new IllegalStateException("The port mode is not bidirectional");
        }
        this.rcv_timeout = rcv_timeout;
        if (this.port_fd > 0) {
            this.setNativeTimeout(this.port_fd, this.rcv_timeout * 1000);
            return;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public void disableReceiveTimeout() {
        if (this.port_mode != 2) {
            throw new IllegalStateException("The port mode is not bidirectional");
        }
        this.rcv_timeout = 0;
        if (this.port_fd > 0) {
            this.setNativeTimeout(this.port_fd, this.rcv_timeout);
            return;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isReceiveTimeoutEnabled() {
        return this.rcv_timeout > 0;
    }
    
    public int getReceiveTimeout() {
        return this.rcv_timeout;
    }
    
    public void disableReceiveFraming() {
        if (this.port_mode == 2) {
            this.framing_byte = -1;
            return;
        }
        throw new IllegalStateException("This port is not in bidirectional mode");
    }
    
    public void enableReceiveFraming(final int n) throws UnsupportedCommOperationException {
        if (this.port_mode == 2) {
            this.framing_byte = -1;
            throw new UnsupportedCommOperationException("Receive framing is not supported in Solaris parallel port");
        }
        throw new IllegalStateException("This port is not in bidirectional mode");
    }
    
    public boolean isReceiveFramingEnabled() {
        return false;
    }
    
    public int getReceiveFramingByte() {
        return this.framing_byte;
    }
    
    public void setInputBufferSize(final int ibuffer_size) {
        this.ibuffer_size = ibuffer_size;
    }
    
    public int getInputBufferSize() {
        return this.ibuffer_size;
    }
    
    public void setOutputBufferSize(final int obuffer_size) {
        this.obuffer_size = obuffer_size;
    }
    
    public int getOutputBufferSize() {
        return this.obuffer_size;
    }
    
    static {
        System.loadLibrary("SolarisSerialParallel");
    }
    
    public class SPInputStream extends InputStream
    {
        private byte[] buf;
        private int pos;
        private int high;
        
        public SPInputStream() {
            this.buf = null;
            this.pos = -1;
            this.high = 0;
        }
        
        public int available() throws IOException {
            if (SolarisParallel.this.port_fd != 0) {
                return SolarisParallel.this.available(SolarisParallel.this.port_fd);
            }
            throw new IllegalStateException("This port is closed");
        }
        
        public int read(byte[] array, final int n, final int n2) throws IOException {
            int n3 = n2;
            if (array == null) {
                array = new byte[n2 + n];
            }
            if (n2 + n > array.length) {
                n3 = array.length - n;
            }
            try {
                if (SolarisParallel.this.port_fd > 0) {
                    return SolarisParallel.this.read(SolarisParallel.this.port_fd, array, n, n3);
                }
                throw new IllegalStateException("Port Closed");
            }
            catch (IOException ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        
        public int read() throws IOException {
            if (SolarisParallel.this.port_fd > 0) {
                return SolarisParallel.this.readByte(SolarisParallel.this.port_fd);
            }
            throw new IllegalStateException("This port is closed");
        }
        
        void printbuf(final byte[] array) {
            String string = new String();
            for (int i = 0; i < array.length; ++i) {
                string = string + Integer.toHexString(array[i]) + " ";
            }
            System.out.println("\nhigh = " + this.high + "IN: " + string);
        }
    }
    
    public class SPOutputStream extends OutputStream
    {
        private SolarisParallel spPort;
        
        public SPOutputStream(final SolarisParallel spPort) {
            this.spPort = spPort;
        }
        
        public void write(final int n) throws IOException {
            final byte[] array = { (byte)n };
            if (SolarisParallel.this.port_fd <= 0) {
                throw new IllegalStateException("This port is closed");
            }
            if (SolarisParallel.this.write(SolarisParallel.this.port_fd, array, 0, 1) != 1) {
                throw new IOException("write to parallel port failed");
            }
        }
        
        public void write(final byte[] bytes, final int n, final int n2) throws IOException {
            int n3 = n2;
            final String s = new String(bytes);
            if (bytes != null) {
                if (n2 + n > bytes.length) {
                    n3 = bytes.length - n;
                }
                if (SolarisParallel.this.port_fd <= 0) {
                    throw new IllegalStateException("Port Closed");
                }
                if (this.spPort.write(SolarisParallel.this.port_fd, bytes, n, n3) != n3) {
                    throw new IOException("write to parallel port failed");
                }
            }
        }
    }
}
