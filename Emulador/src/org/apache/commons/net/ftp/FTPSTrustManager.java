// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.net.ftp;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class FTPSTrustManager implements X509TrustManager
{
    public void checkClientTrusted(final X509Certificate[] certificates, final String authType) {
    }
    
    public void checkServerTrusted(final X509Certificate[] certificates, final String authType) throws CertificateException {
        for (int i = 0; i < certificates.length; ++i) {
            certificates[i].checkValidity();
        }
    }
    
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
