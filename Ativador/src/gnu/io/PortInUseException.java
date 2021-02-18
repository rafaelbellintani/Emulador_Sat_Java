// 
// Decompiled by Procyon v0.5.36
// 

package gnu.io;

public class PortInUseException extends Exception
{
    public String currentOwner;
    
    PortInUseException(final String s) {
        super(s);
        this.currentOwner = s;
    }
    
    public PortInUseException() {
    }
}
