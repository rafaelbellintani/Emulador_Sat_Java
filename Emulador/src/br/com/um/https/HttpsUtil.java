// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.https;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import java.security.cert.Certificate;
import java.io.IOException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;
import java.io.InputStream;
import java.security.SecureRandom;
import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public abstract class HttpsUtil
{
    private static TrustManager[] trustManager;
    
    static {
        HttpsUtil.trustManager = null;
    }
    
    public static SSLContext getSSLContext(final String path, final String senha) {
        return getSSLContext(path, senha, "jks");
    }
    
    public static SSLContext getSSLContext(final String path, final String senha, final String type) {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            final KeyStore keyStore = KeyStore.getInstance(type);
            final InputStream keyInput = new FileInputStream(path);
            keyStore.load(keyInput, senha.toCharArray());
            keyInput.close();
            keyManagerFactory.init(keyStore, senha.toCharArray());
            if (HttpsUtil.trustManager == null) {
                HttpsUtil.trustManager = getTrustManager();
            }
            sc.init(keyManagerFactory.getKeyManagers(), HttpsUtil.trustManager, new SecureRandom());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sc;
    }
    
    public static HostnameVerifier getHostnameVerifier() {
        final HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession session) {
                return true;
            }
        };
        return hv;
    }
    
    public static void setTrustManager(final TrustManager[] trustManager) {
        HttpsUtil.trustManager = trustManager;
    }
    
    public static TrustManager[] getTrustManager() {
        HttpsUtil.trustManager = new TrustManager[1];
        final TrustManager tm = new miTM();
        HttpsUtil.trustManager[0] = tm;
        return HttpsUtil.trustManager;
    }
    
    public static void print_https_cert(final HttpsURLConnection con) {
        if (con != null) {
            try {
                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");
                final Certificate[] certs = con.getServerCertificates();
                Certificate[] array;
                for (int length = (array = certs).length, i = 0; i < length; ++i) {
                    final Certificate cert = array[i];
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }
            }
            catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public static class miTM implements TrustManager, X509TrustManager
    {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        
        public boolean isServerTrusted(final X509Certificate[] certs) {
            return true;
        }
        
        public boolean isClientTrusted(final X509Certificate[] certs) {
            return true;
        }
        
        @Override
        public void checkServerTrusted(final X509Certificate[] certs, final String authType) throws CertificateException {
        }
        
        @Override
        public void checkClientTrusted(final X509Certificate[] certs, final String authType) throws CertificateException {
        }
    }
}
