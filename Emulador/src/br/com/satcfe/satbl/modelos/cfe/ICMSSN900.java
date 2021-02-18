// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class ICMSSN900
{
    private String orig;
    private String CSOSN;
    private String pICMS;
    private String vICMS;
    
    public ICMSSN900(final Node no) {
        this.orig = null;
        this.CSOSN = null;
        this.pICMS = null;
        this.vICMS = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("Orig")) {
                this.orig = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CSOSN")) {
                this.CSOSN = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("pICMS")) {
                this.pICMS = filhoAtual.getTextContent();
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
    
    public void setCSOSN(final String CSOSN) {
        this.CSOSN = CSOSN;
    }
    
    public String getpICMS() {
        return this.pICMS;
    }
    
    public void setpICMS(final String pICMS) {
        this.pICMS = pICMS;
    }
    
    public String getvICMS() {
        return this.vICMS;
    }
    
    public void setvICMS(final String vICMS) {
        this.vICMS = vICMS;
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
        else if (this.pICMS == null) {
            ControleLogs.logar("Campo 'pICMS' inexistente");
            resultado = "1999";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) <= 0.05 && (Integer.parseInt(this.orig) < 0 || Integer.parseInt(this.orig) > 7)) {
            ControleLogs.logar("Erro no Campo 'orig' : Campo inv\u00e1lido.");
            resultado = "1471";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) > 0.05 && (Integer.parseInt(this.orig) < 0 || Integer.parseInt(this.orig) > 8)) {
            ControleLogs.logar("Erro no Campo 'orig' : Campo inv\u00e1lido.");
            resultado = "1471";
        }
        else if (!this.CSOSN.equals("900")) {
            ControleLogs.logar("Erro no Campo 'CSOSN' : Campo inv\u00e1lido.");
            resultado = "1477";
        }
        else if (!ControleDados.validarDouble(1, 5, 2, this.pICMS)) {
            ControleLogs.logar("Erro no Campo 'pICMS' : Campo inv\u00e1lido..");
            resultado = "1999";
        }
        else if (Double.parseDouble(this.pICMS) < 0.0) {
            ControleLogs.logar("Erro no Campo 'pICMS' : Campo menor que 0 (zero).");
            resultado = "1473";
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
        if (this.pICMS != null) {
            retorno.append("<pICMS>").append(this.pICMS).append("</pICMS>");
        }
        if (this.vICMS != null) {
            retorno.append("<vICMS>").append(this.vICMS).append("</vICMS>");
        }
    }
}
