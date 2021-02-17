// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import java.security.cert.CertificateEncodingException;

class ExtCertificateEncodingException extends CertificateEncodingException
{
    Throwable cause;
    
    ExtCertificateEncodingException(final String message, final Throwable cause) {
        super(message);
        this.cause = cause;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
