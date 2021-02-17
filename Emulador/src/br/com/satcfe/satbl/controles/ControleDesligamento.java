// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles;

import br.com.satcfe.satbl.Configuracoes;

public class ControleDesligamento
{
    public static synchronized void desligarSAT() {
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    ControleLogs.logar("Fechando o Sistema!");
                    if (Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.WINDOWS) {
                        Thread.sleep(1000L);
                        final String command = "shutdown -h now";
                        final Runtime r = Runtime.getRuntime();
                        r.exec(command);
                    }else if(Configuracoes.Gerais.SO_SAT == Configuracoes.Gerais.Sistema.LINUX) {
                    	Thread.sleep(1000L);
                        final String command = "shutdown -h now";
                        final Runtime r = Runtime.getRuntime();
                        r.exec(command); //VAI SER NECESSARIO ALTERAR
                    }
                    else {
                        System.exit(0);
                    }
                }
                catch (Exception e) {
                    ControleLogs.logar(e.getMessage());
                    Configuracoes.SAT.desligamentoIniciado = false;
                }
                Configuracoes.SAT.desligamentoIniciado = false;
            }
        };
        new Thread(run).start();
    }
}
