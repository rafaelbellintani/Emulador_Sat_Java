// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.exception;

import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;

public class ExtCertPathValidatorException extends CertPathValidatorException implements ExtException
{
    private Throwable cause;
    
    public ExtCertPathValidatorException(final String msg, final Throwable cause) {
        super(msg);
        this.cause = cause;
    }
    
    public ExtCertPathValidatorException(final String msg, final Throwable t, final CertPath certPath, final int index) {
        super(msg, t, certPath, index);
        this.cause = t;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
