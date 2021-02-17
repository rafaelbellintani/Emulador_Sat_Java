// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.threads;

import java.util.List;
import br.com.satcfe.satbl.modelos.ReciboCFe;
import br.com.satcfe.satbl.excecoes.ErroComunicacaoRetaguardaException;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeRetRecepcao;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.Configuracoes;

public class ThreadVerificacaoEnvio implements Runnable
{
    public static boolean verificando;
    
    static {
        ThreadVerificacaoEnvio.verificando = false;
    }
    
    @Override
    public void run() {
        if (Configuracoes.SAT.ativado || Configuracoes.SAT.bloqueado) {
            ThreadVerificacaoEnvio.verificando = true;
            ControleLogs.logar("Iniciando verifica\u00e7\u00e3o dos lotes.");
            this.verificacaoArquivosEmissao();
            ControleLogs.logar("Fim verifica\u00e7\u00e3o dos lotes.");
            ThreadVerificacaoEnvio.verificando = false;
        }
    }
    
    private void verificacaoArquivosEmissao() {
        try {
            final String[] recibos = ControleArquivos.listarDiretorio(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS);
            for (int i = 0; i < recibos.length; ++i) {
                final WebServiceCFeRetRecepcao wsRetRecepcao = new WebServiceCFeRetRecepcao();
                wsRetRecepcao.setnRec(recibos[i]);
                if (!wsRetRecepcao.consumirWebService()) {
                    throw new ErroComunicacaoRetaguardaException("Erro de Comunicacao com a Sefaz.");
                }
                if (wsRetRecepcao.getcStat().equals("106")) {
                    final List<String> listaRecibos = (List<String>)wsRetRecepcao.getListaRetCFe();
                    for (int j = 0; j < listaRecibos.size(); ++j) {
                        final ReciboCFe retCFe = new ReciboCFe(listaRecibos.get(j));
                        final String chave = "CFe" + retCFe.getChCFe();
                        final String codigo = retCFe.getcStat();
                        if (codigo != null) {
                            if (codigo.equals("100") || codigo.equals("101") || codigo.equals("102") || codigo.equals("103")) {
                                this.removerCFeProcessado(recibos[i], chave, String.valueOf(codigo) + " - " + retCFe.getxMotivo());
                            }
                            else if (codigo.equals("124")) {
                                this.adiarTransmissaoCFe(recibos[i], chave);
                                ControleLogs.logar("Adiar transmiss\u00e3o do CF-e: " + chave);
                            }
                            else if (codigo.equals("201") || codigo.equals("202") || codigo.equals("261")) {
                                this.adiarTransmissaoCFe(recibos[i], chave);
                                ControleLogs.logar("Erro de assinatura, retransmiss\u00e3o do CF-e: " + chave);
                            }
                            else {
                                this.realocarCFeProcessadoComErro(recibos[i], chave, String.valueOf(codigo) + " - " + retCFe.getxMotivo());
                                ControleLogs.logar("CFe Processado com Erro: " + chave);
                            }
                            final String[] s = ControleArquivos.listarDiretorio(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibos[i]);
                            if (s != null && s.length == 0) {
                                ControleArquivos.excluirArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibos[i]);
                            }
                        }
                    }
                }
                else if (wsRetRecepcao.getcStat().equals("107")) {
                    ControleLogs.logar(String.valueOf(wsRetRecepcao.getcStat()) + " - " + wsRetRecepcao.getxMotivo());
                }
                else {
                    ControleLogs.logar("Erro: " + wsRetRecepcao.getcStat() + " - " + wsRetRecepcao.getxMotivo());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void removerCFeProcessado(final String recibo, final String chave, final String codigo) {
        final String origem = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibo + "/" + chave + ".xml";
        if (ControleArquivos.existeArquivo(origem)) {
            if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS) {
                ControleArquivos.excluirArquivo(origem);
            }else if(Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.LINUX) {
            	ControleArquivos.excluirArquivo(origem);
            }
            else {
                final String destino = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_REMOVIDOS) + "/" + chave + "/";
                ControleArquivos.criarDiretorio(destino);
                br.com.um.controles.ControleArquivos.moverArquivo(origem, String.valueOf(destino) + chave + ".xml");
                ControleArquivos.escreverCaracteresArquivo(String.valueOf(destino) + "recibo.txt", codigo.toCharArray());
            }
        }
        else {
            System.err.println("Erro ao remover CFe processado: " + origem);
        }
    }
    
    public void adiarTransmissaoCFe(final String recibo, final String chave) {
        try {
            if (recibo != null && chave != null) {
                final String origem = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibo + "/" + chave + ".xml";
                final String s = ControleArquivos.lerArquivo(origem);
                String destino;
                if (s.indexOf("<CFeCanc") >= 0) {
                    destino = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_CANCELADOS) + chave + ".xml";
                }
                else {
                    destino = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFe) + chave + ".xml";
                }
                br.com.um.controles.ControleArquivos.moverArquivo(origem, destino);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void realocarCFeProcessadoComErro(final String recibo, final String chave, final String codigo) {
        final String origem = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_ENVIADOS) + recibo + "/" + chave + ".xml";
        if (ControleArquivos.existeArquivo(origem)) {
            final String destino = String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_ARQUIVOS_CFE_COM_ERRO) + "/" + chave + "/";
            ControleArquivos.criarDiretorio(destino);
            br.com.um.controles.ControleArquivos.moverArquivo(origem, String.valueOf(destino) + chave + ".xml");
            ControleArquivos.escreverCaracteresArquivo(String.valueOf(destino) + "recibo.txt", codigo.toCharArray());
        }
        else {
            System.err.println("Erro ao realocar CFe processado com erro: " + origem);
        }
    }
    
    public boolean isVerificando() {
        return ThreadVerificacaoEnvio.verificando;
    }
}
