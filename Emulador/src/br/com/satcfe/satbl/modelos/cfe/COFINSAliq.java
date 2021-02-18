// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class COFINSAliq
{
    private String CST;
    private String pCOFINS;
    private String vCOFINS;
    private String vBC;
    
    public COFINSAliq(final Node no) {
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CST")) {
                this.CST = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vBC")) {
                this.vBC = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("pCOFINS")) {
                this.pCOFINS = filhoAtual.getTextContent();
            }
        }
    }
    
    public COFINSAliq() {
    }
    
    public String getCST() {
        return this.CST;
    }
    
    public void setCST(final String CST) {
        this.CST = CST;
    }
    
    public String getpCOFINS() {
        return this.pCOFINS;
    }
    
    public void setpCOFINS(final String pCOFINS) {
        this.pCOFINS = pCOFINS;
    }
    
    public String getvBC() {
        return this.vBC;
    }
    
    public void setvBC(final String vBC) {
        this.vBC = vBC;
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
            ControleLogs.logar("campo 'CST' inexistente");
            resultado = "1999";
        }
        else if (this.vBC == null) {
            ControleLogs.logar("campo 'vBC' inexistente");
            resultado = "1999";
        }
        else if (this.pCOFINS == null) {
            ControleLogs.logar("campo 'pCOFINS' inexistente");
            resultado = "1999";
        }
        else if (!ControleDados.validarDouble(1, 15, 2, this.vBC)) {
            ControleLogs.logar("Erro no Campo 'vBC' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (!ControleDados.validarDouble(1, 5, 4, this.pCOFINS)) {
            ControleLogs.logar("Erro no Campo 'pCOFINS' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (!this.CST.equals("01") && !this.CST.equals("02") && !this.CST.equals("05")) {
            ControleLogs.logar("Erro no Campo 'CST' : Campo inv\u00e1lido.");
            resultado = "1490";
        }
        else if (Double.parseDouble(this.vBC) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vBC' : Campo menor que 0 (zero).");
            resultado = "1491";
        }
        else if (Double.parseDouble(this.pCOFINS) < 0.0) {
            ControleLogs.logar("Erro no Campo 'pCOFINS' : Campo menor que 0 (zero).");
            resultado = "1492";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.CST != null) {
            retorno.append("<CST>").append(this.CST).append("</CST>");
        }
        if (this.vBC != null) {
            retorno.append("<vBC>").append(this.vBC).append("</vBC>");
        }
        if (this.pCOFINS != null) {
            retorno.append("<pCOFINS>").append(this.pCOFINS).append("</pCOFINS>");
        }
        if (this.vCOFINS != null) {
            retorno.append("<vCOFINS>").append(this.vCOFINS).append("</vCOFINS>");
        }
    }
}
