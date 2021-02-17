// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.soap;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class SoapRequest
{
    private String mensagem;
    private String nameSpace;
    private String nomeMetodo;
    
    public SoapRequest(final String nomeMetodo) {
        this.mensagem = null;
        this.nameSpace = null;
        this.nomeMetodo = null;
        this.nomeMetodo = nomeMetodo;
    }
    
    public String getRequest() {
        final StringBuffer sm = new StringBuffer();
        sm.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sm.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        sm.append(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"");
        sm.append(" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sm.append("<soap:Header>");
        sm.append("<cfeCabecMsg xmlns=\"").append(this.nameSpace).append("\">");
        sm.append("<versaoDados>").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("</versaoDados>");
        sm.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        sm.append("</cfeCabecMsg>");
        sm.append("</soap:Header>");
        sm.append("<soap:Body>");
        sm.append("<").append(this.nomeMetodo).append(" xmlns=\"").append(this.nameSpace).append("\">");
        sm.append("<cfeDadosMsg>");
        sm.append(ControleDados.addCData(this.mensagem));
        sm.append("</cfeDadosMsg>");
        sm.append("</").append(this.nomeMetodo).append(">");
        sm.append("</soap:Body>");
        sm.append("</soap:Envelope>");
        return sm.toString();
    }
    
    public void setMensagem(final String mensagem) {
        this.mensagem = mensagem;
    }
    
    public void setNameSpace(final String nameSpace) {
        this.nameSpace = nameSpace;
    }
}
