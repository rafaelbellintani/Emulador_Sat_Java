// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.soap;

import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class MensagemCancelamento extends MensagemEntrada
{
    private String idLote;
    private String lote;
    
    public MensagemCancelamento() {
        this.idLote = null;
        this.lote = null;
    }
    
    @Override
    public String versao003() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<cancCFe xmlns=\"").append(this.nameSpaceRaiz).append("\" versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\"> ");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<idLote>").append(this.idLote).append("</idLote>");
        ds.append("<LoteCFeCanc>").append(this.lote).append("</LoteCFeCanc>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</cancCFe>");
        return ds.toString();
    }
    
    @Override
    public String versao004() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<cancCFe versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\" xmlns=\"").append(this.nameSpaceRaiz).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<idLote>").append(this.idLote).append("</idLote>");
        ds.append("<LoteCFeCanc>").append(this.lote).append("</LoteCFeCanc>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</cancCFe>");
        return ds.toString();
    }
    
    public void setLote(final String lote) {
        this.lote = lote;
    }
    
    public void setIdLote(final String idLote) {
        this.idLote = idLote;
    }
}
