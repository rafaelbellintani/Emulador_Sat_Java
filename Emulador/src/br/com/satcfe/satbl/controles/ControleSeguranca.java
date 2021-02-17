// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import br.com.satcfe.satbl.modelos.PKCS10CertificationRequest;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.X509Extension;
import java.util.Vector;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.KeyFactory;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import br.com.satcfe.satbl.Parametrizacoes;
import java.util.Calendar;
import java.util.Random;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.CipherParameters;
import java.net.NetworkInterface;
import java.net.InetAddress;
import br.com.um.controles.ControleTempo;
import br.com.um.controles.StringUtil;
import br.com.um.controles.ControleDados;
import java.io.File;
import java.io.BufferedOutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.KeyPair;
import java.util.Iterator;
import java.io.OutputStream;
import java.io.FileOutputStream;
import br.com.satcfe.satbl.modelos.BaseCertificados;
import br.com.um.controles.ControleCadeiaCertificado;
import java.security.cert.X509Certificate;
import br.com.um.controles.ControleKeyStore;
import br.com.satcfe.satbl.Configuracoes;
import java.util.Date;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.cert.Certificate;

public class ControleSeguranca
{
    public static Certificate carregarCertificado(final String path) throws CertificateException, IOException {
        try {
            final InputStream fis = new FileInputStream(path);
            final BufferedInputStream bis = new BufferedInputStream(fis);
            final CertificateFactory cf = CertificateFactory.getInstance("X.509");
            if (bis.available() > 0) {
                final Certificate cert = cf.generateCertificate(bis);
                bis.close();
                return cert;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean certificadoValido() {
        final Date dataAtual = new Date();
        final ControleKeyStore keyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
        keyStore.carregarKeyStore();
        final X509Certificate cert = (X509Certificate)keyStore.getCertificado();
        if (cert != null) {
            if (dataAtual.after(cert.getNotAfter())) {
                return false;
            }
            if (dataAtual.before(cert.getNotBefore())) {
                return false;
            }
        }
        return true;
    }
    
    public static void carregarCadeiaCertificados() {
        try {
            final ControleCadeiaCertificado cadeia = ControleCadeiaCertificado.getInstance();
            cadeia.limpar();
            final BaseCertificados base = new BaseCertificados();
            base.setCaminhoCertHttps(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT_HTTPS);
            base.setCaminhoCertSefaz(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT_SEFAZ);
            base.carregar();
            for (final String cert : base.getCertHttps()) {
                final X509Certificate c = (X509Certificate)ControleKeyStore.parseCertificado(cert.getBytes());
                cadeia.addCertificado(c);
            }
            for (final String cert : base.getCertHttps()) {
                final X509Certificate c = (X509Certificate)ControleKeyStore.parseCertificado(cert.getBytes());
                cadeia.addCertificado(c);
            }
            
            cadeia.refatorar();
			/*cadeia.exibirCadeia((OutputStream) out);
            out.flush();
            out.close();*/
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized X509Certificate gerarCertificado(final KeyPair kp, final int months, final String issuerDN, final String subjectDN, final String domain, final String signAlgoritm) throws GeneralSecurityException, IOException {
        final PublicKey pubKey = kp.getPublic();
        final PrivateKey privKey = kp.getPrivate();
        final byte[] serno = new byte[8];
        final SecureRandom random = new SecureRandom();
        random.setSeed(new Date().getTime());
        random.nextBytes(serno);
        final BigInteger serial = new BigInteger(serno).abs();
        final X509V3CertificateGenerator certGenerator = new X509V3CertificateGenerator();
        certGenerator.reset();
        certGenerator.setSerialNumber(serial);
        certGenerator.setIssuerDN(new X509Name(issuerDN));
        certGenerator.setNotBefore(new Date(System.currentTimeMillis()));
        certGenerator.setNotAfter(new Date(System.currentTimeMillis() + months * 2592000000L));
        certGenerator.setSubjectDN(new X509Name(subjectDN));
        certGenerator.setPublicKey(pubKey);
        certGenerator.setSignatureAlgorithm(signAlgoritm);
        final boolean critical = subjectDN == null || "".equals(subjectDN.trim());
        final DERSequence othernameSequence = new DERSequence(new ASN1Encodable[] { (ASN1Encodable)new DERObjectIdentifier("1.3.6.1.5.5.7.8.5"), (ASN1Encodable)new DERTaggedObject(true, 0, (DEREncodable)new DERUTF8String(domain)) });
        final GeneralName othernameGN = new GeneralName(0, (ASN1Encodable)othernameSequence);
        final GeneralNames subjectAltNames = new GeneralNames((ASN1Sequence)new DERSequence(new ASN1Encodable[] { (ASN1Encodable)othernameGN }));
        certGenerator.addExtension(X509Extensions.SubjectAlternativeName, critical, (DEREncodable)subjectAltNames);
        final X509Certificate cert = certGenerator.generate(privKey);
        cert.checkValidity(new Date());
        cert.verify(pubKey);
        return cert;
    }
    
    public static boolean baixarSAT(final String urlSAT) {
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
        try {
            final SSLContext sc = SSLContext.getInstance("SSL");
            final ControleKeyStore keyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
            keyStore.carregarKeyStore();
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore.getKeyStore(), "123456".toCharArray());
            sc.init(keyStore.getKeyManager(), trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception ex) {}
        String path = "/software_Basico/SAT-CFe-novo.jar";
        if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS) {
            path = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO) + "SAT-CFe-novo.jar";
        }else if(Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.LINUX) {
        	path = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO) + "SAT-CFe-novo.jar";
        }
        if (urlSAT == null || urlSAT.length() < 10) {
            return false;
        }
        try {
            final BufferedInputStream in = new BufferedInputStream(new URL(urlSAT).openStream());
            final FileOutputStream fos = new FileOutputStream(path);
            final BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            final byte[] data = new byte[1024];
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return new File(path).exists();
    }
    
    public static String recuperarCodigoAtivacao() {
        try {
            final String codigo = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CODIGO_ATIVACAO);
            final String senha = ControleDados.gerarHashMD5("codigoAtivacao");
            if (codigo == null || codigo.length() < 10) {
                return null;
            }
            final String hashCodigoAtivacao = new String(descriptografarAES(senha.getBytes(), StringUtil.base64Decode(codigo)));
            return hashCodigoAtivacao;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Deprecated
    public static String retiraMascara(String texto) {
        texto = texto.replace(".", "").replace("-", "").replace("/", "").replace(" ", "").replace(",", "");
        return texto;
    }
    
    public static String carregarAviso() {
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_AVISO)) {
            final String aviso = ControleArquivos.lerArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_AVISO);
            ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_AVISO);
            return aviso;
        }
        return "";
    }
    
    public static void gravarCodigoAtivacao(final String codigoAtivacao) {
        try {
            String dados = null;
            final String senha = ControleDados.gerarHashMD5("codigoAtivacao");
            dados = StringUtil.base64Encode(criptografarAES(senha.getBytes(), ControleDados.gerarHashMD5(codigoAtivacao).getBytes()));
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CODIGO_ATIVACAO, dados.toCharArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void salvarAviso(final String aviso) {
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_AVISO, aviso.toCharArray());
    }
    
    public static boolean existeAviso() {
        return ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_AVISO);
    }
    
    public static void salvarDataHoraTransmissao() {
        final String timeStamp = ControleTempo.getTimeStamp();
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ULTIMA_TRANSMISSAO, timeStamp.toCharArray());
    }
    
    public static void salvarDataHoraComunicacao() {
        final String timeStamp = ControleTempo.getTimeStamp();
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ULTIMA_COMUNICACAO, timeStamp.toCharArray());
    }
    
    public static synchronized String getMacAddress() {
        String macAddress = "";
        try {
            final InetAddress localHost = InetAddress.getLocalHost();
            final NetworkInterface netInter = NetworkInterface.getByInetAddress(localHost);
            final byte[] macAddressBytes = netInter.getHardwareAddress();
            macAddress = String.format("%1$02x-%2$02x-%3$02x-%4$02x-%5$02x-%6$02x", macAddressBytes[0], macAddressBytes[1], macAddressBytes[2], macAddressBytes[3], macAddressBytes[4], macAddressBytes[5]).toUpperCase();
        }
        catch (Exception ex) {}
        return macAddress;
    }
    
    public static byte[] gerarAssinatura(final CipherParameters key, final byte[] conteudo) throws DataLengthException, CryptoException {
        final RSADigestSigner sig = new RSADigestSigner((Digest)new SHA1Digest());
        sig.init(true, key);
        sig.update(conteudo, 0, conteudo.length);
        final byte[] fim = sig.generateSignature();
        return fim;
    }
    
    public static final AsymmetricCipherKeyPair gerarChavesRSA() {
        final SecureRandom sr = new SecureRandom();
        final BigInteger pubExp = new BigInteger("10001", 16);
        final RSAKeyGenerationParameters RSAKeyGenPara = new RSAKeyGenerationParameters(pubExp, sr, 2048, 80);
        final RSAKeyPairGenerator RSAKeyPairGen = new RSAKeyPairGenerator();
        RSAKeyPairGen.init((KeyGenerationParameters)RSAKeyGenPara);
        final AsymmetricCipherKeyPair keyPair = RSAKeyPairGen.generateKeyPair();
        return keyPair;
    }
    
    @Deprecated
    public static final byte[] criptografarRSA(final RSAKeyParameters key, final byte[] conteudo) {
        final RSAEngine rsaCifrador = new RSAEngine();
        rsaCifrador.init(true, (CipherParameters)key);
        final byte[] resultado = rsaCifrador.processBlock(conteudo, 0, conteudo.length);
        return resultado;
    }
    
    @Deprecated
    public static byte[] desCriptografarRSA(final RSAKeyParameters key, final byte[] conteudo) {
        final RSAEngine rsaCifrador = new RSAEngine();
        rsaCifrador.init(false, (CipherParameters)key);
        final byte[] resultado = rsaCifrador.processBlock(conteudo, 0, conteudo.length);
        return resultado;
    }
    
    @Deprecated
    public static boolean verificarAssinatura(final CipherParameters key, final byte[] conteudo, final byte[] assinatura) throws DataLengthException, CryptoException {
        final RSADigestSigner sig = new RSADigestSigner((Digest)new SHA1Digest());
        sig.init(false, key);
        sig.update(conteudo, 0, conteudo.length);
        final boolean fim = sig.verifySignature(assinatura);
        return fim;
    }
    
    public static final byte[] criptografarAES(final byte[] key, final byte[] ptBytes) {
        final PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher((BlockCipher)new CBCBlockCipher((BlockCipher)new AESEngine()));
        cipher.init(true, (CipherParameters)new KeyParameter(key));
        final byte[] resultado = new byte[cipher.getOutputSize(ptBytes.length)];
        final int tamanhoBuffer = cipher.processBytes(ptBytes, 0, ptBytes.length, resultado, 0);
        try {
            cipher.doFinal(resultado, tamanhoBuffer);
        }
        catch (Exception e) {
            ControleLogs.logar("ControleSeguranca.criptografarAES erro = " + e.toString());
        }
        return resultado;
    }
    
    public static final byte[] descriptografarAES(final byte[] key, final byte[] cipherText) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        final PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher((BlockCipher)new CBCBlockCipher((BlockCipher)new AESEngine()));
        cipher.init(false, (CipherParameters)new KeyParameter(key));
        final byte[] resultado = new byte[cipher.getOutputSize(cipherText.length)];
        final int tamanhoBuffer = cipher.processBytes(cipherText, 0, cipherText.length, resultado, 0);
        cipher.doFinal(resultado, tamanhoBuffer);
        return new String(resultado).trim().getBytes();
    }
    
    @Deprecated
    public static int getRandom(final int tamanho) {
        int max = 1;
        for (int i = 0; i < tamanho; ++i) {
            max *= 10;
        }
        final int resultado = new Random().nextInt(max);
        if (resultado >= max / 10) {
            return resultado;
        }
        return getRandom(tamanho);
    }
    
    @Deprecated
    public static synchronized String gerarNumDocFiscal() {
        String numDocFiscal = new String();
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL)) {
            String codigoAtualString = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL);
            int codigoAtualInteiro = 0;
            try {
                codigoAtualInteiro = Integer.parseInt(codigoAtualString);
            }
            catch (Exception e) {
                ControleLogs.logar("O seguinte erro foi contornado: ");
                ControleLogs.logar(e.toString());
                codigoAtualInteiro = 0;
            }
            codigoAtualString = String.valueOf(++codigoAtualInteiro);
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL, codigoAtualString.toCharArray());
            numDocFiscal = ControleDados.preencheCom(codigoAtualString, "0", 6, 1);
            return numDocFiscal;
        }
        ControleArquivos.criarArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL);
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL, "000000".toCharArray());
        return gerarNumDocFiscal();
    }
    
    @Deprecated
    public static String gerarNumDocFiscalTeste() {
        String numDocFiscal = "";
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE)) {
            String codigoAtualString = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE);
            int codigoAtualInteiro = 0;
            try {
                codigoAtualInteiro = Integer.parseInt(codigoAtualString);
            }
            catch (Exception e) {
                ControleLogs.logar("O seguinte erro foi contornado: ");
                ControleLogs.logar(e.toString());
            }
            codigoAtualString = String.valueOf(++codigoAtualInteiro);
            if (ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE, codigoAtualString.toCharArray())) {
                numDocFiscal = ControleDados.preencheCom(codigoAtualString, "0", 6, 1);
            }
            return numDocFiscal;
        }
        ControleArquivos.criarArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE);
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE, "000000".toCharArray());
        return gerarNumDocFiscalTeste();
    }
    
    @Deprecated
    public static String getNumDocFiscal() {
        String numDocFiscal = "";
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL)) {
            String codigoAtualString = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL);
            try {
                Integer.parseInt(codigoAtualString);
            }
            catch (Exception e) {
                ControleLogs.logar("O seguinte erro foi contornado: ");
                ControleLogs.logar(e.toString());
                codigoAtualString = "1";
            }
            numDocFiscal = ControleDados.preencheCom(codigoAtualString, "0", 6, 1);
            return numDocFiscal;
        }
        return "0";
    }
    
    public static String gerarIdLote() {
        String idLote = "";
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE)) {
            String idAtualString = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE);
            int idAtualInteiro = 0;
            try {
                idAtualInteiro = Integer.parseInt(idAtualString);
            }
            catch (Exception e) {
                ControleLogs.logar("O seguinte erro foi contornado: ");
                ControleLogs.logar(e.toString());
            }
            idAtualString = String.valueOf(++idAtualInteiro);
            if (ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE, idAtualString.toCharArray())) {
                idLote = ControleDados.preencheCom(idAtualString, "0", 15, 1);
            }
            return idLote;
        }
        ControleArquivos.criarArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE);
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE, "000000000000000".toCharArray());
        return gerarIdLote();
    }
    
    public static String gerarIdLoteTeste() {
        String idLote = "";
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE_TESTE)) {
            String idAtualString = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE_TESTE);
            int idAtualInteiro = 0;
            try {
                idAtualInteiro = Integer.parseInt(idAtualString);
            }
            catch (Exception e) {
                ControleLogs.logar("O seguinte erro foi contornado: ");
                ControleLogs.logar(e.toString());
            }
            idAtualString = String.valueOf(++idAtualInteiro);
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE_TESTE, idAtualString.toCharArray());
            idLote = idAtualString;
            return idLote;
        }
        ControleArquivos.criarArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE_TESTE);
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE_TESTE, "000000000000000".toCharArray());
        return gerarIdLoteTeste();
    }
    
    @Deprecated
    public static String getIdLoteTeste() {
        String idLote = "";
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE_TESTE)) {
            String idAtualString = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE_TESTE);
            try {
                Integer.parseInt(idAtualString);
            }
            catch (Exception e) {
                ControleLogs.logar("O seguinte erro foi contornado: ");
                ControleLogs.logar(e.toString());
                idAtualString = "1";
            }
            idLote = ControleDados.preencheCom(idAtualString, "0", 15, 1);
            return idLote;
        }
        return "0";
    }
    
    @Deprecated
    public static String gerarCodigoDeAcesso() {
        final Calendar cal = Calendar.getInstance();
        String mes = String.valueOf(cal.get(2) + 1);
        final String ano = String.valueOf(cal.get(1)).substring(2, 4);
        if (mes.length() < 2) {
            mes = "0" + mes;
        }
        final char[] chave = (String.valueOf(Parametrizacoes.cUF) + ano + mes + Configuracoes.SAT.CNPJEstabelecimento + "59" + Configuracoes.SAT.numeroDeSerie + gerarNumDocFiscal() + getRandom(6)).toCharArray();
        int numeroSequencia = 2;
        int multiplicacao = 0;
        for (int i = 1; i <= chave.length; ++i) {
            multiplicacao += Integer.parseInt(new String(new char[] { chave[chave.length - i] })) * numeroSequencia;
            if (++numeroSequencia == 10) {
                numeroSequencia = 2;
            }
        }
        final int moduloOnze = (multiplicacao % 11 == 1 || multiplicacao % 11 == 0) ? 0 : (11 - multiplicacao % 11);
        return String.valueOf(new String(chave)) + moduloOnze;
    }
    
    public static String[] gerarInformacoesCodigoDeAcesso(final int tipo) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ControleTempo.getCurrentTime());
        String mes = String.valueOf(cal.get(2) + 1);
        String dia = String.valueOf(cal.get(5));
        final String ano = String.valueOf(cal.get(1));
        String horas = String.valueOf(cal.get(11));
        String minutos = String.valueOf(cal.get(12));
        String segundos = String.valueOf(cal.get(13));
        if (dia.length() < 2) {
            dia = "0" + dia;
        }
        if (mes.length() < 2) {
            mes = "0" + mes;
        }
        if (horas.length() < 2) {
            horas = "0" + horas;
        }
        if (minutos.length() < 2) {
            minutos = "0" + minutos;
        }
        if (segundos.length() < 2) {
            segundos = "0" + segundos;
        }
        final String UF = Parametrizacoes.cUF;
        final String NF = String.valueOf(ControleDados.getRandom(6));
        final String mod = "59";
        final String nserieSAT = Configuracoes.SAT.numeroDeSerie;
        String nCFe = null;
        final ControleNumeracaoSAT cNumeracao = new ControleNumeracaoSAT();
        if (tipo == 1) {
            nCFe = cNumeracao.gerarNumDocFiscal();
        }
        else {
            nCFe = cNumeracao.gerarNumDocFiscalTeste();
        }
        final String dEmit = String.valueOf(ano) + mes + dia;
        final String hEmit = String.valueOf(horas) + minutos + segundos;
        final StringBuffer sb = new StringBuffer();
        sb.append(UF).append(ano.substring(2)).append(mes).append(Configuracoes.SAT.CNPJEstabelecimento).append(mod).append(nserieSAT).append(nCFe).append(NF);
        final char[] chave = sb.toString().toCharArray();
        int numeroSequencia = 2;
        int multiplicacao = 0;
        for (int i = 1; i <= chave.length; ++i) {
            multiplicacao += Integer.parseInt(new String(new char[] { chave[chave.length - i] })) * numeroSequencia;
            if (++numeroSequencia == 10) {
                numeroSequencia = 2;
            }
        }
        final String cDV = new StringBuilder(String.valueOf((multiplicacao % 11 == 1 || multiplicacao % 11 == 0) ? 0 : (11 - multiplicacao % 11))).toString();
        final String tpAmb = Parametrizacoes.ambiente;
        return new String[] { UF, NF, mod, nserieSAT, nCFe, dEmit, hEmit, cDV, tpAmb };
    }
    
    @Deprecated
    public static String[] gerarInformacoesCodigoDeAcessoCancelamento() {
        final Calendar cal = Calendar.getInstance();
        String mes = String.valueOf(cal.get(2) + 1);
        String dia = String.valueOf(cal.get(5));
        final String ano = String.valueOf(cal.get(1));
        String horas = String.valueOf(cal.get(11));
        String minutos = String.valueOf(cal.get(12));
        String segundos = String.valueOf(cal.get(13));
        if (dia.length() < 2) {
            dia = "0" + dia;
        }
        if (mes.length() < 2) {
            mes = "0" + mes;
        }
        if (horas.length() < 2) {
            horas = "0" + horas;
        }
        if (minutos.length() < 2) {
            minutos = "0" + minutos;
        }
        if (segundos.length() < 2) {
            segundos = "0" + segundos;
        }
        final String UF = Parametrizacoes.cUF;
        final String NF = new StringBuilder(String.valueOf(ControleDados.getRandom(6))).toString();
        final String mod = "59";
        final String nserieSAT = Configuracoes.SAT.numeroDeSerie;
        String nCFe = "";
        nCFe = new ControleNumeracaoSAT().gerarNumDocFiscal();
        final String dEmit = String.valueOf(ano) + "-" + mes + "-" + dia;
        final String hEmit = String.valueOf(horas) + ":" + minutos + ":" + segundos;
        final char[] chave = (String.valueOf(UF) + ano.substring(2) + mes + Configuracoes.SAT.CNPJEstabelecimento + mod + nserieSAT + nCFe + NF).toCharArray();
        int numeroSequencia = 2;
        int multiplicacao = 0;
        for (int i = 1; i <= chave.length; ++i) {
            multiplicacao += Integer.parseInt(new String(new char[] { chave[chave.length - i] })) * numeroSequencia;
            if (++numeroSequencia == 10) {
                numeroSequencia = 2;
            }
        }
        final String cDV = new StringBuilder(String.valueOf((multiplicacao % 11 == 1 || multiplicacao % 11 == 0) ? 0 : (11 - multiplicacao % 11))).toString();
        return new String[] { UF, NF, mod, nserieSAT, nCFe, dEmit, hEmit, cDV };
    }
    
    public static String serializarChaveRSA(final RSAKeyParameters chave) throws IOException {
        final String delimitador = "|";
        if (chave.isPrivate()) {
            final RSAPrivateCrtKeyParameters chavePrivada = (RSAPrivateCrtKeyParameters)chave;
            final String modulo = StringUtil.base64Encode(chavePrivada.getModulus().toByteArray());
            final String expoentePublico = StringUtil.base64Encode(chavePrivada.getPublicExponent().toByteArray());
            final String expoentePrivado = StringUtil.base64Encode(chavePrivada.getExponent().toByteArray());
            final String dp = StringUtil.base64Encode(chavePrivada.getDP().toByteArray());
            final String dq = StringUtil.base64Encode(chavePrivada.getDQ().toByteArray());
            final String p = StringUtil.base64Encode(chavePrivada.getP().toByteArray());
            final String q = StringUtil.base64Encode(chavePrivada.getQ().toByteArray());
            final String qInv = StringUtil.base64Encode(chavePrivada.getQInv().toByteArray());
            final String chaveSerializada = String.valueOf(modulo) + delimitador + expoentePublico + delimitador + expoentePrivado + delimitador + dp + delimitador + dq + delimitador + p + delimitador + q + delimitador + qInv;
            return chaveSerializada;
        }
        final RSAKeyParameters chavePublica = chave;
        final String modulo = StringUtil.base64Encode(chavePublica.getModulus().toByteArray());
        final String expoente = StringUtil.base64Encode(chavePublica.getExponent().toByteArray());
        final String chaveSerializada2 = String.valueOf(modulo) + delimitador + expoente;
        return chaveSerializada2;
    }
    
    public static PublicKey deserializarPublicaRSA(final String parametros) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final String[] partes = parametros.split("\\|");
        final BigInteger modulus = new BigInteger(StringUtil.base64Decode(partes[0]));
        final BigInteger publicExponent = new BigInteger(StringUtil.base64Decode(partes[1]));
        return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
    }
    
    public static PrivateKey deserializarPrivadaRSA(final String parametros) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final String[] partes = parametros.split("\\|");
        final BigInteger modulus = new BigInteger(StringUtil.base64Decode(partes[0]));
        final BigInteger publicExponent = new BigInteger(StringUtil.base64Decode(partes[1]));
        final BigInteger privateExponent = new BigInteger(StringUtil.base64Decode(partes[2]));
        final BigInteger dp = new BigInteger(StringUtil.base64Decode(partes[3]));
        final BigInteger dq = new BigInteger(StringUtil.base64Decode(partes[4]));
        final BigInteger p = new BigInteger(StringUtil.base64Decode(partes[5]));
        final BigInteger q = new BigInteger(StringUtil.base64Decode(partes[6]));
        final BigInteger qInv = new BigInteger(StringUtil.base64Decode(partes[7]));
        return KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, p, q, dp, dq, qInv));
    }
    
    public static PublicKey deserializarChavePublicaRSA(final String parametros) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final String[] partes = parametros.split("\\|");
        final BigInteger modulus = new BigInteger(StringUtil.base64Decode(partes[0]));
        final BigInteger publicExponent = new BigInteger(StringUtil.base64Decode(partes[1]));
        return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
    }
    
    public static PrivateKey deserializarChavePrivadaRSA(final String parametros) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final String[] partes = parametros.split("\\|");
        final BigInteger modulus = new BigInteger(StringUtil.base64Decode(partes[0]));
        final BigInteger publicExponent = new BigInteger(StringUtil.base64Decode(partes[1]));
        final BigInteger privateExponent = new BigInteger(StringUtil.base64Decode(partes[2]));
        final BigInteger dp = new BigInteger(StringUtil.base64Decode(partes[3]));
        final BigInteger dq = new BigInteger(StringUtil.base64Decode(partes[4]));
        final BigInteger p = new BigInteger(StringUtil.base64Decode(partes[5]));
        final BigInteger q = new BigInteger(StringUtil.base64Decode(partes[6]));
        final BigInteger qInv = new BigInteger(StringUtil.base64Decode(partes[7]));
        return KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, p, q, dp, dq, qInv));
    }
    
    @Deprecated
    public static RSAKeyParameters gerarChavePublica(final byte[] key) throws IOException {
        return (RSAKeyParameters)PublicKeyFactory.createKey(key);
    }
    
    @Deprecated
    public static RSAKeyParameters gerarChavePrivada(final byte[] key) throws IOException {
        return (RSAKeyParameters)PrivateKeyFactory.createKey(key);
    }
    
    @Deprecated
    public static String gerarCertificateSigningRequest(final AsymmetricCipherKeyPair parDeChaves, final String C, final String O, final String OU, final String OU2, final String CN, final String CNPJ, final String ST, final String SN, final boolean formatoURL) throws DataLengthException, CryptoException, IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {
        String parametros = "";
        if (C != null || C == "") {
            parametros = String.valueOf(parametros) + "C=" + C;
        }
        if (O != null && O != "") {
            parametros = String.valueOf(parametros) + ",O=" + O;
        }
        if (OU != null && OU != "") {
            parametros = String.valueOf(parametros) + ",OU=" + OU;
        }
        if (OU2 != null && OU2 != "") {
            parametros = String.valueOf(parametros) + ",OU=" + OU2;
        }
        if (CN != null && CN != "") {
            parametros = String.valueOf(parametros) + ",CN=" + CN;
        }
        if (ST != null && ST != "") {
            parametros = String.valueOf(parametros) + ",ST=" + ST;
        }
        if (SN != null && SN != "") {
            parametros = String.valueOf(parametros) + ",SN=" + SN;
        }
        final X509Name nameCert = new X509Name(parametros);
        final String oid = "2.16.76.1.3.3";
        final DERObjectIdentifier oidCnpj = new DERObjectIdentifier(oid);
        final DERTaggedObject der = new DERTaggedObject(true, 0, (DEREncodable)new DEROctetString(CNPJ.getBytes()));
        final DERSequence seq = new DERSequence(new ASN1Encodable[] { (ASN1Encodable)oidCnpj, (ASN1Encodable)der });
        final GeneralName gn = new GeneralName(0, (ASN1Encodable)seq);
        final GeneralNames san = new GeneralNames(gn);
        final Vector oids = new Vector();
        final Vector values = new Vector();
        oids.add(X509Extensions.SubjectAlternativeName);
        values.add(new X509Extension(false, (ASN1OctetString)new DEROctetString((DEREncodable)san)));
        final X509Extensions extensions = new X509Extensions(oids, values);
        final Attribute attribute = new Attribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, (ASN1Set)new DERSet((DEREncodable)extensions));
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETROS_CSR, parametros.toCharArray());
        final PKCS10CertificationRequest cert = new PKCS10CertificationRequest(nameCert, (RSAKeyParameters)parDeChaves.getPublic(), (ASN1Set)new DERSet((DEREncodable)attribute), (RSAKeyParameters)parDeChaves.getPrivate());
        final String certificado64 = "-----BEGIN CERTIFICATE REQUEST-----\n" + ControleDados.formatarCertificado(StringUtil.base64Encode(cert.getDERObject().getDEREncoded()), false) + "\n" + "-----END CERTIFICATE REQUEST-----";
        return certificado64;
    }
    
    public static String gerarCertificateSigningRequest(final KeyPair parDeChaves, final String C, final String O, final String OU, final String OU2, final String CN, final String CNPJ, final String ST, final String SN, final boolean formatoURL, final boolean icpBrasil) throws DataLengthException, CryptoException, IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {
        String parametros = "";
        if (icpBrasil) {
            if (CN != null && CN != "") {
                parametros = String.valueOf(parametros) + "CN=" + CN;
            }
            parametros = String.valueOf(parametros) + ",C=BR";
            parametros = String.valueOf(parametros) + ",O=ICP-Brasil";
        }
        else {
            if (C != null || C == "") {
                parametros = String.valueOf(parametros) + "C=" + C;
            }
            if (O != null && O != "") {
                parametros = String.valueOf(parametros) + ",O=" + O;
            }
            if (OU != null && OU != "") {
                parametros = String.valueOf(parametros) + ",OU=" + OU;
            }
            if (OU2 != null && OU2 != "") {
                parametros = String.valueOf(parametros) + ",OU=" + OU2;
            }
            if (CN != null && CN != "") {
                parametros = String.valueOf(parametros) + ",CN=" + CN;
            }
            if (ST != null && ST != "") {
                parametros = String.valueOf(parametros) + ",ST=" + ST;
            }
            if (SN != null && SN != "") {
                parametros = String.valueOf(parametros) + ",SN=" + SN;
            }
        }
        final X509Name nameCert = new X509Name(parametros);
        final String oid = "2.16.76.1.3.3";
        final DERObjectIdentifier oidCnpj = new DERObjectIdentifier(oid);
        final DERTaggedObject der = new DERTaggedObject(true, 0, (DEREncodable)new DEROctetString(CNPJ.getBytes()));
        final DERSequence seq = new DERSequence(new ASN1Encodable[] { (ASN1Encodable)oidCnpj, (ASN1Encodable)der });
        final GeneralName gn = new GeneralName(0, (ASN1Encodable)seq);
        final GeneralNames san = new GeneralNames(gn);
        final Vector oids = new Vector();
        final Vector values = new Vector();
        oids.add(X509Extensions.SubjectAlternativeName);
        values.add(new X509Extension(false, (ASN1OctetString)new DEROctetString((DEREncodable)san)));
        final X509Extensions extensions = new X509Extensions(oids, values);
        final Attribute attribute = new Attribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, (ASN1Set)new DERSet((DEREncodable)extensions));
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETROS_CSR, parametros.toCharArray());
        DERSet atributo = new DERSet((DEREncodable)attribute);
        if (icpBrasil) {
            atributo = null;
        }
        final PKCS10CertificationRequest cert = new PKCS10CertificationRequest(nameCert, parDeChaves.getPublic(), (ASN1Set)atributo, parDeChaves.getPrivate());
        final String certificado64 = "-----BEGIN CERTIFICATE REQUEST-----\n" + ControleDados.formatarCertificado(StringUtil.base64Encode(cert.getDERObject().getDEREncoded()), false) + "\n" + "-----END CERTIFICATE REQUEST-----";
        return certificado64;
    }
    
    @Deprecated
    public static synchronized String formatarCertificado(String cert, final boolean isCertificate) {
        try {
            if (cert == null) {
                return null;
            }
            cert = cert.replace("-----BEGIN CERTIFICATE-----", "");
            cert = cert.replace("-----END CERTIFICATE-----", "");
            cert = cert.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("\\t", "");
            final byte[] array = cert.getBytes();
            final byte[] newArray = new byte[array.length + array.length / 64];
            for (int j = 0, i = 0, n = 1; i < array.length; ++i, ++n, ++j) {
                newArray[j] = array[i];
                if (n == 64) {
                    ++j;
                    n = 0;
                    newArray[j] = 10;
                }
            }
            if (isCertificate) {
                return "-----BEGIN CERTIFICATE-----\n" + new String(newArray) + '\n' + "-----END CERTIFICATE-----";
            }
            return new String(newArray);
        }
        catch (Exception e) {
            e.printStackTrace();
            return cert;
        }
    }
    
    public static boolean validarCodigoAtivacao(final String codigoAtivacao) {
        try {
            final String codigo = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CODIGO_ATIVACAO);
            if (codigo == null || codigo.length() < 10) {
                return false;
            }
            final String hashCodigoAtivacao = new String(descriptografarAES(ControleDados.gerarHashMD5("codigoAtivacao").getBytes(), StringUtil.base64Decode(codigo)));
            if (!hashCodigoAtivacao.equals(ControleDados.gerarHashMD5(codigoAtivacao))) {
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static boolean validarChaveAcesso(String chaveAcesso) {
        chaveAcesso = chaveAcesso.replace("CFe", "");
        if (chaveAcesso.equals("00000000000000000000000000000000000000000000")) {
            return false;
        }
        if (!ControleDados.validarModulo11(chaveAcesso)) {
            return false;
        }
        final String cnpj = chaveAcesso.substring(6, 20);
        return Configuracoes.SAT.emuladorOffLine || ControleDados.validarCNPJCPF(cnpj);
    }
}
