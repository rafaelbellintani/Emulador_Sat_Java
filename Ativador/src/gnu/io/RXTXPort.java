// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.TooManyListenersException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class RXTXPort extends SerialPort
{
    protected static final boolean debug = false;
    protected static final boolean debug_read = false;
    protected static final boolean debug_read_results = false;
    protected static final boolean debug_write = false;
    protected static final boolean debug_events = false;
    protected static final boolean debug_verbose = false;
    private static Zystem z;
    boolean MonitorThreadAlive;
    int IOLocked;
    private int fd;
    long eis;
    int pid;
    static boolean dsrFlag;
    private final SerialOutputStream out;
    private final SerialInputStream in;
    private int speed;
    private int dataBits;
    private int stopBits;
    private int parity;
    private int flowmode;
    private int timeout;
    private int threshold;
    private int InputBuffer;
    private int OutputBuffer;
    private SerialPortEventListener SPEventListener;
    private MonitorThread monThread;
    boolean monThreadisInterrupted;
    boolean MonitorThreadLock;
    boolean closeLock;
    
    private static native void Initialize();
    
    public RXTXPort(final String name) throws PortInUseException {
        this.MonitorThreadAlive = false;
        this.IOLocked = 0;
        this.fd = 0;
        this.eis = 0L;
        this.pid = 0;
        this.out = new SerialOutputStream();
        this.in = new SerialInputStream();
        this.speed = 9600;
        this.dataBits = 8;
        this.stopBits = 1;
        this.parity = 0;
        this.flowmode = 0;
        this.threshold = 0;
        this.InputBuffer = 0;
        this.OutputBuffer = 0;
        this.monThreadisInterrupted = true;
        this.MonitorThreadLock = true;
        this.closeLock = false;
        this.fd = this.open(name);
        this.name = name;
        this.MonitorThreadLock = true;
        (this.monThread = new MonitorThread()).start();
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadAlive = true;
        this.timeout = -1;
    }
    
    private synchronized native int open(final String p0) throws PortInUseException;
    
    public OutputStream getOutputStream() {
        return this.out;
    }
    
    public InputStream getInputStream() {
        return this.in;
    }
    
    private native int nativeGetParity(final int p0);
    
    private native int nativeGetFlowControlMode(final int p0);
    
    public synchronized void setSerialPortParams(final int n, final int n2, final int n3, final int n4) throws UnsupportedCommOperationException {
        if (this.nativeSetSerialPortParams(n, n2, n3, n4)) {
            throw new UnsupportedCommOperationException("Invalid Parameter");
        }
        this.speed = n;
        if (n3 == 3) {
            this.dataBits = 5;
        }
        else {
            this.dataBits = n2;
        }
        this.stopBits = n3;
        this.parity = n4;
        RXTXPort.z.reportln("RXTXPort:setSerialPortParams(" + n + " " + n2 + " " + n3 + " " + n4 + ") returning");
    }
    
    private native boolean nativeSetSerialPortParams(final int p0, final int p1, final int p2, final int p3) throws UnsupportedCommOperationException;
    
    public int getBaudRate() {
        return this.speed;
    }
    
    public int getDataBits() {
        return this.dataBits;
    }
    
    public int getStopBits() {
        return this.stopBits;
    }
    
    public int getParity() {
        return this.parity;
    }
    
    public void setFlowControlMode(final int flowmode) {
        if (this.monThreadisInterrupted) {
            return;
        }
        try {
            this.setflowcontrol(flowmode);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        this.flowmode = flowmode;
    }
    
    public int getFlowControlMode() {
        return this.flowmode;
    }
    
    native void setflowcontrol(final int p0) throws IOException;
    
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
    
    public native int NativegetReceiveTimeout();
    
    private native boolean NativeisReceiveTimeoutEnabled();
    
    private native void NativeEnableReceiveTimeoutThreshold(final int p0, final int p1, final int p2);
    
    public void disableReceiveTimeout() {
        this.NativeEnableReceiveTimeoutThreshold(this.timeout = -1, this.threshold, this.InputBuffer);
    }
    
    public void enableReceiveTimeout(final int timeout) {
        if (timeout >= 0) {
            this.NativeEnableReceiveTimeoutThreshold(this.timeout = timeout, this.threshold, this.InputBuffer);
            return;
        }
        throw new IllegalArgumentException("Unexpected negative timeout value");
    }
    
    public boolean isReceiveTimeoutEnabled() {
        return this.NativeisReceiveTimeoutEnabled();
    }
    
    public int getReceiveTimeout() {
        return this.NativegetReceiveTimeout();
    }
    
    public void enableReceiveThreshold(final int threshold) {
        if (threshold >= 0) {
            this.threshold = threshold;
            this.NativeEnableReceiveTimeoutThreshold(this.timeout, this.threshold, this.InputBuffer);
            return;
        }
        throw new IllegalArgumentException("Unexpected negative threshold value");
    }
    
    public void disableReceiveThreshold() {
        this.enableReceiveThreshold(0);
    }
    
    public int getReceiveThreshold() {
        return this.threshold;
    }
    
    public boolean isReceiveThresholdEnabled() {
        return this.threshold > 0;
    }
    
    public void setInputBufferSize(final int inputBuffer) {
        if (inputBuffer < 0) {
            throw new IllegalArgumentException("Unexpected negative buffer size value");
        }
        this.InputBuffer = inputBuffer;
    }
    
    public int getInputBufferSize() {
        return this.InputBuffer;
    }
    
    public void setOutputBufferSize(final int outputBuffer) {
        if (outputBuffer < 0) {
            throw new IllegalArgumentException("Unexpected negative buffer size value");
        }
        this.OutputBuffer = outputBuffer;
    }
    
    public int getOutputBufferSize() {
        return this.OutputBuffer;
    }
    
    public native boolean isDTR();
    
    public native void setDTR(final boolean p0);
    
    public native void setRTS(final boolean p0);
    
    private native void setDSR(final boolean p0);
    
    public native boolean isCTS();
    
    public native boolean isDSR();
    
    public native boolean isCD();
    
    public native boolean isRI();
    
    public native boolean isRTS();
    
    public native void sendBreak(final int p0);
    
    protected native void writeByte(final int p0, final boolean p1) throws IOException;
    
    protected native void writeArray(final byte[] p0, final int p1, final int p2, final boolean p3) throws IOException;
    
    protected native boolean nativeDrain(final boolean p0) throws IOException;
    
    protected native int nativeavailable() throws IOException;
    
    protected native int readByte() throws IOException;
    
    protected native int readArray(final byte[] p0, final int p1, final int p2) throws IOException;
    
    protected native int readTerminatedArray(final byte[] p0, final int p1, final int p2, final byte[] p3) throws IOException;
    
    native void eventLoop();
    
    private native void interruptEventLoop();
    
    public boolean checkMonitorThread() {
        return this.monThread == null || this.monThreadisInterrupted;
    }
    
    public boolean sendEvent(final int i, final boolean b) {
        if (this.fd == 0 || this.SPEventListener == null || this.monThread == null) {
            return true;
        }
        switch (i) {
            case 1: {}
            case 2: {}
            case 3: {}
            case 4: {}
            case 5: {}
            case 6: {}
            case 7: {}
            case 8: {}
            case 9: {}
        }
        switch (i) {
            case 1: {
                if (this.monThread.Data) {
                    break;
                }
                return false;
            }
            case 2: {
                if (this.monThread.Output) {
                    break;
                }
                return false;
            }
            case 3: {
                if (this.monThread.CTS) {
                    break;
                }
                return false;
            }
            case 4: {
                if (this.monThread.DSR) {
                    break;
                }
                return false;
            }
            case 5: {
                if (this.monThread.RI) {
                    break;
                }
                return false;
            }
            case 6: {
                if (this.monThread.CD) {
                    break;
                }
                return false;
            }
            case 7: {
                if (this.monThread.OE) {
                    break;
                }
                return false;
            }
            case 8: {
                if (this.monThread.PE) {
                    break;
                }
                return false;
            }
            case 9: {
                if (this.monThread.FE) {
                    break;
                }
                return false;
            }
            case 10: {
                if (this.monThread.BI) {
                    break;
                }
                return false;
            }
            default: {
                System.err.println("unknown event: " + i);
                return false;
            }
        }
        final SerialPortEvent serialPortEvent = new SerialPortEvent(this, i, !b, b);
        if (this.monThreadisInterrupted) {
            return true;
        }
        if (this.SPEventListener != null) {
            this.SPEventListener.serialEvent(serialPortEvent);
        }
        return this.fd == 0 || this.SPEventListener == null || this.monThread == null;
    }
    
    public void addEventListener(final SerialPortEventListener spEventListener) throws TooManyListenersException {
        if (this.SPEventListener != null) {
            throw new TooManyListenersException();
        }
        this.SPEventListener = spEventListener;
        if (!this.MonitorThreadAlive) {
            this.MonitorThreadLock = true;
            (this.monThread = new MonitorThread()).start();
            this.waitForTheNativeCodeSilly();
            this.MonitorThreadAlive = true;
        }
    }
    
    public void removeEventListener() {
        this.waitForTheNativeCodeSilly();
        if (this.monThreadisInterrupted) {
            RXTXPort.z.reportln("\tRXTXPort:removeEventListener() already interrupted");
            this.monThread = null;
            this.SPEventListener = null;
            return;
        }
        if (this.monThread != null && this.monThread.isAlive()) {
            this.monThreadisInterrupted = true;
            this.interruptEventLoop();
            try {
                this.monThread.join(1000L);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            while (this.monThread.isAlive()) {
                try {
                    this.monThread.join(1000L);
                    Thread.sleep(1000L);
                }
                catch (Exception ex2) {}
            }
        }
        this.monThread = null;
        this.SPEventListener = null;
        this.MonitorThreadLock = false;
        this.MonitorThreadAlive = false;
        this.monThreadisInterrupted = true;
        RXTXPort.z.reportln("RXTXPort:removeEventListener() returning");
    }
    
    protected void waitForTheNativeCodeSilly() {
        while (this.MonitorThreadLock) {
            try {
                Thread.sleep(5L);
            }
            catch (Exception ex) {}
        }
    }
    
    private native void nativeSetEventFlag(final int p0, final int p1, final boolean p2);
    
    public void notifyOnDataAvailable(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 1, b);
        this.monThread.Data = b;
        this.MonitorThreadLock = false;
    }
    
    public void notifyOnOutputEmpty(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 2, b);
        this.monThread.Output = b;
        this.MonitorThreadLock = false;
    }
    
    public void notifyOnCTS(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 3, b);
        this.monThread.CTS = b;
        this.MonitorThreadLock = false;
    }
    
    public void notifyOnDSR(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 4, b);
        this.monThread.DSR = b;
        this.MonitorThreadLock = false;
    }
    
    public void notifyOnRingIndicator(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 5, b);
        this.monThread.RI = b;
        this.MonitorThreadLock = false;
    }
    
    public void notifyOnCarrierDetect(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 6, b);
        this.monThread.CD = b;
        this.MonitorThreadLock = false;
    }
    
    public void notifyOnOverrunError(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 7, b);
        this.monThread.OE = b;
        this.MonitorThreadLock = false;
    }
    
    public void notifyOnParityError(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 8, b);
        this.monThread.PE = b;
        this.MonitorThreadLock = false;
    }
    
    public void notifyOnFramingError(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 9, b);
        this.monThread.FE = b;
        this.MonitorThreadLock = false;
    }
    
    public void notifyOnBreakInterrupt(final boolean b) {
        this.waitForTheNativeCodeSilly();
        this.MonitorThreadLock = true;
        this.nativeSetEventFlag(this.fd, 10, b);
        this.monThread.BI = b;
        this.MonitorThreadLock = false;
    }
    
    private native void nativeClose(final String p0);
    
    public synchronized void close() {
        if (this.closeLock) {
            return;
        }
        this.closeLock = true;
        while (this.IOLocked > 0) {
            try {
                Thread.sleep(500L);
            }
            catch (Exception ex) {}
        }
        if (this.fd <= 0) {
            RXTXPort.z.reportln("RXTXPort:close detected bad File Descriptor");
            return;
        }
        this.setDTR(false);
        this.setDSR(false);
        if (!this.monThreadisInterrupted) {
            this.removeEventListener();
        }
        this.nativeClose(this.name);
        super.close();
        this.fd = 0;
        this.closeLock = false;
    }
    
    protected void finalize() {
        if (this.fd > 0) {
            this.close();
        }
        RXTXPort.z.finalize();
    }
    
    public void setRcvFifoTrigger(final int n) {
    }
    
    private static native void nativeStaticSetSerialPortParams(final String p0, final int p1, final int p2, final int p3, final int p4) throws UnsupportedCommOperationException;
    
    private static native boolean nativeStaticSetDSR(final String p0, final boolean p1) throws UnsupportedCommOperationException;
    
    private static native boolean nativeStaticSetDTR(final String p0, final boolean p1) throws UnsupportedCommOperationException;
    
    private static native boolean nativeStaticSetRTS(final String p0, final boolean p1) throws UnsupportedCommOperationException;
    
    private static native boolean nativeStaticIsDSR(final String p0) throws UnsupportedCommOperationException;
    
    private static native boolean nativeStaticIsDTR(final String p0) throws UnsupportedCommOperationException;
    
    private static native boolean nativeStaticIsRTS(final String p0) throws UnsupportedCommOperationException;
    
    private static native boolean nativeStaticIsCTS(final String p0) throws UnsupportedCommOperationException;
    
    private static native boolean nativeStaticIsCD(final String p0) throws UnsupportedCommOperationException;
    
    private static native boolean nativeStaticIsRI(final String p0) throws UnsupportedCommOperationException;
    
    private static native int nativeStaticGetBaudRate(final String p0) throws UnsupportedCommOperationException;
    
    private static native int nativeStaticGetDataBits(final String p0) throws UnsupportedCommOperationException;
    
    private static native int nativeStaticGetParity(final String p0) throws UnsupportedCommOperationException;
    
    private static native int nativeStaticGetStopBits(final String p0) throws UnsupportedCommOperationException;
    
    private native byte nativeGetParityErrorChar() throws UnsupportedCommOperationException;
    
    private native boolean nativeSetParityErrorChar(final byte p0) throws UnsupportedCommOperationException;
    
    private native byte nativeGetEndOfInputChar() throws UnsupportedCommOperationException;
    
    private native boolean nativeSetEndOfInputChar(final byte p0) throws UnsupportedCommOperationException;
    
    private native boolean nativeSetUartType(final String p0, final boolean p1) throws UnsupportedCommOperationException;
    
    native String nativeGetUartType() throws UnsupportedCommOperationException;
    
    private native boolean nativeSetBaudBase(final int p0) throws UnsupportedCommOperationException;
    
    private native int nativeGetBaudBase() throws UnsupportedCommOperationException;
    
    private native boolean nativeSetDivisor(final int p0) throws UnsupportedCommOperationException;
    
    private native int nativeGetDivisor() throws UnsupportedCommOperationException;
    
    private native boolean nativeSetLowLatency() throws UnsupportedCommOperationException;
    
    private native boolean nativeGetLowLatency() throws UnsupportedCommOperationException;
    
    private native boolean nativeSetCallOutHangup(final boolean p0) throws UnsupportedCommOperationException;
    
    private native boolean nativeGetCallOutHangup() throws UnsupportedCommOperationException;
    
    private native boolean nativeClearCommInput() throws UnsupportedCommOperationException;
    
    public static int staticGetBaudRate(final String s) throws UnsupportedCommOperationException {
        return nativeStaticGetBaudRate(s);
    }
    
    public static int staticGetDataBits(final String s) throws UnsupportedCommOperationException {
        return nativeStaticGetDataBits(s);
    }
    
    public static int staticGetParity(final String s) throws UnsupportedCommOperationException {
        return nativeStaticGetParity(s);
    }
    
    public static int staticGetStopBits(final String s) throws UnsupportedCommOperationException {
        return nativeStaticGetStopBits(s);
    }
    
    public static void staticSetSerialPortParams(final String s, final int n, final int n2, final int n3, final int n4) throws UnsupportedCommOperationException {
        nativeStaticSetSerialPortParams(s, n, n2, n3, n4);
    }
    
    public static boolean staticSetDSR(final String s, final boolean b) throws UnsupportedCommOperationException {
        return nativeStaticSetDSR(s, b);
    }
    
    public static boolean staticSetDTR(final String s, final boolean b) throws UnsupportedCommOperationException {
        return nativeStaticSetDTR(s, b);
    }
    
    public static boolean staticSetRTS(final String s, final boolean b) throws UnsupportedCommOperationException {
        return nativeStaticSetRTS(s, b);
    }
    
    public static boolean staticIsRTS(final String s) throws UnsupportedCommOperationException {
        return nativeStaticIsRTS(s);
    }
    
    public static boolean staticIsCD(final String s) throws UnsupportedCommOperationException {
        return nativeStaticIsCD(s);
    }
    
    public static boolean staticIsCTS(final String s) throws UnsupportedCommOperationException {
        return nativeStaticIsCTS(s);
    }
    
    public static boolean staticIsDSR(final String s) throws UnsupportedCommOperationException {
        return nativeStaticIsDSR(s);
    }
    
    public static boolean staticIsDTR(final String s) throws UnsupportedCommOperationException {
        return nativeStaticIsDTR(s);
    }
    
    public static boolean staticIsRI(final String s) throws UnsupportedCommOperationException {
        return nativeStaticIsRI(s);
    }
    
    public byte getParityErrorChar() throws UnsupportedCommOperationException {
        return this.nativeGetParityErrorChar();
    }
    
    public boolean setParityErrorChar(final byte b) throws UnsupportedCommOperationException {
        return this.nativeSetParityErrorChar(b);
    }
    
    public byte getEndOfInputChar() throws UnsupportedCommOperationException {
        return this.nativeGetEndOfInputChar();
    }
    
    public boolean setEndOfInputChar(final byte b) throws UnsupportedCommOperationException {
        return this.nativeSetEndOfInputChar(b);
    }
    
    public boolean setUARTType(final String s, final boolean b) throws UnsupportedCommOperationException {
        return this.nativeSetUartType(s, b);
    }
    
    public String getUARTType() throws UnsupportedCommOperationException {
        return this.nativeGetUartType();
    }
    
    public boolean setBaudBase(final int n) throws UnsupportedCommOperationException, IOException {
        return this.nativeSetBaudBase(n);
    }
    
    public int getBaudBase() throws UnsupportedCommOperationException, IOException {
        return this.nativeGetBaudBase();
    }
    
    public boolean setDivisor(final int n) throws UnsupportedCommOperationException, IOException {
        return this.nativeSetDivisor(n);
    }
    
    public int getDivisor() throws UnsupportedCommOperationException, IOException {
        return this.nativeGetDivisor();
    }
    
    public boolean setLowLatency() throws UnsupportedCommOperationException {
        return this.nativeSetLowLatency();
    }
    
    public boolean getLowLatency() throws UnsupportedCommOperationException {
        return this.nativeGetLowLatency();
    }
    
    public boolean setCallOutHangup(final boolean b) throws UnsupportedCommOperationException {
        return this.nativeSetCallOutHangup(b);
    }
    
    public boolean getCallOutHangup() throws UnsupportedCommOperationException {
        return this.nativeGetCallOutHangup();
    }
    
    public boolean clearCommInput() throws UnsupportedCommOperationException {
        return this.nativeClearCommInput();
    }
    
    static {
        try {
            RXTXPort.z = new Zystem();
        }
        catch (Exception ex) {}
        System.loadLibrary("rxtxSerial");
        Initialize();
        RXTXPort.dsrFlag = false;
    }
    
    class SerialOutputStream extends OutputStream
    {
        public void write(final int n) throws IOException {
            if (RXTXPort.this.speed == 0) {
                return;
            }
            if (RXTXPort.this.monThreadisInterrupted) {
                return;
            }
            final RXTXPort this$0 = RXTXPort.this;
            ++this$0.IOLocked;
            RXTXPort.this.waitForTheNativeCodeSilly();
            if (RXTXPort.this.fd == 0) {
                final RXTXPort this$2 = RXTXPort.this;
                --this$2.IOLocked;
                throw new IOException();
            }
            try {
                RXTXPort.this.writeByte(n, RXTXPort.this.monThreadisInterrupted);
            }
            catch (IOException ex) {
                final RXTXPort this$3 = RXTXPort.this;
                --this$3.IOLocked;
                throw ex;
            }
            final RXTXPort this$4 = RXTXPort.this;
            --this$4.IOLocked;
        }
        
        public void write(final byte[] array) throws IOException {
            if (RXTXPort.this.speed == 0) {
                return;
            }
            if (RXTXPort.this.monThreadisInterrupted) {
                return;
            }
            if (RXTXPort.this.fd == 0) {
                throw new IOException();
            }
            final RXTXPort this$0 = RXTXPort.this;
            ++this$0.IOLocked;
            RXTXPort.this.waitForTheNativeCodeSilly();
            try {
                RXTXPort.this.writeArray(array, 0, array.length, RXTXPort.this.monThreadisInterrupted);
            }
            catch (IOException ex) {
                final RXTXPort this$2 = RXTXPort.this;
                --this$2.IOLocked;
                throw ex;
            }
            final RXTXPort this$3 = RXTXPort.this;
            --this$3.IOLocked;
        }
        
        public void write(final byte[] array, final int n, final int n2) throws IOException {
            if (RXTXPort.this.speed == 0) {
                return;
            }
            if (n + n2 > array.length) {
                throw new IndexOutOfBoundsException("Invalid offset/length passed to read");
            }
            final byte[] array2 = new byte[n2];
            System.arraycopy(array, n, array2, 0, n2);
            if (RXTXPort.this.fd == 0) {
                throw new IOException();
            }
            if (RXTXPort.this.monThreadisInterrupted) {
                return;
            }
            final RXTXPort this$0 = RXTXPort.this;
            ++this$0.IOLocked;
            RXTXPort.this.waitForTheNativeCodeSilly();
            try {
                RXTXPort.this.writeArray(array2, 0, n2, RXTXPort.this.monThreadisInterrupted);
            }
            catch (IOException ex) {
                final RXTXPort this$2 = RXTXPort.this;
                --this$2.IOLocked;
                throw ex;
            }
            final RXTXPort this$3 = RXTXPort.this;
            --this$3.IOLocked;
        }
        
        public void flush() throws IOException {
            if (RXTXPort.this.speed == 0) {
                return;
            }
            if (RXTXPort.this.fd == 0) {
                throw new IOException();
            }
            if (RXTXPort.this.monThreadisInterrupted) {
                return;
            }
            final RXTXPort this$0 = RXTXPort.this;
            ++this$0.IOLocked;
            RXTXPort.this.waitForTheNativeCodeSilly();
            try {
                if (RXTXPort.this.nativeDrain(RXTXPort.this.monThreadisInterrupted)) {
                    RXTXPort.this.sendEvent(2, true);
                }
            }
            catch (IOException ex) {
                final RXTXPort this$2 = RXTXPort.this;
                --this$2.IOLocked;
                throw ex;
            }
            final RXTXPort this$3 = RXTXPort.this;
            --this$3.IOLocked;
        }
    }
    
    class SerialInputStream extends InputStream
    {
        public synchronized int read() throws IOException {
            if (RXTXPort.this.fd == 0) {
                throw new IOException();
            }
            if (RXTXPort.this.monThreadisInterrupted) {
                RXTXPort.z.reportln("+++++++++ read() monThreadisInterrupted");
            }
            final RXTXPort this$0 = RXTXPort.this;
            ++this$0.IOLocked;
            RXTXPort.this.waitForTheNativeCodeSilly();
            try {
                return RXTXPort.this.readByte();
            }
            finally {
                final RXTXPort this$2 = RXTXPort.this;
                --this$2.IOLocked;
            }
        }
        
        public synchronized int read(final byte[] array) throws IOException {
            if (RXTXPort.this.monThreadisInterrupted) {
                return 0;
            }
            final RXTXPort this$0 = RXTXPort.this;
            ++this$0.IOLocked;
            RXTXPort.this.waitForTheNativeCodeSilly();
            try {
                return this.read(array, 0, array.length);
            }
            finally {
                final RXTXPort this$2 = RXTXPort.this;
                --this$2.IOLocked;
            }
        }
        
        public synchronized int read(final byte[] array, final int n, final int n2) throws IOException {
            if (RXTXPort.this.fd == 0) {
                RXTXPort.z.reportln("+++++++ IOException()\n");
                throw new IOException();
            }
            if (array == null) {
                RXTXPort.z.reportln("+++++++ NullPointerException()\n");
                throw new NullPointerException();
            }
            if (n < 0 || n2 < 0 || n + n2 > array.length) {
                RXTXPort.z.reportln("+++++++ IndexOutOfBoundsException()\n");
                throw new IndexOutOfBoundsException();
            }
            if (n2 == 0) {
                return 0;
            }
            int n3;
            if (RXTXPort.this.threshold == 0) {
                final int nativeavailable = RXTXPort.this.nativeavailable();
                if (nativeavailable == 0) {
                    n3 = 1;
                }
                else {
                    n3 = Math.min(n2, nativeavailable);
                }
            }
            else {
                n3 = Math.min(n2, RXTXPort.this.threshold);
            }
            if (RXTXPort.this.monThreadisInterrupted) {
                return 0;
            }
            final RXTXPort this$0 = RXTXPort.this;
            ++this$0.IOLocked;
            RXTXPort.this.waitForTheNativeCodeSilly();
            try {
                return RXTXPort.this.readArray(array, n, n3);
            }
            finally {
                final RXTXPort this$2 = RXTXPort.this;
                --this$2.IOLocked;
            }
        }
        
        public synchronized int read(final byte[] array, final int n, final int n2, final byte[] array2) throws IOException {
            if (RXTXPort.this.fd == 0) {
                RXTXPort.z.reportln("+++++++ IOException()\n");
                throw new IOException();
            }
            if (array == null) {
                RXTXPort.z.reportln("+++++++ NullPointerException()\n");
                throw new NullPointerException();
            }
            if (n < 0 || n2 < 0 || n + n2 > array.length) {
                RXTXPort.z.reportln("+++++++ IndexOutOfBoundsException()\n");
                throw new IndexOutOfBoundsException();
            }
            if (n2 == 0) {
                return 0;
            }
            int n3;
            if (RXTXPort.this.threshold == 0) {
                final int nativeavailable = RXTXPort.this.nativeavailable();
                if (nativeavailable == 0) {
                    n3 = 1;
                }
                else {
                    n3 = Math.min(n2, nativeavailable);
                }
            }
            else {
                n3 = Math.min(n2, RXTXPort.this.threshold);
            }
            if (RXTXPort.this.monThreadisInterrupted) {
                return 0;
            }
            final RXTXPort this$0 = RXTXPort.this;
            ++this$0.IOLocked;
            RXTXPort.this.waitForTheNativeCodeSilly();
            try {
                return RXTXPort.this.readTerminatedArray(array, n, n3, array2);
            }
            finally {
                final RXTXPort this$2 = RXTXPort.this;
                --this$2.IOLocked;
            }
        }
        
        public synchronized int available() throws IOException {
            if (RXTXPort.this.monThreadisInterrupted) {
                return 0;
            }
            final RXTXPort this$0 = RXTXPort.this;
            ++this$0.IOLocked;
            try {
                return RXTXPort.this.nativeavailable();
            }
            finally {
                final RXTXPort this$2 = RXTXPort.this;
                --this$2.IOLocked;
            }
        }
    }
    
    class MonitorThread extends Thread
    {
        private volatile boolean CTS;
        private volatile boolean DSR;
        private volatile boolean RI;
        private volatile boolean CD;
        private volatile boolean OE;
        private volatile boolean PE;
        private volatile boolean FE;
        private volatile boolean BI;
        private volatile boolean Data;
        private volatile boolean Output;
        
        MonitorThread() {
            this.CTS = false;
            this.DSR = false;
            this.RI = false;
            this.CD = false;
            this.OE = false;
            this.PE = false;
            this.FE = false;
            this.BI = false;
            this.Data = false;
            this.Output = false;
        }
        
        public void run() {
            RXTXPort.this.monThreadisInterrupted = false;
            RXTXPort.this.eventLoop();
        }
        
        protected void finalize() throws Throwable {
        }
    }
}
