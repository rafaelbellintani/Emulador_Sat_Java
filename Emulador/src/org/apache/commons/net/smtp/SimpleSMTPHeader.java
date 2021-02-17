// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.smtp;

public class SimpleSMTPHeader
{
    private String __subject;
    private String __from;
    private String __to;
    private StringBuffer __headerFields;
    private StringBuffer __cc;
    
    public SimpleSMTPHeader(final String from, final String to, final String subject) {
        this.__to = to;
        this.__from = from;
        this.__subject = subject;
        this.__headerFields = new StringBuffer();
        this.__cc = null;
    }
    
    public void addHeaderField(final String headerField, final String value) {
        this.__headerFields.append(headerField);
        this.__headerFields.append(": ");
        this.__headerFields.append(value);
        this.__headerFields.append('\n');
    }
    
    public void addCC(final String address) {
        if (this.__cc == null) {
            this.__cc = new StringBuffer();
        }
        else {
            this.__cc.append(", ");
        }
        this.__cc.append(address);
    }
    
    @Override
    public String toString() {
        final StringBuffer header = new StringBuffer();
        if (this.__headerFields.length() > 0) {
            header.append(this.__headerFields.toString());
        }
        header.append("From: ");
        header.append(this.__from);
        header.append("\nTo: ");
        header.append(this.__to);
        if (this.__cc != null) {
            header.append("\nCc: ");
            header.append(this.__cc.toString());
        }
        if (this.__subject != null) {
            header.append("\nSubject: ");
            header.append(this.__subject);
        }
        header.append('\n');
        header.append('\n');
        return header.toString();
    }
}
