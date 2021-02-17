// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class IdentificacaoLocalEntrega
{
    private String xLgr;
    private String nro;
    private String xCpl;
    private String xBairro;
    private String xMun;
    private String UF;
    
    public IdentificacaoLocalEntrega(final Node no) {
        this.xLgr = null;
        this.nro = null;
        this.xCpl = null;
        this.xBairro = null;
        this.xMun = null;
        this.UF = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("xLgr")) {
                this.xLgr = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("nro")) {
                this.nro = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xCpl")) {
                this.xCpl = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xBairro")) {
                this.xBairro = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xMun")) {
                this.xMun = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("UF")) {
                this.UF = filhoAtual.getTextContent();
            }
        }
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
    
    public String getUF() {
        return this.UF;
    }
    
    public void setUF(final String UF) {
        this.UF = UF;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.xLgr == null) {
            resultado = "1999";
        }
        else if (this.nro == null) {
            resultado = "1999";
        }
        else if (this.xBairro == null) {
            resultado = "1999";
        }
        else if (this.xMun == null) {
            resultado = "1999";
        }
        else if (this.UF == null) {
            resultado = "1999";
        }
        else if (this.xLgr.length() < 2 || this.xLgr.length() > 60) {
            resultado = "1999";
        }
        else if (this.nro.length() < 1 || this.nro.length() > 60) {
            resultado = "1999";
        }
        else if (this.xCpl != null && (this.xCpl.length() < 1 || this.xCpl.length() > 60)) {
            resultado = "1999";
        }
        else if (this.xBairro.length() < 1 || this.xBairro.length() > 60) {
            resultado = "1999";
        }
        else if (this.xMun.length() < 2 || this.xMun.length() > 60) {
            resultado = "1999";
        }
        else if (this.UF.length() != 2) {
            resultado = "1999";
        }
        return resultado;
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
        if (this.UF != null) {
            retorno.append("<UF>").append(this.UF).append("</UF>");
        }
    }
}
