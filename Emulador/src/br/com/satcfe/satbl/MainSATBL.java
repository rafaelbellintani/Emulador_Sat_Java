// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl;

import br.com.satcfe.satbl.controles.ControladorDesativacao;
import br.com.satcfe.satbl.controles.ComandosSefazFacade;
import br.com.satcfe.satbl.controles.ControladorEmuladorOffLine;
import br.com.satcfe.satbl.controles.ControladorComandosSAT;
import br.com.satcfe.satbl.controles.ControladorBloqueio;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoAtivacao;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoUf;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoBloqueio;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoUso;
import br.com.satcfe.satbl.controles.ControleParametrizacao;
import br.com.satcfe.satbl.modelos.parametrizacao.ParametrizacaoFabrica;
import java.util.TimerTask;
import br.com.um.interfaces.NTPAdapter;
import br.com.satcfe.satbl.controles.ControleNTP;
import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.controles.ControleSeguranca;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.controles.ControleNumeroSessao;
import br.com.satcfe.satbl.conexao.MonitorComandosSAT;
import java.util.Timer;
import br.com.satcfe.satbl.controles.ControleConsultaComandos;
import br.com.satcfe.satbl.threads.ConsultaPeriodicaComandos;
import br.com.satcfe.satbl.threads.ThreadVerificacaoEnvio;
import br.com.satcfe.satbl.threads.ThreadEnvioNota;
import br.com.satcfe.satbl.threads.ThreadStatus;
import br.com.satcfe.satbl.conexao.IOListener;

public class MainSATBL implements IOListener
{
    private static ThreadStatus tStatus;
    private static ThreadEnvioNota tEnvio;
    private static ThreadVerificacaoEnvio tVerificaEnvio;
    private static ConsultaPeriodicaComandos tVerificacaoComandos;
    private static ControleConsultaComandos cComandos;
    private static Timer timerVerificaEnvioNota;
    private static Timer timerEnvioNota;
    private static Timer timerVerificacaoComandos;
    private static Timer timerVerificacaoStatusSefaz;
    private MonitorComandosSAT mcs;
    private ControleNumeroSessao cNumeroSessao;
    private final String COMANDO_ATIVACAO = "AtivarSAT";
    private final String COMANDO_CERTIFICADO_ICPBRASIL = "ComunicarCertificadoICPBRASIL";
    private final String COMANDO_ENVIAR_VENDA = "EnviarDadosVenda";
    private final String COMANDO_CANCELAR_VENDA = "CancelarUltimaVenda";
    private final String COMANDO_CONSULTAR = "ConsultarSAT";
    private final String COMANDO_TESTE_FIMFIM = "TesteFimAFim";
    private final String COMANDO_STATUS_OPERACIONAL = "ConsultarStatusOperacional";
    private final String COMANDO_CONSULTA_SESSAO = "ConsultarNumeroSessao";
    private final String COMANDO_CONFIGURAR_REDE = "ConfigurarInterfaceDeRede";
    private final String COMANDO_ASSOCIAR_ASSINATURA = "AssociarAssinatura";
    private final String COMANDO_ATUALIZAR = "AtualizarSoftwareSAT";
    private final String COMANDO_EXTRAIR_LOGS = "ExtrairLogs";
    private final String COMANDO_BLOQUEAR = "BloquearSAT";
    private final String COMANDO_DESBLOQUEAR = "DesbloquearSAT";
    private final String COMANDO_TROCAR_CODIGO = "TrocarCodigoDeAtivacao";
    private final String COMANDO_DESLIGAR = "DesligarSAT";
    private final String COMANDO_DESATIVAR_SAT = "DesativarSAT";
    
    static {
        MainSATBL.tStatus = null;
        MainSATBL.tEnvio = null;
        MainSATBL.tVerificaEnvio = null;
        MainSATBL.tVerificacaoComandos = null;
        MainSATBL.cComandos = null;
        MainSATBL.timerVerificaEnvioNota = null;
        MainSATBL.timerEnvioNota = null;
        MainSATBL.timerVerificacaoComandos = null;
        MainSATBL.timerVerificacaoStatusSefaz = null;
    }
    
    public MainSATBL() {
        this.cNumeroSessao = null;
    }
    
    public void executar() {
    	
        ControleLogs.logar("Carregando parametriza\u00e7\u00f5es.");
        this.carregarPamametrizacaoAtual();
        ControleSeguranca.carregarCadeiaCertificados();
        ControleLogs.logar("Sincronizando Relogio NTP.");
        final ControleTempo tempo = new ControleTempo();
        tempo.setNtp((NTPAdapter)new ControleNTP("ntp.cais.rnp.br", 123)); //ANTERIOR (Parametrizacoes.enderecoNtp, Parametrizacoes.portaNTP)
        if (!Configuracoes.SAT.emuladorOffLine) {
            tempo.atualizarNTP();
        }
        ControleLogs.logar("Aguardando Comunica\u00e7\u00e3o.");
        this.iniciarTarefas();
        this.iniciarLeituraComandos();
        this.cNumeroSessao = new ControleNumeroSessao();
    }
    
    public void iniciarTarefas() {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                MainSATBL.this.verificarBloqueioAutonomo();
            }
        }, 5000L, 60000L);
        if (Configuracoes.SAT.emuladorOffLine && Configuracoes.SAT.ativado) {
            if (Parametrizacoes.valorComandos < 1000L) {
                Parametrizacoes.valorComandos = Parametrizacoes.valorVerificacao;
            }
            this.iniciarConsultaPeriodicaPorComandos();
            this.iniciarVerificacaoPeriodicaPorStatusSefaz();
            if (!Configuracoes.SAT.bloqueado) {
                this.iniciarEnvioDasNotas();
            }
        }
    }
    
    public void carregarPamametrizacaoAtual() {
    	
        if(!ParametrizacaoFabrica.existe()) {
            ControleLogs.logar("Parametrizacao de f\u00e1brica n\u00e3o encontrada.");
            Configuracoes.SAT.corrompido = true;
            return;
        }
       boolean paramOK = ControleParametrizacao.carregarParametrizacao(0);
       ControleLogs.logar("Parametrização concluída.");
       if (!paramOK) {
            ControleLogs.logar("Parametrizacao de f\u00e1brica corrompida.");
            Configuracoes.SAT.corrompido = true;
            return;
        }
      
       if (Configuracoes.SAT.emuladorOffLine) {
            paramOK = false;
            if (ParametrizacaoUso.existe()) {
            	System.out.println("Esta aqui");
                paramOK = ControleParametrizacao.carregarParametrizacao(3);
                if (!paramOK) {
                    ControleLogs.logar("Parametrizacao de uso corrompida.");
                    Configuracoes.SAT.ativado = false;
                    Configuracoes.SAT.associado = false;
                    return;
                }
                if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA) && ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT)) {
                    Configuracoes.SAT.ativado = true;
                }
                if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ASSINATURA)) {
                    Configuracoes.SAT.associado = true;
                }
                Configuracoes.SAT.bloqueado = false;
            }
            else if (ParametrizacaoBloqueio.existe()) {
                paramOK = ControleParametrizacao.carregarParametrizacao(4);
                if (!paramOK) {
                    ControleLogs.logar("Parametrizacao de bloqueio corrompida.");
                    Configuracoes.SAT.ativado = false;
                    Configuracoes.SAT.associado = false;
                    Configuracoes.SAT.bloqueado = false;
                    return;
                }
                if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA) && ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT)) {
                    Configuracoes.SAT.ativado = true;
                }
                Configuracoes.SAT.bloqueado = true;
            }
            return;
        }
        if (ParametrizacaoUf.existe()) {
            paramOK = ControleParametrizacao.carregarParametrizacao(2);
            if (!paramOK) {
                ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS, "Parametrizacao UF corrompida.\r\n".toCharArray(), true);
                ControleLogs.logar("Parametrizacao UF corrompida.");
                Configuracoes.SAT.ativado = false;
                Configuracoes.SAT.associado = false;
                return;
            }
        }
        if (ParametrizacaoAtivacao.existe()) {
            paramOK = ControleParametrizacao.carregarParametrizacao(1);
            if (!paramOK) {
                ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS, "Parametrizacao Ativa\u00e7\u00e3o corrompida.\r\n".toCharArray(), true);
                ControleLogs.logar("Parametrizacao Ativa\u00e7\u00e3o corrompida.");
                Configuracoes.SAT.ativado = false;
                Configuracoes.SAT.associado = false;
                return;
            }
        }
        if (ParametrizacaoUso.existe()) {
            paramOK = ControleParametrizacao.carregarParametrizacao(3);
            if (!paramOK) {
                ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS, "Parametrizacao de uso corrompido.\r\n".toCharArray(), true);
                ControleLogs.logar("Parametrizacao de uso corrompido.");
                Configuracoes.SAT.ativado = false;
                Configuracoes.SAT.associado = false;
                return;
            }
            if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA) && ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT)) {
                Configuracoes.SAT.ativado = true;
            }
            Configuracoes.SAT.bloqueado = false;
            if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ASSINATURA)) {
                Configuracoes.SAT.associado = true;
            }
            if (Configuracoes.SAT.CNPJEstabelecimento.length() != 14 || !ControleDados.isNumerico(Configuracoes.SAT.CNPJEstabelecimento)) {
                ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS, "CNPJ corrompido.\r\n".toCharArray(), true);
                ControleLogs.logar("CNPJ corrompido.");
                Configuracoes.SAT.ativado = false;
                Configuracoes.SAT.associado = false;
            }
        }
        else if (ParametrizacaoBloqueio.existe()) {
            paramOK = ControleParametrizacao.carregarParametrizacao(4);
            if (!paramOK) {
                ControleArquivos.escreverCaracteresArquivoUTF8(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_LOGS, "Parametrizacao Bloqueio corrompida.\r\n".toCharArray(), true);
                ControleLogs.logar("Parametrizacao Bloqueio corrompida.");
                Configuracoes.SAT.ativado = false;
                Configuracoes.SAT.associado = false;
                Configuracoes.SAT.bloqueado = false;
                return;
            }
            if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE_PUBLICA) && ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_CERT)) {
                Configuracoes.SAT.ativado = true;
            }
            if (ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_ASSINATURA)) {
                Configuracoes.SAT.associado = true;
            }
            Configuracoes.SAT.bloqueado = true;
        }
    
    }
    
    private void verificarBloqueioAutonomo() {
        try {
            if (Configuracoes.SAT.bloqueado) {
                return;
            }
            if (!ControleArquivos.existeArquivo(Configuracoes.SistemaArquivos.CAMINHO_ULTIMA_COMUNICACAO)) {
                return;
            }
            ControladorBloqueio.verificaBloqueioAutonomo();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void iniciarLeituraComandos() {
        (this.mcs = new MonitorComandosSAT(50L, Configuracoes.SistemaArquivos.DIRETORIO_COMANDOS)).setIOListener(this);
    }
    
    public void iniciarEnvioDasNotas() {
        if (MainSATBL.timerEnvioNota != null) {
            MainSATBL.timerEnvioNota.cancel();
        }
        MainSATBL.timerEnvioNota = new Timer(false);
        MainSATBL.tEnvio = new ThreadEnvioNota();
        MainSATBL.timerEnvioNota.schedule(MainSATBL.tEnvio, 0L, Parametrizacoes.valorTransmissao);
    }
    
    public void iniciarEnvioDasNotasPeriodico() {
        if (MainSATBL.timerVerificaEnvioNota != null) {
            MainSATBL.timerVerificaEnvioNota.cancel();
        }
        MainSATBL.timerVerificaEnvioNota = new Timer(false);
        MainSATBL.tVerificaEnvio = new ThreadVerificacaoEnvio();
        MainSATBL.timerVerificaEnvioNota.schedule(MainSATBL.tEnvio, 0L, Parametrizacoes.valorTransmissao);
    }
    
    public void iniciarConsultaPeriodicaPorComandos() {
        if (MainSATBL.timerVerificacaoComandos != null) {
            MainSATBL.timerVerificacaoComandos.cancel();
        }
        MainSATBL.timerVerificacaoComandos = new Timer(false);
        MainSATBL.tVerificacaoComandos = new ConsultaPeriodicaComandos();
        MainSATBL.timerVerificacaoComandos.schedule(MainSATBL.tVerificacaoComandos, 0L, Parametrizacoes.valorComandos);
    }
    
    public void iniciarVerificacaoPeriodicaPorStatusSefaz() {
        if (MainSATBL.timerVerificacaoStatusSefaz != null) {
            MainSATBL.timerVerificacaoStatusSefaz.cancel();
        }
        MainSATBL.timerVerificacaoStatusSefaz = new Timer(false);
        MainSATBL.tStatus = new ThreadStatus();
        MainSATBL.timerVerificacaoStatusSefaz.schedule(MainSATBL.tStatus, 0L, Parametrizacoes.valorVerificacao);
    }
    
    public void pararEnvioDasNotas() {
        MainSATBL.timerEnvioNota.cancel();
        while (MainSATBL.tEnvio.isEnviando()) {}
    }
    
    @Override
    public void notifyIncomingData(final String cmd, final String mensagem) {
        if (!Configuracoes.SAT.corrompido) {
            this.tratarComandos(cmd, mensagem);
        }
        else {
            this.mcs.responder(cmd, "SAT n\u00e3o conseguiu carregar as parametriza\u00e7\u00f5es de f\u00e1brica.");
        }
    }
    
    public void tratarComandos(final String numeroComando, final String parametros) {
        try {
            String resposta = "";
            String numeroSessao = "";
            final String[] parametro = ControleDados.quebrarString(parametros, "|");
            final String nomeComando = parametro[0];
            if (parametro.length >= 2) {
                numeroSessao = parametro[1];
            }
            final String aviso = ControleSeguranca.carregarAviso();
            final ControladorComandosSAT cComandos = new ControladorComandosSAT(this.cNumeroSessao, aviso);
            if (nomeComando.equals("AtivarSAT")) {
                resposta = cComandos.tratarComandoAtivacao(parametro);
            }
            else if (nomeComando.equals("ComunicarCertificadoICPBRASIL")) {
                resposta = cComandos.tratarComandoComunicarCertificadoICPBRASIL(parametro);
            }
            else if (nomeComando.equals("EnviarDadosVenda")) {
                resposta = cComandos.tratarComandoEnviarDadosVenda(parametro);
            }
            else if (nomeComando.equals("CancelarUltimaVenda")) {
                resposta = cComandos.tratarComandoCancelarUltimaVenda(parametro);
            }
            else if (nomeComando.equals("ConsultarSAT")) {
                resposta = cComandos.tratarComandoConsultarSAT(parametro);
                ControleLogs.logar("FIM CONSULTA");
            }
            else if (nomeComando.equals("TesteFimAFim")) {
                resposta = cComandos.tratarComandoTesteFimAFim(parametro);
            }
            else if (nomeComando.equals("ConsultarStatusOperacional")) {
                resposta = cComandos.tratarComandoConsultarStatusOperacional(parametro);
            }
            else if (nomeComando.equals("ConsultarNumeroSessao")) {
                resposta = cComandos.tratarComandoConsultarNumeroSessao(parametro);
            }
            else if (!nomeComando.equals("ConfigurarInterfaceDeRede")) {
                if (nomeComando.equals("AssociarAssinatura")) {
                    resposta = cComandos.tratarComandoAssociarAssinatura(parametro);
                }
                else if (nomeComando.equals("AtualizarSoftwareSAT")) {
                    if (Configuracoes.SAT.emuladorOffLine) {
                        final ControladorEmuladorOffLine controle = new ControladorEmuladorOffLine();
                        resposta = String.valueOf(controle.comandoAtualizar(parametro)) + "||" + aviso;
                    }
                    else {
                        final ComandosSefazFacade facade = new ComandosSefazFacade();
                        if (!facade.atualizarSoftwareBasico("123456789098765432")) {
                            resposta = "14003|Erro na atualiza\u00e7\u00e3o.||" + aviso;
                            String path = "/software_Basico/SAT-CFe-novo.jar";
                            if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS) {
                                path = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO) + "SAT-CFe-novo.jar";
                            }else if(Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.LINUX) {
                            	path = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVO_ATUALIZACAO) + "SAT-CFe-novo.jar";
                            }
                            final String hash = ControleArquivos.lerCaracteresArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_HASH);
                            final String hashCalc = ControleDados.getHashArquivo("SHA-256", path);
                            if (hash != null && hashCalc != null && !hash.equalsIgnoreCase(hashCalc)) {
                                resposta = "14004|Arquivo de atualiza\u00e7\u00e3o inv\u00e1lido.||" + aviso;
                            }
                        }
                    }
                }
                else if (nomeComando.equals("ExtrairLogs")) {
                    resposta = cComandos.tratarComandoExtrairLogs(parametro);
                }
                else if (nomeComando.equals("BloquearSAT")) {
                    resposta = cComandos.tratarComandoBloquearSAT(parametro);
                }
                else if (nomeComando.equals("DesbloquearSAT")) {
                    resposta = cComandos.tratarComandoDesbloquearSAT(parametro);
                }
                else if (nomeComando.equals("TrocarCodigoDeAtivacao")) {
                    resposta = cComandos.tratarComandoTrocarCodigoDeAtivacao(parametro);
                }
                else if (nomeComando.equals("DesligarSAT")) {
                    ControleLogs.logar("INICIO DESLIGAMENTO");
                    Configuracoes.SAT.desligamentoIniciado = true;
                    resposta = "19099|SAT-CFe sera Desligado.||";
                    this.mcs.responder(numeroComando, resposta);
                }
                else if (nomeComando.equals("DesativarSAT")) {
                    ControleLogs.logar("INICIO RESET DO EQUIPAMENTO SAT");
                    resposta = new ControladorDesativacao().tratarComandoDesativarSAT(aviso);
                    this.mcs.responder(numeroComando, resposta);
                    ControleLogs.logar("FIM RESET DO EQUIPAMENTO SAT");
                }
            }
            if (this.cNumeroSessao.validarNumeroSessao(Integer.parseInt(numeroSessao))) {
                this.cNumeroSessao.addNumeroSessao(Integer.parseInt(numeroSessao));
                this.cNumeroSessao.armazenarRetorno(numeroSessao, resposta);
            }
            resposta = String.valueOf(numeroSessao) + "|" + resposta;
            this.mcs.responder(numeroComando, resposta);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public ThreadEnvioNota getThreadEnvio() {
        return MainSATBL.tEnvio;
    }
}
