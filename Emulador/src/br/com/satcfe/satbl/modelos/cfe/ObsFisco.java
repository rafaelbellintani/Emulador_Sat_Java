// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class ObsFisco
{
    private String xCampo;
    private String xTexto;
    
    public ObsFisco() {
    }
    
    public ObsFisco(final Node no) {
        this.xCampo = null;
        this.xTexto = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("xTexto")) {
                this.xTexto = filhoAtual.getTextContent();
            }
        }
        final NamedNodeMap atributos = no.getAttributes();
        for (int j = 0; j < atributos.getLength(); ++j) {
            final Node filhoAtual2 = atributos.item(j);
            if (filhoAtual2.getNodeName().equalsIgnoreCase("xCampo")) {
                this.xCampo = filhoAtual2.getTextContent();
            }
        }
    }
    
    public String getxCampo() {
        return this.xCampo;
    }
    
    public void setxCampo(final String xCampo) {
        this.xCampo = xCampo;
    }
    
    public String getxTexto() {
        return this.xTexto;
    }
    
    public void setxTexto(final String xTexto) {
        this.xTexto = xTexto;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.xTexto != null) {
            retorno.append("<xTexto>").append(this.xTexto).append("</xTexto>");
        }
    }
}
