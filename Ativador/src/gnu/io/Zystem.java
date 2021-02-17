// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

import java.util.logging.Logger;
import java.io.RandomAccessFile;

public class Zystem
{
    public static final int SILENT_MODE = 0;
    public static final int FILE_MODE = 1;
    public static final int NET_MODE = 2;
    public static final int MEX_MODE = 3;
    public static final int PRINT_MODE = 4;
    public static final int J2EE_MSG_MODE = 5;
    public static final int J2SE_LOG_MODE = 6;
    static int mode;
    private static String target;
    
    public Zystem(final int mode) throws UnSupportedLoggerException {
        Zystem.mode = mode;
        this.startLogger("asdf");
    }
    
    public Zystem() throws UnSupportedLoggerException {
        final String property = System.getProperty("gnu.io.log.mode");
        if (property != null) {
            if ("SILENT_MODE".equals(property)) {
                Zystem.mode = 0;
            }
            else if ("FILE_MODE".equals(property)) {
                Zystem.mode = 1;
            }
            else if ("NET_MODE".equals(property)) {
                Zystem.mode = 2;
            }
            else if ("MEX_MODE".equals(property)) {
                Zystem.mode = 3;
            }
            else if ("PRINT_MODE".equals(property)) {
                Zystem.mode = 4;
            }
            else if ("J2EE_MSG_MODE".equals(property)) {
                Zystem.mode = 5;
            }
            else if ("J2SE_LOG_MODE".equals(property)) {
                Zystem.mode = 6;
            }
            else {
                try {
                    Zystem.mode = Integer.parseInt(property);
                }
                catch (NumberFormatException ex) {
                    Zystem.mode = 0;
                }
            }
        }
        else {
            Zystem.mode = 0;
        }
        this.startLogger("asdf");
    }
    
    public void startLogger() throws UnSupportedLoggerException {
        if (Zystem.mode == 0 || Zystem.mode == 4) {
            return;
        }
        throw new UnSupportedLoggerException("Target Not Allowed");
    }
    
    public void startLogger(final String target) throws UnSupportedLoggerException {
        Zystem.target = target;
    }
    
    public void finalize() {
        Zystem.mode = 0;
        Zystem.target = null;
    }
    
    public void filewrite(final String s) {
        try {
            final RandomAccessFile randomAccessFile = new RandomAccessFile(Zystem.target, "rw");
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.writeBytes(s);
            randomAccessFile.close();
        }
        catch (Exception ex) {
            System.out.println("Debug output file write failed");
        }
    }
    
    public boolean report(final String s) {
        if (Zystem.mode != 2) {
            if (Zystem.mode == 4) {
                System.out.println(s);
                return true;
            }
            if (Zystem.mode != 3) {
                if (Zystem.mode == 0) {
                    return true;
                }
                if (Zystem.mode == 1) {
                    this.filewrite(s);
                }
                else {
                    if (Zystem.mode == 5) {
                        return false;
                    }
                    if (Zystem.mode == 6) {
                        Logger.getLogger("gnu.io").fine(s);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean reportln() {
        if (Zystem.mode != 2) {
            if (Zystem.mode == 4) {
                System.out.println();
                return true;
            }
            if (Zystem.mode != 3) {
                if (Zystem.mode == 0) {
                    return true;
                }
                if (Zystem.mode == 1) {
                    this.filewrite("\n");
                }
                else if (Zystem.mode == 5) {
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean reportln(final String s) {
        if (Zystem.mode != 2) {
            if (Zystem.mode == 4) {
                System.out.println(s);
                return true;
            }
            if (Zystem.mode != 3) {
                if (Zystem.mode == 0) {
                    return true;
                }
                if (Zystem.mode == 1) {
                    this.filewrite(s + "\n");
                }
                else {
                    if (Zystem.mode == 5) {
                        return false;
                    }
                    if (Zystem.mode == 6) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    static {
        Zystem.mode = 0;
    }
}
