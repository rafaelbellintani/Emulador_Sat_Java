// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.Configuracoes;

public class ControladorComandosSAT
{
    private ControleNumeroSessao cNumeroSessao;
    private String aviso;
    
    public ControladorComandosSAT(final ControleNumeroSessao cNumeroSessao, final String aviso) {
        this.cNumeroSessao = null;
        this.aviso = "";
        this.cNumeroSessao = cNumeroSessao;
        this.aviso = aviso;
    }
    
    public String tratarComandoAtivacao(final String[] parametro) {
        String resposta = "";
        final String numeroSessao = parametro[1];
        final String subComando = parametro[2];
        final String codigoAtivacao = parametro[3];
        final String CNPJ = parametro[4];
        final String cUF = parametro[5];
        final ControladorAtivacao cAtivacao = new ControladorAtivacao(this.cNumeroSessao);
        final String msg = cAtivacao.validarParametros(numeroSessao, subComando, codigoAtivacao, CNPJ, cUF);
        if (msg.equalsIgnoreCase("true")) {
            if (subComando.equals("1") || subComando.equals("2")) {
                resposta = cAtivacao.trataMensagem(subComando, codigoAtivacao, CNPJ, cUF, this.aviso);
            }
            else if (subComando.equals("3")) {
                resposta = cAtivacao.tratarComandoRenovacaoCertificado(subComando, codigoAtivacao, CNPJ, cUF, this.aviso);
            }
        }
        else {
            resposta = String.valueOf(msg) + "||" + this.aviso;
        }
        return resposta;
    }
    
    public String tratarComandoEnviarDadosVenda(final String[] parametro) {
        ControleLogs.logar("INICIO EMISS\u00c3O");
        final String numeroSessao = parametro[1];
        final String codigoAtivacao = parametro[2];
        final String dadosVenda = parametro[3];
        final ControladorEmissao cEmissao = new ControladorEmissao(this.cNumeroSessao);
        final String msg = cEmissao.validarParametros(numeroSessao, codigoAtivacao, dadosVenda);
        if (msg.equalsIgnoreCase("true")) {
            return cEmissao.trataMensagem(codigoAtivacao, dadosVenda, this.aviso);
        }
        return String.valueOf(msg) + "||" + this.aviso;
    }
    
    public String tratarComandoCancelarUltimaVenda(final String[] parametro) {
        ControleLogs.logar("INICIO CANCELAMENTO");
        final String numeroSessao = parametro[1];
        final String codigoAtivacao = parametro[2];
        final String chave = parametro[3];
        final String dadosCancelamento = parametro[4];
        final ControladorCancelamento cCancelamento = new ControladorCancelamento(this.cNumeroSessao);
        final String msg = cCancelamento.validarParametros(numeroSessao, codigoAtivacao, chave, dadosCancelamento);
        if (msg.equalsIgnoreCase("true")) {
            return cCancelamento.trataMensagem(codigoAtivacao, chave, dadosCancelamento, this.aviso);
        }
        return String.valueOf(msg) + "||" + this.aviso;
    }
    
    public String tratarComandoConsultarSAT(final String[] parametro) {
        final String numeroSessao = parametro[1];
        ControleLogs.logar("INICIO CONSULTA");
        if (this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
            return "08000|SAT-CFe em opera\u00e7\u00e3o.||" + this.aviso;
        }
        return "08099|Erro desconhecido||" + this.aviso;
    }
    
    public String tratarComandoTesteFimAFim(final String[] parametro) {
        ControleLogs.logar("INICIO TESTE FIM A FIM");
        final String numeroSessao = parametro[1];
        final String codigoAtivacao = parametro[2];
        final String dadosVenda = parametro[3];
        final ControladorEmissaoTeste cEmissaoTeste = new ControladorEmissaoTeste(this.cNumeroSessao);
        final String msg = cEmissaoTeste.validarParametros(numeroSessao, codigoAtivacao, dadosVenda);
        if (msg.equalsIgnoreCase("true")) {
            return cEmissaoTeste.trataMensagem(codigoAtivacao, numeroSessao, dadosVenda, this.aviso);
        }
        return String.valueOf(msg) + "||" + this.aviso;
    }
    
    public String tratarComandoConsultarNumeroSessao(final String[] parametro) {
        final String codigoAtivacao = parametro[2];
        final String cNumeroSessao = parametro[3];
        if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
            ControleLogs.logar("Erro: Codigo de Ativa\u00e7\u00e3o Invalido");
            return "11001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.||" + this.aviso;
        }
        if (ControladorEmuladorOffLine.isAtualizando()) {
            return "11099|SAT-CFe em processo de Atualiza\u00e7\u00e3o.||" + this.aviso;
        }
        ControleLogs.logar("INICIO CONSULTA SESS\u00c3O");
        if (!Configuracoes.SAT.associado || !Configuracoes.SAT.ativado) {
            ControleLogs.logar("ERRO NA CONSULTA");
            return "11002|SAT ainda n\u00e3o ativado.||" + this.aviso;
        }
        if (ControleArquivos.existeArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO) + cNumeroSessao + ".txt")) {
            ControleLogs.logar("CONSULTA EFETUADA COM SUCESSO");
            return ControleArquivos.lerCaracteresArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_NUMERO_SESSAO) + cNumeroSessao + ".txt");
        }
        ControleLogs.logar("ERRO NA CONSULTA");
        return "11003|Documento n\u00e3o existe.||" + this.aviso;
    }
    
    public String tratarComandoComunicarCertificadoICPBRASIL(final String[] parametro) {
        ControleLogs.logar("INICIAR COMUNICACAO CERTIFICADO");
        final String numeroSessao = parametro[1];
        final String codigoAtivacao = parametro[2];
        final String certificado = parametro[3];
        final ControleComunicarCertificado cCertificado = new ControleComunicarCertificado(this.cNumeroSessao);
        final String msg = cCertificado.validarParametros(numeroSessao, codigoAtivacao, certificado);
        if (msg.equalsIgnoreCase("true")) {
            return cCertificado.tratarMensagem(codigoAtivacao, certificado, this.aviso);
        }
        return String.valueOf(msg) + "||" + this.aviso;
    }
    
    public String tratarComandoConsultarStatusOperacional(final String[] parametro) {
        ControleLogs.logar("INICIO CONSULTAR STATUS OPERACIONAL");
        final String numeroSessao = parametro[1];
        final String codigoAtivacao = parametro[2];
        final ControladorConsultaStatus cConsultaStatus = new ControladorConsultaStatus(this.cNumeroSessao);
        final String msg = cConsultaStatus.validarParametros(numeroSessao, codigoAtivacao);
        if (msg.equalsIgnoreCase("true")) {
            return cConsultaStatus.tratarMensagem(codigoAtivacao, this.aviso);
        }
        return String.valueOf(msg) + "||" + this.aviso;
    }
    
    public String tratarComandoAssociarAssinatura(final String[] parametro) {
        ControleLogs.logar("INICIO ASSOCIAR ASSINATURA");
        final String numeroSessao = parametro[1];
        final String codigoDeAtivacao = parametro[2];
        final String CNPJValue = parametro[3];
        final String assinaturaCNPJValue = parametro[4];
        final ControladorAssociacaoAssinatura cAssociacaoAssinatura = new ControladorAssociacaoAssinatura(this.cNumeroSessao);
        final String msg = cAssociacaoAssinatura.validarParametros(codigoDeAtivacao, numeroSessao, CNPJValue, assinaturaCNPJValue, this.aviso);
        if (msg.equalsIgnoreCase("true")) {
            return cAssociacaoAssinatura.tratarMensagem(CNPJValue, assinaturaCNPJValue, this.aviso);
        }
        return String.valueOf(msg) + "||" + this.aviso;
    }
    
    public String tratarComandoConfigurarInterfaceDeRede(final String[] parametro) {
        ControleLogs.logar("NAO IMPLEMENTADO");
        return "12099|Erro Desconhecido||" + this.aviso;
    }
    
    public String tratarComandoAtualizarSoftwareSAT(final String[] parametro) {
        ControleLogs.logar("NAO IMPLEMENTADO");
        return "14000|Erro desconhecido||" + this.aviso;
    }
    
    public String tratarComandoExtrairLogs(final String[] parametro) {
        try {
            ControleLogs.logar("INICIO EXTRAIR LOGS");
            final String numeroSessao = parametro[1];
            final String codigoDeAtivacao = parametro[2];
            if (!ControleSeguranca.validarCodigoAtivacao(codigoDeAtivacao)) {
                ControleLogs.logar("Erro: C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
                return "15001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.||" + this.aviso;
            }
            if (!ControleDados.isNumerico(numeroSessao) || !this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                ControleLogs.logar("Erro: N\u00famero de Sess\u00e3o inv\u00e1lido.");
                return "15099|Erro desconhecido||" + this.aviso;
            }
            final String logs = ControleLogs.getArquivoLogs();
            if (logs != null) {
                ControleLogs.logar("FIM EXTRAIR LOGS");
                return "15000|Transferencia Completa||" + this.aviso + "|" + logs;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            ControleLogs.logar("Erro ao Extrair os Logs");
        }
        return "15099|Erro desconhecido||" + this.aviso;
    }
    
    public String tratarComandoBloquearSAT(final String[] parametro) {
        ControleLogs.logar("INICIO BLOQUEIO");
        String resposta = "";
        final String numeroSessao = parametro[1];
        final String codigoAtivacao = parametro[2];
        final ControladorBloqueio cBloqueio = new ControladorBloqueio(this.cNumeroSessao);
        final String msg = cBloqueio.validarParametrosBloqueio(numeroSessao, codigoAtivacao);
        if (msg.equalsIgnoreCase("true")) {
            resposta = cBloqueio.tratarMensagemBloqueio(codigoAtivacao, this.aviso);
        }
        else {
            resposta = String.valueOf(msg) + "||" + this.aviso;
        }
        ControleLogs.logar("FIM BLOQUEIO");
        return resposta;
    }
    
    public String tratarComandoDesbloquearSAT(final String[] parametro) {
        ControleLogs.logar("INICIO DESBLOQUEIO");
        String resposta = "";
        final String numeroSessao = parametro[1];
        final String codigoAtivacao = parametro[2];
        final ControladorBloqueio cBloqueio = new ControladorBloqueio(this.cNumeroSessao);
        final String msg = cBloqueio.validarParametrosDesbloqueio(numeroSessao, codigoAtivacao);
        if (msg.equalsIgnoreCase("true")) {
            resposta = cBloqueio.tratarMensagemDesbloqueio(codigoAtivacao, this.aviso);
        }
        else {
            resposta = String.valueOf(msg) + "||" + this.aviso;
        }
        ControleLogs.logar("FIM DESBLOQUEIO");
        return resposta;
    }
    
    public String tratarComandoTrocarCodigoDeAtivacao(final String[] parametro) {
        ControleLogs.logar("INICIO TROCAR CODIGO ATIVACAO");
        String resposta = "";
        final String numeroSessao = parametro[1];
        final String codigoAtivacao = parametro[2];
        final String opcao = parametro[3];
        final String novoCodigo = parametro[4];
        final String confNovoCodigo = parametro[5];
        final ControladorTrocarCodigoAtivacao cTrocarCodigo = new ControladorTrocarCodigoAtivacao(this.cNumeroSessao);
        final String msg = cTrocarCodigo.validarParametros(numeroSessao, codigoAtivacao, opcao, novoCodigo, confNovoCodigo, this.aviso);
        if (msg.equalsIgnoreCase("true")) {
            resposta = cTrocarCodigo.tratarMensagem(codigoAtivacao, opcao, novoCodigo, this.aviso);
        }
        else {
            resposta = String.valueOf(msg) + "||" + this.aviso;
        }
        return resposta;
    }
}
