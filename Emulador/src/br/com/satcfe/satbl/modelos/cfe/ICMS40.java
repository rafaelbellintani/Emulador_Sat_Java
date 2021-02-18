// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class ICMS40
{
    private String orig;
    private String CST;
    
    public ICMS40(final Node no) {
        this.orig = null;
        this.CST = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("Orig")) {
                this.orig = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CST")) {
                this.CST = filhoAtual.getTextContent();
            }
        }
    }
    
    public String getOrig() {
        return this.orig;
    }
    
    public void setOrig(final String orig) {
        this.orig = orig;
    }
    
    public String getCST() {
        return this.CST;
    }
    
    public void setCST(final String CST) {
        this.CST = CST;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.orig == null) {
            ControleLogs.logar("Campo 'orig' inexistente");
            resultado = "1999";
        }
        else if (this.CST == null) {
            ControleLogs.logar("Campo 'CST' inexistente");
            resultado = "1999";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.06 && (Integer.parseInt(this.orig) < 0 || Integer.parseInt(this.orig) > 7)) {
            ControleLogs.logar("Erro no Campo 'orig' : Campo inv\u00e1lido.");
            resultado = "1471";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.06 && (Integer.parseInt(this.orig) < 0 || Integer.parseInt(this.orig) > 8)) {
            ControleLogs.logar("Erro no Campo 'orig' : Campo inv\u00e1lido.");
            resultado = "1471";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) <= 0.06 && !this.CST.equals("40") && !this.CST.equals("41") && !this.CST.equals("50") && !this.CST.equals("60")) {
            ControleLogs.logar("Erro no Campo 'CST' : Campo inv\u00e1lido.");
            resultado = "1475";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.07 && !this.CST.equals("40") && !this.CST.equals("41") && !this.CST.equals("60")) {
            ControleLogs.logar("Erro no Campo 'CST' : Campo inv\u00e1lido.");
            resultado = "1475";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.orig != null) {
            retorno.append("<Orig>").append(this.orig).append("</Orig>");
        }
        if (this.CST != null) {
            retorno.append("<CST>").append(this.CST).append("</CST>");
        }
    }
}
