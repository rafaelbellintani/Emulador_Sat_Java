// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class ObservacaoFiscoDet
{
    private String xCampoDet;
    private String xTextoDet;
    
    public ObservacaoFiscoDet() {
    }
    
    public ObservacaoFiscoDet(final Node no) {
        this.xCampoDet = null;
        this.xTextoDet = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("xTextoDet")) {
                this.xTextoDet = filhoAtual.getTextContent();
            }
        }
        final NamedNodeMap atributos = no.getAttributes();
        for (int j = 0; j < atributos.getLength(); ++j) {
            final Node filhoAtual2 = atributos.item(j);
            if (filhoAtual2.getNodeName().equalsIgnoreCase("xCampoDet")) {
                this.xCampoDet = filhoAtual2.getTextContent();
            }
        }
    }
    
    public String getxCampoDet() {
        return this.xCampoDet;
    }
    
    public void setxCampoDet(final String xCampoDet) {
        this.xCampoDet = xCampoDet;
    }
    
    public String getxTextoDet() {
        return this.xTextoDet;
    }
    
    public void setxTextoDet(final String xTextoDet) {
        this.xTextoDet = xTextoDet;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.xCampoDet == null) {
            ControleLogs.logar("Campo 'xCampoDet' inexistente");
            resultado = "1999";
        }
        else if (this.xTextoDet == null) {
            ControleLogs.logar("Campo 'xTextoDet' inexistente");
            resultado = "1999";
        }
        else if (this.xCampoDet.length() < 1 || this.xCampoDet.length() > 20) {
            ControleLogs.logar("erro no campo 'xCampoDet'");
            resultado = "1999";
        }
        else if (this.xTextoDet.length() < 1 || this.xTextoDet.length() > 60) {
            ControleLogs.logar("erro no campo 'xTextoDet'");
            resultado = "1999";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.xTextoDet != null) {
            retorno.append("<xTextoDet>").append(this.xTextoDet).append("</xTextoDet>");
        }
    }
}
