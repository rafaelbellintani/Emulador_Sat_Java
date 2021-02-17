// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.comunicacao;

import java.net.URLConnection;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import br.com.um.controles.ControleKeyStore;
import java.net.URL;

public class DownloadHttps
{
    private String pathFile;
    private String pathKeyStore;
    private String pathTrustStore;
    private String senhaKeyStore;
    private boolean trustStore;
    private boolean isAutenticacaoMutua;
    private boolean debugSSL;
    
    public DownloadHttps(final String pathKeyStore, final String senhaKeyStore, final boolean isAutenticacaoMutua, final boolean debugSSL) {
        this.pathFile = null;
        this.pathKeyStore = null;
        this.pathTrustStore = null;
        this.senhaKeyStore = null;
        this.trustStore = true;
        this.isAutenticacaoMutua = true;
        this.debugSSL = false;
        this.pathKeyStore = pathKeyStore;
        this.senhaKeyStore = senhaKeyStore;
        this.isAutenticacaoMutua = isAutenticacaoMutua;
        this.debugSSL = debugSSL;
    }
    
    public DownloadHttps(final String pathKeyStore, final String senhaKeyStore) {
        this.pathFile = null;
        this.pathKeyStore = null;
        this.pathTrustStore = null;
        this.senhaKeyStore = null;
        this.trustStore = true;
        this.isAutenticacaoMutua = true;
        this.debugSSL = false;
        this.pathKeyStore = pathKeyStore;
        this.senhaKeyStore = senhaKeyStore;
    }
    
    public DownloadHttps() {
        this.pathFile = null;
        this.pathKeyStore = null;
        this.pathTrustStore = null;
        this.senhaKeyStore = null;
        this.trustStore = true;
        this.isAutenticacaoMutua = true;
        this.debugSSL = false;
    }
    
    public boolean consumir(final URL url) {
        try {
            if (this.isAutenticacaoMutua && this.senhaKeyStore == null) {
                throw new Exception("Erro: Senha do Key Store n\u00e3o informada!");
            }
            if (this.isAutenticacaoMutua && this.pathKeyStore == null) {
                throw new Exception("Erro: Caminho do Key Store n\u00e3o informado!");
            }
            if (this.debugSSL) {
                System.setProperty("javax.net.debug", "ssl");
            }
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            if (this.trustStore) {
                System.setProperty("javax.net.ssl.trustStore", this.pathTrustStore);
                System.setProperty("javax.net.ssl.trustStoreType", "JKS");
                System.setProperty("javax.net.ssl.trustStorePassword", this.senhaKeyStore);
            }
            if (this.isAutenticacaoMutua) {
                System.setProperty("javax.net.ssl.keyStore", this.pathKeyStore);
                System.setProperty("javax.net.ssl.keyStoreType", ControleKeyStore.getTipoKeyStore());
                System.setProperty("javax.net.ssl.keyStorePassword", this.senhaKeyStore);
            }
            final URLConnection conexao = url.openConnection();
            final SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, ControleKeyStore.getTrustAllCerts(), new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new TrustAllHostNameVerifier());
            BufferedInputStream in = null;
            try {
                in = new BufferedInputStream(conexao.getInputStream());
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            final FileOutputStream fos = new FileOutputStream(this.pathFile);
            final BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            final byte[] data = new byte[1024];
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
            return true;
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }
    
    public void setPathKeyStore(final String path) {
        this.pathKeyStore = path;
    }
    
    public void setPathTrustStore(final String pathTrustStore) {
        this.pathTrustStore = pathTrustStore;
    }
    
    public void setAutenticacaoMutua(final boolean isAutenticacaoMutua) {
        this.isAutenticacaoMutua = isAutenticacaoMutua;
    }
    
    public void setDebugSSL(final boolean debugSSL) {
        this.debugSSL = debugSSL;
    }
    
    public void setPathFile(final String pathFile) {
        this.pathFile = pathFile;
    }
    
    public void setTrustStore(final boolean b) {
        this.trustStore = b;
    }
}
