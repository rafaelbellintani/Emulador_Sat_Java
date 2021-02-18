// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.soap;

import br.com.um.controles.ControleTempo;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;

public class MensagemCertificacao extends MensagemEntrada
{
    private String CRT;
    private String CSR;
    private String opt;
    
    public MensagemCertificacao() {
        this.CRT = null;
        this.CSR = null;
        this.opt = null;
    }
    
    @Override
    public String versao003() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<certifica xmlns=\"").append(this.nameSpaceRaiz).append("\" versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<opt>").append(this.opt).append("</opt>");
        if (this.opt.equalsIgnoreCase("CRT")) {
            ds.append("<CRT>").append(this.CRT).append("</CRT>");
        }
        else {
            ds.append("<CSR>").append(this.CSR).append("</CSR>");
        }
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</certifica>");
        return ds.toString();
    }
    
    @Override
    public String versao004() {
        final StringBuffer ds = new StringBuffer();
        ds.append("<certifica xmlns=\"").append(this.nameSpaceRaiz).append("\" versao=\"").append(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL).append("\">");
        ds.append("<tpAmb>").append(Parametrizacoes.ambiente).append("</tpAmb>");
        ds.append("<cUF>").append(Parametrizacoes.cUF).append("</cUF>");
        ds.append("<opt>").append(this.opt).append("</opt>");
        if (this.opt.equalsIgnoreCase("CRT")) {
            ds.append("<CRT>").append(this.CRT).append("</CRT>");
        }
        else {
            ds.append("<CSR>").append(this.CSR).append("</CSR>");
        }
        ds.append("<nSeg>").append(Configuracoes.SAT.numeroDeSeguranca).append("</nSeg>");
        ds.append("<dhEnvio>").append(ControleTempo.getTimeStamp()).append("</dhEnvio>");
        ds.append("<nserieSAT>").append(Configuracoes.SAT.numeroDeSerie).append("</nserieSAT>");
        ds.append("</certifica>");
        return ds.toString();
    }
    
    public void setCRT(final String crt) {
        this.CRT = crt;
    }
    
    public void setCSR(final String csr) {
        this.CSR = csr;
    }
    
    public void setOpt(final String opt) {
        this.opt = opt;
    }
}
