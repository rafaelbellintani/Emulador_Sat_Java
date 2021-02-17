// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.threads;

import br.com.satcfe.satbl.controles.ControleConsultaComandos;

public class ThreadComandos
{
    public ThreadComandos() {
        if (!ControleConsultaComandos.isExecutandoConsulta()) {
            new Thread(new VerificacaoContinuaComandos((VerificacaoContinuaComandos)null)).start();
        }
    }
    
    public void executar() {
        try {
            new ControleConsultaComandos().consultarComandos();
            Thread.sleep(500L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private class VerificacaoContinuaComandos implements Runnable
    {

    	
       /*CRIADO CONSTRUTOR*/
    	public VerificacaoContinuaComandos(VerificacaoContinuaComandos verificacaoContinuaComandos) {
			// TODO Auto-generated constructor stub
		}

		@Override
        public void run() {
            try {
                synchronized (this) {
                    ThreadComandos.this.executar();
                    Thread.sleep(100L);
                }
            }
            catch (InterruptedException e) {
                System.err.println("verifica\u00e7\u00e3o Interrompida.");
            }
        }
    }
}
