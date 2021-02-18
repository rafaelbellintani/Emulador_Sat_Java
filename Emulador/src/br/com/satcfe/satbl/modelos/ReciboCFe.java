// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import br.com.um.controles.ControleDados;

public class ReciboCFe
{
    private String id;
    private String tpAmb;
    private String chCFe;
    private String dhRecbto;
    private String digVal;
    private String cStat;
    private String xMotivo;
    
    public ReciboCFe(String xml) {
        this.id = null;
        this.tpAmb = null;
        this.chCFe = null;
        this.dhRecbto = null;
        this.digVal = null;
        this.cStat = null;
        this.xMotivo = null;
        xml = ControleDados.recuperaTag(xml, "infReci");
        try {
            final Document doc = ControleDados.parseXML(xml);
            final NodeList raizes = doc.getChildNodes();
            this.parse(raizes.item(0));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void parse(final Node raiz) {
        final NodeList filhos = raiz.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("tpAmb")) {
                this.tpAmb = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("chCFe")) {
                this.chCFe = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("dhRecbto")) {
                this.dhRecbto = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("digVal")) {
                this.digVal = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cStat")) {
                this.cStat = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xMotivo")) {
                this.xMotivo = filhoAtual.getTextContent();
            }
        }
        final NamedNodeMap atributos = raiz.getAttributes();
        for (int j = 0; j < atributos.getLength(); ++j) {
            final Node filhoAtual2 = atributos.item(j);
            if (filhoAtual2.getNodeName().equalsIgnoreCase("Id")) {
                this.id = filhoAtual2.getTextContent();
            }
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getTpAmb() {
        return this.tpAmb;
    }
    
    public String getChCFe() {
        return this.chCFe;
    }
    
    public String getDhRecbto() {
        return this.dhRecbto;
    }
    
    public String getDigVal() {
        return this.digVal;
    }
    
    public String getcStat() {
        return this.cStat;
    }
    
    public String getxMotivo() {
        return this.xMotivo;
    }
}
