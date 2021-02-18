// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.modelos.ComandoSefaz;
import java.util.Date;
import br.com.um.interfaces.NTPAdapter;
import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.MainSATBL;
import br.com.um.controles.StringUtil;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeParametrizacao;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeStatus;
import br.com.satcfe.satbl.modelos.StatusOperacionalSAT;
import java.io.IOException;
import br.com.satcfe.satbl.Main;
import java.io.File;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeAtualizacao;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeLogs;
import br.com.satcfe.satbl.threads.ThreadVerificacaoEnvio;
import br.com.satcfe.satbl.threads.ThreadEnvioNota;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import java.security.cert.Certificate;
import br.com.um.controles.ControleKeyStore;
import java.security.cert.CertificateEncodingException;
import br.com.satcfe.satbl.excecoes.SATAtivoException;
import br.com.satcfe.satbl.excecoes.ErroDesconhecidoException;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeCertificacao;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.Parametrizacoes;
import java.security.KeyPair;
import org.bouncycastle.crypto.params.RSAKeyParameters;

public class ComandosSefazFacade
{
    public static final String COMANDO_001 = "COMANDO_001";
    public static final String COMANDO_002 = "COMANDO_002";
    public static final String COMANDO_003 = "COMANDO_003";
    public static final String COMANDO_004 = "COMANDO_004";
    public static final String COMANDO_005 = "COMANDO_005";
    public static final String COMANDO_006 = "COMANDO_006";
    public static final String COMANDO_007 = "COMANDO_007";
    public static final String COMANDO_008 = "COMANDO_008";
    public static final String COMANDO_009 = "COMANDO_009";
    
    public boolean renovarCertificado(final boolean icpBrasil) {
        try {
            ControleLogs.logar("Iniciando a renova\u00e7\u00e3o do certificado digital do equipamento SAT");
            ControleLogs.logar("Gerando par de chaves.");
            final AsymmetricCipherKeyPair parDeChaves = ControleSeguranca.gerarChavesRSA();
            final String publica = ControleSeguranca.serializarChaveRSA((RSAKeyParameters)parDeChaves.getPublic());
            final String privada = ControleSeguranca.serializarChaveRSA((RSAKeyParameters)parDeChaves.getPrivate());
            final PublicKey publicKey = ControleSeguranca.deserializarChavePublicaRSA(publica);
            final PrivateKey privateKey = ControleSeguranca.deserializarChavePrivadaRSA(privada);
            ControleLogs.logar("Gerando Certificate Signing Request.");
            final String csr = ControleSeguranca.gerarCertificateSigningRequest(new KeyPair(publicKey, privateKey), "BR", "SEFAZ-SP", "AC-SAT", "AC-SAT", String.valueOf(Parametrizacoes.razaoSocialEmitente) + ":" + Parametrizacoes.CNPJ, Parametrizacoes.CNPJ, "SAO PAULO", Configuracoes.SAT.numeroDeSerie, false, icpBrasil);
            final String hashCodigoAcesso = ControleSeguranca.recuperarCodigoAtivacao();
            final String keystore = ControleDados.base64Encode(new String(ControleSeguranca.criptografarAES(ControleDados.gerarHashMD5(hashCodigoAcesso).getBytes(), (String.valueOf(publica) + ":" + privada).getBytes())));
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA, keystore.toCharArray());
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CSR, csr.toCharArray());
            final WebServiceCFeCertificacao wsCertificacao = new WebServiceCFeCertificacao();
            wsCertificacao.setCSR(csr);
            wsCertificacao.setOpt("CSR");
            ControleLogs.logar("Comunicando CSR ao Servidor da Sefaz");
            if (!wsCertificacao.consumirWebService()) {
                throw new ErroComunicacaoRetaguardaException();
            }
            if (wsCertificacao.getcStat() == null) {
                throw new ErroDesconhecidoException();
            }
            if (wsCertificacao.getcStat().equals("205")) {
                throw new SATAtivoException();
            }
            String cert = wsCertificacao.getCert();
            if (cert == null || cert.length() <= 100) {
                throw new CertificateEncodingException();
            }
            final ControleKeyStore cKeyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
            cKeyStore.carregarKeyStore();
            if (Configuracoes.SAT.protocolo.equals("http")) {
                final Certificate certificate = ControleKeyStore.parseCertificado(cert.getBytes());
                cKeyStore.setPrivateKey(privateKey, new Certificate[] { certificate });
            }
            else {
                Certificate[] chain = null;
                if (!Configuracoes.SAT.fixarCadeiaCert) {
                    chain = ControleKeyStore.parseCadeiaCertificacao(cert);
                }
                else {
                    cert = ControleDados.formatarCertificado(cert, true);
                    final Certificate raiz = ControleKeyStore.parseCertificado(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_KEYSTORE) + "raiz.cer");
                    final Certificate intermediario = ControleKeyStore.parseCertificado(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_KEYSTORE) + "intermediario.cer");
                    final Certificate c = ControleKeyStore.parseCertificado(cert.getBytes());
                    chain = new Certificate[] { c, intermediario, raiz };
                }
                cKeyStore.setPrivateKey(privateKey, chain);
            }
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT, cert.toCharArray());
            ControleLogs.logar("O Certificado Digital do SAT foi Renovado!");
            return true;
        }
        catch (Exception e) {
            ControleLogs.logar("Erro ao renovar o certificado ");
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean transmitirArquivosVenda() {
        try {
            if (!Configuracoes.SAT.bloqueado) {
                new ThreadEnvioNota().run();
                while (ThreadVerificacaoEnvio.verificando) {
                    Thread.sleep(1000L);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean transmitirArquivosLogs() {
        try {
            ControleLogs.logar("Iniciando Transmiss\u00e3o de Logs do SAT-CFe.");
            final String log = ControleLogs.getArquivoLogs();
            if (log == null) {
                ControleLogs.logar("Erro ao extrair os Logs do SAT.");
                return false;
            }
            final WebServiceCFeLogs wsLogs = new WebServiceCFeLogs();
            wsLogs.setLog(log);
            if (wsLogs.consumirWebService()) {
                if (wsLogs.getcStat() != null && wsLogs.getcStat().equals("118")) {
                    ControleLogs.logar("Logs extraidos com sucesso.");
                    return true;
                }
                ControleLogs.logar("Erro ao extrair os Logs do SAT.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Boolean atualizarSoftwareBasico(final String idCmd) {
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ATUALIZACOES)) {
            final String resultado = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ATUALIZACOES);
            ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ATUALIZACOES);
            ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_IDCMD);
            if (resultado.equalsIgnoreCase("OK")) {
                return true;
            }
            return false;
        }
        else {
            ControleLogs.logar(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_IDCMD);
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_IDCMD, idCmd.toCharArray());
            final WebServiceCFeAtualizacao wsAtualizacao = new WebServiceCFeAtualizacao();
            wsAtualizacao.setxServ("ATUALIZA");
            if (!wsAtualizacao.consumirWebService()) {
                return false;
            }
            final String urlSAT = wsAtualizacao.getUrlAtualizacao();
            final String hash = wsAtualizacao.getHash();
            ControleLogs.logar("Endere\u00e7o Software: " + urlSAT);
            if (hash != null) {
                ControleLogs.logar("Hash do arquivo: " + hash);
            }
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_HASH, hash.toCharArray());
            if (!ControleSeguranca.baixarSAT(urlSAT)) {
                ControleLogs.logar("Erro ao efetuar o download do software basico");
                return false;
            }
            ControleLogs.logar("Download do software basico concluido.");
            String pathArquivo = "/software_Basico/SAT-CFe-novo.jar";
            if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS) {
                pathArquivo = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO) + "SAT-CFe-novo.jar";
            }else if(Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.LINUX) {
            	pathArquivo = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO) + "SAT-CFe-novo.jar";
            }
            if (!hash.equalsIgnoreCase(ControleDados.getHashArquivo("SHA-256", pathArquivo))) {
                ControleLogs.logar("Erro na verifica\u00e7\u00e3o do hash do arquivo.");
                return false;
            }
            try {
                final String path = new File("").getAbsolutePath();
                String command = "/java/ejre/bin/java -jar /software_Basico/Atualizador-SAT.jar";
                if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS) {
                    command = "cmd /c java -jar \"" + path + "\\Atualizador-SAT.jar\"";
                }
                System.err.println("COMANDO EXECUTADO: " + command);
                final Runtime r = Runtime.getRuntime();
                r.exec(command);
                ControleLogs.logar("Fechando o Sistema!");
                Main.sair();
            }
            catch (IOException e) {
                e.printStackTrace();
                ControleLogs.logar("Erro ao Reiniciar o SAT-CFe");
                return false;
            }
            return null;
        }
    }
    
    public boolean transmitirEstadoOperacional() {
        ControleLogs.logar("iniciando envio do estado operacional do Equipamento SAT");
        final StatusOperacionalSAT status = new StatusOperacionalSAT();
        final WebServiceCFeStatus wsCFeStatus = new WebServiceCFeStatus("STATUS-SAT");
        wsCFeStatus.setStatus(status.getStatusXML());
        if (!wsCFeStatus.consumirWebService()) {
            ControleLogs.logar("Erro de Comunicac\u00e3o com a SEFAZ");
        }
        ControleLogs.logar("fim envio do estado operacional do Equipamento SAT");
        return true;
    }
    
    public boolean atualizarParametrizacao() {
        try {
            final WebServiceCFeParametrizacao wsCFeParametrizacao = new WebServiceCFeParametrizacao();
            if (!wsCFeParametrizacao.consumirWebService()) {
                throw new ErroComunicacaoRetaguardaException();
            }
            if (wsCFeParametrizacao.getcStat() == null || wsCFeParametrizacao.getcStat().equals("999")) {
                throw new ErroComunicacaoRetaguardaException();
            }
            if (wsCFeParametrizacao.getParamUso() != null) {
                if (Configuracoes.SAT.bloqueado) {
                    ControleLogs.logar("Iniciando desbloqueio do SAT");
                }
                else {
                    ControleLogs.logar("Iniciando atualizacao de parametrizacao do SAT");
                }
                String arquivoParam = wsCFeParametrizacao.getParamUso();
                if (StringUtil.isBase64(arquivoParam)) {
                    arquivoParam = StringUtil.base64DecodeUTF8(arquivoParam);
                }
                ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO, arquivoParam.toCharArray(), false);
                if (ControleParametrizacao.carregarParametrizacao(3) && Configuracoes.SAT.bloqueado) {
                    ControleParametrizacao.removerParametrizacaoBloqueio();
                }
                if (Configuracoes.SAT.bloqueado) {
                    ControleLogs.logar("SAT Desbloqueado com Sucesso");
                    Configuracoes.SAT.bloqueado = false;
                    this.transmitirEstadoOperacional();
                }
                else {
                    ControleLogs.logar("SAT Atualizou a Parametrizacao");
                }
                new MainSATBL().iniciarTarefas();
            }
            else if (wsCFeParametrizacao.getParamBloqueio() != null) {
                ControleLogs.logar("Iniciando bloqueio do SAT");
                String arquivoParam = wsCFeParametrizacao.getParamBloqueio();
                if (StringUtil.isBase64(arquivoParam)) {
                    arquivoParam = StringUtil.base64DecodeUTF8(arquivoParam);
                }
                ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO, arquivoParam.toCharArray(), false);
                if (ControleParametrizacao.carregarParametrizacao(4)) {
                    ControleParametrizacao.removerParametrizacaoUso();
                }
                new ThreadEnvioNota().run();
                while (ThreadVerificacaoEnvio.verificando) {
                    Thread.sleep(100L);
                }
                Configuracoes.SAT.bloqueado = true;
                this.transmitirStatusSAT();
                new MainSATBL().iniciarTarefas();
                ControleLogs.logar("SAT bloqueado com sucesso");
            }
        }
        catch (ErroComunicacaoRetaguardaException e2) {
            ControleLogs.logar("Erro de Comunicacao com a Sefaz.");
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    private void transmitirStatusSAT() {
        ControleLogs.logar("Iniciando Transmiss\u00e3o Status do SAT");
        final StatusOperacionalSAT status = new StatusOperacionalSAT();
        final WebServiceCFeStatus wsStatus = new WebServiceCFeStatus("STATUS-SAT");
        wsStatus.setStatus(status.getStatusXML());
        if (!wsStatus.consumirWebService()) {
            ControleLogs.logar("Erro de Comunica\u00e7\u00e3o com a Sefaz.");
        }
        else if (wsStatus.getcStat() == null) {
            ControleLogs.logar("Erro de Comunica\u00e7\u00e3o com a Sefaz.");
        }
        else if (wsStatus.getcStat().equals("109")) {
            ControleLogs.logar("Status do SAT Transmitido \u00e0 SEFAZ com sucesso!");
        }
        else {
            ControleLogs.logar("Erro Desconhecido na Transmiss\u00e3o");
        }
        ControleLogs.logar("Fim da Transmiss\u00e3o de Status do SAT");
    }
    
    public boolean sincronizarNTP() {
        final ControleTempo tempo = new ControleTempo();
        tempo.setNtp((NTPAdapter)new ControleNTP(Parametrizacoes.enderecoNtp, Parametrizacoes.portaNTP));
        if (tempo.atualizarNTPSinc()) {
            ControleLogs.logar("Data e Hora Sincronizada: " + new Date(ControleTempo.getCurrentTime()));
            return true;
        }
        ControleLogs.logar("Falha ao Sincronizar o rel\u00f3gio.");
        return false;
    }
    
    public boolean envioAvisoUsuario(final ComandoSefaz comando) {
        try {
            if (comando != null) {
                final String msg = comando.getxMsg();
                ControleSeguranca.salvarAviso(msg);
                return true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean consultarParametrosGestao(final ComandoSefaz comando) {
        try {
            ControleLogs.logar("Baixando par\u00e2metros de gest\u00e3o..");
            ControleParametrizacao.baixarParametrosDeGestao(Parametrizacoes.cUF, 300000L);
            ControleLogs.logar("carregando par\u00e2metros de gest\u00e3o.");
            ControleParametrizacao.carregarParametrizacao(5);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
