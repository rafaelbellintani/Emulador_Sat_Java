// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.threads;

import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.controles.webservices.WebServiceCFeStatus;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.Configuracoes;
import java.util.TimerTask;

public class ThreadStatus extends TimerTask
{
    @Override
    public void run() {
        if (Configuracoes.SAT.bloqueado || Configuracoes.SAT.ativado) {
            ControleLogs.logar("Iniciando consulta status sefaz");
            final String dir = Configuracoes.SistemaArquivos.DIRETORIO_STATUS;
            final String[] arquivos = ControleArquivos.listarDiretorio(dir);
            if (arquivos.length > 10) {
                for (int i = 0; i < arquivos.length - 3; ++i) {
                    ControleArquivos.excluirArquivo(String.valueOf(dir) + arquivos[i]);
                }
            }
            final WebServiceCFeStatus wsStatus = new WebServiceCFeStatus("STATUS");
            if (!wsStatus.consumirWebService()) {
                ControleLogs.logar("Erro de Comunica\u00e7\u00e3o com a Sefaz.");
            }
            final String retorno = wsStatus.getcStat();
            if (retorno != null) {
                if (wsStatus.getDataRecibo() == null) {
                    ControleArquivos.escreverCaracteresArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_STATUS) + ControleTempo.getTimeStamp() + ".txt", retorno.toCharArray());
                }
                else {
                    ControleArquivos.escreverCaracteresArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_STATUS) + wsStatus.getDataRecibo() + ".txt", retorno.toCharArray());
                }
            }
            ControleLogs.logar("Fim consulta status sefaz");
        }
    }
}
