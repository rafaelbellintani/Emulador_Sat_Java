// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.modelos.ABNT5891;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class InformacoesCOFINSST
{
    private String vBC;
    private String pCOFINS;
    private String qBCProd;
    private String vAliqProd;
    private String vCOFINS;
    
    public InformacoesCOFINSST(final Node no) {
        this.vBC = null;
        this.pCOFINS = null;
        this.qBCProd = null;
        this.vAliqProd = null;
        this.vCOFINS = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("vBC")) {
                this.vBC = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("pCOFINS")) {
                this.pCOFINS = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("qBCProd")) {
                this.qBCProd = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vAliqProd")) {
                this.vAliqProd = filhoAtual.getTextContent();
            }
        }
    }
    
    public InformacoesCOFINSST() {
        this.vBC = null;
        this.pCOFINS = null;
        this.qBCProd = null;
        this.vAliqProd = null;
        this.vCOFINS = null;
    }
    
    public String getvBC() {
        return this.vBC;
    }
    
    public void setvBC(final String vBC) {
        this.vBC = vBC;
    }
    
    public String getpCOFINS() {
        return this.pCOFINS;
    }
    
    public void setpCOFINS(final String pCOFINS) {
        this.pCOFINS = pCOFINS;
    }
    
    public String getqBCProd() {
        return this.qBCProd;
    }
    
    public void setqBCProd(final String qBCProd) {
        this.qBCProd = qBCProd;
    }
    
    public String getvAliqProd() {
        return this.vAliqProd;
    }
    
    public void setvAliqProd(final String vAliqProd) {
        this.vAliqProd = vAliqProd;
    }
    
    public String getvCOFINS() {
        return this.vCOFINS;
    }
    
    public void setvCOFINS(final String vCOFINS) {
        this.vCOFINS = vCOFINS;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.vBC == null && this.pCOFINS == null && this.qBCProd == null && this.vAliqProd == null) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do COFINS Substitui\u00e7\u00e3o Tribut\u00e1ria com aliquota em percentual ou campos com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.vBC != null && this.pCOFINS != null && (this.qBCProd != null || this.vAliqProd != null)) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do COFINS Substitui\u00e7\u00e3o Tribut\u00e1ria com aliquota em percentual ou campos com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.qBCProd != null && this.vAliqProd != null && (this.vBC != null || this.pCOFINS != null)) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do COFINS Substitui\u00e7\u00e3o Tribut\u00e1ria com aliquota em percentual ou campos com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.qBCProd != null && this.pCOFINS != null && this.vBC != null && this.vAliqProd != null) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do COFINS Substitui\u00e7\u00e3o Tribut\u00e1ria com aliquota em percentual ou campos com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.vBC != null ^ this.pCOFINS != null) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do COFINS Substitui\u00e7\u00e3o Tribut\u00e1ria com aliquota em percentual ou campos com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.qBCProd != null ^ this.vAliqProd != null) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do COFINS Substitui\u00e7\u00e3o Tribut\u00e1ria com aliquota em percentual ou campos com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.vBC != null && !ControleDados.validarDouble(1, 15, 2, this.vBC)) {
            ControleLogs.logar("Erro no Campo 'vBC' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.pCOFINS != null && !ControleDados.validarDouble(1, 5, 4, this.pCOFINS)) {
            ControleLogs.logar("Erro no Campo 'pCOFINS' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.qBCProd != null && !ControleDados.validarDouble(1, 16, 4, this.qBCProd)) {
            ControleLogs.logar("Erro no Campo 'qBCProd' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.vAliqProd != null && !ControleDados.validarDouble(1, 15, 4, this.vAliqProd)) {
            ControleLogs.logar("Erro no Campo 'vAliqProd' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.vBC != null && Double.parseDouble(this.vBC) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vBC' : Campo menor que 0 (zero).");
            resultado = "1491";
        }
        else if (this.pCOFINS != null && Double.parseDouble(this.pCOFINS) < 0.0) {
            ControleLogs.logar("Erro no Campo 'pCOFINS' : Campo menor que 0 (zero)");
            resultado = "1492";
        }
        else if (this.qBCProd != null && Double.parseDouble(this.qBCProd) < 0.0) {
            ControleLogs.logar("Erro no Campo 'qBCProd' : Campo menor que 0 (zero)");
            resultado = "1483";
        }
        else if (this.vAliqProd != null && Double.parseDouble(this.vAliqProd) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vAliqProd' : Campo menor que 0 (zero)");
            resultado = "1496";
        }
        return resultado;
    }
    
    public void completar() {
        if (this.vBC != null) {
            this.vBC = new ABNT5891(this.vBC).arredondarNBR(2).toString();
        }
        if (this.pCOFINS != null) {
            this.pCOFINS = new ABNT5891(this.pCOFINS).arredondarNBR(4).toString();
        }
        if (this.qBCProd != null) {
            this.qBCProd = new ABNT5891(this.qBCProd).arredondarNBR(4).toString();
        }
        if (this.vAliqProd != null) {
            this.vAliqProd = new ABNT5891(this.vAliqProd).arredondarNBR(4).toString();
        }
        if (this.vCOFINS != null) {
            this.vCOFINS = new ABNT5891(this.vCOFINS).arredondarNBR(2).toString();
        }
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.vBC != null) {
            retorno.append("<vBC>").append(this.vBC).append("</vBC>");
        }
        if (this.pCOFINS != null) {
            retorno.append("<pCOFINS>").append(this.pCOFINS).append("</pCOFINS>");
        }
        if (this.qBCProd != null) {
            retorno.append("<qBCProd>").append(this.qBCProd).append("</qBCProd>");
        }
        if (this.vAliqProd != null) {
            retorno.append("<vAliqProd>").append(this.vAliqProd).append("</vAliqProd>");
        }
        if (this.vCOFINS != null) {
            retorno.append("<vCOFINS>").append(this.vCOFINS).append("</vCOFINS>");
        }
    }
}
