// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class IdentificacaoEmitenteCFe
{
    private String CNPJ;
    private String xNome;
    private String xFant;
    private EnderecoEmitente enderEmit;
    private String IE;
    private String IM;
    private String cRegTrib;
    private String cRegTribISSQN;
    private String indRatISSQN;
    
    public IdentificacaoEmitenteCFe(final Node no) {
        this.CNPJ = null;
        this.xNome = null;
        this.xFant = null;
        this.enderEmit = null;
        this.IE = null;
        this.IM = null;
        this.cRegTrib = null;
        this.cRegTribISSQN = null;
        this.indRatISSQN = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CNPJ")) {
                this.CNPJ = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("IE")) {
                this.IE = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("IM")) {
                this.IM = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cRegTribISSQN")) {
                this.cRegTribISSQN = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("indRatISSQN")) {
                this.indRatISSQN = filhoAtual.getTextContent();
            }
        }
    }
    
    public String getCNPJ() {
        return this.CNPJ;
    }
    
    public void setCNPJ(final String CNPJ) {
        this.CNPJ = CNPJ;
    }
    
    public String getxNome() {
        return this.xNome;
    }
    
    public void setxNome(final String xNome) {
        this.xNome = xNome;
    }
    
    public String getxFant() {
        return this.xFant;
    }
    
    public void setxFant(final String xFant) {
        this.xFant = xFant;
    }
    
    public EnderecoEmitente getEnderEmit() {
        return this.enderEmit;
    }
    
    public void getEnderEmit(final EnderecoEmitente enderEmitCfe) {
        this.enderEmit = enderEmitCfe;
    }
    
    public String getIE() {
        return this.IE;
    }
    
    public void setIE(final String IE) {
        this.IE = IE;
    }
    
    public String getIM() {
        return this.IM;
    }
    
    public void setIM(final String iM) {
        this.IM = iM;
    }
    
    public String getCRT() {
        return this.cRegTrib;
    }
    
    public void setCRT(final String CRT) {
        this.cRegTrib = CRT;
    }
    
    public String getIndRatISSQN() {
        return this.indRatISSQN;
    }
    
    public String validar() {
        String resultado = "1000";
        if (Configuracoes.SAT.associado && !this.IE.equals(Parametrizacoes.IE)) {
            ControleLogs.logar("Campo 'IE' invalido");
            resultado = "1230";
        }
        else if (!Configuracoes.SAT.associado && !this.IE.equals("000000000000")) {
            ControleLogs.logar("Campo 'IE' invalido");
            resultado = "1209";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) <= 0.07 && (this.IE == null || !ControleDados.isNumerico(this.IE) || this.IE.length() != 12)) {
            ControleLogs.logar("Campo 'IE' invalido");
            resultado = "1209";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && (this.IE == null || !ControleDados.isNumerico(this.IE) || this.IE.length() < 2 || this.IE.length() > 14)) {
            ControleLogs.logar("Campo 'IE' invalido");
            resultado = "1209";
        }
        else if (this.CNPJ == null || this.CNPJ.length() != 14 || !this.CNPJ.equals(Parametrizacoes.CNPJ)) {
            ControleLogs.logar("Campo 'CNPJ' invalido");
            resultado = "1207";
        }
        else if (this.IM != null && (this.IM.length() < 1 || this.IM.length() > 15)) {
            ControleLogs.logar("Campo 'IM' invalido");
            resultado = "1999";
        }
        else if (this.cRegTribISSQN != null && (this.cRegTribISSQN.length() != 1 || !ControleDados.isNumerico(this.cRegTribISSQN))) {
            ControleLogs.logar("Campo 'cRegTribISSQN' invalido");
            resultado = "1457";
        }
        else if (this.cRegTribISSQN != null && (Integer.parseInt(this.cRegTribISSQN) < 1 || Integer.parseInt(this.cRegTribISSQN) > 5)) {
            ControleLogs.logar("Campo 'cRegTribISSQN' invalido");
            resultado = "1457";
        }
        else if (this.indRatISSQN == null) {
            ControleLogs.logar("Campo 'indRatISSQN' invalido");
            resultado = "1507";
        }
        else if (!this.indRatISSQN.equals("S") && !this.indRatISSQN.equals("N")) {
            ControleLogs.logar("Campo 'indRatISSQN' invalido");
            resultado = "1507";
        }
        return resultado;
    }
    
    public void completar() {
        this.xNome = Parametrizacoes.razaoSocialEmitente;
        if (Parametrizacoes.nomeFantasiaEmitente != null && Parametrizacoes.nomeFantasiaEmitente.length() > 0) {
            this.xFant = Parametrizacoes.nomeFantasiaEmitente;
        }
        (this.enderEmit = new EnderecoEmitente()).completar();
        this.cRegTrib = ((Parametrizacoes.codigoRegimeTributario == null) ? "3" : Parametrizacoes.codigoRegimeTributario);
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.CNPJ != null) {
            retorno.append("<CNPJ>").append(this.CNPJ).append("</CNPJ>");
        }
        if (this.xNome != null) {
            retorno.append("<xNome>").append(this.xNome).append("</xNome>");
        }
        if (this.xFant != null) {
            retorno.append("<xFant>").append(this.xFant).append("</xFant>");
        }
        if (this.enderEmit != null) {
            retorno.append("<enderEmit>");
            this.enderEmit.toString(retorno);
            retorno.append("</enderEmit>");
        }
        if (this.IE != null) {
            retorno.append("<IE>").append(this.IE).append("</IE>");
        }
        if (this.IM != null) {
            retorno.append("<IM>").append(this.IM).append("</IM>");
        }
        if (this.cRegTrib != null) {
            retorno.append("<cRegTrib>").append(this.cRegTrib).append("</cRegTrib>");
        }
        if (this.cRegTribISSQN != null) {
            retorno.append("<cRegTribISSQN>").append(this.cRegTribISSQN).append("</cRegTribISSQN>");
        }
        if (this.indRatISSQN != null) {
            retorno.append("<indRatISSQN>").append(this.indRatISSQN).append("</indRatISSQN>");
        }
    }
}
