// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.comunicacao;

import java.io.OutputStream;
import javax.net.ssl.SSLSocketFactory;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.KeyManagerFactory;
import br.com.um.controles.ControleKeyStore;
import java.net.URL;

public class ConexaoSoapHttps implements Conexao
{
    private String response;
    private String request;
    private String pathKeyStore;
    private String senhaKeyStore;
    private boolean isAutenticacaoMutua;
    private boolean debugSSL;
    
    public ConexaoSoapHttps(final String pathKeyStore, final String senhaKeyStore, final boolean isAutenticacaoMutua, final boolean debugSSL) {
        this.response = null;
        this.request = null;
        this.pathKeyStore = null;
        this.senhaKeyStore = null;
        this.isAutenticacaoMutua = true;
        this.debugSSL = false;
        this.pathKeyStore = pathKeyStore;
        this.senhaKeyStore = senhaKeyStore;
        this.isAutenticacaoMutua = isAutenticacaoMutua;
        this.debugSSL = debugSSL;
    }
    
    public ConexaoSoapHttps(final String pathKeyStore, final String senhaKeyStore) {
        this.response = null;
        this.request = null;
        this.pathKeyStore = null;
        this.senhaKeyStore = null;
        this.isAutenticacaoMutua = true;
        this.debugSSL = false;
        this.pathKeyStore = pathKeyStore;
        this.senhaKeyStore = senhaKeyStore;
    }
    
    public ConexaoSoapHttps() {
        this.response = null;
        this.request = null;
        this.pathKeyStore = null;
        this.senhaKeyStore = null;
        this.isAutenticacaoMutua = true;
        this.debugSSL = false;
    }
    
    @Override
    public boolean consumir(final URL url, final String soapAction) {
        try {
            if (this.isAutenticacaoMutua && this.senhaKeyStore == null) {
                throw new Exception("Erro: Senha do Key Store n\u00e3o informada!");
            }
            if (this.isAutenticacaoMutua && this.pathKeyStore == null) {
                throw new Exception("Erro: Caminho do Key Store n\u00e3o informado!");
            }
            if (this.request == null) {
                throw new Exception("Erro: dados de entrada do SOAP n\u00e3o informado!");
            }
            if (this.debugSSL) {
                System.setProperty("javax.net.debug", "ssl");
            }
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            final ControleKeyStore keyStore = new ControleKeyStore(this.pathKeyStore, this.senhaKeyStore);
            keyStore.carregarKeyStore();
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore.getKeyStore(), this.senhaKeyStore.toCharArray());
            final SSLContext context = SSLContext.getInstance("ssl");
            if (this.isAutenticacaoMutua) {
                context.init(kmf.getKeyManagers(), ControleKeyStore.getTrustAllCerts(), null);
            }
            else {
                context.init(null, ControleKeyStore.getTrustAllCerts(), null);
            }
            final SSLSocketFactory sslSocketFactory = context.getSocketFactory();
            final HttpsURLConnection conexao = (HttpsURLConnection)url.openConnection();
            conexao.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(final String arg0, final SSLSession arg1) {
                    return true;
                }
            });
            conexao.setSSLSocketFactory(sslSocketFactory);
            conexao.setDoOutput(true);
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            conexao.setRequestProperty("SOAPAction", soapAction);
            final String reqXML = this.request;
            final OutputStream reqStream = conexao.getOutputStream();
            reqStream.write(reqXML.getBytes("utf-8"));
            BufferedReader rd = null;
            try {
                rd = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "utf-8"));
            }
            catch (Exception e2) {
                try {
                    rd = new BufferedReader(new InputStreamReader(conexao.getErrorStream(), "utf-8"));
                }
                catch (Exception e3) {
                    System.out.println(String.valueOf(conexao.getResponseCode()) + " - " + conexao.getResponseMessage());
                    throw new Exception();
                }
            }
            int character = 0;
            final StringBuffer strBuffer = new StringBuffer(conexao.getContentLength() + 100);
            while ((character = rd.read()) != -1) {
                strBuffer.append((char)character);
            }
            this.response = strBuffer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public void setPathKeyStore(final String path) {
        this.pathKeyStore = path;
    }
    
    @Override
    public String getResponse() {
        return this.response;
    }
    
    @Override
    public void setRequest(final String request) {
        this.request = request;
    }
    
    public void setAutenticacaoMutua(final boolean isAutenticacaoMutua) {
        this.isAutenticacaoMutua = isAutenticacaoMutua;
    }
    
    public void setDebugSSL(final boolean debugSSL) {
        this.debugSSL = debugSSL;
    }
}
