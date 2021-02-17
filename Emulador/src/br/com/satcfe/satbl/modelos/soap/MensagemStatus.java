// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.soap;

import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class MensagemStatus extends MensagemEntrada
{
    private String xServ;
    private String status;
    
    public MensagemStatus() {
        this.xServ = null;
    }
    
    @Override
    public String versao003() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<consStat versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\" xmlns=\"").append(this.nameSpaceRaiz).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<xServ>").append(this.xServ).append("</xServ>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        if (this.xServ.equalsIgnoreCase("STATUS-SAT")) {
            ds.append("<status>").append(this.status).append("</status>");
        }
        ds.append("</consStat>");
        return ds.toString();
    }
    
    @Override
    public String versao004() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<consStat versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\" xmlns=\"").append(this.nameSpaceRaiz).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<xServ>").append(this.xServ).append("</xServ>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        if (this.xServ.equalsIgnoreCase("STATUS-SAT")) {
            ds.append("<status>").append(this.status).append("</status>");
        }
        ds.append("</consStat>");
        return ds.toString();
    }
    
    public void setxServ(final String xServ) {
        this.xServ = xServ;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
}
