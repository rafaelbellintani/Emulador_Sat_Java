// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class PISSN
{
    private String CST;
    
    public PISSN(final Node no) {
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CST")) {
                this.CST = filhoAtual.getTextContent();
            }
        }
    }
    
    public String getCST() {
        return this.CST;
    }
    
    public void setCST(final String CST) {
        this.CST = CST;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.CST == null) {
            ControleLogs.logar("Campo 'CST' inexistente");
            resultado = "1999";
        }
        else if (!this.CST.equals("49")) {
            ControleLogs.logar("Erro no Campo 'CST' : Campo inv\u00e1lido.");
            resultado = "1487";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (ControleDados.isNumerico(this.CST)) {
            retorno.append("<CST>").append(this.CST).append("</CST>");
        }
    }
}
