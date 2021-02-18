// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.conexao;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.io.OutputStream;
import javax.net.ssl.SSLSocketFactory;
import java.net.UnknownHostException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import br.com.um.controles.ControleKeyStore;
import br.com.satcfe.satbl.Configuracoes;
import javax.net.ssl.SSLContext;
import java.net.URL;

public class ConexaoSoapHttps implements Conexao
{
    private String response;
    private String request;
    private String responseCode;
    private String responseMessage;
    private boolean isAutenticacaoMutua;
    private boolean debug;
    private int timeout;
    
    public ConexaoSoapHttps() {
        this.response = null;
        this.request = null;
        this.responseCode = null;
        this.responseMessage = null;
        this.isAutenticacaoMutua = true;
        this.debug = false;
        this.timeout = 0;
    }
    
    @Override
    public boolean consumir(final URL url, final String soapAction) {
        HttpsURLConnection conexao = null;
        try {
            System.err.println(url);
            if (this.debug) {
                System.setProperty("javax.net.debug", "ssl");
            }
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            final SSLContext context = SSLContext.getInstance("TLSv1");
            final ControleKeyStore keyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
            keyStore.carregarKeyStore();
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore.getKeyStore(), "123456".toCharArray());
            context.init(keyStore.getKeyManager(), getTrustAllCerts(), null);
            final SSLSocketFactory sslSocketFactory = context.getSocketFactory();
            conexao = (HttpsURLConnection)url.openConnection();
            conexao.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(final String arg0, final SSLSession arg1) {
                    return true;
                }
            });
            conexao.setConnectTimeout(this.timeout);
            conexao.setReadTimeout(this.timeout);
            conexao.setSSLSocketFactory(sslSocketFactory);
            conexao.setDoOutput(true);
            conexao.setRequestMethod("POST");
            conexao.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            conexao.setRequestProperty("SOAPAction", soapAction);
            final String reqXML = this.request;
            final OutputStream reqStream = conexao.getOutputStream();
            reqStream.write(reqXML.getBytes("utf-8"));
            BufferedReader rd = null;
            int length = 0;
            try {
                rd = new BufferedReader(new InputStreamReader(conexao.getInputStream(), "utf-8"));
                this.responseCode = String.valueOf(conexao.getResponseCode());
                this.responseMessage = conexao.getResponseMessage();
                length = conexao.getContentLength();
            }
            catch (Exception e) {
                try {
                    this.responseCode = String.valueOf(conexao.getResponseCode());
                    this.responseMessage = conexao.getResponseMessage();
                    rd = new BufferedReader(new InputStreamReader(conexao.getErrorStream(), "utf-8"));
                    length = conexao.getContentLength();
                }
                catch (Exception e4) {
                    System.out.println(String.valueOf(conexao.getResponseCode()) + " - " + conexao.getResponseMessage());
                    e.printStackTrace();
                    throw new Exception();
                }
            }
            int character = 0;
            final StringBuffer strBuffer = new StringBuffer(length + 100);
            while ((character = rd.read()) != -1) {
                strBuffer.append((char)character);
            }
            this.response = strBuffer.toString();
        }
        catch (UnknownHostException e2) {
            e2.printStackTrace();
            System.err.println(url);
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
        finally {
            if (conexao != null) {
                conexao.disconnect();
            }
        }
        if (conexao != null) {
            conexao.disconnect();
        }
        return true;
    }
    
    public static TrustManager[] getTrustAllCerts() {
        final TrustManager[] trustAllCerts = { new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                
                @Override
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                }
            } };
        return trustAllCerts;
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
    
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
    
    @Override
    public String getResponseCode() {
        return this.responseCode;
    }
    
    @Override
    public String getResponseMessage() {
        return this.responseMessage;
    }
    
    @Override
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }
    
    public int getTimeout() {
        return this.timeout;
    }
}
