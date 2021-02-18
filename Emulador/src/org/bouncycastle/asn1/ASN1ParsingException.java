// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

public class ASN1ParsingException extends IllegalStateException
{
    private Throwable cause;
    
    ASN1ParsingException(final String s) {
        super(s);
    }
    
    ASN1ParsingException(final String s, final Throwable cause) {
        super(s);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
