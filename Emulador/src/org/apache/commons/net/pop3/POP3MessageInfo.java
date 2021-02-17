// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.pop3;

public final class POP3MessageInfo
{
    public int number;
    public int size;
    public String identifier;
    
    public POP3MessageInfo() {
        final int n = 0;
        this.size = n;
        this.number = n;
        this.identifier = null;
    }
    
    public POP3MessageInfo(final int num, final int octets) {
        this.number = num;
        this.size = octets;
        this.identifier = null;
    }
    
    public POP3MessageInfo(final int num, final String uid) {
        this.number = num;
        this.size = -1;
        this.identifier = uid;
    }
}
