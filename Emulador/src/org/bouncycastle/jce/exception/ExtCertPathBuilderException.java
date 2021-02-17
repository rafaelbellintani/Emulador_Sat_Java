// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.exception;

import java.security.cert.CertPath;
import java.security.cert.CertPathBuilderException;

public class ExtCertPathBuilderException extends CertPathBuilderException implements ExtException
{
    private Throwable cause;
    
    public ExtCertPathBuilderException(final String msg, final Throwable cause) {
        super(msg);
        this.cause = cause;
    }
    
    public ExtCertPathBuilderException(final String msg, final Throwable t, final CertPath certPath, final int n) {
        super(msg, t);
        this.cause = t;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
