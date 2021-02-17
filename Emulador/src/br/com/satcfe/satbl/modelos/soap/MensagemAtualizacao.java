// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.soap;

import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class MensagemAtualizacao extends MensagemEntrada
{
    private String xServ;
    
    @Override
    public String versao003() {
        final String verSoft = "00.00.01";
        final StringBuffer ds = new StringBuffer();
        ds.append("<consAtualiza xmlns=\"").append(this.nameSpaceRaiz).append("\" versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<verSoft>").append(verSoft).append("</verSoft>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<xServ>").append(this.xServ).append("</xServ>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</consAtualiza>");
        return ds.toString();
    }
    
    @Override
    public String versao004() {
        final String verSoft = String.valueOf("000003".substring(0, 2)) + "." + "000003".substring(2, 4) + "." + "000003".substring(4);
        final StringBuffer ds = new StringBuffer();
        ds.append("<consAtualiza xmlns=\"").append(this.nameSpaceRaiz).append("\" versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<verSoft>").append(verSoft).append("</verSoft>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<xServ>").append(this.xServ).append("</xServ>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</consAtualiza>");
        return ds.toString();
    }
    
    public void setxServ(final String xServ) {
        this.xServ = xServ;
    }
}
