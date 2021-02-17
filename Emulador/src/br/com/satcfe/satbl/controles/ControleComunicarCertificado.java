// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.excecoes.SATAtivoException;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeCertificacao;
import java.security.cert.X509Certificate;
import br.com.um.controles.ControleCadeiaCertificado;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import java.security.spec.InvalidKeySpecException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.excecoes.TimeoutSATException;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.um.controles.ControleDados;
import br.com.um.controles.StringUtil;
import br.com.um.controles.ControleKeyStore;
import br.com.satcfe.satbl.excecoes.ErroDesconhecidoException;
import br.com.satcfe.satbl.Configuracoes;
import br.com.um.controles.ControleTempo;

public class ControleComunicarCertificado
{
    private long timeout;
    private long timestamp;
    private ControleNumeroSessao cNumeroSessao;
    
    public ControleComunicarCertificado(final ControleNumeroSessao cNumeroSessao) {
        this.timeout = 300000L;
        this.timestamp = 0L;
        this.cNumeroSessao = null;
        this.cNumeroSessao = cNumeroSessao;
    }
    
    public String tratarMensagem(final String codigoAtivacao, final String cadeiaCertificacao, final String aviso) {
        try {
            this.timestamp = ControleTempo.getCurrentTime();
            if (!Configuracoes.SAT.aguardandoCertificado) {
                throw new ErroDesconhecidoException();
            }
            if (Configuracoes.SAT.emuladorOffLine) {
                return String.valueOf(this.tratarEmuladorOffLine(cadeiaCertificacao)) + aviso;
            }
            Certificate[] cadeia = null;
            try {
                cadeia = ControleKeyStore.parseCadeiaCertificacao(cadeiaCertificacao);
                cadeia = this.organizarCadeiaCert(cadeia);
            }
            catch (Exception e5) {
                return "05099|Erro na cadeia de certificados||" + aviso;
            }
            String crt = ControleDados.formatarCertificado(StringUtil.base64Encode(cadeia[0].getEncoded()), true);
            if (Configuracoes.SAT.informarCadeia && cadeia.length > 1) {
                for (int i = 1; i < cadeia.length; ++i) {
                    final String cert = ControleDados.formatarCertificado(StringUtil.base64Encode(cadeia[i].getEncoded()), true);
                    crt = String.valueOf(crt) + "\n" + cert;
                }
            }
            if (!this.comunicarCrtACSEFAZ(crt, this.timeout).equalsIgnoreCase("122")) {
                throw new ErroComunicacaoRetaguardaException();
            }
            ControleLogs.logar("Certificado Comunicado com sucesso!");
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT, cadeiaCertificacao.toCharArray());
            final PrivateKey privada = this.carregarChavePrivada(codigoAtivacao);
            final ControleKeyStore cKeyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
            cKeyStore.carregarKeyStore();
            cKeyStore.setPrivateKey(privada, cadeia);
            this.timeout -= ControleTempo.getCurrentTime() - this.timestamp;
            if (this.timeout <= 0L) {
                throw new TimeoutSATException();
            }
            ControleLogs.logar("Baixando parametriza\u00e7\u00e3o de uso.");
            ControleParametrizacao.baixarParametrizacaoUso(Parametrizacoes.cUF, this.timeout);
            ControleLogs.logar("carregando parametriza\u00e7\u00e3o de uso.");
            ControleParametrizacao.carregarParametrizacao(3);
            Configuracoes.SAT.ativado = true;
            Configuracoes.SAT.aguardandoCertificado = false;
            ControleLogs.logar("FIM COMUNICACAO CERTIFICADO");
            ControleLogs.logar("SAT ATIVADO COM SUCESSO");
            return "05001|Certificado transmitido com Sucesso||" + aviso;
        }
        catch (ErroDesconhecidoException e) {
            ControleLogs.logar("ERRO: nao foi iniciada a ativacao.");
            e.printStackTrace();
        }
        catch (ErroComunicacaoRetaguardaException e2) {
            ControleLogs.logar("Erro na comunicacao com a sefaz");
            e2.printStackTrace();
        }
        catch (TimeoutSATException e3) {
            ControleLogs.logar("ERRO: O Timeout da fun\u00e7\u00e3o foi ultrapassado.");
            e3.printStackTrace();
        }
        catch (Exception e4) {
            ControleLogs.logar("ERRO DESCONHECIDO");
            e4.printStackTrace();
        }
        return "05099|Erro desconhecido||" + aviso;
    }
    
    private PrivateKey carregarChavePrivada(final String codigoAtivacao) throws InvalidKeySpecException, InvalidCipherTextException, NoSuchAlgorithmException {
        final String base64ks = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA);
        byte[] ks;
        try {
            ks = StringUtil.base64Decode(base64ks);
        }
        catch (Exception e) {
            throw new InvalidKeySpecException();
        }
        final byte[] ksDescriptografado = ControleSeguranca.descriptografarAES(ControleDados.gerarHashMD5(codigoAtivacao).getBytes(), ks);
        final String[] keyStore = new String(ksDescriptografado).split("\\:");
        final PrivateKey privada = ControleSeguranca.deserializarPrivadaRSA(keyStore[1]);
        return privada;
    }
    
    private Certificate[] organizarCadeiaCert(final Certificate[] cadeia) {
        if (cadeia.length == 0) {
            return null;
        }
        final ControleCadeiaCertificado cCadeia = ControleCadeiaCertificado.createNewInstance();
        cCadeia.limpar();
        for (int i = 0; i < cadeia.length; ++i) {
            final X509Certificate c = (X509Certificate)cadeia[i];
            cCadeia.addCertificado(c);
        }
        cCadeia.refatorar();
        return cCadeia.cadeiaToArray();
    }
    
    private String comunicarCrtACSEFAZ(final String crt, final long timeout) throws ErroComunicacaoRetaguardaException, ErroDesconhecidoException, SATAtivoException {
        final WebServiceCFeCertificacao wsCertificacao = new WebServiceCFeCertificacao();
        wsCertificacao.setOpt("CRT");
        wsCertificacao.setCRT(crt);
        wsCertificacao.setTimeout(timeout);
        if (!wsCertificacao.consumirWebService()) {
            throw new ErroComunicacaoRetaguardaException();
        }
        if (wsCertificacao.getcStat() == null) {
            throw new ErroDesconhecidoException();
        }
        if (wsCertificacao.getcStat().equals("205")) {
            throw new SATAtivoException();
        }
        return wsCertificacao.getcStat();
    }
    
    private String tratarEmuladorOffLine(final String certificado) {
        try {
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT, certificado.toCharArray());
            final String s = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SAT_OFFLINE);
            final String[] r = ControleDados.quebrarString(s, ",");
            final String paramUso = StringUtil.base64DecodeUTF8(r[2]);
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO, paramUso.toCharArray());
            ControleLogs.logar("Carregando parametriza\u00e7\u00e3o de Uso.");
            if (!ControleParametrizacao.carregarParametrizacao(3)) {
                throw new ErroDesconhecidoException();
            }
            ControleLogs.logar("SAT ativado com Sucesso");
            Configuracoes.SAT.ativado = true;
            Configuracoes.SAT.aguardandoCertificado = false;
            return "05000|Certificado transmitido com Sucesso||";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "05099|Erro desconhecido||";
        }
    }
    
    public String validarParametros(final String numeroSessao, final String codigoAtivacao, final String certificado) {
        final String r = "true";
        try {
            if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                ControleLogs.logar("ERRO: Numero de Sessao Invalido");
                return "05099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
            if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
                ControleLogs.logar("ERRO: C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
                return "05001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.";
            }
            if (certificado.length() < 6) {
                ControleLogs.logar("ERRO: certificado Invalido");
                return "05099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "05099|Erro desconhecido na ativa\u00e7\u00e3o";
        }
        return r;
    }
}
