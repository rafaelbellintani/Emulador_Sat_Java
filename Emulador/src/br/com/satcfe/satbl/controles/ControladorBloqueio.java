// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.controles.webservices.WebServiceCFeRetRecepcao;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeRecepcao;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Paths;
import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeStatus;
import br.com.satcfe.satbl.modelos.StatusOperacionalSAT;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.modelos.ConfiguracoesOffLine;
import br.com.satcfe.satbl.MainSATBL;
import br.com.um.controles.StringUtil;
import br.com.satcfe.satbl.excecoes.SATNaoAtivoException;
import br.com.satcfe.satbl.excecoes.ErroDesconhecidoException;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeParametrizacao;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeLogs;
import br.com.satcfe.satbl.threads.ThreadVerificacaoEnvio;
import br.com.satcfe.satbl.threads.ThreadEnvioNota;
import br.com.satcfe.satbl.Configuracoes;

public class ControladorBloqueio
{
    private ControleNumeroSessao cNumeroSessao;
    private static boolean desbloqueando;
    
    static {
        ControladorBloqueio.desbloqueando = false;
    }
    
    public ControladorBloqueio() {
    }
    
    public ControladorBloqueio(final ControleNumeroSessao cNumeroSessao) {
        this.cNumeroSessao = cNumeroSessao;
    }
    
    public String tratarMensagemBloqueio(final String codigoAtivacao, final String aviso) {
        try {
            if (Configuracoes.SAT.bloqueado) {
                return "16001|Equipamento SAT j\u00e1 est\u00e1 bloqueado||" + aviso;
            }
            if (Configuracoes.SAT.emuladorOffLine) {
                return this.bloquearEmuladorOffLine(aviso);
            }
            new ThreadEnvioNota().run();
            while (ThreadVerificacaoEnvio.verificando) {
                Thread.sleep(100L);
            }
            final String[] cupons = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe);
            if (cupons != null && cupons.length > 0) {
                return "16099|Erro no bloqueio, existem cupons a serem transmitidos||" + aviso;
            }
            final String[] cuponsCanc = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS);
            if (cuponsCanc != null && cuponsCanc.length > 0) {
                return "16099|Erro no bloqueio, existem cupons a serem transmitidos||" + aviso;
            }
            final String[] cuponsEnv = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS);
            if (cuponsEnv != null && cuponsEnv.length > 0) {
                return "16099|Erro no bloqueio, existem cupons a serem transmitidos||" + aviso;
            }
            final WebServiceCFeLogs wsLogs = new WebServiceCFeLogs();
            final String log = ControleLogs.getArquivoLogs();
            if (log == null) {
                return "16099|Erro no bloqueio, n\u00e3o foi possivel transmitir o aquivo de logs||" + aviso;
            }
            wsLogs.setLog(log);
            if (!wsLogs.consumirWebService() || !wsLogs.getcStat().equals("118")) {
                return "16099|Erro no bloqueio, n\u00e3o foi possivel transmitir o aquivo de logs||" + aviso;
            }
            final WebServiceCFeParametrizacao wsParametrizacao = new WebServiceCFeParametrizacao();
            if (!wsParametrizacao.consumirWebService()) {
                throw new ErroComunicacaoRetaguardaException();
            }
            String paramBloqueio = wsParametrizacao.getParamBloqueio();
            if (wsParametrizacao.getParamUso() != null && paramBloqueio == null) {
                ControleLogs.logar("N\u00e3o foi comunicado o bloqueio junto a sefaz!");
            }
            if (wsParametrizacao.getcStat() == null || paramBloqueio == null) {
                throw new ErroDesconhecidoException();
            }
            if (wsParametrizacao.getcStat().equals("208")) {
                throw new SATNaoAtivoException();
            }
            if (!wsParametrizacao.getcStat().equals("117")) {
                throw new ErroDesconhecidoException();
            }
            if (StringUtil.isBase64(paramBloqueio)) {
                paramBloqueio = StringUtil.base64DecodeUTF8(paramBloqueio);
            }
            ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO, paramBloqueio.toCharArray());
            if (ControleParametrizacao.carregarParametrizacao(4)) {
                ControleParametrizacao.removerParametrizacaoUso();
            }
            Configuracoes.SAT.bloqueado = true;
            if (this.transmitirStatusSAT()) {
                return "16099|Erro no bloqueio, n\u00e3o foi possivel transmitir o status do SAT||" + aviso;
            }
            new MainSATBL().iniciarTarefas();
            ControleLogs.logar("Equipamento SAT-CFe  bloqueado com sucesso");
            return "16000|Equipamento SAT bloqueado com sucesso||" + aviso;
        }
        catch (ErroComunicacaoRetaguardaException e) {
            e.printStackTrace();
            Configuracoes.SAT.bloqueado = false;
            return "16002|Erro de comunica\u00e7\u00e3o com a SEFAZ||" + aviso;
        }
        catch (Exception e2) {
            e2.printStackTrace();
            Configuracoes.SAT.bloqueado = false;
            ControleLogs.logar("ERRO DESCONHECIDO NO BLOQUEIO");
            return "16099|Erro Desconhecido||" + aviso;
        }
    }
    
    private String bloquearEmuladorOffLine(final String aviso) {
        if (!ConfiguracoesOffLine.getInstance().getHabilitarBloqueioContribuinte()) {
            return "16099|SAT N\u00e3o pode ser bloqueado||" + aviso;
        }
        final String pBloqueio = this.carregarParamBloqueioOffLine();
        if (pBloqueio == null) {
            return "16099|Erro Desconhecido||" + aviso;
        }
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO, pBloqueio.toCharArray());
        if (ControleParametrizacao.carregarParametrizacao(4)) {
            ControleParametrizacao.removerParametrizacaoUso();
        }
        Configuracoes.SAT.bloqueado = true;
        ControleLogs.logar("Equipamento SAT-CFe  bloqueado com sucesso");
        return "16000|Equipamento SAT bloqueado com sucesso||" + aviso;
    }
    
    public String bloquearOffLineComandoSefaz() {
        ControleLogs.logar("Inicio do Bloqueio do Equipamento SAT-CFe");
        final String pBloqueio = this.carregarParamBloqueioOffLine();
        if (pBloqueio == null) {
            return null;
        }
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_BLOQUEIO, pBloqueio.toCharArray());
        if (ControleParametrizacao.carregarParametrizacao(4)) {
            ControleParametrizacao.removerParametrizacaoUso();
        }
        Configuracoes.SAT.bloqueado = true;
        ControleLogs.logar("Equipamento SAT-CFe  bloqueado com sucesso");
        return "bloqueado";
    }
    
    private String carregarParamBloqueioOffLine() {
        final String s = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SAT_OFFLINE);
        final String[] r = ControleDados.quebrarString(s, ",");
        if (r.length != 4) {
            ControleLogs.logar("arquivo de parametriza\u00e7\u00e3o de bloqueio nao encontrado");
            return null;
        }
        String pBloqueio = r[3];
        pBloqueio = StringUtil.base64DecodeUTF8(pBloqueio);
        return pBloqueio;
    }
    
    public String tratarMensagemDesbloqueio(final String codigoAtivacao, final String aviso) {
        try {
            if (!Configuracoes.SAT.bloqueado) {
                return "17099|Equipamento SAT nao esta bloqueado||" + aviso;
            }
            if (Parametrizacoes.autorBloqueio == 2) {
                return "17002|SAT bloqueado pela SEFAZ||" + aviso;
            }
            if (Configuracoes.SAT.emuladorOffLine) {
                return this.desbloquearEmuladorOffLine(aviso);
            }
            final WebServiceCFeParametrizacao wsParametrizacao = new WebServiceCFeParametrizacao();
            if (!wsParametrizacao.consumirWebService()) {
                throw new ErroComunicacaoRetaguardaException();
            }
            String paramUso = wsParametrizacao.getParamUso();
            if (wsParametrizacao.getParamBloqueio() != null && paramUso == null) {
                ControleLogs.logar("Nao foi comunicado o desbloqueio perante a sefaz!");
            }
            if (wsParametrizacao.getcStat() == null || paramUso == null) {
                throw new ErroDesconhecidoException();
            }
            if (wsParametrizacao.getcStat().equals("208")) {
                throw new SATNaoAtivoException();
            }
            if (wsParametrizacao.getcStat().equals("117")) {
                if (StringUtil.isBase64(paramUso)) {
                    paramUso = StringUtil.base64DecodeUTF8(paramUso);
                }
                ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO, paramUso.toCharArray());
                if (ControleParametrizacao.carregarParametrizacao(3)) {
                    ControleParametrizacao.removerParametrizacaoBloqueio();
                }
                Configuracoes.SAT.bloqueado = false;
                this.transmitirStatusSAT();
                new MainSATBL().iniciarTarefas();
                return "17000|Equipamento SAT desbloqueado com sucesso||" + aviso;
            }
            throw new ErroDesconhecidoException();
        }
        catch (ErroComunicacaoRetaguardaException e) {
            return "17003|Erro de comunica\u00e7\u00e3o com a SEFAZ||" + aviso;
        }
        catch (Exception e2) {
            ControleLogs.logar("Erro desconhecido no bloqueio");
            return "17099|Erro Desconhecido||" + aviso;
        }
    }
    
    private String desbloquearEmuladorOffLine(final String aviso) {
        if (ConfiguracoesOffLine.getInstance().getHabilitarBloqueioContribuinte()) {
            return "17099|SAT N\u00e3o pode ser desbloqueado||" + aviso;
        }
        final String s = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SAT_OFFLINE);
        final String[] r = ControleDados.quebrarString(s, ",");
        if (r.length != 4) {
            ControleLogs.logar("arquivo de parametriza\u00e7\u00e3o de uso");
            return "17099|Erro Desconhecido||" + aviso;
        }
        String paramUso = r[2];
        paramUso = StringUtil.base64DecodeUTF8(paramUso);
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO, paramUso.toCharArray());
        if (ControleParametrizacao.carregarParametrizacao(3)) {
            ControleParametrizacao.removerParametrizacaoBloqueio();
        }
        Configuracoes.SAT.bloqueado = false;
        return "17000|Equipamento SAT desbloqueado com sucesso||" + aviso;
    }
    
    public boolean transmitirStatusSAT() {
        final StatusOperacionalSAT status = new StatusOperacionalSAT();
        final WebServiceCFeStatus wsStatus = new WebServiceCFeStatus("STATUS-SAT");
        wsStatus.setStatus(status.getStatusXML());
        if (!wsStatus.consumirWebService() || wsStatus.getcStat() == null) {
            ControleLogs.logar("Erro de Comunica\u00e7\u00e3o com a Sefaz.");
            return false;
        }
        if (wsStatus.getcStat().equals("109") || wsStatus.getcStat().equals("110")) {
            ControleLogs.logar("Informa\u00e7\u00f5es Transmitidas \u00e0 SEFAZ com sucesso!");
            return true;
        }
        ControleLogs.logar("Erro na Transmissao de Status: " + wsStatus.getxMotivo());
        return false;
    }
    
    public String validarParametrosBloqueio(final String numeroSessao, final String codigoAtivacao) {
        final String r = "true";
        if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
            ControleLogs.logar("ERRO: Numero de Sessao Invalido");
            return "16099|Erro desconhecido na ativa\u00e7\u00e3o";
        }
        if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
            ControleLogs.logar("ERRO: C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
            return "16001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.";
        }
        return r;
    }
    
    public String validarParametrosDesbloqueio(final String numeroSessao, final String codigoAtivacao) {
        final String r = "true";
        if (!this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
            ControleLogs.logar("ERRO: Numero de Sessao Invalido");
            return "17099|Erro desconhecido na ativa\u00e7\u00e3o";
        }
        if (!ControleSeguranca.validarCodigoAtivacao(codigoAtivacao)) {
            ControleLogs.logar("ERRO: C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.");
            return "17001|C\u00f3digo de ativa\u00e7\u00e3o inv\u00e1lido.";
        }
        return r;
    }
    
    public String desbloquearOffLineComandoSefaz() {
        final String s = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_SAT_OFFLINE);
        final String[] r = ControleDados.quebrarString(s, ",");
        if (r.length != 4) {
            ControleLogs.logar("arquivo de parametriza\u00e7\u00e3o de uso corrompida");
            return null;
        }
        String paramUso = r[2];
        paramUso = StringUtil.base64DecodeUTF8(paramUso);
        ControleArquivos.escreverCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_PARAMETRIZACAO_USO, paramUso.toCharArray());
        if (ControleParametrizacao.carregarParametrizacao(3)) {
            ControleParametrizacao.removerParametrizacaoBloqueio();
        }
        Configuracoes.SAT.bloqueado = false;
        return "desbloqueado";
    }
    
    public void iniciarBloqueioAutonomo() {
        Configuracoes.SAT.bloqueado = true;
        Configuracoes.SAT.autoBloqueado = true;
    }
    
    public void iniciarDesbloqueioAutonomo() {
        try {
            if (ControladorBloqueio.desbloqueando) {
                return;
            }
            ControladorBloqueio.desbloqueando = true;
            ControleLogs.logar("Comunica\u00e7\u00e3o Reestabelecida, iniciando desbloqueio do SAT-CFe!");
            new ThreadEnvioNota().enviarLotesDeCFe();
            if (!this.transmitirLoteVazio()) {
                ControleLogs.logar("Equipamento SAT-CFe n\u00e3o conseguiu se comunicar com a SEFAZ!");
                return;
            }
            final ThreadEnvioNota tEnvio = new ThreadEnvioNota();
            ControleLogs.logar("Iniciando envio dos cancelamentos.");
            tEnvio.enviarArquivosCancelamento();
            ControleLogs.logar("Fim envio dos cancelamentos.");
            new ControleConsultaComandos().consultarComandos();
            final ComandosSefazFacade comandos = new ComandosSefazFacade();
            comandos.transmitirEstadoOperacional();
            Configuracoes.SAT.bloqueado = false;
            Configuracoes.SAT.autoBloqueado = false;
            ControleLogs.logar("Equipamento SAT-CFe Desbloqueado!");
            ControladorBloqueio.desbloqueando = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void verificaBloqueioAutonomo() {
        try {
            final String dhComunicacao = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ULTIMA_COMUNICACAO);
            final long ultimaComunicacao = ControleTempo.parseTimeStamp(dhComunicacao);
            final long periodoSemComunicacao = ControleTempo.getCurrentTime() / 1000L - ultimaComunicacao / 1000L;
            if (Parametrizacoes.tipoBloqueio == null) {
                return;
            }
            if (Parametrizacoes.tipoBloqueio.equals("conexao")) {
                final long m = Parametrizacoes.valorBloqueio;
                if (periodoSemComunicacao > m * 60L) {
                    final ControladorBloqueio cBloqueio = new ControladorBloqueio();
                    ControleLogs.logar("Equipamento sem comunica\u00e7\u00e3o por muito tempo, SAT-CFe Bloqueado!");
                    cBloqueio.iniciarBloqueioAutonomo();
                }
            }
            else if (verificarArquivosCFe()) {
                final ControladorBloqueio cBloqueio2 = new ControladorBloqueio();
                ControleLogs.logar("Presen\u00e7a de CF-e na mem\u00f3ria do SAT-CF-e por mais tempo que o permitido!");
                ControleLogs.logar("Equipamento SAT-CFe Bloqueado!");
                cBloqueio2.iniciarBloqueioAutonomo();
            }
            if (!ControleSeguranca.certificadoValido()) {
                final ControladorBloqueio cBloqueio2 = new ControladorBloqueio();
                ControleLogs.logar("Certificado com data Inv\u00e1lida, SAT-CFe Bloqueado!");
                cBloqueio2.iniciarBloqueioAutonomo();
                final ComandosSefazFacade comandos = new ComandosSefazFacade();
                final String tipo = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TIPO_CERT);
                final boolean icpBrasil = tipo != null && tipo.equals("2");
                if (comandos.renovarCertificado(icpBrasil)) {
                    cBloqueio2.iniciarDesbloqueioAutonomo();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static boolean verificarArquivosCFe() {
        try {
            final long now = System.currentTimeMillis();
            final long maximo = Parametrizacoes.valorBloqueio * 60L;
            boolean existeCFeNaoTransmitido = false;
            final String[] cfes = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe);
            for (int i = 0; i < cfes.length; ++i) {
                final Path source = Paths.get(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe) + cfes[i], new String[0]);
                final BasicFileAttributes att = Files.readAttributes(source, BasicFileAttributes.class, new LinkOption[0]);
                final long dt = att.creationTime().toMillis();
                if ((now - dt) / 1000L > maximo) {
                    existeCFeNaoTransmitido = true;
                    return true;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean transmitirLoteVazio() {
        try {
            ControleLogs.logar("Iniciando Comunica\u00e7\u00e3o com a SEFAZ.");
            final WebServiceCFeRecepcao wsRecepcao = new WebServiceCFeRecepcao();
            wsRecepcao.setCFes("<CFe></CFe>");
            wsRecepcao.setIdLote(ControleSeguranca.gerarIdLote());
            if (!wsRecepcao.consumirWebService()) {
                return false;
            }
            String cStat = wsRecepcao.getcStat();
            if (cStat == null || !cStat.equals("105")) {
                return false;
            }
            final String recibo = wsRecepcao.getnRec();
            cStat = null;
            final WebServiceCFeRetRecepcao wsRetRecepcao = new WebServiceCFeRetRecepcao();
            wsRetRecepcao.setnRec(recibo);
            if (!wsRetRecepcao.consumirWebService()) {
                return false;
            }
            cStat = wsRetRecepcao.getcStat();
            return cStat != null && (cStat.equals("105") || cStat.equals("106"));
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
