// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.controles;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.security.cert.CertificateFactory;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.security.Signature;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.PrivateKey;
import java.security.Key;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.KeyManager;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.security.KeyStore;

public class ControleKeyStore
{
    private String path;
    private String senha;
    private KeyStore keyStore;
    private static String tipoKeyStore;
    
    static {
        ControleKeyStore.tipoKeyStore = "pkcs12";
    }
    
    public ControleKeyStore(final String path, final String senha) {
        this.path = null;
        this.senha = null;
        this.keyStore = null;
        this.path = path;
        this.senha = senha;
    }
    
    public boolean carregarKeyStore() {
        try {
            if (this.senha == null) {
                throw new Exception("Erro ao carregar KeyStore. senha invalida!");
            }
            if (this.path == null) {
                throw new Exception("Erro ao carregar KeyStore. caminho invalido!");
            }
            InputStream is = null;
            if (this.existeKeyStore()) {
                is = new FileInputStream(new File(this.path));
            }
            (this.keyStore = KeyStore.getInstance(ControleKeyStore.tipoKeyStore)).load(is, this.senha.toCharArray());
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void salvarKeyStore() {
        try {
            if (this.keyStore == null || this.senha == null) {
                throw new Exception("Falha ao salvar keyStore");
            }
            final OutputStream out = new FileOutputStream(new File(this.path));
            this.keyStore.store(out, this.senha.toCharArray());
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public KeyManager[] getKeyManager() {
        try {
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunX509");
            kmf.init(this.keyStore, this.senha.toCharArray());
            return kmf.getKeyManagers();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public TrustManager[] getTrustManager() {
        try {
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunX509");
            tmf.init(this.keyStore);
            return tmf.getTrustManagers();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static TrustManager[] getTrustAllCerts() {
        final TrustManager[] trustAllCerts = { new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    System.out.println("trustAllCerts");
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
    
    public Key getPrivateKey() throws Exception {
        if (this.keyStore == null) {
            throw new Exception("Erro ao carregar chave privada!");
        }
        return this.keyStore.getKey("privateKey", this.senha.toCharArray());
    }
    
    public void setPrivateKey(final PrivateKey privateKey, final byte[] pathCert) {
    }
    
    public void setPrivateKey(final PrivateKey privateKey, final Certificate[] certs) {
        try {
            this.keyStore.setKeyEntry("privateKey", privateKey, this.senha.toCharArray(), certs);
            this.salvarKeyStore();
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }
    
    public void setPrivateKey(final PrivateKey privateKey, final Certificate cert) {
        final Certificate[] chain = { cert };
        this.setPrivateKey(privateKey, chain);
    }
    
    public Certificate getCertificado() {
        try {
            if (this.keyStore != null) {
                return this.keyStore.getCertificate("privateKey");
            }
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Certificate[] getCadeiaCertificado() {
        try {
            if (this.keyStore != null) {
                return this.keyStore.getCertificateChain("privateKey");
            }
        }
        catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Deprecated
    public String gerarAssinatura(final PrivateKey key, final byte[] conteudo) {
        Signature sign = null;
        if (key == null) {
            return null;
        }
        try {
            sign = Signature.getInstance("SHA1withRSA");
            sign.initSign(key);
            sign.update(conteudo);
            return new String(sign.sign());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String gerarAssinaturaSHA1(final PrivateKey key, final byte[] conteudo) {
        Signature sign = null;
        if (key == null) {
            return null;
        }
        try {
            sign = Signature.getInstance("SHA1withRSA");
            sign.initSign(key);
            sign.update(conteudo);
            return new String(Base64.encode(sign.sign(), 0));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public String gerarAssinaturaSHA256(final PrivateKey key, final byte[] conteudo) {
        Signature sign = null;
        if (key == null) {
            return null;
        }
        try {
            sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(key);
            sign.update(conteudo);
            return new String(Base64.encode(sign.sign(), 0));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private boolean existeKeyStore() {
        final File file = new File(this.path);
        return file.exists();
    }
    
    private static InputStream getInputStream(final String path) throws Exception {
        final BufferedReader br = new BufferedReader(new FileReader(path));
        final StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        final String s = ControleDados.formatarCertificado(sb.toString(), true);
        final InputStream io = new ByteArrayInputStream(s.getBytes());
        return io;
    }
    
    public static Certificate parseCertificado(final String path) throws Exception {
        final InputStream io = getInputStream(path);
        return parseCertificado(io);
    }
    
    public static Certificate parseCertificado(final byte[] bytes) throws Exception {
        final InputStream io = new ByteArrayInputStream(bytes);
        return parseCertificado(io);
    }
    
    private static Certificate parseCertificado(final InputStream io) throws Exception {
        final BufferedInputStream bis = new BufferedInputStream(io);
        final CertificateFactory cf = CertificateFactory.getInstance("X.509");
        if (bis.available() > 0) {
            final Certificate cert = cf.generateCertificate(bis);
            bis.close();
            return cert;
        }
        return null;
    }
    
    public static Certificate[] parseCadeiaCertificacao(String cadeia) {
        try {
            final List<Certificate> list = new ArrayList<Certificate>();
            final String inicio = "-----BEGIN CERTIFICATE-----";
            final String fim = "-----END CERTIFICATE-----";
            do {
                String cert = cadeia.substring(0, cadeia.indexOf(fim) + fim.length());
                cert = ControleDados.formatarCertificado(cert, true);
                list.add(parseCertificado(cert.getBytes()));
                cadeia = cadeia.substring(cadeia.indexOf(fim) + fim.length());
            } while (cadeia != null && cadeia.indexOf(inicio) != -1);
            final Certificate[] certs = new Certificate[list.size()];
            for (int i = 0; i < list.size(); ++i) {
                certs[i] = list.get(i);
            }
            return certs;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getCommonName(final Certificate certificate) {
        try {
            if (certificate == null) {
                return null;
            }
            final Pattern pattern = Pattern.compile("CN=([^\\,]*)");
            final Matcher matcher = pattern.matcher(certificate.toString());
            matcher.find();
            return matcher.group(1);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void setPathKeyStore(final String path) {
        this.path = path;
    }
    
    public void setSenha(final String senha) {
        this.senha = senha;
    }
    
    public KeyStore getKeyStore() {
        return this.keyStore;
    }
    
    public static String getTipoKeyStore() {
        return ControleKeyStore.tipoKeyStore;
    }
}
