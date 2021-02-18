// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.io.File;
import java.util.Iterator;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.StringTokenizer;

public class RXTXCommDriver implements CommDriver
{
    private static final boolean debug = false;
    private static final boolean devel = true;
    private String deviceDirectory;
    private String osName;
    
    private native boolean registerKnownPorts(final int p0);
    
    private native boolean isPortPrefixValid(final String p0);
    
    private native boolean testRead(final String p0, final int p1);
    
    private native String getDeviceDirectory();
    
    public static native String nativeGetVersion();
    
    private final String[] getValidPortPrefixes(final String[] array) {
        final String[] array2 = new String[256];
        if (array == null) {}
        int n = 0;
        for (int i = 0; i < array.length; ++i) {
            if (this.isPortPrefixValid(array[i])) {
                array2[n++] = new String(array[i]);
            }
        }
        final String[] array3 = new String[n];
        System.arraycopy(array2, 0, array3, 0, n);
        if (array2[0] == null) {}
        return array3;
    }
    
    private void checkSolaris(final String s, final int n) {
        final char[] array = { '[' };
        array[0] = 'a';
        while (array[0] < '{') {
            if (this.testRead(s.concat(new String(array)), n)) {
                CommPortIdentifier.addPortName(s.concat(new String(array)), n, this);
            }
            final char[] array2 = array;
            final int n2 = 0;
            ++array2[n2];
        }
    }
    
    private void registerValidPorts(final String[] array, final String[] array2, final int n) {
        if (array != null && array2 != null) {
            for (int i = 0; i < array.length; ++i) {
                for (int j = 0; j < array2.length; ++j) {
                    final String other = array2[j];
                    final int length = other.length();
                    final String s = array[i];
                    if (s.length() >= length) {
                        final String upperCase = s.substring(length).toUpperCase();
                        final String lowerCase = s.substring(length).toLowerCase();
                        if (s.regionMatches(0, other, 0, length)) {
                            if (upperCase.equals(lowerCase)) {
                                String s2;
                                if (this.osName.toLowerCase().indexOf("windows") == -1) {
                                    s2 = new String(this.deviceDirectory + s);
                                }
                                else {
                                    s2 = new String(s);
                                }
                                if (this.osName.equals("Solaris") || this.osName.equals("SunOS")) {
                                    this.checkSolaris(s2, n);
                                }
                                else if (this.testRead(s2, n)) {
                                    CommPortIdentifier.addPortName(s2, n, this);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void initialize() {
        this.osName = System.getProperty("os.name");
        this.deviceDirectory = this.getDeviceDirectory();
        for (int i = 1; i <= 2; ++i) {
            if (!this.registerSpecifiedPorts(i) && !this.registerKnownPorts(i)) {
                this.registerScannedPorts(i);
            }
        }
    }
    
    private void addSpecifiedPorts(final String str, final int n) {
        final StringTokenizer stringTokenizer = new StringTokenizer(str, System.getProperty("path.separator", ":"));
        while (stringTokenizer.hasMoreElements()) {
            final String nextToken = stringTokenizer.nextToken();
            if (this.testRead(nextToken, n)) {
                CommPortIdentifier.addPortName(nextToken, n, this);
            }
        }
    }
    
    private boolean registerSpecifiedPorts(final int n) {
        String s = null;
        try {
            final FileInputStream inStream = new FileInputStream(System.getProperty("java.ext.dirs") + System.getProperty("file.separator") + "gnu.io.rxtx.properties");
            final Properties properties = new Properties();
            properties.load(inStream);
            System.setProperties(properties);
            for (final String s2 : properties.keySet()) {
                System.setProperty(s2, properties.getProperty(s2));
            }
        }
        catch (Exception ex) {}
        switch (n) {
            case 1: {
                if ((s = System.getProperty("gnu.io.rxtx.SerialPorts")) == null) {
                    s = System.getProperty("gnu.io.SerialPorts");
                    break;
                }
                break;
            }
            case 2: {
                if ((s = System.getProperty("gnu.io.rxtx.ParallelPorts")) == null) {
                    s = System.getProperty("gnu.io.ParallelPorts");
                    break;
                }
                break;
            }
        }
        if (s != null) {
            this.addSpecifiedPorts(s, n);
            return true;
        }
        return false;
    }
    
    private void registerScannedPorts(final int n) {
        String[] list;
        if (this.osName.equals("Windows CE")) {
            list = new String[] { "COM1:", "COM2:", "COM3:", "COM4:", "COM5:", "COM6:", "COM7:", "COM8:" };
        }
        else if (this.osName.toLowerCase().indexOf("windows") != -1) {
            final String[] array = new String[259];
            for (int i = 1; i <= 256; ++i) {
                array[i - 1] = new String("COM" + i);
            }
            for (int j = 1; j <= 3; ++j) {
                array[j + 255] = new String("LPT" + j);
            }
            list = array;
        }
        else if (this.osName.equals("Solaris") || this.osName.equals("SunOS")) {
            final String[] array2 = new String[2];
            int k = 0;
            if (new File("/dev/term").list().length > 0) {}
            array2[k++] = new String("term/");
            final String[] array3 = new String[k];
            --k;
            while (k >= 0) {
                array3[k] = array2[k];
                --k;
            }
            list = array3;
        }
        else {
            list = new File(this.deviceDirectory).list();
        }
        if (list == null) {
            return;
        }
        String[] array4 = new String[0];
        switch (n) {
            case 1: {
                if (this.osName.equals("Linux")) {
                    array4 = new String[] { "ttyS", "ttySA", "ttyUSB" };
                    break;
                }
                if (this.osName.equals("Linux-all-ports")) {
                    array4 = new String[] { "comx", "holter", "modem", "rfcomm", "ttyircomm", "ttycosa0c", "ttycosa1c", "ttyC", "ttyCH", "ttyD", "ttyE", "ttyF", "ttyH", "ttyI", "ttyL", "ttyM", "ttyMX", "ttyP", "ttyR", "ttyS", "ttySI", "ttySR", "ttyT", "ttyUSB", "ttyV", "ttyW", "ttyX" };
                    break;
                }
                if (this.osName.toLowerCase().indexOf("qnx") != -1) {
                    array4 = new String[] { "ser" };
                    break;
                }
                if (this.osName.equals("Irix")) {
                    array4 = new String[] { "ttyc", "ttyd", "ttyf", "ttym", "ttyq", "tty4d", "tty4f", "midi", "us" };
                    break;
                }
                if (this.osName.equals("FreeBSD")) {
                    array4 = new String[] { "ttyd", "cuaa", "ttyA", "cuaA", "ttyD", "cuaD", "ttyE", "cuaE", "ttyF", "cuaF", "ttyR", "cuaR", "stl" };
                    break;
                }
                if (this.osName.equals("NetBSD")) {
                    array4 = new String[] { "tty0" };
                    break;
                }
                if (this.osName.equals("Solaris") || this.osName.equals("SunOS")) {
                    array4 = new String[] { "term/", "cua/" };
                    break;
                }
                if (this.osName.equals("HP-UX")) {
                    array4 = new String[] { "tty0p", "tty1p" };
                    break;
                }
                if (this.osName.equals("UnixWare") || this.osName.equals("OpenUNIX")) {
                    array4 = new String[] { "tty00s", "tty01s", "tty02s", "tty03s" };
                    break;
                }
                if (this.osName.equals("OpenServer")) {
                    array4 = new String[] { "tty1A", "tty2A", "tty3A", "tty4A", "tty5A", "tty6A", "tty7A", "tty8A", "tty9A", "tty10A", "tty11A", "tty12A", "tty13A", "tty14A", "tty15A", "tty16A", "ttyu1A", "ttyu2A", "ttyu3A", "ttyu4A", "ttyu5A", "ttyu6A", "ttyu7A", "ttyu8A", "ttyu9A", "ttyu10A", "ttyu11A", "ttyu12A", "ttyu13A", "ttyu14A", "ttyu15A", "ttyu16A" };
                    break;
                }
                if (this.osName.equals("Compaq's Digital UNIX") || this.osName.equals("OSF1")) {
                    array4 = new String[] { "tty0" };
                    break;
                }
                if (this.osName.equals("BeOS")) {
                    array4 = new String[] { "serial" };
                    break;
                }
                if (this.osName.equals("Mac OS X")) {
                    array4 = new String[] { "cu.KeyUSA28X191.", "tty.KeyUSA28X191.", "cu.KeyUSA28X181.", "tty.KeyUSA28X181.", "cu.KeyUSA19181.", "tty.KeyUSA19181." };
                    break;
                }
                if (this.osName.toLowerCase().indexOf("windows") != -1) {
                    array4 = new String[] { "COM" };
                    break;
                }
                break;
            }
            case 2: {
                if (this.osName.equals("Linux")) {
                    array4 = new String[] { "lp" };
                    break;
                }
                if (this.osName.equals("FreeBSD")) {
                    array4 = new String[] { "lpt" };
                    break;
                }
                if (this.osName.toLowerCase().indexOf("windows") != -1) {
                    array4 = new String[] { "LPT" };
                    break;
                }
                array4 = new String[0];
                break;
            }
        }
        this.registerValidPorts(list, array4, n);
    }
    
    public CommPort getCommPort(final String str, final int n) {
        try {
            switch (n) {
                case 1: {
                    if (this.osName.toLowerCase().indexOf("windows") == -1) {
                        return new RXTXPort(str);
                    }
                    return new RXTXPort(this.deviceDirectory + str);
                }
                case 2: {
                    return new LPRPort(str);
                }
            }
        }
        catch (PortInUseException ex) {}
        return null;
    }
    
    public void Report(final String x) {
        System.out.println(x);
    }
    
    static {
        System.loadLibrary("rxtxSerial");
        final String version = RXTXVersion.getVersion();
        String str;
        try {
            str = RXTXVersion.nativeGetVersion();
        }
        catch (Error error) {
            str = nativeGetVersion();
        }
        System.out.println("Stable Library");
        System.out.println("=========================================");
        System.out.println("Native lib Version = " + str);
        System.out.println("Java lib Version   = " + version);
        if (!version.equals(str)) {
            System.out.println("WARNING:  RXTX Version mismatch\n\tJar version = " + version + "\n\tnative lib Version = " + str);
        }
    }
}
