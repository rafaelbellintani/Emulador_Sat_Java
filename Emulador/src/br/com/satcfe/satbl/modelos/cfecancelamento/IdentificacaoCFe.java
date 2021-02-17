// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfecancelamento;

import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleSeguranca;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class IdentificacaoCFe
{
    private String signAC;
    private String qrCode;
    private String cnpj;
    private String cUF;
    private String cNF;
    private String mod;
    private String nserieSAT;
    private String nCFe;
    private String dEmi;
    private String hEmi;
    private String cDV;
    private String numeroCaixa;
    
    public IdentificacaoCFe() {
        this.signAC = null;
        this.qrCode = null;
        this.cnpj = null;
        this.cUF = null;
        this.cNF = null;
        this.mod = null;
        this.nserieSAT = null;
        this.nCFe = null;
        this.dEmi = null;
        this.hEmi = null;
        this.cDV = null;
        this.numeroCaixa = null;
    }
    
    public IdentificacaoCFe(final Node no) {
        this.signAC = null;
        this.qrCode = null;
        this.cnpj = null;
        this.cUF = null;
        this.cNF = null;
        this.mod = null;
        this.nserieSAT = null;
        this.nCFe = null;
        this.dEmi = null;
        this.hEmi = null;
        this.cDV = null;
        this.numeroCaixa = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CNPJ")) {
                this.cnpj = filhoAtual.getTextContent().trim();
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
    
    public String getSignAC() {
        return this.signAC;
    }
    
    public void setSignAC(final String signAC) {
        this.signAC = signAC;
    }
    
    public String getQrCode() {
        return this.qrCode;
    }
    
    public void setQrCode(final String qrCode) {
        this.qrCode = qrCode;
    }
    
    public String getCnpj() {
        return this.cnpj;
    }
    
    public void setCnpj(final String cnpj) {
        this.cnpj = cnpj;
    }
    
    public void completar() {
        final String[] parametros = ControleSeguranca.gerarInformacoesCodigoDeAcesso(1);
        this.cUF = parametros[0];
        this.cNF = parametros[1];
        this.mod = parametros[2];
        this.nserieSAT = parametros[3];
        this.nCFe = parametros[4];
        this.dEmi = parametros[5];
        this.hEmi = parametros[6];
        this.cDV = parametros[7];
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
        if (this.cnpj != null) {
            retorno.append("<CNPJ>").append(this.cnpj).append("</CNPJ>");
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
    
    public String validar() {
        String resultado = "1000";
        if (this.cnpj == null) {
            resultado = "1999";
            ControleLogs.logar("Erro no campo 'cnpj': campo inv\u00e1lido (vazio).");
        }
        else if (this.cnpj.length() != 14 || (!Configuracoes.SAT.emuladorOffLine && !ControleDados.validarCNPJCPF(this.cnpj))) {
            resultado = "1454";
            ControleLogs.logar("Erro no campo 'cnpj': CNPJ da Software House inv\u00e1lido.");
        }
        else if (this.signAC == null) {
            resultado = "1999";
            ControleLogs.logar("Erro no campo 'signAC': campo inv\u00e1lido.");
        }
        else if (this.signAC.length() > 344 || this.signAC.length() == 0) {
            resultado = "1455";
            ControleLogs.logar("Erro no campo 'signAC': campo inv\u00e1lido.");
        }
        else if (this.numeroCaixa == null || this.numeroCaixa.length() != 3) {
            ControleLogs.logar("Erro no campo 'numeroCaixa' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        return resultado;
    }
}
