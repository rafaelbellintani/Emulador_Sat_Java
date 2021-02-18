// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class PISOutr
{
    private String CST;
    private String pPIS;
    private String qBCProd;
    private String vBC;
    private String vAliqProd;
    private String vPIS;
    
    public PISOutr(final Node no) {
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CST")) {
                this.CST = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vBC")) {
                this.vBC = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("pPIS")) {
                this.pPIS = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("qBCProd")) {
                this.qBCProd = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vAliqProd")) {
                this.vAliqProd = filhoAtual.getTextContent();
            }
        }
    }
    
    public String getCST() {
        return this.CST;
    }
    
    public void setCST(final String CST) {
        this.CST = CST;
    }
    
    public String getvBC() {
        return this.vBC;
    }
    
    public void setvBC(final String vBC) {
        this.vBC = vBC;
    }
    
    public String getpPIS() {
        return this.pPIS;
    }
    
    public void setpPIS(final String pPIS) {
        this.pPIS = pPIS;
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
    
    public String getvPIS() {
        return this.vPIS;
    }
    
    public void setvPIS(final String vPIS) {
        this.vPIS = vPIS;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.CST == null) {
            ControleLogs.logar("Campo 'CST' inexistente");
            resultado = "1999";
        }
        else if (this.vBC == null && this.pPIS == null && this.qBCProd == null && this.vAliqProd == null) {
            ControleLogs.logar("Campos 'vBC', 'pPIS', 'qBCProd' e 'vAliqProd' inexistentes");
            resultado = "1999";
        }
        else if (this.vBC != null && this.qBCProd != null && this.vAliqProd != null) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do PIS com aliquota em percentual ou campos para PIS com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.pPIS != null && this.qBCProd != null && this.vAliqProd != null) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do PIS com aliquota em percentual ou campos para PIS com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.qBCProd != null && (this.pPIS != null || this.vBC != null)) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do PIS com aliquota em percentual ou campos para PIS com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.vAliqProd != null && (this.pPIS != null || this.vBC != null)) {
            ControleLogs.logar("Informar campos para c\u00e1lculo do PIS com aliquota em percentual ou campos para PIS com aliquota em valor.");
            resultado = "1999";
        }
        else if (this.vBC != null && !ControleDados.validarDouble(1, 15, 2, this.vBC)) {
            ControleLogs.logar("Erro no Campo 'vBC' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.pPIS != null && !ControleDados.validarDouble(1, 5, 4, this.pPIS)) {
            ControleLogs.logar("Erro no Campo 'pPIS' : Campo inv\u00e1lido.");
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
        else if (!this.CST.equals("99")) {
            ControleLogs.logar("Erro no Campo 'CST' : Campo inv\u00e1lido.");
            resultado = "1488";
        }
        else if (this.vBC != null && Double.parseDouble(this.vBC) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vBC' : Campo menor que 0 (zero).");
            resultado = "1479";
        }
        else if (this.pPIS != null && Double.parseDouble(this.pPIS) < 0.0) {
            ControleLogs.logar("Erro no Campo 'pPIS' : Campo menor que 0 (zero).");
            resultado = "1480";
        }
        else if (this.qBCProd != null && Double.parseDouble(this.qBCProd) < 0.0) {
            ControleLogs.logar("Erro no Campo 'qBCProd' : Campo menor que 0 (zero).");
            resultado = "1483";
        }
        else if (this.vAliqProd != null && Double.parseDouble(this.vAliqProd) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vAliqProd' : Campo menor que 0 (zero).");
            resultado = "1484";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (ControleDados.isNumerico(this.CST)) {
            retorno.append("<CST>").append(this.CST).append("</CST>");
        }
        if (ControleDados.isNumerico(this.vBC)) {
            retorno.append("<vBC>").append(this.vBC).append("</vBC>");
        }
        if (ControleDados.isNumerico(this.pPIS)) {
            retorno.append("<pPIS>").append(this.pPIS).append("</pPIS>");
        }
        if (ControleDados.isNumerico(this.qBCProd)) {
            retorno.append("<qBCProd>").append(this.qBCProd).append("</qBCProd>");
        }
        if (ControleDados.isNumerico(this.vAliqProd)) {
            retorno.append("<vAliqProd>").append(this.vAliqProd).append("</vAliqProd>");
        }
        if (ControleDados.isNumerico(this.vPIS)) {
            retorno.append("<vPIS>").append(this.vPIS).append("</vPIS>");
        }
    }
}
