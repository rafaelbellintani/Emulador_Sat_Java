// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.controles.webservices;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.threads.ThreadComandos;
import br.com.satcfe.satbl.controles.ControladorBloqueio;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.controles.ControleSeguranca;

public abstract class WebService
{
    protected int timeout;
    protected String responseCode;
    protected String responseMessage;
    protected String cStat;
    protected String xMotivo;
    protected String nameSpace;
    protected String soapAction;
    
    public WebService() {
        this.timeout = 0;
        this.responseCode = null;
        this.responseMessage = null;
        this.cStat = null;
        this.xMotivo = null;
        this.nameSpace = null;
        this.soapAction = null;
    }
    
    protected void salvarUltimaComunicacao() {
        ControleSeguranca.salvarDataHoraComunicacao();
        if (Configuracoes.SAT.bloqueado && Configuracoes.SAT.autoBloqueado) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new ControladorBloqueio().iniciarDesbloqueioAutonomo();
                }
            }).start();
        }
    }
    
    protected synchronized void setExisteComando(final boolean cmd) {
        if (cmd) {
            new ThreadComandos();
        }
    }
    
    protected static String desformatarDadosSOAP(String texto) {
        texto = texto.replace("&gt;", ">").replace("&lt;", "<");
        return ControleDados.removerCabecalhoXML(texto);
    }
    
    public String getResponseCode() {
        return this.responseCode;
    }
    
    public String getResponseMessage() {
        return this.responseMessage;
    }
    
    public String getxMotivo() {
        return this.xMotivo;
    }
    
    public String getcStat() {
        return this.cStat;
    }
    
    public void setTimeout(final long timeout) {
        this.timeout = (int)timeout;
    }
    
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }
    
    public long getTimeout() {
        return this.timeout;
    }
}
