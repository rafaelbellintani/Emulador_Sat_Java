// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.satcfe.satbl.controles.ControleSeguranca;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.Configuracoes;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class IdentificacaoCFe
{
    private String cUF;
    private String cNF;
    private String mod;
    private String nserieSAT;
    private String numeroCaixa;
    private String nCFe;
    private String dEmi;
    private String hEmi;
    private String cDV;
    private String tpAmb;
    private String CNPJ;
    private String signAC;
    private String qrCode;
    
    public IdentificacaoCFe(final Node no) {
        this.cUF = null;
        this.cNF = null;
        this.mod = null;
        this.nserieSAT = null;
        this.numeroCaixa = null;
        this.nCFe = null;
        this.dEmi = null;
        this.hEmi = null;
        this.cDV = null;
        this.tpAmb = null;
        this.CNPJ = null;
        this.signAC = null;
        this.qrCode = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CNPJ")) {
                this.CNPJ = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("signAC")) {
                this.signAC = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("numeroCaixa")) {
                this.numeroCaixa = filhoAtual.getTextContent();
            }
        }
    }
    
    public String getcUF() {
        return this.cUF;
    }
    
    public void setcUF(final String cUF) {
        this.cUF = cUF;
    }
    
    public String getcNF() {
        return this.cNF;
    }
    
    public void setcNF(final String cNF) {
        this.cNF = cNF;
    }
    
    public String getMod() {
        return this.mod;
    }
    
    public void setMod(final String mod) {
        this.mod = mod;
    }
    
    public String getNserieSAT() {
        return this.nserieSAT;
    }
    
    public void setNserieSAT(final String nserieSAT) {
        this.nserieSAT = nserieSAT;
    }
    
    public String getnCFe() {
        return this.nCFe;
    }
    
    public void setnCFe(final String nCFe) {
        this.nCFe = nCFe;
    }
    
    public String getdEmi() {
        return this.dEmi;
    }
    
    public void setdEmi(final String dEmi) {
        this.dEmi = dEmi;
    }
    
    public String gethEmi() {
        return this.hEmi;
    }
    
    public void sethEmi(final String hEmi) {
        this.hEmi = hEmi;
    }
    
    public String getcDV() {
        return this.cDV;
    }
    
    public void setcDV(final String cDV) {
        this.cDV = cDV;
    }
    
    public String getTpAmb() {
        return this.tpAmb;
    }
    
    public void setTpAmb(final String tpAmb) {
        this.tpAmb = tpAmb;
    }
    
    public String getCNPJ() {
        return this.CNPJ;
    }
    
    public void setCNPJ(final String CNPJ) {
        this.CNPJ = CNPJ;
    }
    
    public String getSignAC() {
        return this.signAC;
    }
    
    public void setSignAC(final String signAC) {
        this.signAC = signAC;
    }
    
    public void setQrCode(final String qrCode) {
        this.qrCode = qrCode;
    }
    
    public String getQrCode() {
        return this.qrCode;
    }
    
    public String getNumeroCaixa() {
        return this.numeroCaixa;
    }
    
    public void setNumeroCaixa(final String numeroCaixa) {
        this.numeroCaixa = numeroCaixa;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.CNPJ == null || this.CNPJ.length() != 14 || (!Configuracoes.SAT.emuladorOffLine && !ControleDados.validarCNPJCPF(this.CNPJ))) {
            ControleLogs.logar("Erro no campo 'CNPJ' : Campo inv\u00e1lido.");
            resultado = "1224";
        }
        else if (!Configuracoes.SAT.associado && !this.CNPJ.equals("00000000000000")) {
            ControleLogs.logar("Erro no campo 'CNPJ' : Campo inv\u00e1lido, difere de 00000000000000");
            resultado = "1999";
        }
        else if (Configuracoes.SAT.associado && !this.CNPJ.equals(Configuracoes.SAT.cnpjSoftwareHouse)) {
            ControleLogs.logar("Erro no campo 'CNPJ' : Campo inv\u00e1lido, difere de " + Configuracoes.SAT.cnpjSoftwareHouse);
            resultado = "1999";
        }
        else if (this.signAC == null) {
            ControleLogs.logar("Erro no campo 'signAC' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.signAC.length() > 344 || this.signAC.length() == 0) {
            ControleLogs.logar("Erro no campo 'signAC' : Campo com tamanho invalido.");
            if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) <= 0.06) {
                resultado = "1222";
            }
            if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.06) {
                resultado = "1455";
            }
        }
        else if (this.signAC.length() < 344 && Configuracoes.SAT.VERSAO_LAYOUT_CFE.equals("0.03")) {
            ControleLogs.logar("Erro no campo 'signAC' : Campo difere de 344 caracteres.");
            resultado = "1999";
        }
        else if (!this.signAC.equals(Configuracoes.SAT.signAC)) {
            ControleLogs.logar("Erro no campo 'signAC' : Assinatura do AC n\u00e3o confere com o registro do SAT.");
            resultado = "1085";
        }
        else if (this.numeroCaixa == null || this.numeroCaixa.length() != 3) {
            ControleLogs.logar("Erro no campo 'numeroCaixa' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        return resultado;
    }
    
    public void completar(final int tipo) {
        final String[] parametros = ControleSeguranca.gerarInformacoesCodigoDeAcesso(tipo);
        this.cUF = parametros[0];
        this.cNF = parametros[1];
        this.mod = parametros[2];
        this.nserieSAT = parametros[3];
        this.nCFe = parametros[4];
        this.dEmi = parametros[5];
        this.hEmi = parametros[6];
        this.cDV = parametros[7];
        this.tpAmb = parametros[8];
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.cUF != null) {
            retorno.append("<cUF>").append(this.cUF).append("</cUF>");
        }
        if (this.cNF != null) {
            retorno.append("<cNF>").append(this.cNF).append("</cNF>");
        }
        if (this.mod != null) {
            retorno.append("<mod>").append(this.mod).append("</mod>");
        }
        if (this.nserieSAT != null) {
            retorno.append("<nserieSAT>").append(this.nserieSAT).append("</nserieSAT>");
        }
        if (this.nCFe != null) {
            retorno.append("<nCFe>").append(ControleDados.formatarDouble(this.nCFe, 6, 6, 0)).append("</nCFe>");
        }
        if (this.dEmi != null) {
            retorno.append("<dEmi>").append(this.dEmi).append("</dEmi>");
        }
        if (this.hEmi != null) {
            retorno.append("<hEmi>").append(this.hEmi).append("</hEmi>");
        }
        if (this.cDV != null) {
            retorno.append("<cDV>").append(this.cDV).append("</cDV>");
        }
        if (this.tpAmb != null) {
            retorno.append("<tpAmb>").append(this.tpAmb).append("</tpAmb>");
        }
        if (this.CNPJ != null) {
            retorno.append("<CNPJ>").append(this.CNPJ).append("</CNPJ>");
        }
        if (this.signAC != null) {
            retorno.append("<signAC>").append(this.signAC).append("</signAC>");
        }
        if (this.qrCode != null) {
            retorno.append("<assinaturaQRCODE>").append(this.qrCode).append("</assinaturaQRCODE>");
        }
        if (this.numeroCaixa != null) {
            retorno.append("<numeroCaixa>").append(this.numeroCaixa).append("</numeroCaixa>");
        }
    }
}
