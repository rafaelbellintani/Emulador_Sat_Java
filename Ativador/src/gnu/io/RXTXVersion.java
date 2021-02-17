// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

public class RXTXVersion
{
    private static String Version;
    
    public static String getVersion() {
        return RXTXVersion.Version;
    }
    
    public static native String nativeGetVersion();
    
    static {
        System.loadLibrary("rxtxSerial");
        RXTXVersion.Version = "RXTX-2.1-7";
    }
}
