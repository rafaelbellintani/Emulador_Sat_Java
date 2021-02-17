// 
// Decompiled by Procyon v0.5.36
// 

package javax.comm;

import java.io.FileDescriptor;
import java.util.Enumeration;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class CommPortIdentifier
{
    String name;
    public static final int PORT_SERIAL = 1;
    public static final int PORT_PARALLEL = 2;
    boolean owned;
    String owner;
    boolean inOpen;
    Vector listeners;
    private int portType;
    CommPortIdentifier next;
    private CommPort port;
    private CommDriver driver;
    private static CommDriver localDriver;
    static CommPortIdentifier masterIdList;
    static Object lock;
    static String propfilename;
    
    CommPortIdentifier(final String name, final CommPort port, final int portType, final CommDriver driver) {
        this.listeners = null;
        this.name = name;
        this.port = port;
        this.portType = portType;
        this.next = null;
        this.driver = driver;
        this.owner = null;
        this.owned = false;
    }
    
    private static String[] parsePropsFile(final InputStream inputStream) {
        final Vector vector = new Vector<String>();
        try {
            final byte[] array = new byte[4096];
            int n = 0;
            boolean b = false;
            int read;
            while ((read = inputStream.read()) != -1) {
                switch (read) {
                    case 9:
                    case 32: {
                        continue;
                    }
                    case 10:
                    case 13: {
                        if (n > 0) {
                            vector.addElement(new String(array, 0, n));
                        }
                        n = 0;
                        b = false;
                        continue;
                    }
                    case 35: {
                        b = true;
                        if (n > 0) {
                            vector.addElement(new String(array, 0, n));
                        }
                        n = 0;
                        continue;
                    }
                    default: {
                        if (!b && n < 4096) {
                            array[n++] = (byte)read;
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
        catch (Throwable obj) {
            System.err.println("Caught " + obj + " parsing prop file.");
        }
        if (vector.size() > 0) {
            final String[] array2 = new String[vector.size()];
            for (int i = 0; i < vector.size(); ++i) {
                array2[i] = vector.elementAt(i);
            }
            return array2;
        }
        return null;
    }
    
    private static void InitializeDriver(final String s) throws IOException {
        try {
            if (CommPortIdentifier.localDriver == null) {
                CommPortIdentifier.localDriver = (CommDriver)Class.forName(s).newInstance();
            }
            CommPortIdentifier.localDriver.initialize();
        }
        catch (Throwable obj) {
            System.err.println("Caught " + obj + " while loading driver " + s);
        }
    }
    
    private static void loadDriver(final String pathname) throws IOException {
        final String[] propsFile = parsePropsFile(new BufferedInputStream(new FileInputStream(new File(pathname))));
        if (propsFile != null) {
            for (int i = 0; i < propsFile.length; ++i) {
                if (propsFile[i].regionMatches(true, 0, "driver=", 0, 7)) {
                    final String substring = propsFile[i].substring(7);
                    substring.trim();
                    InitializeDriver(substring);
                }
            }
        }
    }
    
    private static String findPropFile(final String s) {
        final StreamTokenizer streamTokenizer = new StreamTokenizer(new StringReader(System.getProperty("java.class.path")));
        streamTokenizer.whitespaceChars(File.pathSeparatorChar, File.pathSeparatorChar);
        streamTokenizer.wordChars(File.separatorChar, File.separatorChar);
        streamTokenizer.ordinaryChar(46);
        streamTokenizer.wordChars(46, 46);
        streamTokenizer.ordinaryChar(32);
        streamTokenizer.wordChars(32, 32);
        streamTokenizer.wordChars(95, 95);
        try {
            while (streamTokenizer.nextToken() != -1) {
                final int index;
                if (streamTokenizer.ttype == -3 && (index = streamTokenizer.sval.indexOf("comm.jar")) != -1) {
                    final String pathname = new String(streamTokenizer.sval);
                    if (!new File(pathname).exists()) {
                        continue;
                    }
                    final String substring = pathname.substring(0, index);
                    String s2;
                    if (substring != null) {
                        s2 = substring + "." + File.separator + s;
                    }
                    else {
                        s2 = "." + File.separator + s;
                    }
                    if (new File(s2).exists()) {
                        return new String(s2);
                    }
                    return null;
                }
            }
        }
        catch (IOException ex) {}
        return null;
    }
    
    public static Enumeration getPortIdentifiers() {
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(CommPortIdentifier.propfilename);
        }
        CommPortIdentifier.masterIdList = null;
        try {
            InitializeDriver("com.sun.comm.SolarisDriver");
        }
        catch (IOException ex) {}
        return new CommPortEnumerator();
    }
    
    public static CommPortIdentifier getPortIdentifier(final String anObject) throws NoSuchPortException {
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(CommPortIdentifier.propfilename);
        }
        CommPortIdentifier commPortIdentifier = null;
        synchronized (CommPortIdentifier.lock) {
            for (commPortIdentifier = CommPortIdentifier.masterIdList; commPortIdentifier != null && !commPortIdentifier.name.equals(anObject); commPortIdentifier = commPortIdentifier.next) {}
        }
        if (commPortIdentifier != null) {
            return commPortIdentifier;
        }
        throw new NoSuchPortException();
    }
    
    public static CommPortIdentifier getPortIdentifier(final CommPort commPort) throws NoSuchPortException {
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(CommPortIdentifier.propfilename);
        }
        CommPortIdentifier commPortIdentifier = null;
        synchronized (CommPortIdentifier.lock) {
            for (commPortIdentifier = CommPortIdentifier.masterIdList; commPortIdentifier != null && commPortIdentifier.port != commPort; commPortIdentifier = commPortIdentifier.next) {}
        }
        if (commPortIdentifier != null) {
            return commPortIdentifier;
        }
        throw new NoSuchPortException();
    }
    
    private static void addPort(final CommPort commPort, final int n) {
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(CommPortIdentifier.propfilename);
        }
        final CommPortIdentifier commPortIdentifier = new CommPortIdentifier(commPort.getName(), commPort, n, null);
        CommPortIdentifier commPortIdentifier2 = CommPortIdentifier.masterIdList;
        CommPortIdentifier commPortIdentifier3 = null;
        synchronized (CommPortIdentifier.lock) {
            while (commPortIdentifier2 != null) {
                commPortIdentifier3 = commPortIdentifier2;
                commPortIdentifier2 = commPortIdentifier2.next;
            }
            if (commPortIdentifier3 != null) {
                commPortIdentifier3.next = commPortIdentifier;
            }
            else {
                CommPortIdentifier.masterIdList = commPortIdentifier;
            }
        }
    }
    
    public static void addPortName(final String s, final int n, final CommDriver commDriver) {
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(CommPortIdentifier.propfilename);
        }
        final CommPortIdentifier commPortIdentifier = new CommPortIdentifier(s, null, n, commDriver);
        CommPortIdentifier commPortIdentifier2 = CommPortIdentifier.masterIdList;
        CommPortIdentifier commPortIdentifier3 = null;
        synchronized (CommPortIdentifier.lock) {
            while (commPortIdentifier2 != null) {
                commPortIdentifier3 = commPortIdentifier2;
                commPortIdentifier2 = commPortIdentifier2.next;
            }
            if (commPortIdentifier3 != null) {
                commPortIdentifier3.next = commPortIdentifier;
            }
            else {
                CommPortIdentifier.masterIdList = commPortIdentifier;
            }
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getPortType() {
        return this.portType;
    }
    
    public String getCurrentOwner() {
        if (this.owned) {
            return this.owner;
        }
        return "Port currently not owned";
    }
    
    public boolean isCurrentlyOwned() {
        return this.owned;
    }
    
    public synchronized CommPort open(final String owner, final int n) throws PortInUseException {
        try {
            while (this.owned) {
                this.inOpen = true;
                this.fireOwnershipEvent(3);
                this.wait(n);
                if (this.owned) {
                    this.inOpen = false;
                    throw new PortInUseException(this.getCurrentOwner());
                }
            }
            this.inOpen = false;
        }
        catch (InterruptedException ex) {
            this.inOpen = false;
            return null;
        }
        if (this.port == null) {
            this.port = this.driver.getCommPort(this.name, this.portType);
        }
        if (this.port == null) {
            throw new PortInUseException("another application, or cannot be accessed");
        }
        this.fireOwnershipEvent(1);
        this.owned = true;
        this.owner = owner;
        return this.port;
    }
    
    public CommPort open(final FileDescriptor fileDescriptor) throws UnsupportedCommOperationException {
        throw new UnsupportedCommOperationException();
    }
    
    synchronized void internalClosePort() {
        this.owned = false;
        this.owner = null;
        this.port = null;
        this.notifyAll();
        if (!this.inOpen) {
            this.fireOwnershipEvent(2);
        }
    }
    
    public void addPortOwnershipListener(final CommPortOwnershipListener commPortOwnershipListener) {
        if (this.listeners == null) {
            this.listeners = new Vector(1, 1);
        }
        if (!this.listeners.contains(commPortOwnershipListener)) {
            this.listeners.addElement(commPortOwnershipListener);
        }
    }
    
    public void removePortOwnershipListener(final CommPortOwnershipListener obj) {
        if (this.listeners != null) {
            this.listeners.removeElement(obj);
        }
    }
    
    void fireOwnershipEvent(final int n) {
        if (this.listeners != null) {
            final Enumeration<CommPortOwnershipListener> elements = this.listeners.elements();
            while (elements.hasMoreElements()) {
                elements.nextElement().ownershipChange(n);
            }
        }
    }
    
    static {
        CommPortIdentifier.localDriver = null;
        CommPortIdentifier.lock = new Object();
        final String property;
        if ((property = System.getProperty("javax.comm.properties")) != null) {
            System.err.println("Comm Drivers: " + property);
        }
        final String string = System.getProperty("java.home") + File.separator + "lib" + File.separator + "javax.comm.properties";
        try {
            CommPortIdentifier.propfilename = new String(string);
            loadDriver(string);
        }
        catch (IOException ex) {
            CommPortIdentifier.propfilename = findPropFile("javax.comm.properties");
            try {
                if (CommPortIdentifier.propfilename != null) {
                    loadDriver(CommPortIdentifier.propfilename);
                }
                else {
                    CommPortIdentifier.propfilename = new String(" ");
                    InitializeDriver("com.sun.comm.SolarisDriver");
                }
            }
            catch (IOException obj) {
                CommPortIdentifier.propfilename = new String(" ");
                System.err.println("Default SolarisDriver not found:" + obj);
            }
        }
    }
}
