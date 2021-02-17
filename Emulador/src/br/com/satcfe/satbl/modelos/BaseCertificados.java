// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleArquivos;
import java.util.ArrayList;
import java.util.List;

public class BaseCertificados
{
    private List<String> listaCertSefaz;
    private List<String> listaCertHttps;
    private String caminhoCertSefaz;
    private String caminhoCertHttps;
    
    public BaseCertificados() {
        this.listaCertSefaz = null;
        this.listaCertHttps = null;
        this.caminhoCertSefaz = null;
        this.caminhoCertHttps = null;
        this.listaCertSefaz = new ArrayList<String>();
        this.listaCertHttps = new ArrayList<String>();
    }
    
    public void gravar() {
        this.gravarCertSefaz();
        this.gravarCertHttps();
    }
    
    public void carregar() {
        try {
            if (ControleArquivos.existeArquivo(this.caminhoCertSefaz)) {
                final String xml = ControleArquivos.lerArquivo(this.caminhoCertSefaz);
                final Document doc = ControleDados.parseXML(xml);
                final NodeList filhos = doc.getFirstChild().getChildNodes();
                for (int i = 0; i < filhos.getLength(); ++i) {
                    final Node filhoAtual = filhos.item(i);
                    if (filhoAtual.getNodeName().equalsIgnoreCase("cert")) {
                        this.listaCertSefaz.add(filhoAtual.getTextContent());
                    }
                }
            }
            if (ControleArquivos.existeArquivo(this.caminhoCertHttps)) {
                final String xml = ControleArquivos.lerArquivo(this.caminhoCertHttps);
                final Document doc = ControleDados.parseXML(xml);
                final NodeList filhos = doc.getFirstChild().getChildNodes();
                for (int i = 0; i < filhos.getLength(); ++i) {
                    final Node filhoAtual = filhos.item(i);
                    if (filhoAtual.getNodeName().equalsIgnoreCase("cert")) {
                        this.listaCertHttps.add(filhoAtual.getTextContent());
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<String> getCertSefaz() {
        return this.listaCertSefaz;
    }
    
    public List<String> getCertHttps() {
        return this.listaCertHttps;
    }
    
    public void addCertSefaz(final String cert) {
        if (cert != null) {
            this.listaCertSefaz.add(cert);
        }
    }
    
    public void addCertHttps(final String cert) {
        if (cert != null) {
            this.listaCertHttps.add(cert);
        }
    }
    
    public void gravarCertSefaz() {
        if (this.listaCertSefaz == null || this.listaCertSefaz.size() == 0) {
            return;
        }
        final StringBuffer sb = new StringBuffer();
        sb.append("<cert_sefaz>");
        for (int i = 0; i < this.listaCertSefaz.size(); ++i) {
            sb.append("<cert>").append(this.listaCertSefaz.get(i)).append("</cert>");
        }
        sb.append("</cert_sefaz>");
        ControleArquivos.escreverCaracteresArquivo(this.caminhoCertSefaz, sb.toString().toCharArray());
    }
    
    public void gravarCertHttps() {
        if (this.listaCertHttps == null || this.listaCertHttps.size() == 0) {
            return;
        }
        final StringBuffer sb = new StringBuffer();
        sb.append("<cert_https>");
        for (int i = 0; i < this.listaCertHttps.size(); ++i) {
            sb.append("<cert>").append(this.listaCertHttps.get(i)).append("</cert>");
        }
        sb.append("</cert_https>");
        ControleArquivos.escreverCaracteresArquivo(this.caminhoCertHttps, sb.toString().toCharArray());
    }
    
    public void limparBase() {
        this.listaCertSefaz = new ArrayList<String>();
        this.listaCertHttps = new ArrayList<String>();
    }
    
    public void setCaminhoCertHttps(final String caminhoCertHttps) {
        this.caminhoCertHttps = caminhoCertHttps;
    }
    
    public void setCaminhoCertSefaz(final String caminhoCertSefaz) {
        this.caminhoCertSefaz = caminhoCertSefaz;
    }
}
