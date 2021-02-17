// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.um.modelos.ABNT5891;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class DescontosAcrescimosTotal
{
    private String vDescSubtot;
    private String vAcresSubtot;
    
    public DescontosAcrescimosTotal(final Node no) {
        this.vDescSubtot = null;
        this.vAcresSubtot = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("vDescSubtot")) {
                this.vDescSubtot = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vAcresSubtot")) {
                this.vAcresSubtot = filhoAtual.getTextContent();
            }
        }
    }
    
    public String getvDescSubtot() {
        return this.vDescSubtot;
    }
    
    public void setvDescSubtot(final String vDescSubtot) {
        this.vDescSubtot = vDescSubtot;
    }
    
    public String getvAcresSubtot() {
        return this.vAcresSubtot;
    }
    
    public void setvAcresSubtot(final String vAcresSubtot) {
        this.vAcresSubtot = vAcresSubtot;
    }
    
    public void completar() {
        if (this.vDescSubtot != null) {
            this.vDescSubtot = new ABNT5891(this.vDescSubtot).arredondarNBR(2).toString();
        }
        if (this.vAcresSubtot != null) {
            this.vAcresSubtot = new ABNT5891(this.vAcresSubtot).arredondarNBR(2).toString();
        }
    }
    
    public String validar() {
        String resultado = "1000";
        if ((this.vDescSubtot == null && this.vAcresSubtot == null) || (this.vDescSubtot != null && this.vAcresSubtot != null)) {
            ControleLogs.logar("Erro: Os valores de desconto e acr\u00e9scimo sobre subtotal s\u00e3o mutamente exclusivos.");
            resultado = "1999";
        }
        else if (this.vDescSubtot != null && !ControleDados.validarDouble(1, 15, 2, this.vDescSubtot)) {
            ControleLogs.logar("Erro no Campo 'vDescSubtot' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.vDescSubtot != null && Double.parseDouble(this.vDescSubtot) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vDescSubtot' : Campo menor que 0 (zero).");
            resultado = "1073";
        }
        else if (this.vAcresSubtot != null && !ControleDados.validarDouble(1, 15, 2, this.vAcresSubtot)) {
            ControleLogs.logar("Erro no Campo 'vAcresSubtot' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.vAcresSubtot != null && Double.parseDouble(this.vAcresSubtot) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vAcresSubtot' : Campo menor que 0 (zero)");
            resultado = "1074";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (ControleDados.isNumerico(this.vDescSubtot)) {
            retorno.append("<vDescSubtot>").append(this.vDescSubtot).append("</vDescSubtot>");
        }
        if (ControleDados.isNumerico(this.vAcresSubtot)) {
            retorno.append("<vAcresSubtot>").append(this.vAcresSubtot).append("</vAcresSubtot>");
        }
    }
}
