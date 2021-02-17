// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.soap;

import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class MensagemRecepcao extends MensagemEntrada
{
    private String idLote;
    private String loteCFes;
    
    public MensagemRecepcao() {
        this.idLote = null;
        this.loteCFes = null;
    }
    
    @Override
    public String versao003() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<envCFe versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\"");
        ds.append(" xmlns=\"").append(this.nameSpaceRaiz).append("\"");
        ds.append(" >");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<idLote>").append(this.idLote).append("</idLote>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<LoteCFe>").append(this.loteCFes).append("</LoteCFe>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</envCFe>");
        return ds.toString();
    }
    
    @Override
    public String versao004() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<envCFe versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\"");
        ds.append(" xmlns=\"").append(this.nameSpaceRaiz).append("\"");
        ds.append(" >");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<idLote>").append(this.idLote).append("</idLote>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<LoteCFe>").append(this.loteCFes).append("</LoteCFe>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</envCFe>");
        return ds.toString();
    }
    
    public void setIdLote(final String idLote) {
        this.idLote = idLote;
    }
    
    public void setLoteCFes(final String loteCFes) {
        this.loteCFes = loteCFes;
    }
}
