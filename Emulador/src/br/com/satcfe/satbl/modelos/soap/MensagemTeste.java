// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.soap;

import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class MensagemTeste extends MensagemEntrada
{
    private String idLote;
    private String optTeste;
    private String CFe;
    
    public MensagemTeste() {
        this.idLote = null;
        this.optTeste = null;
        this.CFe = null;
    }
    
    @Override
    public String versao003() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<envTeste xmlns=\"").append(this.nameSpaceRaiz).append("\" versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<idLote>").append(this.idLote).append("</idLote>");
        ds.append("<optTeste>").append(this.optTeste).append("</optTeste>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append(this.CFe);
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</envTeste>");
        return ds.toString();
    }
    
    @Override
    public String versao004() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<envTeste xmlns=\"").append(this.nameSpaceRaiz).append("\" versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<idLote>").append(this.idLote).append("</idLote>");
        ds.append("<optTeste>").append(this.optTeste).append("</optTeste>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append(this.CFe);
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</envTeste>");
        return ds.toString();
    }
    
    public void setOptTeste(final String optTeste) {
        this.optTeste = optTeste;
    }
    
    public void setIdLote(final String idLote) {
        this.idLote = idLote;
    }
    
    public void setCFeTeste(final String cfe) {
        this.CFe = cfe;
    }
}
