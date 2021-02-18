// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.TooManyListenersException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class I2C extends I2CPort
{
    private int fd;
    static boolean dsrFlag;
    private final I2COutputStream out;
    private final I2CInputStream in;
    private int speed;
    private int dataBits;
    private int stopBits;
    private int parity;
    private int flowmode;
    private int timeout;
    private int threshold;
    private int InputBuffer;
    private int OutputBuffer;
    private I2CPortEventListener SPEventListener;
    private MonitorThread monThread;
    private int dataAvailable;
    
    private static native void Initialize();
    
    public I2C(final String s) throws PortInUseException {
        this.out = new I2COutputStream();
        this.in = new I2CInputStream();
        this.speed = 9600;
        this.dataBits = 8;
        this.stopBits = 1;
        this.parity = 0;
        this.flowmode = 0;
        this.timeout = 0;
        this.threshold = 0;
        this.InputBuffer = 0;
        this.OutputBuffer = 0;
        this.dataAvailable = 0;
        this.fd = this.open(s);
    }
    
    private native int open(final String p0) throws PortInUseException;
    
    public OutputStream getOutputStream() {
        return this.out;
    }
    
    public InputStream getInputStream() {
        return this.in;
    }
    
    public void setI2CPortParams(final int speed, final int dataBits, final int stopBits, final int parity) throws UnsupportedCommOperationException {
        this.nativeSetI2CPortParams(speed, dataBits, stopBits, parity);
        this.speed = speed;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }
    
    private native void nativeSetI2CPortParams(final int p0, final int p1, final int p2, final int p3) throws UnsupportedCommOperationException;
    
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
    
    public native boolean NativeisReceiveTimeoutEnabled();
    
    public native void NativeEnableReceiveTimeoutThreshold(final int p0, final int p1, final int p2);
    
    public void disableReceiveTimeout() {
        this.enableReceiveTimeout(0);
    }
    
    public void enableReceiveTimeout(final int timeout) {
        if (timeout >= 0) {
            this.NativeEnableReceiveTimeoutThreshold(this.timeout = timeout, this.threshold, this.InputBuffer);
        }
        else {
            System.out.println("Invalid timeout");
        }
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
        }
        else {
            System.out.println("Invalid Threshold");
        }
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
        this.InputBuffer = inputBuffer;
    }
    
    public int getInputBufferSize() {
        return this.InputBuffer;
    }
    
    public void setOutputBufferSize(final int outputBuffer) {
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
    
    private native void writeByte(final int p0) throws IOException;
    
    private native void writeArray(final byte[] p0, final int p1, final int p2) throws IOException;
    
    private native void drain() throws IOException;
    
    private native int nativeavailable() throws IOException;
    
    private native int readByte() throws IOException;
    
    private native int readArray(final byte[] p0, final int p1, final int p2) throws IOException;
    
    native void eventLoop();
    
    public void sendEvent(final int i, final boolean b) {
        switch (i) {
            case 1: {
                this.dataAvailable = 1;
                if (this.monThread.Data) {
                    break;
                }
                return;
            }
            case 2: {
                if (this.monThread.Output) {
                    break;
                }
                return;
            }
            case 3: {
                if (this.monThread.CTS) {
                    break;
                }
                return;
            }
            case 4: {
                if (this.monThread.DSR) {
                    break;
                }
                return;
            }
            case 5: {
                if (this.monThread.RI) {
                    break;
                }
                return;
            }
            case 6: {
                if (this.monThread.CD) {
                    break;
                }
                return;
            }
            case 7: {
                if (this.monThread.OE) {
                    break;
                }
                return;
            }
            case 8: {
                if (this.monThread.PE) {
                    break;
                }
                return;
            }
            case 9: {
                if (this.monThread.FE) {
                    break;
                }
                return;
            }
            case 10: {
                if (this.monThread.BI) {
                    break;
                }
                return;
            }
            default: {
                System.err.println("unknown event:" + i);
                return;
            }
        }
        final I2CPortEvent i2CPortEvent = new I2CPortEvent(this, i, !b, b);
        if (this.SPEventListener != null) {
            this.SPEventListener.I2CEvent(i2CPortEvent);
        }
    }
    
    public void addEventListener(final I2CPortEventListener spEventListener) throws TooManyListenersException {
        if (this.SPEventListener != null) {
            throw new TooManyListenersException();
        }
        this.SPEventListener = spEventListener;
        (this.monThread = new MonitorThread()).start();
    }
    
    public void removeEventListener() {
        this.SPEventListener = null;
        if (this.monThread != null) {
            this.monThread.interrupt();
            this.monThread = null;
        }
    }
    
    public void notifyOnDataAvailable(final boolean b) {
        this.monThread.Data = b;
    }
    
    public void notifyOnOutputEmpty(final boolean b) {
        this.monThread.Output = b;
    }
    
    public void notifyOnCTS(final boolean b) {
        this.monThread.CTS = b;
    }
    
    public void notifyOnDSR(final boolean b) {
        this.monThread.DSR = b;
    }
    
    public void notifyOnRingIndicator(final boolean b) {
        this.monThread.RI = b;
    }
    
    public void notifyOnCarrierDetect(final boolean b) {
        this.monThread.CD = b;
    }
    
    public void notifyOnOverrunError(final boolean b) {
        this.monThread.OE = b;
    }
    
    public void notifyOnParityError(final boolean b) {
        this.monThread.PE = b;
    }
    
    public void notifyOnFramingError(final boolean b) {
        this.monThread.FE = b;
    }
    
    public void notifyOnBreakInterrupt(final boolean b) {
        this.monThread.BI = b;
    }
    
    private native void nativeClose();
    
    public void close() {
        this.setDTR(false);
        this.setDSR(false);
        this.nativeClose();
        super.close();
        this.fd = 0;
    }
    
    protected void finalize() {
        if (this.fd > 0) {
            this.close();
        }
    }
    
    static {
        System.loadLibrary("rxtxI2C");
        Initialize();
        I2C.dsrFlag = false;
    }
    
    class I2COutputStream extends OutputStream
    {
        public void write(final int n) throws IOException {
            I2C.this.writeByte(n);
        }
        
        public void write(final byte[] array) throws IOException {
            I2C.this.writeArray(array, 0, array.length);
        }
        
        public void write(final byte[] array, final int n, final int n2) throws IOException {
            I2C.this.writeArray(array, n, n2);
        }
        
        public void flush() throws IOException {
            I2C.this.drain();
        }
    }
    
    class I2CInputStream extends InputStream
    {
        public int read() throws IOException {
            I2C.this.dataAvailable = 0;
            return I2C.this.readByte();
        }
        
        public int read(final byte[] array) throws IOException {
            return this.read(array, 0, array.length);
        }
        
        public int read(final byte[] array, final int n, final int n2) throws IOException {
            I2C.this.dataAvailable = 0;
            int i;
            int[] array2;
            for (i = 0, array2 = new int[] { array.length, I2C.this.InputBuffer, n2 }; array2[i] == 0 && i < array2.length; ++i) {}
            int min = array2[i];
            while (i < array2.length) {
                if (array2[i] > 0) {
                    min = Math.min(min, array2[i]);
                }
                ++i;
            }
            int min2 = Math.min(min, I2C.this.threshold);
            if (min2 == 0) {
                min2 = 1;
            }
            this.available();
            return I2C.this.readArray(array, n, min2);
        }
        
        public int available() throws IOException {
            return I2C.this.nativeavailable();
        }
    }
    
    class MonitorThread extends Thread
    {
        private boolean CTS;
        private boolean DSR;
        private boolean RI;
        private boolean CD;
        private boolean OE;
        private boolean PE;
        private boolean FE;
        private boolean BI;
        private boolean Data;
        private boolean Output;
        
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
            I2C.this.eventLoop();
        }
    }
}
