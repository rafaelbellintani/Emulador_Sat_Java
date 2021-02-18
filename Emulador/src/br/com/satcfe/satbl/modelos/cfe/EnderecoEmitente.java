// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.satcfe.satbl.Parametrizacoes;

public class EnderecoEmitente
{
    private String xLgr;
    private String nro;
    private String xCpl;
    private String xBairro;
    private String xMun;
    private String CEP;
    
    public EnderecoEmitente() {
        this.xLgr = null;
        this.nro = null;
        this.xCpl = null;
        this.xBairro = null;
        this.xMun = null;
        this.CEP = null;
    }
    
    public EnderecoEmitente(final String xLgr, final String nro, final String xCpl, final String xBairro, final String xMun, final String CEP) {
        this.xLgr = null;
        this.nro = null;
        this.xCpl = null;
        this.xBairro = null;
        this.xMun = null;
        this.CEP = null;
        this.xLgr = xLgr;
        this.nro = nro;
        this.xCpl = xCpl;
        this.xBairro = xBairro;
        this.xMun = xMun;
        this.CEP = CEP;
    }
    
    public String getxLgr() {
        return this.xLgr;
    }
    
    public void setxLgr(final String xLgr) {
        this.xLgr = xLgr;
    }
    
    public String getNro() {
        return this.nro;
    }
    
    public void setNro(final String nro) {
        this.nro = nro;
    }
    
    public String getxCpl() {
        return this.xCpl;
    }
    
    public void setxCpl(final String xCpl) {
        this.xCpl = xCpl;
    }
    
    public String getxBairro() {
        return this.xBairro;
    }
    
    public void setxBairro(final String xBairro) {
        this.xBairro = xBairro;
    }
    
    public String getxMun() {
        return this.xMun;
    }
    
    public void setxMun(final String xMun) {
        this.xMun = xMun;
    }
    
    public String getCEP() {
        return this.CEP;
    }
    
    public void setCEP(final String CEP) {
        this.CEP = CEP;
    }
    
    public void completar() {
        this.xLgr = Parametrizacoes.xLgr;
        this.nro = Parametrizacoes.nro;
        if (Parametrizacoes.xCpl != null) {
            this.xCpl = ((Parametrizacoes.xCpl.length() > 0) ? Parametrizacoes.xCpl : null);
        }
        this.xBairro = Parametrizacoes.xBairro;
        this.xMun = Parametrizacoes.xMun;
        this.CEP = Parametrizacoes.CEP;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.xLgr != null) {
            retorno.append("<xLgr>").append(this.xLgr).append("</xLgr>");
        }
        if (this.nro != null) {
            retorno.append("<nro>").append(this.nro).append("</nro>");
        }
        if (this.xCpl != null) {
            retorno.append("<xCpl>").append(this.xCpl).append("</xCpl>");
        }
        if (this.xBairro != null) {
            retorno.append("<xBairro>").append(this.xBairro).append("</xBairro>");
        }
        if (this.xMun != null) {
            retorno.append("<xMun>").append(this.xMun).append("</xMun>");
        }
        if (this.CEP != null) {
            retorno.append("<CEP>").append(this.CEP).append("</CEP>");
        }
    }
}
