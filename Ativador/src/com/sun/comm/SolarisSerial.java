// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.comm;

import javax.comm.SerialPortEvent;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.TooManyListenersException;
import java.io.IOException;
import javax.comm.UnsupportedCommOperationException;
import javax.comm.SerialPortEventListener;
import javax.comm.SerialPort;

class SolarisSerial extends SerialPort
{
    private int baud;
    private int databits;
    private int stopbits;
    private int parity;
    private int flowcontrol;
    private int port_fd;
    protected boolean portInError;
    private SerialPortEventListener listener;
    private SSCheckStatusThread CheckStatus;
    private SSInputStream inputStream;
    private SSOutputStream outputStream;
    private int rcvThreshold;
    private int rcvTimeout;
    byte[] tmprbuf;
    private boolean write_finished_flag;
    private Object write_finished_flag_lock;
    private SSReaderThread readerThread;
    private static final int STATUS_DTR = 1;
    private static final int STATUS_RTS = 2;
    private static final int STATUS_CTS = 4;
    private static final int STATUS_DSR = 8;
    private static final int STATUS_RI = 16;
    private static final int STATUS_CD = 32;
    private static final int STATUS_DATA_AVAIL = 1;
    private static final int STATUS_OUTPUTEMPTY = 2;
    private int notificationMask;
    private static final int NOTIFY_DataAvailable = 1;
    private static final int NOTIFY_OutputEmpty = 2;
    private static final int NOTIFY_CTS = 4;
    private static final int NOTIFY_DSR = 8;
    private static final int NOTIFY_RingIndicator = 16;
    private static final int NOTIFY_CarrierDetect = 32;
    private static final int NOTIFY_OverrunError = 64;
    private static final int NOTIFY_ParityError = 128;
    private static final int NOTIFY_FramingError = 256;
    private static final int NOTIFY_BreakInterrupt = 512;
    private int framing_byte;
    private int ibuffer_size;
    private int obuffer_size;
    
    private native void nWaitForEvent(final int p0);
    
    private native int nCheckBreak(final int p0);
    
    private native int nCheckParityErrors(final int p0);
    
    public SolarisSerial(final String name) throws IOException {
        this.baud = 9600;
        this.databits = 8;
        this.stopbits = 1;
        this.parity = 0;
        this.flowcontrol = 0;
        this.port_fd = 0;
        this.portInError = false;
        this.listener = null;
        this.CheckStatus = null;
        this.inputStream = null;
        this.outputStream = null;
        this.rcvThreshold = -1;
        this.rcvTimeout = -1;
        this.write_finished_flag = false;
        this.write_finished_flag_lock = new Object();
        this.readerThread = null;
        this.notificationMask = 0;
        this.framing_byte = -1;
        this.ibuffer_size = 1024;
        this.obuffer_size = 1024;
        this.name = name;
        if (this.port_fd != 0) {
            return;
        }
        this.port_fd = this.openNativePort(name);
        if (this.port_fd > 0) {
            try {
                this.setNativeSerialPortParams(this.port_fd, 1, this.baud, this.databits, this.stopbits, this.parity, this.flowcontrol);
            }
            catch (UnsupportedCommOperationException ex) {}
            this.tmprbuf = new byte[1];
            (this.readerThread = new SSReaderThread()).start();
            return;
        }
        throw new IOException(String.valueOf(-this.port_fd));
    }
    
    private native int nReaderThread(final int p0);
    
    protected void finalize() {
        this.close();
    }
    
    public int getBaudRate() {
        if (this.port_fd != 0) {
            return this.nativeGetBaudRate(this.port_fd);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public int getDataBits() {
        if (this.port_fd != 0) {
            return this.nativeGetDataBits(this.port_fd);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public int getStopBits() {
        if (this.port_fd != 0) {
            return this.nativeGetStopBits(this.port_fd);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public int getParity() {
        if (this.port_fd != 0) {
            return this.nativeGetParity(this.port_fd);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    private native int nativeGetBaudRate(final int p0);
    
    private native int nativeGetDataBits(final int p0);
    
    private native int nativeGetStopBits(final int p0);
    
    private native int nativeGetParity(final int p0);
    
    public void sendBreak(final int n) {
        if (this.port_fd != 0) {
            this.nativeSendBreak(this.port_fd);
            return;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    private native void nativeSendBreak(final int p0);
    
    public void setFlowControlMode(final int flowcontrol) throws UnsupportedCommOperationException {
        try {
            if (this.port_fd == 0) {
                throw new IllegalStateException("This port is closed");
            }
            if ((flowcontrol & 0x1) != 0x0) {
                if ((flowcontrol & 0x8) != 0x0 || (flowcontrol & 0x4) != 0x0) {
                    throw new UnsupportedCommOperationException("Cannot mix hardware and software flow control");
                }
            }
            else if ((flowcontrol & 0x2) != 0x0 && ((flowcontrol & 0x4) != 0x0 || (flowcontrol & 0x8) != 0x0)) {
                throw new UnsupportedCommOperationException("Cannot mix hardware and software flow control");
            }
            this.setNativeSerialPortParams(this.port_fd, 0, this.baud, this.databits, this.stopbits, this.parity, flowcontrol);
            this.flowcontrol = flowcontrol;
        }
        catch (UnsupportedCommOperationException ex) {
            ex.printStackTrace();
        }
    }
    
    public int getFlowControlMode() {
        if (this.port_fd != 0) {
            return this.nativeGetFlowctrl(this.port_fd);
        }
        throw new IllegalStateException("This port is closed");
    }
    
    private native int nativeGetFlowctrl(final int p0);
    
    public void setSerialPortParams(final int baud, final int databits, final int n, final int parity) throws UnsupportedCommOperationException {
        if (databits != 5 && databits != 6 && databits != 7 && databits != 8) {
            throw new UnsupportedCommOperationException("Unsupported number of databits");
        }
        if (n != 1 && n != 2) {
            throw new UnsupportedCommOperationException("Unsupported number of stopbits" + n);
        }
        if (parity != 2 && parity != 1 && parity != 0) {
            throw new UnsupportedCommOperationException("Unsupported parity value");
        }
        if (this.port_fd != 0) {
            this.setNativeSerialPortParams(this.port_fd, 0, baud, databits, n, parity, this.flowcontrol);
            this.baud = baud;
            this.databits = databits;
            this.stopbits = n;
            this.parity = parity;
            return;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    private native void setNativeSerialPortParams(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6) throws UnsupportedCommOperationException;
    
    public void setDTR(final boolean b) {
        if (this.port_fd != 0) {
            this.nativeSetDTR(this.port_fd, b);
            return;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public native void nativeSetDTR(final int p0, final boolean p1);
    
    public void setRTS(final boolean b) {
        if (this.port_fd == 0) {
            throw new IllegalStateException("This port is closed");
        }
        if ((this.flowcontrol & 0x1) == 0x1) {
            throw new IllegalStateException("Cannot modify RTS when Hardware flowcontrol is on.");
        }
        this.nativeSetRTS(this.port_fd, b);
    }
    
    public native void nativeSetRTS(final int p0, final boolean p1);
    
    public boolean isDTR() {
        if (this.port_fd != 0) {
            return (this.getStatusFlags(this.port_fd) & 0x1) > 0;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isRTS() {
        if (this.port_fd != 0) {
            return (this.getStatusFlags(this.port_fd) & 0x2) > 0;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isCTS() {
        if (this.port_fd != 0) {
            return (this.getStatusFlags(this.port_fd) & 0x4) > 0;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isDSR() {
        if (this.port_fd != 0) {
            return (this.getStatusFlags(this.port_fd) & 0x8) > 0;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isRI() {
        if (this.port_fd != 0) {
            return (this.getStatusFlags(this.port_fd) & 0x10) > 0;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isCD() {
        if (this.port_fd != 0) {
            return (this.getStatusFlags(this.port_fd) & 0x20) > 0;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    private native int getStatusFlags(final int p0);
    
    public void addEventListener(final SerialPortEventListener listener) throws TooManyListenersException {
        if (this.port_fd == 0) {
            throw new IllegalStateException("This port is closed");
        }
        if (this.listener != null) {
            throw new TooManyListenersException("Only one listener is allowed");
        }
        this.listener = listener;
        if (this.CheckStatus == null) {
            this.CheckStatus = new SSCheckStatusThread(this, listener);
        }
        this.CheckStatus.start();
    }
    
    public void removeEventListener() {
        this.listener = null;
        this.CheckStatus.stop();
    }
    
    public void notifyOnDataAvailable(final boolean b) {
        this.notificationMask &= 0xFFFFFFFE;
        if (b) {
            this.notificationMask |= 0x1;
        }
    }
    
    public void notifyOnOutputEmpty(final boolean b) {
        this.notificationMask &= 0xFFFFFFFD;
        if (b) {
            this.notificationMask |= 0x2;
        }
    }
    
    public void notifyOnCTS(final boolean b) {
        this.notificationMask &= 0xFFFFFFFB;
        if (b) {
            this.notificationMask |= 0x4;
        }
    }
    
    public void notifyOnDSR(final boolean b) {
        this.notificationMask &= 0xFFFFFFF7;
        if (b) {
            this.notificationMask |= 0x8;
        }
    }
    
    public void notifyOnRingIndicator(final boolean b) {
        this.notificationMask &= 0xFFFFFFEF;
        if (b) {
            this.notificationMask |= 0x10;
        }
    }
    
    public void notifyOnCarrierDetect(final boolean b) {
        this.notificationMask &= 0xFFFFFFDF;
        if (b) {
            this.notificationMask |= 0x20;
        }
    }
    
    public void notifyOnOverrunError(final boolean b) {
        this.notificationMask &= 0xFFFFFFBF;
        if (b) {
            this.notificationMask |= 0x40;
        }
    }
    
    public void notifyOnParityError(final boolean b) {
        this.notificationMask &= 0xFFFFFF7F;
        if (b) {
            this.notificationMask |= 0x80;
        }
    }
    
    public void notifyOnFramingError(final boolean b) {
        this.notificationMask &= 0xFFFFFEFF;
        if (b) {
            this.notificationMask |= 0x100;
        }
    }
    
    public void notifyOnBreakInterrupt(final boolean b) {
        this.notificationMask &= 0xFFFFFDFF;
        if (b) {
            this.notificationMask |= 0x200;
        }
    }
    
    public InputStream getInputStream() throws IOException {
        if (this.port_fd != 0) {
            if (this.inputStream == null) {
                this.inputStream = new SSInputStream();
            }
            return this.inputStream;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public OutputStream getOutputStream() throws IOException {
        if (this.port_fd != 0) {
            if (this.outputStream == null) {
                this.outputStream = new SSOutputStream();
            }
            return this.outputStream;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public void close() {
        if (this.port_fd == 0) {
            return;
        }
        try {
            if (this.port_fd > 0) {
                this.closeNativePort(this.port_fd);
                this.port_fd = 0;
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        super.close();
    }
    
    private native int openNativePort(final String p0) throws IOException;
    
    private native void closeNativePort(final int p0) throws IOException;
    
    private native int available(final int p0) throws IOException;
    
    private native int read(final int p0, final byte[] p1, final int p2, final int p3, final int p4, final int p5) throws IOException;
    
    private native int write(final int p0, final byte[] p1, final int p2, final int p3) throws IOException;
    
    private native int flush(final int p0);
    
    public void enableReceiveThreshold(final int rcvThreshold) throws UnsupportedCommOperationException {
        if (this.port_fd == 0) {
            throw new IllegalStateException("This port is closed");
        }
        if (rcvThreshold < 0) {
            throw new UnsupportedCommOperationException("This threshold value is not supported");
        }
        this.rcvThreshold = rcvThreshold;
    }
    
    public void disableReceiveThreshold() {
        if (this.port_fd == 0) {
            throw new IllegalStateException("This port is closed");
        }
        this.rcvThreshold = -1;
    }
    
    public boolean isReceiveThresholdEnabled() {
        return this.rcvThreshold >= 0;
    }
    
    public int getReceiveThreshold() {
        return this.rcvThreshold;
    }
    
    public void enableReceiveTimeout(final int rcvTimeout) throws UnsupportedCommOperationException {
        if (this.port_fd == 0) {
            throw new IllegalStateException("This port is closed");
        }
        if (rcvTimeout < 0) {
            throw new UnsupportedCommOperationException("This timeout value is not supported");
        }
        this.rcvTimeout = rcvTimeout;
    }
    
    public void disableReceiveTimeout() {
        if (this.port_fd == 0) {
            throw new IllegalStateException("This port is closed");
        }
        this.rcvTimeout = -1;
    }
    
    public boolean isReceiveTimeoutEnabled() {
        return this.rcvTimeout > 0;
    }
    
    public int getReceiveTimeout() {
        return this.rcvTimeout;
    }
    
    private native void nSetFramingByte(final int p0, final int p1);
    
    public void disableReceiveFraming() {
        this.framing_byte = -1;
        if (this.port_fd != 0) {
            this.nSetFramingByte(this.port_fd, this.framing_byte);
            return;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public void enableReceiveFraming(final int n) throws UnsupportedCommOperationException {
        if (this.port_fd != 0) {
            this.framing_byte = (n & 0xFF);
            this.nSetFramingByte(this.port_fd, this.framing_byte);
            return;
        }
        throw new IllegalStateException("This port is closed");
    }
    
    public boolean isReceiveFramingEnabled() {
        return this.framing_byte != -1;
    }
    
    public int getReceiveFramingByte() {
        return this.framing_byte;
    }
    
    public void setInputBufferSize(final int ibuffer_size) {
        if (this.port_fd == 0) {
            throw new IllegalStateException("This port is closed");
        }
        this.ibuffer_size = ibuffer_size;
    }
    
    public int getInputBufferSize() {
        return this.ibuffer_size;
    }
    
    public void setOutputBufferSize(final int obuffer_size) {
        if (this.port_fd == 0) {
            throw new IllegalStateException("This port is closed");
        }
        this.obuffer_size = obuffer_size;
    }
    
    public int getOutputBufferSize() {
        return this.obuffer_size;
    }
    
    public class SSInputStream extends InputStream
    {
        private byte[] buf;
        private int pos;
        
        public SSInputStream() {
            this.buf = null;
            this.pos = -1;
        }
        
        public int available() throws IOException {
            if (SolarisSerial.this.portInError) {
                return Integer.MAX_VALUE;
            }
            if (SolarisSerial.this.port_fd != 0) {
                return SolarisSerial.this.available(SolarisSerial.this.port_fd);
            }
            throw new IOException("This port is closed");
        }
        
        public int read(byte[] array, final int n, final int n2) throws IOException {
            int n3 = n2;
            if (array == null) {
                array = new byte[n2 + n];
            }
            if (n2 + n > array.length) {
                n3 = array.length - n;
            }
            if (SolarisSerial.this.port_fd != 0) {
                return SolarisSerial.this.read(SolarisSerial.this.port_fd, array, n, n3, SolarisSerial.this.rcvTimeout, SolarisSerial.this.rcvThreshold);
            }
            throw new IOException("This port is closed");
        }
        
        void hexdump(final byte[] array, final int i) {
            int j = 0;
            System.out.println("\nDumping " + i + " bytes from input buffer:\n");
            int n = 0;
            if (i % 16 != 0) {
                n = 1;
            }
            for (int k = 0; k < i / 16 + n; ++k) {
                String str = Integer.toHexString(j);
                switch (str.length()) {
                    case 0: {
                        str = "0000" + str;
                        break;
                    }
                    case 1: {
                        str = "000" + str;
                        break;
                    }
                    case 2: {
                        str = "00" + str;
                        break;
                    }
                    case 3: {
                        str = "0" + str;
                        break;
                    }
                }
                System.out.print(str + ": ");
                for (int l = 0; l < 16; ++l) {
                    String s;
                    if (l >= i - k * 16) {
                        s = "00";
                    }
                    else {
                        s = Integer.toHexString(array[l + j]);
                    }
                    if (s.length() < 2) {
                        s = "0" + s;
                    }
                    else if (s.length() > 2) {
                        s = s.substring(6);
                    }
                    System.out.print(s + " ");
                    if ((l + 1) % 8 == 0) {
                        System.out.print(" ");
                    }
                }
                for (int n2 = 0; n2 < 16; ++n2) {
                    if (n2 >= i - k * 16) {
                        System.out.print('.');
                    }
                    else {
                        char c = (char)array[n2 + j];
                        final byte b = array[n2 + j];
                        if (b < 32 || b > 127) {
                            c = '.';
                        }
                        System.out.print(c);
                    }
                }
                System.out.println("");
                j += 16;
            }
            System.out.println("");
        }
        
        public int read() throws IOException {
            synchronized (this) {
                if (SolarisSerial.this.portInError) {
                    this.close();
                    throw new IOException("Port closed due to error");
                }
            }
            if (SolarisSerial.this.port_fd == 0) {
                throw new IOException("This port is closed");
            }
            if (SolarisSerial.this.read(SolarisSerial.this.port_fd, SolarisSerial.this.tmprbuf, 0, 1, SolarisSerial.this.rcvTimeout, SolarisSerial.this.rcvThreshold) == 1) {
                return SolarisSerial.this.tmprbuf[0] & 0xFF;
            }
            return -1;
        }
    }
    
    public class SSOutputStream extends OutputStream
    {
        private boolean open;
        
        public SSOutputStream() {
            this.open = true;
        }
        
        public void close() {
            this.open = false;
        }
        
        public void write(final int n) throws IOException {
            this.write(new byte[] { (byte)n }, 0, 1);
        }
        
        public void write(final byte[] array, final int n, final int n2) throws IOException {
            int n3 = n2;
            if (!this.open) {
                throw new IOException("This output stream is closed");
            }
            if (SolarisSerial.this.port_fd != 0) {
                if (array != null) {
                    if (n2 + n > array.length) {
                        n3 = array.length - n;
                    }
                    SolarisSerial.this.write(SolarisSerial.this.port_fd, array, n, n3);
                    synchronized (SolarisSerial.this.write_finished_flag_lock) {
                        SolarisSerial.this.write_finished_flag = true;
                    }
                }
                return;
            }
            throw new IOException("This port is closed");
        }
        
        public void flush() throws IOException {
            if (!this.open) {
                throw new IOException("This output stream is closed");
            }
            if (SolarisSerial.this.port_fd == 0) {
                throw new IOException("This port is closed");
            }
            if (SolarisSerial.this.flush(SolarisSerial.this.port_fd) == -1) {
                throw new IOException("failed to flushoutput stream");
            }
        }
    }
    
    public class SSCheckStatusThread extends Thread
    {
        private boolean lastCD;
        private boolean lastRI;
        private boolean lastCTS;
        private boolean lastDSR;
        private boolean lastOE;
        private boolean lastPE;
        private boolean lastFE;
        private boolean lastBI;
        private SolarisSerial SSport;
        
        SSCheckStatusThread(final SolarisSerial sSport, final SerialPortEventListener serialPortEventListener) {
            this.SSport = sSport;
        }
        
        public void run() {
            this.lastCD = SolarisSerial.this.isCD();
            this.lastRI = SolarisSerial.this.isRI();
            this.lastDSR = SolarisSerial.this.isDSR();
            this.lastCTS = SolarisSerial.this.isCTS();
            while (SolarisSerial.this.port_fd != 0) {
                synchronized (SolarisSerial.this) {
                    if (SolarisSerial.this.portInError) {
                        SolarisSerial.this.close();
                        this.RaiseEvent(SolarisSerial.this.notificationMask, 1, this.SSport, 1, false, true);
                        break;
                    }
                }
                SolarisSerial.this.nWaitForEvent(SolarisSerial.this.port_fd);
                if (SolarisSerial.this.port_fd == 0) {
                    break;
                }
                try {
                    if (SolarisSerial.this.available(SolarisSerial.this.port_fd) > 0) {
                        this.RaiseEvent(SolarisSerial.this.notificationMask, 1, this.SSport, 1, false, true);
                    }
                }
                catch (IOException obj) {
                    System.err.println(obj + " trying to send data avail ev");
                }
                if (SolarisSerial.this.port_fd == 0) {
                    break;
                }
                synchronized (SolarisSerial.this.write_finished_flag_lock) {
                    if (SolarisSerial.this.write_finished_flag) {
                        SolarisSerial.this.write_finished_flag = false;
                        this.RaiseEvent(SolarisSerial.this.notificationMask, 2, this.SSport, 2, false, true);
                    }
                }
                if (SolarisSerial.this.port_fd == 0) {
                    break;
                }
                int access$1100;
                if ((access$1100 = SolarisSerial.this.nCheckBreak(SolarisSerial.this.port_fd)) > 0) {
                    while (access$1100-- > 0) {
                        this.RaiseEvent(SolarisSerial.this.notificationMask, 512, this.SSport, 10, false, true);
                        if (SolarisSerial.this.port_fd == 0) {
                            break;
                        }
                    }
                }
                if (SolarisSerial.this.port_fd == 0) {
                    break;
                }
                int access$1101;
                if ((access$1101 = SolarisSerial.this.nCheckParityErrors(SolarisSerial.this.port_fd)) > 0) {
                    while (access$1101-- > 0) {
                        this.RaiseEvent(SolarisSerial.this.notificationMask, 128, this.SSport, 8, false, true);
                        if (SolarisSerial.this.port_fd == 0) {
                            break;
                        }
                    }
                }
                if (SolarisSerial.this.port_fd == 0) {
                    break;
                }
                final int access$1102 = SolarisSerial.this.getStatusFlags(SolarisSerial.this.port_fd);
                final boolean lastCD = (access$1102 & 0x20) > 0;
                final boolean lastDSR = (access$1102 & 0x8) > 0;
                final boolean lastCTS = (access$1102 & 0x4) > 0;
                final boolean lastRI = (access$1102 & 0x10) > 0;
                if (lastCD != this.lastCD) {
                    this.RaiseEvent(SolarisSerial.this.notificationMask, 32, this.SSport, 6, this.lastCD, lastCD);
                }
                if (SolarisSerial.this.port_fd == 0) {
                    break;
                }
                if (lastDSR != this.lastDSR) {
                    this.RaiseEvent(SolarisSerial.this.notificationMask, 8, this.SSport, 4, this.lastDSR, lastDSR);
                }
                if (SolarisSerial.this.port_fd == 0) {
                    break;
                }
                if (lastCTS != this.lastCTS) {
                    this.RaiseEvent(SolarisSerial.this.notificationMask, 4, this.SSport, 3, this.lastCTS, lastCTS);
                }
                if (SolarisSerial.this.port_fd == 0) {
                    break;
                }
                if (lastRI != this.lastRI) {
                    this.RaiseEvent(SolarisSerial.this.notificationMask, 16, this.SSport, 5, this.lastRI, lastRI);
                }
                if (SolarisSerial.this.port_fd == 0) {
                    break;
                }
                this.lastCD = lastCD;
                this.lastDSR = lastDSR;
                this.lastCTS = lastCTS;
                this.lastRI = lastRI;
            }
        }
        
        private void RaiseEvent(final int n, final int n2, final SolarisSerial solarisSerial, final int n3, final boolean b, final boolean b2) {
            if (SolarisSerial.this.listener != null && b != b2 && (n & n2) != 0x0) {
                SolarisSerial.this.listener.serialEvent(new SerialPortEvent(solarisSerial, n3, b, b2));
            }
        }
    }
    
    private class SSReaderThread extends Thread
    {
        public void run() {
            if (SolarisSerial.this.nReaderThread(SolarisSerial.this.port_fd) < 0) {
                synchronized (SolarisSerial.this) {
                    SolarisSerial.this.portInError = true;
                }
            }
        }
    }
}
