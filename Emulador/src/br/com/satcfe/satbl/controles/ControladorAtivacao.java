// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.modelos.ConfiguracoesOffLine;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeCertificacao;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import java.security.spec.InvalidKeySpecException;
import br.com.um.controles.StringUtil;
import java.security.KeyPair;
import br.com.satcfe.satbl.excecoes.ErroDesconhecidoException;
import br.com.satcfe.satbl.excecoes.SATBloqueadoException;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import java.io.IOException;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import br.com.satcfe.satbl.MainSATBL;
import java.security.cert.Certificate;
import br.com.um.controles.ControleKeyStore;
import java.security.cert.CertificateEncodingException;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.modelos.BaseCertificados;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.excecoes.TimeoutSATException;
import br.com.satcfe.satbl.excecoes.SATAtivoException;
import br.com.satcfe.satbl.Configuracoes;
import br.com.um.controles.ControleTempo;

public class ControladorAtivacao
{
    private long timeout;
    private long timestamp;
    private ControleNumeroSessao cNumeroSessao;
    
    public ControladorAtivacao(final ControleNumeroSessao cNumeroSessao) {
        this.timeout = 300000L;
        this.timestamp = 0L;
        this.cNumeroSessao = null;
        this.cNumeroSessao = cNumeroSessao;
    }
    
    public String trataMensagem(final String subComando, final String codigoAtivacao, final String CNPJ, final String cUF, final String aviso) {
        try {
            ControleLogs.logar("INICIO ATIVA\u00c7\u00c3O");
            this.timestamp = ControleTempo.getCurrentTime();
            boolean icpBrasil = false;
            if (Configuracoes.SAT.emuladorOffLine) {
                return this.ativarEmuladorOffLine(subComando, codigoAtivacao, CNPJ, cUF, aviso);
            }
            if (Configuracoes.SAT.ativado) {
                throw new SATAtivoException();
            }
            desativarEquipamentoSAT();
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_UF, cUF.toCharArray());
            ControleLogs.logar("Baixando parametriza\u00e7\u00e3o de UF.");
            ControleParametrizacao.baixarParametrizacaoUF(cUF, this.timeout);
            ControleLogs.logar("Carregando parametriza\u00e7\u00e3o de UF.");
            ControleParametrizacao.carregarParametrizacao(2);
            this.timeout -= ControleTempo.getCurrentTime() - this.timestamp;
            if (this.timeout <= 0L) {
                throw new TimeoutSATException();
            }
            ControleLogs.logar("Baixando parametriza\u00e7\u00e3o de ativa\u00e7\u00e3o.");
            ControleParametrizacao.baixarParametrizacaoAtivacao(CNPJ, cUF, this.timeout);
            ControleLogs.logar("Carregando parametriza\u00e7\u00e3o de ativa\u00e7\u00e3o.");
            ControleParametrizacao.carregarParametrizacao(1);
            this.timeout -= ControleTempo.getCurrentTime() - this.timestamp;
            if (this.timeout <= 0L) {
                throw new TimeoutSATException();
            }
            ControleLogs.logar("Gerando par de chaves.");
            final KeyPair keyPair = this.gerarParDeChaves(codigoAtivacao);
            ControleLogs.logar("Gerando Certificate Signing Request.");
            if (subComando.equals("2")) {
                icpBrasil = true;
            }
            String cn = Parametrizacoes.razaoSocialEmitente;
            if (cn.length() > 49) {
                cn = cn.substring(0, 49);
            }
            cn = String.valueOf(cn) + ":" + CNPJ;
            final String csr = ControleSeguranca.gerarCertificateSigningRequest(keyPair, "BR", "SEFAZ-SP", "AC-SAT", "AC-SAT", cn, CNPJ, "SAO PAULO", Configuracoes.SAT.numeroDeSerie, false, icpBrasil);
            ControleSeguranca.gravarCodigoAtivacao(codigoAtivacao);
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CSR, csr.toCharArray());
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CNPJ, CNPJ.toCharArray());
            Configuracoes.SAT.CNPJEstabelecimento = CNPJ;
            String cert = "";
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TIPO_CERT, subComando.toCharArray());
            this.timeout -= ControleTempo.getCurrentTime() - this.timestamp;
            if (this.timeout <= 0L) {
                throw new TimeoutSATException();
            }
            if (subComando.equals("1")) {
                cert = this.comunicarCsrACSEFAZ(csr, this.timeout);
                if (Configuracoes.SAT.emuladorOffLine && cert == null && CNPJ.equals("11111111111111")) {
                    final BaseCertificados base = new BaseCertificados();
                    base.carregar();
                    cert = ControleDados.formatarCertificado(base.getCertHttps().get(0).toString(), true);
                }
            }
            else if (subComando.equals("2")) {
                Configuracoes.SAT.aguardandoCertificado = true;
                ControleLogs.logar("CSR ICP-BRASIL criado com sucesso");
                return "04006|CSR  ICP-BRASIL criado com sucesso||" + aviso + "|" + csr;
            }
            if (cert == null || cert.length() < 4) {
                throw new CertificateEncodingException();
            }
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT, cert.toCharArray());
            if ("2.9.4".indexOf("teste") >= 0) {
                return "04099|Vers\u00e3o de Teste, para apos a comunica\u00e7\u00e3o do certificado||" + aviso + "|";
            }
            ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE);
            final ControleKeyStore cKeyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
            cKeyStore.carregarKeyStore();
            this.timeout -= ControleTempo.getCurrentTime() - this.timestamp;
            if (this.timeout <= 0L) {
                throw new TimeoutSATException();
            }
            try {
                if (Configuracoes.SAT.protocolo.equals("http")) {
                    final Certificate certificate = ControleKeyStore.parseCertificado(cert.getBytes());
                    cKeyStore.setPrivateKey(keyPair.getPrivate(), new Certificate[] { certificate });
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
                    cKeyStore.setPrivateKey(keyPair.getPrivate(), chain);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new CertificateEncodingException("Erro na cadeia de Certifica\u00e7\u00e3o");
            }
            this.timeout -= ControleTempo.getCurrentTime() - this.timestamp;
            if (this.timeout <= 0L) {
                throw new TimeoutSATException();
            }
            ControleLogs.logar("Baixando parametriza\u00e7\u00e3o de uso.");
            ControleParametrizacao.baixarParametrizacaoUso(cUF, this.timeout);
            ControleLogs.logar("carregando parametriza\u00e7\u00e3o de uso.");
            ControleParametrizacao.carregarParametrizacao(3);
            if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08) {
                try {
                    ControleLogs.logar("Baixando par\u00e2metros de gest\u00e3o.");
                    ControleParametrizacao.baixarParametrosDeGestao(cUF, this.timeout);
                    ControleLogs.logar("carregando par\u00e2metros de gest\u00e3o.");
                    ControleParametrizacao.carregarParametrizacao(5);
                }
                catch (Exception e) {
                    ControleLogs.logar("Erro ao carregar os par\u00e2metros de gest\u00e3o.");
                    throw e;
                }
            }
            Configuracoes.SAT.ativado = true;
            Configuracoes.SAT.bloqueado = false;
            Configuracoes.SAT.aguardandoCertificado = false;
            ControleLogs.logar("ATIVADO COM SUCESSO");
            final MainSATBL main = new MainSATBL();
            main.iniciarTarefas();
            return "04000|Ativado corretamente||" + aviso;
        }
        catch (CertificateEncodingException e2) {
            e2.printStackTrace();
            ControleLogs.logar("ERRO: Certificado invalido");
            return "04001|Erro na cria\u00e7\u00e3o do certificado||" + aviso;
        }
        catch (DataLengthException e3) {
            e3.printStackTrace();
            ControleLogs.logar("ERRO NA ATIVA\u00c7\u00c3O");
            return "04001|Erro na cria\u00e7\u00e3o do certificado||" + aviso;
        }
        catch (CryptoException e4) {
            e4.printStackTrace();
            ControleLogs.logar("ERRO NA ATIVA\u00c7\u00c3O");
            return "04001|Erro na cria\u00e7\u00e3o do certificado||" + aviso;
        }
        catch (IOException e5) {
            e5.printStackTrace();
            ControleLogs.logar("ERRO NA ATIVA\u00c7\u00c3O");
            return "04001|Erro na cria\u00e7\u00e3o do certificado||" + aviso;
        }
        catch (SATAtivoException e6) {
            e6.printStackTrace();
            ControleLogs.logar("ERRO: SAT j\u00e1 est\u00e1 Ativado");
            return "04003|SAT-CF-e j\u00e1 ativado||" + aviso;
        }
        catch (ErroComunicacaoRetaguardaException e7) {
            e7.printStackTrace();
            ControleLogs.logar("ERRO NA ATIVA\u00c7\u00c3O");
            return "04005|Erro de comunica\u00e7\u00e3o com a SEFAZ||" + aviso;
        }
        catch (SATBloqueadoException e8) {
            e8.printStackTrace();
            ControleLogs.logar("ERRO: SAT Bloqueado");
            return "04099|Erro desconhecido na ativa\u00e7\u00e3o||" + aviso;
        }
        catch (TimeoutSATException e9) {
            e9.printStackTrace();
            ControleLogs.logar("ERRO: O Timeout da fun\u00e7\u00e3o foi ultrapassado.");
            return "04099|Erro desconhecido na ativa\u00e7\u00e3o||" + aviso;
        }
        catch (ErroDesconhecidoException e10) {
            e10.printStackTrace();
            if (e10.getMessage() != null) {
                final String[] s = ControleDados.quebrarString(e10.getMessage(), "|");
                ControleLogs.logar("Erro na Ativacao: " + s[0] + " - " + s[1]);
                return "04099|" + s[1] + "||" + aviso;
            }
            ControleLogs.logar("Erro desconhecido na ativa\u00e7\u00e3o");
            return "04099|Erro desconhecido na ativa\u00e7\u00e3o||" + aviso;
        }
        catch (Exception e11) {
            e11.printStackTrace();
            ControleLogs.logar("ERRO NA ATIVA\u00c7\u00c3O");
            return "04099|Erro desconhecido na ativa\u00e7\u00e3o||" + aviso;
        }
    }
    
    public KeyPair gerarParDeChaves(final String codigoAtivacao) throws InvalidKeySpecException, DataLengthException, IllegalStateException, InvalidCipherTextException, NoSuchAlgorithmException, Exception {
        final String hashCodigoAtivacao = ControleSeguranca.recuperarCodigoAtivacao();
        if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA) && ControleDados.gerarHashMD5(codigoAtivacao).equals(hashCodigoAtivacao)) {
            ControleLogs.logar("Carregando Par de Chaves Existente");
            final String base64ks = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA);
            byte[] ks;
            try {
                ks = StringUtil.base64Decode(base64ks);
            }
            catch (Exception e) {
                throw new InvalidKeySpecException();
            }
            final byte[] ksDescriptografado = ControleSeguranca.descriptografarAES(hashCodigoAtivacao.getBytes(), ks);
            final String[] keyStore = new String(ksDescriptografado).split("\\:");
            final PublicKey publicKey = ControleSeguranca.deserializarPublicaRSA(keyStore[0]);
            final PrivateKey privateKey = ControleSeguranca.deserializarPrivadaRSA(keyStore[1]);
            return new KeyPair(publicKey, privateKey);
        }
        ControleLogs.logar("Gerando Novo Par de Chaves.");
        final AsymmetricCipherKeyPair parDeChaves = ControleSeguranca.gerarChavesRSA();
        final String publica = ControleSeguranca.serializarChaveRSA((RSAKeyParameters)parDeChaves.getPublic());
        final String privada = ControleSeguranca.serializarChaveRSA((RSAKeyParameters)parDeChaves.getPrivate());
        final PublicKey publicKey2 = ControleSeguranca.deserializarChavePublicaRSA(publica);
        final PrivateKey privateKey2 = ControleSeguranca.deserializarChavePrivadaRSA(privada);
        final String keystore = new String(StringUtil.base64Encode(ControleSeguranca.criptografarAES(ControleDados.gerarHashMD5(codigoAtivacao).getBytes(), (String.valueOf(publica) + ":" + privada).getBytes())));
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA, keystore.toCharArray());
        return new KeyPair(publicKey2, privateKey2);
    }
    
    private String comunicarCsrACSEFAZ(final String csr, final long timeout) throws ErroComunicacaoRetaguardaException, ErroDesconhecidoException, SATAtivoException {
        ControleLogs.logar("Comunicando CSR.");
        final WebServiceCFeCertificacao wsCertificacao = new WebServiceCFeCertificacao();
        wsCertificacao.setCSR(csr);
        wsCertificacao.setOpt("CSR");
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
        return wsCertificacao.getCert();
    }
    
    private String ativarEmuladorOffLine(final String subComando, final String codigoAtivacao, final String CNPJ, final String cUF, final String aviso) throws Exception {
        if (Configuracoes.SAT.bloqueado) {
            throw new SATBloqueadoException();
        }
        if (Configuracoes.SAT.ativado) {
            throw new SATAtivoException();
        }
        desativarEquipamentoSAT();
        final String s = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SAT_OFFLINE);
        final String[] dados = ControleDados.quebrarString(s, ",");
        final String paramAtivacao = StringUtil.base64DecodeUTF8(dados[1]);
        final String paramUso = StringUtil.base64DecodeUTF8(dados[2]);
        ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_ATIVACAO, paramAtivacao.toCharArray(), false);
        ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO, paramUso.toCharArray(), false);
        ControleLogs.logar("Carregando parametriza\u00e7\u00e3o de ativa\u00e7\u00e3o.");
        ControleParametrizacao.carregarParametrizacao(1);
        if (!Parametrizacoes.CNPJ.equals(CNPJ)) {
            ControleLogs.logar("CNPJ invalido");
            throw new ErroDesconhecidoException();
        }
        ControleLogs.logar("Gerando par de chaves.");
        final KeyPair keyPair = this.gerarParDeChaves(codigoAtivacao);
        ControleLogs.logar("Gerando Certificate Signing Request.");
        final String csr = ControleSeguranca.gerarCertificateSigningRequest(keyPair, "BR", "SEFAZ-SP", "AC-SAT", "identificacao", String.valueOf(Parametrizacoes.razaoSocialEmitente) + ":" + CNPJ, CNPJ, "SAO PAULO", Configuracoes.SAT.numeroDeSerie, false, false);
        final ControleKeyStore cKeyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, "123456");
        cKeyStore.carregarKeyStore();
        final Certificate certificate = ControleSeguranca.gerarCertificado(keyPair, 24, "C=SAT", "OU=SAT", "www.sat.com.br", "SHA256WITHRSA");
        cKeyStore.setPrivateKey(keyPair.getPrivate(), certificate);
        final String c = ControleDados.formatarCertificado(new String(StringUtil.base64Encode(certificate.getEncoded())), true);
        ControleSeguranca.gravarCodigoAtivacao(codigoAtivacao);
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CSR, csr.toCharArray());
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT, c.toCharArray());
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CNPJ, CNPJ.toCharArray());
        Configuracoes.SAT.CNPJEstabelecimento = CNPJ;
        if (subComando.equals("2")) {
            ControleLogs.logar("CSR ICP-BRASIL criado com sucesso");
            Configuracoes.SAT.aguardandoCertificado = true;
            return "04006|CSR  ICP-BRASIL criado com sucesso||" + aviso + "|" + csr;
        }
        ControleLogs.logar("carregando parametriza\u00e7\u00e3o de uso.");
        ControleParametrizacao.carregarParametrizacao(3);
        Configuracoes.SAT.ativado = true;
        Configuracoes.SAT.bloqueado = false;
        Configuracoes.SAT.aguardandoCertificado = false;
        ControleLogs.logar("ATIVADO COM SUCESSO");
        return "04000|Ativado corretamente||" + aviso;
    }
    
    private static void desativarEquipamentoSAT() {
        apagarParametrizacoes();
        ControleLogs.logar("Desativando SAT-CFe.");
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_UF);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ASSINATURA);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CNPJ);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ID_LOTE_TESTE);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_NUMERO_DOC_FISCAL_TESTE);
    }
    
    private static void apagarParametrizacoes() {
        ControleLogs.logar("Apagando Parametriza\u00e7\u00f5es Antigas.");
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_ATIVACAO);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_UF);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO);
        ControleArquivos.excluirArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO);
        Parametrizacoes.limparParametrizacoes();
        ControleLogs.logar("Carregando parametriza\u00e7\u00e3o de Fabrica");
        ControleParametrizacao.carregarParametrizacao(0);
    }
    
    public String validarParametros(final String numeroSessao, final String subComando, final String codigoAtivacao, final String CNPJ, final String cUF) {
        final String r = "true";
        try {
            if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                ControleLogs.logar("ERRO: Numero de Sessao Invalido");
                return "04099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
            if (Integer.parseInt(subComando) < 0 || Integer.parseInt(subComando) > 3) {
                ControleLogs.logar("ERRO: subComando Invalido");
                return "04099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
            if (codigoAtivacao.length() < 6 || codigoAtivacao.length() > 32) {
                ControleLogs.logar("ERRO: Codigo de Ativacao Invalido");
                return "04099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
            if (CNPJ.length() != 14) {
                ControleLogs.logar("ERRO: CNPJ Invalido");
                return "04099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
            if (cUF.length() != 2) {
                ControleLogs.logar("ERRO: cUF Invalido");
                return "04099|Erro desconhecido na ativa\u00e7\u00e3o";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "04099|Erro desconhecido na ativa\u00e7\u00e3o";
        }
        return r;
    }
    
    public String tratarComandoRenovacaoCertificado(final String subComando, final String codigoAtivacao, final String CNPJ, final String cUF, final String aviso) {
        try {
            if (Configuracoes.SAT.emuladorOffLine && !ConfiguracoesOffLine.getInstance().getHabilitarRenovacao()) {
                return "04099|SAT n\u00e3o pode renovar o Certificado ||" + aviso;
            }
            ControleLogs.logar("Inicio da Renova\u00e7\u00e3o do Certificado ICP-Brasil.");
            ControleLogs.logar("Gerando novo par de chaves.");
            final KeyPair keyPair = this.gerarParDeChaves(codigoAtivacao);
            ControleLogs.logar("Gerando Certificate Signing Request.");
            final String csr = ControleSeguranca.gerarCertificateSigningRequest(keyPair, "BR", "SEFAZ-SP", "AC-SAT", "AC-SAT", String.valueOf(Parametrizacoes.razaoSocialEmitente) + ":" + CNPJ, CNPJ, "SAO PAULO", Configuracoes.SAT.numeroDeSerie, false, true);
            Configuracoes.SAT.aguardandoCertificado = true;
            ControleLogs.logar("CSR ICP-BRASIL criado com sucesso");
            return "04006|CSR  ICP-BRASIL criado com sucesso||" + aviso + "|" + csr;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "04099|Erro desconhecido ||" + aviso;
        }
    }
}
