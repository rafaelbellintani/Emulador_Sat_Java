// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.soap;

import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class MensagemReset extends MensagemEntrada
{
    @Override
    public String versao003() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<confReset xmlns=\"").append(this.nameSpaceRaiz).append("\" versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</confReset>");
        return ds.toString();
    }
    
    @Override
    public String versao004() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<confReset xmlns=\"").append(this.nameSpaceRaiz).append("\" versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</confReset>");
        return ds.toString();
    }
}
