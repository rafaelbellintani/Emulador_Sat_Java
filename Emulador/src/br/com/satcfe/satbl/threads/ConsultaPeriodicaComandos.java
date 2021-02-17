// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.threads;

import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.controles.ControleConsultaComandos;
import br.com.satcfe.satbl.Configuracoes;
import java.util.TimerTask;

public class ConsultaPeriodicaComandos extends TimerTask
{
    @Override
    public void run() {
        if ((Configuracoes.SAT.bloqueado || Configuracoes.SAT.ativado) && !ControleConsultaComandos.isExecutandoConsulta()) {
            ControleLogs.logar("Iniciando verifica\u00e7\u00e3o Comandos.");
            new ThreadComandos();
            ControleLogs.logar("Fim verifica\u00e7\u00e3o de Comandos. ");
        }
    }
}
