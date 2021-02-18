// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class COFINSQtde
{
    private String CST;
    private String qBCProd;
    private String vAliqProd;
    private String vCOFINS;
    
    public COFINSQtde(final Node no) {
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CST")) {
                this.CST = filhoAtual.getTextContent();
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
        if (this.CST == null) {
            ControleLogs.logar("Campo 'CST' inexistente");
            resultado = "1999";
        }
        else if (this.qBCProd == null) {
            ControleLogs.logar("Campo 'qBCProd' inexistente");
            resultado = "1999";
        }
        else if (this.vAliqProd == null) {
            ControleLogs.logar("Campo 'vAliqProd' inexistente");
            resultado = "1999";
        }
        else if (!ControleDados.validarDouble(1, 16, 4, this.qBCProd)) {
            ControleLogs.logar("Erro no Campo 'qBCProd' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (Double.parseDouble(this.qBCProd) < 0.0) {
            ControleLogs.logar("Erro no Campo 'qBCProd' : Campo menor que 0 (zero).");
            resultado = "1483";
        }
        else if (!ControleDados.validarDouble(1, 15, 4, this.vAliqProd)) {
            ControleLogs.logar("Erro no Campo 'vAliqProd' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (Double.parseDouble(this.vAliqProd) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vAliqProd' : Campo menor que 0 (zero).");
            resultado = "1496";
        }
        else if (!this.CST.equals("03")) {
            ControleLogs.logar("Erro no Campo 'CST' : Campo inv\u00e1lido.");
            resultado = "1494";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.CST != null) {
            retorno.append("<CST>").append(this.CST).append("</CST>");
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
