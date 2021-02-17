// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.threads;

import br.com.satcfe.satbl.controles.webservices.WebServiceCFeCancelamento;
import br.com.satcfe.satbl.controles.ControleSeguranca;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeRecepcao;
import br.com.um.controles.ControleDados;
import java.util.Stack;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.controles.ControleLogs;
import java.util.TimerTask;

public class ThreadEnvioNota extends TimerTask
{
    private static boolean enviando;
    
    static {
        ThreadEnvioNota.enviando = false;
    }
    
    @Override
    public void run() {
        try {
            ThreadEnvioNota.enviando = true;
            ControleLogs.logar("Iniciando envio das notas.");
            this.enviarArquivosEmissao();
            ControleLogs.logar("Fim envio das notas.");
            ControleLogs.logar("Iniciando envio dos cancelamentos.");
            this.enviarArquivosCancelamento();
            ControleLogs.logar("Fim envio dos cancelamentos.");
            ThreadEnvioNota.enviando = false;
            ControleLogs.controlarAquivoLogs();
            final ThreadVerificacaoEnvio tEnvio = new ThreadVerificacaoEnvio();
            final Thread t = new Thread(tEnvio);
            Thread.sleep(10000L);
            t.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void enviarArquivosEmissao() {
        try {
            StringBuffer lote = new StringBuffer();
            final int tamanhoMaximo = 1534976;
            int qtdLote = 0;
            final String[] CFes = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe);
            Stack<String> pilhaRealocacao = new Stack<String>();
            for (int i = 0; i < CFes.length; ++i) {
                ++qtdLote;
                final long tamanhoAtual = ControleArquivos.tamanhoArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe) + CFes[i]);
                if (lote.length() + tamanhoAtual > tamanhoMaximo || qtdLote == 51) {
                    final String[] status = this.enviarLoteEmissao(lote.toString());
                    if (status[1].equals("105")) {
                        this.realocarArquivosEmissao(pilhaRealocacao.toArray(), status[0]);
                        pilhaRealocacao = new Stack<String>();
                        lote = new StringBuffer();
                        final String sCfe = ControleArquivos.lerArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe) + CFes[i]);
                        lote.append(ControleDados.removerCabecalhoXML(sCfe));
                        pilhaRealocacao.add(CFes[i]);
                        qtdLote = 1;
                    }
                    else {
                        if (status[1].equals("123")) {
                            break;
                        }
                        break;
                    }
                }
                else {
                    final String sCfe2 = ControleArquivos.lerArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe) + CFes[i]);
                    lote.append(ControleDados.removerCabecalhoXML(sCfe2));
                    pilhaRealocacao.add(CFes[i]);
                }
                if (i + 1 == CFes.length) {
                    final String[] status = this.enviarLoteEmissao(lote.toString());
                    if (status[1].equals("105")) {
                        this.realocarArquivosEmissao(pilhaRealocacao.toArray(), status[0]);
                        pilhaRealocacao = new Stack<String>();
                    }
                    else {
                        if (status[1].equals("123")) {
                            break;
                        }
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String[] enviarLoteEmissao(final String lote) {
        try {
            final WebServiceCFeRecepcao wsRecepcao = new WebServiceCFeRecepcao();
            wsRecepcao.setCFes(lote);
            wsRecepcao.setIdLote(ControleSeguranca.gerarIdLote());
            wsRecepcao.consumirWebService();
            return new String[] { wsRecepcao.getnRec(), wsRecepcao.getcStat() };
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void realocarArquivosEmissao(final Object[] arquivos, final String recibo) {
        try {
            ControleArquivos.criarDiretorio(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibo + "/");
            for (int i = 0; i < arquivos.length; ++i) {
                final String dados = ControleArquivos.lerArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe) + arquivos[i]);
                ControleArquivos.escreverCaracteresArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibo + "/" + arquivos[i], dados.toCharArray(), false);
                if (ControleArquivos.existeArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibo + "/" + arquivos[i])) {
                    ControleArquivos.excluirArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe) + arquivos[i]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void enviarArquivosCancelamento() {
        try {
            StringBuffer lote = new StringBuffer();
            final int tamanhoMaximo = 1534976;
            int qtdLote = 0;
            final String[] CFesCanc = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS);
            Stack<String> pilhaRealocacao = new Stack<String>();
            for (int i = 0; i < CFesCanc.length; ++i) {
                ++qtdLote;
                final long tamanhoAtual = ControleArquivos.tamanhoArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS) + CFesCanc[i]);
                if (lote.length() + tamanhoAtual > tamanhoMaximo || qtdLote == 51) {
                    final String[] status = this.enviarLoteCancelamento(lote.toString());
                    if (status[1].equals("105")) {
                        this.realocarArquivosCancelamento(pilhaRealocacao.toArray(), status[0]);
                        pilhaRealocacao = new Stack<String>();
                        lote = new StringBuffer();
                        final String s = ControleArquivos.lerArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS) + CFesCanc[i]);
                        lote.append(ControleDados.removerCabecalhoXML(s));
                        pilhaRealocacao.add(CFesCanc[i]);
                        qtdLote = 1;
                    }
                    else {
                        if (status[1].equals("123")) {
                            break;
                        }
                        break;
                    }
                }
                else {
                    final String s2 = ControleArquivos.lerArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS) + CFesCanc[i]);
                    lote.append(ControleDados.removerCabecalhoXML(s2));
                    pilhaRealocacao.add(CFesCanc[i]);
                }
                if (i + 1 == CFesCanc.length) {
                    final String[] status = this.enviarLoteCancelamento(lote.toString());
                    if (status[1].equals("105")) {
                        this.realocarArquivosCancelamento(pilhaRealocacao.toArray(), status[0]);
                        pilhaRealocacao = new Stack<String>();
                    }
                    else {
                        if (status[1].equals("123")) {
                            break;
                        }
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String[] enviarLoteCancelamento(final String lote) {
        try {
            final WebServiceCFeCancelamento wsCancelamento = new WebServiceCFeCancelamento();
            wsCancelamento.setLoteCFesCanc(lote);
            wsCancelamento.setIdLote(ControleSeguranca.gerarIdLote());
            wsCancelamento.consumirWebService();
            return new String[] { wsCancelamento.getnRec(), wsCancelamento.getcStat() };
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void realocarArquivosCancelamento(final Object[] arquivos, final String recibo) {
        try {
            ControleArquivos.criarDiretorio(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibo + "/");
            for (int i = 0; i < arquivos.length; ++i) {
                final String dados = ControleArquivos.lerArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS) + arquivos[i]);
                ControleArquivos.escreverCaracteresArquivoUTF8(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibo + "/" + arquivos[i], dados.toCharArray(), false);
                if (ControleArquivos.existeArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibo + "/" + arquivos[i])) {
                    ControleArquivos.excluirArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS) + arquivos[i]);
                }
            }
        }
        catch (Exception e) {
            ControleLogs.logar(e.getMessage());
        }
    }
    
    public void enviarLotesDeCFe() {
        ThreadEnvioNota.enviando = true;
        ControleLogs.logar("Iniciando envio das notas.");
        this.enviarArquivosEmissao();
        ControleLogs.logar("Fim envio das notas.");
        ThreadEnvioNota.enviando = false;
    }
    
    public boolean isEnviando() {
        return ThreadEnvioNota.enviando;
    }
}
