// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.modelos.ABNT5891;
import br.com.satcfe.satbl.Configuracoes;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class InformacoesISSQN
{
    private String vDeducISSQN;
    private String vBC;
    private String vAliq;
    private String vISSQN;
    private String cMunFG;
    private String cListServ;
    private String cServTribMun;
    private String cNatOp;
    private String indIncFisc;
    
    public InformacoesISSQN(final Node no) {
        this.vDeducISSQN = null;
        this.vBC = null;
        this.vAliq = null;
        this.vISSQN = null;
        this.cMunFG = null;
        this.cListServ = null;
        this.cServTribMun = null;
        this.cNatOp = null;
        this.indIncFisc = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("vDeducISSQN")) {
                this.vDeducISSQN = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vAliq")) {
                this.vAliq = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cMunFG")) {
                this.cMunFG = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cListServ")) {
                this.cListServ = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cServTribMun")) {
                this.cServTribMun = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cNatOp")) {
                this.cNatOp = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("indIncFisc")) {
                this.indIncFisc = filhoAtual.getTextContent();
            }
        }
    }
    
    public String getvDeducISSQN() {
        return this.vDeducISSQN;
    }
    
    public void setvDeducISSQN(final String vDeducISSQN) {
        this.vDeducISSQN = vDeducISSQN;
    }
    
    public String getvBC() {
        return this.vBC;
    }
    
    public void setvBC(final String vBC) {
        this.vBC = vBC;
    }
    
    public String getvAliq() {
        return this.vAliq;
    }
    
    public void setvAliq(final String vAliq) {
        this.vAliq = vAliq;
    }
    
    public String getvISSQN() {
        return this.vISSQN;
    }
    
    public void setvISSQN(final String vISSQN) {
        this.vISSQN = vISSQN;
    }
    
    public String getcMunFG() {
        return this.cMunFG;
    }
    
    public void setcMunFG(final String cMunFG) {
        this.cMunFG = cMunFG;
    }
    
    public String getcListServ() {
        return this.cListServ;
    }
    
    public void setcListServ(final String cListServ) {
        this.cListServ = cListServ;
    }
    
    public String getcServTribMun() {
        return this.cServTribMun;
    }
    
    public void setcServTribMun(final String cServTribMun) {
        this.cServTribMun = cServTribMun;
    }
    
    public String getcNatOp() {
        return this.cNatOp;
    }
    
    public void setcNatOp(final String cNatOp) {
        this.cNatOp = cNatOp;
    }
    
    public String getIndIncFisc() {
        return this.indIncFisc;
    }
    
    public void setIndIncFisc(final String indIncFisc) {
        this.indIncFisc = indIncFisc;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.vDeducISSQN == null) {
            ControleLogs.logar("Campo 'vDeducISSQN' inexistente");
            resultado = "1999";
        }
        else if (this.vAliq == null) {
            ControleLogs.logar("Campo 'vAliq' inexistente");
            resultado = "1999";
        }
        else if (this.cNatOp == null) {
            ControleLogs.logar("Campo 'cNatOp' inexistente");
            resultado = "1999";
        }
        else if (this.indIncFisc == null) {
            ControleLogs.logar("Campo 'indIncFisc' inexistente");
            resultado = "1999";
        }
        else if (!ControleDados.validarDouble(1, 15, 2, this.vDeducISSQN)) {
            ControleLogs.logar("Erro no Campo 'vDeducISSQN' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (Double.parseDouble(this.vDeducISSQN) < 0.0) {
            ControleLogs.logar("Campo 'vDeducISSQN' : Campo inv\u00e1lido.");
            resultado = "1503";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.06 && !ControleDados.validarDouble(1, 5, 2, this.vAliq)) {
            ControleLogs.logar("Erro no Campo 'vAliq' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) > 0.05 && !ControleDados.validarDouble(3, 5, 2, this.vAliq)) {
            ControleLogs.logar("Erro no Campo 'vAliq' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.vAliq != null && (Double.parseDouble(this.vAliq) < 2.0 || Double.parseDouble(this.vAliq) > 5.0)) {
            ControleLogs.logar("Erro no Campo 'vAliq' : Campo inv\u00e1lido.");
            resultado = "1505";
        }
        else if (this.cNatOp.length() != 2) {
            ControleLogs.logar("Erro no Campo 'cNatOp' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (!ControleDados.isNumerico(this.cNatOp) || Integer.parseInt(this.cNatOp) < 1 || Integer.parseInt(this.cNatOp) > 8) {
            ControleLogs.logar("Erro no Campo 'cNatOp' : Campo inv\u00e1lido.");
            resultado = "1510";
        }
        else if (this.indIncFisc.length() != 1) {
            ControleLogs.logar("Erro no Campo 'indIncFisc' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (!this.indIncFisc.equals("1") && !this.indIncFisc.equals("2")) {
            ControleLogs.logar("Erro no Campo 'indIncFisc' : Campo inv\u00e1lido.");
            resultado = "1511";
        }
        else if (this.cListServ != null && this.cListServ.length() != 5) {
            ControleLogs.logar("Erro no Campo 'cListServ' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.06 && this.cListServ != null && Double.parseDouble(this.cListServ) < 0.0) {
            ControleLogs.logar("Erro no Campo 'cListServ' : Campo menor que 0 (zero).");
            resultado = "1508";
        }
        else if (this.cMunFG != null && this.cMunFG.length() != 7) {
            ControleLogs.logar("Erro no Campo 'cMunFG' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.cMunFG != null && !ControleDados.validarCodMunicipio(this.cMunFG) && !this.cMunFG.equals("9999999")) {
            ControleLogs.logar("Erro no Campo 'cMunFG' : Campo com d\u00edgito inv\u00e1lido.");
            resultado = "1287";
        }
        else if (this.cServTribMun != null && this.cServTribMun.length() != 20) {
            ControleLogs.logar("Erro no Campo 'cServTribMun' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.cServTribMun != null && this.cServTribMun.trim().length() == 0) {
            ControleLogs.logar("Erro no Campo 'cServTribMun' : Campo em Branco.");
            resultado = "1509";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.vDeducISSQN != null) {
            retorno.append("<vDeducISSQN>").append(this.vDeducISSQN).append("</vDeducISSQN>");
        }
        if (this.vBC != null) {
            retorno.append("<vBC>").append(this.vBC).append("</vBC>");
        }
        if (this.vAliq != null) {
            retorno.append("<vAliq>").append(this.vAliq).append("</vAliq>");
        }
        if (this.vISSQN != null) {
            retorno.append("<vISSQN>").append(this.vISSQN).append("</vISSQN>");
        }
        if (this.cMunFG != null) {
            retorno.append("<cMunFG>").append(this.cMunFG).append("</cMunFG>");
        }
        if (this.cListServ != null) {
            retorno.append("<cListServ>").append(this.cListServ).append("</cListServ>");
        }
        if (this.cServTribMun != null) {
            retorno.append("<cServTribMun>").append(this.cServTribMun).append("</cServTribMun>");
        }
        if (this.cNatOp != null) {
            retorno.append("<cNatOp>").append(this.cNatOp).append("</cNatOp>");
        }
        if (this.indIncFisc != null) {
            retorno.append("<indIncFisc>").append(this.indIncFisc).append("</indIncFisc>");
        }
    }
    
    public void completar() {
        if (this.vDeducISSQN != null) {
            this.vDeducISSQN = new ABNT5891(this.vDeducISSQN).arredondarNBR(2).toString();
        }
        if (this.vBC != null) {
            this.vBC = new ABNT5891(this.vBC).arredondarNBR(2).toString();
        }
        if (this.vISSQN != null) {
            this.vISSQN = new ABNT5891(this.vISSQN).arredondarNBR(2).toString();
        }
    }
}
