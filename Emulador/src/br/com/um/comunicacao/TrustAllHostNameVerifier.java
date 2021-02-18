// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.comunicacao;

import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;

public class TrustAllHostNameVerifier implements HostnameVerifier
{
    @Override
    public boolean verify(final String hostname, final SSLSession session) {
        System.out.println(hostname);
        return true;
    }
}
