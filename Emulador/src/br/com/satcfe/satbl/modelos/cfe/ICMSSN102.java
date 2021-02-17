// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class ICMSSN102
{
    private String orig;
    private String CSOSN;
    
    public ICMSSN102(final Node no) {
        this.orig = null;
        this.CSOSN = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("Orig")) {
                this.orig = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CSOSN")) {
                this.CSOSN = filhoAtual.getTextContent();
            }
        }
    }
    
    public String getOrig() {
        return this.orig;
    }
    
    public void setOrig(final String orig) {
        this.orig = orig;
    }
    
    public String getCSOSN() {
        return this.CSOSN;
    }
    
    public void setCSOSN(final String cSOSN) {
        this.CSOSN = cSOSN;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.orig == null) {
            ControleLogs.logar("Campo 'orig' inexistente");
            resultado = "1999";
        }
        else if (this.CSOSN == null) {
            ControleLogs.logar("Campo 'CSOSN' inexistente");
            resultado = "1999";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) <= 0.05 && (Integer.parseInt(this.orig) < 0 || Integer.parseInt(this.orig) > 7)) {
            ControleLogs.logar("Erro no Campo 'orig' : Campo inv\u00e1lido.");
            resultado = "1471";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.06 && (Integer.parseInt(this.orig) < 0 || Integer.parseInt(this.orig) > 8)) {
            ControleLogs.logar("Erro no Campo 'orig' : Campo inv\u00e1lido.");
            resultado = "1471";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) <= 0.06 && !this.CSOSN.equals("102") && !this.CSOSN.equals("300") && !this.CSOSN.equals("500")) {
            ControleLogs.logar("Erro no Campo 'CSOSN' : Campo inv\u00e1lido.");
            resultado = "1476";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.07 && !this.CSOSN.equals("102") && !this.CSOSN.equals("300") && !this.CSOSN.equals("400") && !this.CSOSN.equals("500")) {
            ControleLogs.logar("Erro no Campo 'CSOSN' : Campo inv\u00e1lido.");
            resultado = "1476";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.orig != null) {
            retorno.append("<Orig>").append(this.orig).append("</Orig>");
        }
        if (this.CSOSN != null) {
            retorno.append("<CSOSN>").append(this.CSOSN).append("</CSOSN>");
        }
    }
}
