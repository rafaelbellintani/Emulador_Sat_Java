// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

public class X509NameTokenizer
{
    private String value;
    private int index;
    private char seperator;
    private StringBuffer buf;
    
    public X509NameTokenizer(final String s) {
        this(s, ',');
    }
    
    public X509NameTokenizer(final String value, final char seperator) {
        this.buf = new StringBuffer();
        this.value = value;
        this.index = -1;
        this.seperator = seperator;
    }
    
    public boolean hasMoreTokens() {
        return this.index != this.value.length();
    }
    
    public String nextToken() {
        if (this.index == this.value.length()) {
            return null;
        }
        int i = this.index + 1;
        boolean b = false;
        int n = 0;
        this.buf.setLength(0);
        while (i != this.value.length()) {
            final char char1 = this.value.charAt(i);
            if (char1 == '\"') {
                if (n == 0) {
                    b = !b;
                }
                else {
                    this.buf.append(char1);
                }
                n = 0;
            }
            else if (n != 0 || b) {
                if (char1 == '#' && this.buf.charAt(this.buf.length() - 1) == '=') {
                    this.buf.append('\\');
                }
                else if (char1 == '+' && this.seperator != '+') {
                    this.buf.append('\\');
                }
                this.buf.append(char1);
                n = 0;
            }
            else if (char1 == '\\') {
                n = 1;
            }
            else {
                if (char1 == this.seperator) {
                    break;
                }
                this.buf.append(char1);
            }
            ++i;
        }
        this.index = i;
        return this.buf.toString().trim();
    }
}
