// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.io.FileDescriptor;
import java.util.Enumeration;
import java.util.Vector;

public class CommPortIdentifier
{
    public static final int PORT_SERIAL = 1;
    public static final int PORT_PARALLEL = 2;
    public static final int PORT_I2C = 3;
    public static final int PORT_RS485 = 4;
    public static final int PORT_RAW = 5;
    private String PortName;
    private boolean Available;
    private String Owner;
    private CommPort commport;
    private CommDriver RXTXDriver;
    static CommPortIdentifier CommPortIndex;
    CommPortIdentifier next;
    private int PortType;
    private static final boolean debug = false;
    static Object Sync;
    Vector ownershipListener;
    private boolean HideOwnerEvents;
    
    CommPortIdentifier(final String portName, final CommPort commport, final int portType, final CommDriver rxtxDriver) {
        this.Available = true;
        this.PortName = portName;
        this.commport = commport;
        this.PortType = portType;
        this.next = null;
        this.RXTXDriver = rxtxDriver;
    }
    
    public static void addPortName(final String s, final int n, final CommDriver commDriver) {
        AddIdentifierToList(new CommPortIdentifier(s, null, n, commDriver));
    }
    
    private static void AddIdentifierToList(final CommPortIdentifier commPortIdentifier) {
        synchronized (CommPortIdentifier.Sync) {
            if (CommPortIdentifier.CommPortIndex == null) {
                CommPortIdentifier.CommPortIndex = commPortIdentifier;
            }
            else {
                CommPortIdentifier commPortIdentifier2;
                for (commPortIdentifier2 = CommPortIdentifier.CommPortIndex; commPortIdentifier2.next != null; commPortIdentifier2 = commPortIdentifier2.next) {}
                commPortIdentifier2.next = commPortIdentifier;
            }
        }
    }
    
    public void addPortOwnershipListener(final CommPortOwnershipListener commPortOwnershipListener) {
        if (this.ownershipListener == null) {
            this.ownershipListener = new Vector();
        }
        if (!this.ownershipListener.contains(commPortOwnershipListener)) {
            this.ownershipListener.addElement(commPortOwnershipListener);
        }
    }
    
    public String getCurrentOwner() {
        return this.Owner;
    }
    
    public String getName() {
        return this.PortName;
    }
    
    public static CommPortIdentifier getPortIdentifier(final String anObject) throws NoSuchPortException {
        CommPortIdentifier commPortIdentifier = CommPortIdentifier.CommPortIndex;
        synchronized (CommPortIdentifier.Sync) {
            while (commPortIdentifier != null && !commPortIdentifier.PortName.equals(anObject)) {
                commPortIdentifier = commPortIdentifier.next;
            }
        }
        if (commPortIdentifier != null) {
            return commPortIdentifier;
        }
        throw new NoSuchPortException();
    }
    
    public static CommPortIdentifier getPortIdentifier(final CommPort commPort) throws NoSuchPortException {
        CommPortIdentifier commPortIdentifier = CommPortIdentifier.CommPortIndex;
        synchronized (CommPortIdentifier.Sync) {
            while (commPortIdentifier != null && commPortIdentifier.commport != commPort) {
                commPortIdentifier = commPortIdentifier.next;
            }
        }
        if (commPortIdentifier != null) {
            return commPortIdentifier;
        }
        throw new NoSuchPortException();
    }
    
    public static Enumeration getPortIdentifiers() {
        CommPortIdentifier.CommPortIndex = null;
        try {
            ((CommDriver)Class.forName("gnu.io.RXTXCommDriver").newInstance()).initialize();
        }
        catch (Throwable obj) {
            System.err.println(obj + " thrown while loading " + "gnu.io.RXTXCommDriver");
        }
        return new CommPortEnumerator();
    }
    
    public int getPortType() {
        return this.PortType;
    }
    
    public synchronized boolean isCurrentlyOwned() {
        return !this.Available;
    }
    
    public synchronized CommPort open(final FileDescriptor fileDescriptor) throws UnsupportedCommOperationException {
        throw new UnsupportedCommOperationException();
    }
    
    private native String native_psmisc_report_owner(final String p0);
    
    public synchronized CommPort open(final String owner, final int n) throws PortInUseException {
        if (!this.Available) {
            synchronized (CommPortIdentifier.Sync) {
                this.fireOwnershipEvent(3);
                try {
                    this.wait(n);
                }
                catch (InterruptedException ex) {}
            }
        }
        if (!this.Available) {
            throw new PortInUseException(this.getCurrentOwner());
        }
        if (this.commport == null) {
            this.commport = this.RXTXDriver.getCommPort(this.PortName, this.PortType);
        }
        if (this.commport != null) {
            this.Owner = owner;
            this.Available = false;
            this.fireOwnershipEvent(1);
            return this.commport;
        }
        throw new PortInUseException(this.native_psmisc_report_owner(this.PortName));
    }
    
    public void removePortOwnershipListener(final CommPortOwnershipListener obj) {
        if (this.ownershipListener != null) {
            this.ownershipListener.removeElement(obj);
        }
    }
    
    synchronized void internalClosePort() {
        this.Owner = null;
        this.Available = true;
        this.commport = null;
        this.notifyAll();
        this.fireOwnershipEvent(2);
    }
    
    void fireOwnershipEvent(final int n) {
        if (this.ownershipListener != null) {
            final Enumeration<CommPortOwnershipListener> elements = this.ownershipListener.elements();
            while (elements.hasMoreElements()) {
                elements.nextElement().ownershipChange(n);
            }
        }
    }
    
    static {
        CommPortIdentifier.Sync = new Object();
        try {
            ((CommDriver)Class.forName("gnu.io.RXTXCommDriver").newInstance()).initialize();
        }
        catch (Throwable obj) {
            System.err.println(obj + " thrown while loading " + "gnu.io.RXTXCommDriver");
        }
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") == -1) {}
        System.loadLibrary("rxtxSerial");
    }
}
