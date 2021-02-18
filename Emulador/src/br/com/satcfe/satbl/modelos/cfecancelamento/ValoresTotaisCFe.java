// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfecancelamento;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class ValoresTotaisCFe
{
    private String vCFe;
    
    public ValoresTotaisCFe(final Node no) {
        this.vCFe = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("vCFe")) {
                this.vCFe = filhoAtual.getTextContent();
            }
        }
    }
    
    public ValoresTotaisCFe(final String vCFe) {
        this.vCFe = vCFe;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.vCFe != null) {
            retorno.append("<vCFe>").append(this.vCFe).append("</vCFe>");
        }
    }
    
    public String getvCFe() {
        return this.vCFe;
    }
    
    public void setvCFe(final String vCFe) {
        this.vCFe = vCFe;
    }
}
