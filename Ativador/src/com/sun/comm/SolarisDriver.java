// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.comm;

import javax.comm.CommPort;
import java.io.IOException;
import java.io.File;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;
import java.util.Vector;
import javax.comm.CommDriver;

public class SolarisDriver implements CommDriver
{
    private boolean driverInitialized;
    
    public SolarisDriver() {
        this.driverInitialized = false;
    }
    
    public void initialize() {
        String propFile = findPropFile("portmap.properties");
        if (!this.driverInitialized) {
            try {
                System.loadLibrary("SolarisSerialParallel");
            }
            catch (SecurityException obj) {
                System.err.println("Security Exception SolarisSerial: " + obj);
            }
            catch (UnsatisfiedLinkError obj2) {
                System.err.println("Error loading SolarisSerial: " + obj2);
            }
        }
        if (propFile == null) {
            propFile = new String("");
        }
        else if (propFile.length() < 1) {
            propFile = "";
        }
        final Vector<String> vector = new Vector<String>();
        readRegistrySerial(vector, propFile);
        final Enumeration<String> elements = vector.elements();
        while (elements.hasMoreElements()) {
            CommPortIdentifier.addPortName(elements.nextElement(), 1, this);
        }
        final Vector<String> vector2 = new Vector<String>();
        readRegistryParallel(vector2);
        final Enumeration<String> elements2 = vector2.elements();
        while (elements2.hasMoreElements()) {
            CommPortIdentifier.addPortName(elements2.nextElement(), 2, this);
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
    
    public CommPort getCommPort(final String s, final int n) {
        CommPort commPort = null;
        try {
            switch (n) {
                case 1: {
                    commPort = new SolarisSerial(s);
                    break;
                }
                case 2: {
                    commPort = new SolarisParallel(s);
                    break;
                }
            }
        }
        catch (IOException ex) {}
        return commPort;
    }
    
    private static native int readRegistrySerial(final Vector p0, final String p1);
    
    private static native int readRegistryParallel(final Vector p0);
}
