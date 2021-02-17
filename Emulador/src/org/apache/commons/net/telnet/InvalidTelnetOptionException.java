// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.telnet;

public class InvalidTelnetOptionException extends Exception
{
    private int optionCode;
    private String msg;
    
    public InvalidTelnetOptionException(final String message, final int optcode) {
        this.optionCode = -1;
        this.optionCode = optcode;
        this.msg = message;
    }
    
    @Override
    public String getMessage() {
        return this.msg + ": " + this.optionCode;
    }
}
