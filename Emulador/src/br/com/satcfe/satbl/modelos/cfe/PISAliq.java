// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class PISAliq
{
    private String CST;
    private String pPIS;
    private String vPIS;
    private String vBC;
    
    public PISAliq(final Node no) {
        this.CST = null;
        this.pPIS = null;
        this.vPIS = null;
        this.vBC = null;
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
        }
    }
    
    public String getCST() {
        return this.CST;
    }
    
    public void setCST(final String CST) {
        this.CST = CST;
    }
    
    public String getpPIS() {
        return this.pPIS;
    }
    
    public void setpPIS(final String pPIS) {
        this.pPIS = pPIS;
    }
    
    public String getvBC() {
        return this.vBC;
    }
    
    public void setvBC(final String vBC) {
        this.vBC = vBC;
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
        else if (this.vBC == null) {
            ControleLogs.logar("Campo 'vBC' inexistente");
            resultado = "1999";
        }
        else if (this.pPIS == null) {
            ControleLogs.logar("Campo 'pPIS' inexistente");
            resultado = "1999";
        }
        else if (!this.CST.equals("01") && !this.CST.equals("02") && !this.CST.equals("05")) {
            ControleLogs.logar("Erro no Campo 'CST' : Campo inv\u00e1lido.");
            resultado = "1478";
        }
        else if (!ControleDados.validarDouble(1, 15, 2, this.vBC)) {
            ControleLogs.logar("Erro no Campo 'vBC' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (Double.parseDouble(this.vBC) < 0.0) {
            ControleLogs.logar("Erro no campo 'vBC' : Campo menor que 0 (zero).");
            resultado = "1479";
        }
        else if (!ControleDados.validarDouble(1, 5, 4, this.pPIS)) {
            ControleLogs.logar("Erro no Campo 'pPIS' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (Double.parseDouble(this.pPIS) < 0.0) {
            ControleLogs.logar("Erro no Campo 'pPIS' : Campo menor que 0 (zero).");
            resultado = "1480";
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
        if (ControleDados.isNumerico(this.vPIS)) {
            retorno.append("<vPIS>").append(this.vPIS).append("</vPIS>");
        }
    }
}
