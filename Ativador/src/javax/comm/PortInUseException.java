// 
// Decompiled by Procyon v0.5.36
// 

package javax.comm;

public class PortInUseException extends Exception
{
    public String currentOwner;
    
    PortInUseException(final String s) {
        super("Port currently owned by " + s);
        this.currentOwner = s;
    }
}
