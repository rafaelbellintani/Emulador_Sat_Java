// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.modelos.ConfiguracoesOffLine;

public class ConfiguracaoOffLineFacade
{
    private ConfiguracoesOffLine conf;
    
    public ConfiguracaoOffLineFacade() {
        this.conf = null;
        this.conf = ConfiguracoesOffLine.getInstance();
    }
    
    public void executar() {
        if (this.conf == null) {
            return;
        }
        final ControladorEmuladorOffLine controle = new ControladorEmuladorOffLine();
        if (this.conf.getAtualizarSoftwareBasico() != 0) {
            controle.atualizacaoSoftwareBasico(this.conf.getAtualizarSoftwareBasico());
        }
        if (this.conf.getAvisoUsuario() != null) {
            ControleSeguranca.salvarAviso(this.conf.getAvisoUsuario());
        }
        if (this.conf.getHabilitarBloqueioAutonomo()) {
            if (!Configuracoes.SAT.bloqueado && Configuracoes.SAT.ativado) {
                Configuracoes.SAT.autoBloqueado = true;
                Configuracoes.SAT.bloqueado = true;
                ControleLogs.logar("Equipamento SAT ser\u00e1 Bloqueado por falta de comunica\u00e7\u00e3o com a SEFAZ");
                new ControladorBloqueio().bloquearOffLineComandoSefaz();
            }
        }
        else if (!this.conf.getHabilitarBloqueioContribuinte() && !this.conf.getHabilitarBloqueioSefaz() && Configuracoes.SAT.autoBloqueado && Configuracoes.SAT.bloqueado && Configuracoes.SAT.ativado) {
            ControleLogs.logar("comunica\u00e7\u00e3o com a SEFAZ reestabelecida");
            ControleLogs.logar("Iniciando desbloqueio do SAT");
            Parametrizacoes.autorBloqueio = 0;
            Configuracoes.SAT.autoBloqueado = false;
            if (new ControladorBloqueio().desbloquearOffLineComandoSefaz() == null) {
                ControleLogs.logar("Parametriza\u00e7\u00e3o de bloqueio invalida");
            }
            ControleLogs.logar("Equipamento SAT desbloqueado com sucesso");
        }
        if (this.conf.getHabilitarBloqueioSefaz()) {
            if (!Configuracoes.SAT.bloqueado && Configuracoes.SAT.ativado) {
                ControleLogs.logar("Comando \"COMANDO_006\" recebido da SEFAZ");
                ControleLogs.logar("Parametrizacao de bloqueio recebida");
                Parametrizacoes.autorBloqueio = 2;
                if (new ControladorBloqueio().bloquearOffLineComandoSefaz() == null) {
                    ControleLogs.logar("Parametriza\u00e7\u00e3o de bloqueio invalida");
                }
            }
        }
        else if (!this.conf.getHabilitarBloqueioAutonomo() && !this.conf.getHabilitarBloqueioContribuinte() && Parametrizacoes.autorBloqueio == 2 && Configuracoes.SAT.bloqueado && Configuracoes.SAT.ativado) {
            ControleLogs.logar("Comando \"COMANDO_006\" recebido da SEFAZ");
            ControleLogs.logar("Parametrizacao de Uso recebida");
            ControleLogs.logar("Iniciando desbloqueio do SAT");
            Parametrizacoes.autorBloqueio = 0;
            if (new ControladorBloqueio().desbloquearOffLineComandoSefaz() == null) {
                ControleLogs.logar("Parametriza\u00e7\u00e3o de bloqueio invalida");
            }
            ControleLogs.logar("Equipamento SAT desbloqueado com sucesso");
        }
        if (this.conf.getHabilitarDesativacao()) {
            Parametrizacoes.cessacao = true;
        }
        else {
            Parametrizacoes.cessacao = false;
        }
        if (this.conf.getHabilitarRenovacaoAviso()) {
            new ControladorEmuladorOffLine().renovacaoAviso();
        }
    }
}
