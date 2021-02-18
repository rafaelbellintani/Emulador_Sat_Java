// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.modelos.ComandoSefaz;
import java.util.List;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeComandos;

public class ControleConsultaComandos
{
    private static boolean executando;
    
    static {
        ControleConsultaComandos.executando = false;
    }
    
    @Deprecated
    public static void procurarNovosComandos() {
        if (!ControleConsultaComandos.executando) {
            ControleLogs.logar("Iniciando Consulta por Novos Comandos");
            new ControleConsultaComandos().consultarComandos();
            ControleLogs.logar("Fim da Consulta por Novos Comandos");
        }
    }
    
    public void consultarComandos() {
        ControleConsultaComandos.executando = true;
        try {
            final WebServiceCFeComandos wsCFeComandos = new WebServiceCFeComandos();
            wsCFeComandos.setxServ("COMANDOS");
            List<ComandoSefaz> comandos = null;
            if (!wsCFeComandos.consumirWebService()) {
                throw new ErroComunicacaoRetaguardaException();
            }
            comandos = (List<ComandoSefaz>)wsCFeComandos.getComando();
            final ControleFilaComandos fila = ControleFilaComandos.getInstance();
            fila.addListaComando(comandos);
            this.executarComandos(fila);
        }
        catch (ErroComunicacaoRetaguardaException e) {
            e.printStackTrace();
            ControleLogs.logar("Erro de Comunicacao com a Sefaz.");
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        ControleConsultaComandos.executando = false;
    }
    
    private void executarComandos(final ControleFilaComandos fila) {
        if (fila.size() == 0) {
            ControleLogs.logar("N\u00e3o existem comandos da SEFAZ pendentes");
        }
        else {
            final ComandosSefazFacade comandosSefaz = new ComandosSefazFacade();
            for (int i = 0; i < fila.size(); ++i) {
                final ComandoSefaz cmd = fila.processarProximoComando();
                if (cmd != null) {
                    final String comando = cmd.getNome();
                    final String idCmd = cmd.getIdCmd();
                    Boolean ack = null;
                    ControleLogs.logar("Comando \"" + comando + "\" recebido da SEFAZ");
                    for (int tentativa = 0; tentativa < 3; ++tentativa) {
                        ack = null;
                        if (comando.equalsIgnoreCase("COMANDO_001")) {
                            final String tipo = ControleArquivos.lerArquivo(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_TIPO_CERT);
                            final boolean icpBrasil = tipo != null && tipo.equals("2");
                            ack = comandosSefaz.renovarCertificado(icpBrasil);
                        }
                        else if (comando.equalsIgnoreCase("COMANDO_002")) {
                            ack = comandosSefaz.transmitirArquivosVenda();
                        }
                        else if (comando.equalsIgnoreCase("COMANDO_003")) {
                            ack = comandosSefaz.transmitirArquivosLogs();
                            ControleLogs.logar("Fim da Transmiss\u00e3o de Logs do SAT-CFe");
                        }
                        else if (comando.equalsIgnoreCase("COMANDO_004")) {
                            ack = comandosSefaz.atualizarSoftwareBasico(cmd.getIdCmd());
                        }
                        else if (comando.equalsIgnoreCase("COMANDO_005")) {
                            ack = comandosSefaz.transmitirEstadoOperacional();
                        }
                        else if (comando.equalsIgnoreCase("COMANDO_006")) {
                            if (ack = comandosSefaz.atualizarParametrizacao()) {
                                ControleLogs.logar("Parametriza\u00e7\u00e3o atualizada com sucesso");
                            }
                            else {
                                ControleLogs.logar("Erro ao atualizar as parametriza\u00e7\u00f5es");
                            }
                        }
                        else if (comando.equalsIgnoreCase("COMANDO_007")) {
                            ack = comandosSefaz.sincronizarNTP();
                        }
                        else if (comando.equalsIgnoreCase("COMANDO_008")) {
                            ack = comandosSefaz.envioAvisoUsuario(cmd);
                        }
                        else if (comando.equalsIgnoreCase("COMANDO_009")) {
                            ack = comandosSefaz.consultarParametrosGestao(cmd);
                        }
                        if (ack == null) {
                            break;
                        }
                        if (ack) {
                            break;
                        }
                        ControleLogs.logar("Erro ao Executar o Comando. Nova Tentativa..");
                    }
                    if (ack != null && ack) {
                        this.responderComando(idCmd, "OK");
                    }
                    else if (ack != null && !ack) {
                        this.responderComando(idCmd, "NO");
                    }
                    else {
                        System.err.println("N\u00e3o deve responder ainda!");
                    }
                }
            }
        }
    }
    
    private void responderComando(final String idCmd, final String resultado) {
        try {
            ControleLogs.logar("Iniciando resposta aos comandos");
            final WebServiceCFeComandos wsCFeComandos = new WebServiceCFeComandos();
            wsCFeComandos.setxServ("RESPOSTA");
            wsCFeComandos.setIdCmd(idCmd);
            wsCFeComandos.setStatus(resultado);
            if ("2.9.4".indexOf("teste") >= 0) {
                ControleLogs.logar("Vers\u00e3o de Teste, os comandos n\u00e3o s\u00e3o respondidos");
                return;
            }
            if (!wsCFeComandos.consumirWebService()) {
                throw new ErroComunicacaoRetaguardaException();
            }
            if (wsCFeComandos.getcStat().equals("120")) {
                ControleLogs.logar("N\u00e3o existem comandos da SEFAZ pendentes");
            }
            ControleFilaComandos.getInstance().removeComando(idCmd);
        }
        catch (ErroComunicacaoRetaguardaException e) {
            e.printStackTrace();
            ControleLogs.logar("Erro de Comunica\u00e7\u00e3o com a Sefaz");
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    
    public static boolean isExecutandoConsulta() {
        return ControleConsultaComandos.executando;
    }
}
