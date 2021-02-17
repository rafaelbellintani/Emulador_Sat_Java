// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.exception;

import java.security.cert.CertificateEncodingException;

public class ExtCertificateEncodingException extends CertificateEncodingException implements ExtException
{
    private Throwable cause;
    
    public ExtCertificateEncodingException(final String message, final Throwable cause) {
        super(message);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
