// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class TributosCFe
{
    private String vItem12741;
    private InformacoesICMS ICMS;
    private InformacoesISSQN ISSQN;
    private InformacoesPIS PIS;
    private InformacoesPISST PISST;
    private InformacoesCOFINS COFINS;
    private InformacoesCOFINSST COFINSST;
    
    public TributosCFe(final Node no) {
        this.vItem12741 = null;
        this.ICMS = null;
        this.ISSQN = null;
        this.PIS = null;
        this.PISST = null;
        this.COFINS = null;
        this.COFINSST = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("vItem12741")) {
                this.vItem12741 = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("ICMS")) {
                this.ICMS = new InformacoesICMS(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("PIS")) {
                this.PIS = new InformacoesPIS(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("PISST")) {
                this.PISST = new InformacoesPISST(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("COFINS")) {
                this.COFINS = new InformacoesCOFINS(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("COFINSST")) {
                this.COFINSST = new InformacoesCOFINSST(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("ISSQN")) {
                this.ISSQN = new InformacoesISSQN(filhoAtual);
            }
        }
    }
    
    public InformacoesICMS getICMS() {
        return this.ICMS;
    }
    
    public void setICMS(final InformacoesICMS ICMS) {
        this.ICMS = ICMS;
    }
    
    public void setPIS(final InformacoesPIS PIS) {
        this.PIS = PIS;
    }
    
    public InformacoesPIS getPIS() {
        return this.PIS;
    }
    
    public void setPISST(final InformacoesPISST PISST) {
        this.PISST = PISST;
    }
    
    public InformacoesPISST getPISST() {
        return this.PISST;
    }
    
    public void setCOFINS(final InformacoesCOFINS COFINS) {
        this.COFINS = COFINS;
    }
    
    public InformacoesCOFINS getCOFINS() {
        return this.COFINS;
    }
    
    public void setCOFINSST(final InformacoesCOFINSST COFINSST) {
        this.COFINSST = COFINSST;
    }
    
    public InformacoesCOFINSST getCOFINSST() {
        return this.COFINSST;
    }
    
    public void setISSQN(final InformacoesISSQN ISSQN) {
        this.ISSQN = ISSQN;
    }
    
    public InformacoesISSQN getISSQN() {
        return this.ISSQN;
    }
    
    public String validar() {
        String resultado = "1000";
        if ((this.ICMS == null && this.ISSQN == null) || (this.ICMS != null && this.ISSQN != null)) {
            ControleLogs.logar("Grupo 'ICMS'/'ISSQN' invalido, Se ISSQN for informado o grupo ICMS n\u00e3o ser\u00e1 informado e vice-versa.");
            resultado = "1999";
        }
        else if (this.PIS == null) {
            ControleLogs.logar("Grupo 'PIS' inexistente");
            resultado = "1999";
        }
        else if (this.COFINS == null) {
            ControleLogs.logar("Grupo 'COFINS' inexistente");
            resultado = "1999";
        }
        else if (this.vItem12741 != null && !ControleDados.validarDouble(1, 15, 2, this.vItem12741)) {
            ControleLogs.logar("Erro no Campo 'vItem12741' ");
            resultado = "1999";
        }
        else if (this.vItem12741 != null && Double.parseDouble(this.vItem12741) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vItem12741' ");
            resultado = "1534";
        }
        else if (this.ICMS != null && !(resultado = this.ICMS.validar()).equals("1000")) {
            ControleLogs.logar("Erro no Grupo 'ICMS' ");
        }
        else if (!(resultado = this.PIS.validar()).equals("1000")) {
            ControleLogs.logar("Erro no Grupo 'PIS'");
        }
        else if (this.PISST != null && !(resultado = this.PISST.validar()).equals("1000")) {
            ControleLogs.logar("Erro no Grupo 'PISST'");
        }
        else if (!(resultado = this.COFINS.validar()).equals("1000")) {
            ControleLogs.logar("Erro no Grupo 'COFINS'");
        }
        else if (this.COFINSST != null && !(resultado = this.COFINSST.validar()).equals("1000")) {
            ControleLogs.logar("Erro no Grupo 'COFINSST'");
        }
        else if (this.ISSQN != null && !(resultado = this.ISSQN.validar()).equals("1000")) {
            ControleLogs.logar("Erro no Grupo 'ISSQN'");
        }
        return resultado;
    }
    
    public void completar() {
        if (this.vItem12741 != null) {
            this.vItem12741 = ControleDados.formatarDouble(this.vItem12741, 1, 13, 2);
        }
        if (this.ICMS != null) {
            this.ICMS.completar();
        }
        if (this.PIS != null) {
            this.PIS.completar();
        }
        if (this.PISST != null) {
            this.PISST.completar();
        }
        if (this.COFINS != null) {
            this.COFINS.completar();
        }
        if (this.COFINSST != null) {
            this.COFINSST.completar();
        }
        if (this.ISSQN != null) {
            this.ISSQN.completar();
        }
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.vItem12741 != null) {
            retorno.append("<vItem12741>");
            retorno.append(this.vItem12741);
            retorno.append("</vItem12741>");
        }
        if (this.ICMS != null) {
            retorno.append("<ICMS>");
            this.ICMS.toString(retorno);
            retorno.append("</ICMS>");
        }
        else if (this.ISSQN != null) {
            retorno.append("<ISSQN>");
            this.ISSQN.toString(retorno);
            retorno.append("</ISSQN>");
        }
        if (this.PIS != null) {
            retorno.append("<PIS>");
            this.PIS.toString(retorno);
            retorno.append("</PIS>");
        }
        if (this.PISST != null) {
            retorno.append("<PISST>");
            this.PISST.toString(retorno);
            retorno.append("</PISST>");
        }
        if (this.COFINS != null) {
            retorno.append("<COFINS>");
            this.COFINS.toString(retorno);
            retorno.append("</COFINS>");
        }
        if (this.COFINSST != null) {
            retorno.append("<COFINSST>");
            this.COFINSST.toString(retorno);
            retorno.append("</COFINSST>");
        }
    }
}
