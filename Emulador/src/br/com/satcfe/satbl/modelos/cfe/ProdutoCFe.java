// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import org.w3c.dom.Node;
import java.util.List;

public class ProdutoCFe
{
    private String cProd;
    private String cEAN;
    private String xProd;
    private String NCM;
    private String CFOP;
    private String CEST;
    private String uCom;
    private String qCom;
    private String vUnCom;
    private String vProd;
    private String indRegra;
    private String vDesc;
    private String vOutro;
    private String vItem;
    private String vRatDesc;
    private String vRatAcr;
    private List obsFiscoDet;
    
    public ProdutoCFe(final Node no) {
        this.cProd = null;
        this.cEAN = null;
        this.xProd = null;
        this.NCM = null;
        this.CFOP = null;
        this.CEST = null;
        this.uCom = null;
        this.qCom = null;
        this.vUnCom = null;
        this.vProd = null;
        this.indRegra = null;
        this.vDesc = null;
        this.vOutro = null;
        this.vItem = null;
        this.vRatDesc = null;
        this.vRatAcr = null;
        this.obsFiscoDet = null;
        this.obsFiscoDet = new ArrayList();
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("cProd")) {
                this.cProd = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cEAN")) {
                this.cEAN = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xProd")) {
                this.xProd = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("NCM")) {
                this.NCM = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CEST")) {
                this.CEST = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("CFOP")) {
                this.CFOP = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("uCom")) {
                this.uCom = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("qCom")) {
                this.qCom = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vUnCom")) {
                this.vUnCom = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("indRegra")) {
                this.indRegra = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vDesc")) {
                this.vDesc = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vOutro")) {
                this.vOutro = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("obsFiscoDet")) {
                final ObservacaoFiscoDet obsFisco = new ObservacaoFiscoDet(filhoAtual);
                this.obsFiscoDet.add(obsFisco);
            }
        }
    }
    
    public String getcProd() {
        return this.cProd;
    }
    
    public void setcProd(final String cProd) {
        this.cProd = cProd;
    }
    
    public String getcEAN() {
        return this.cEAN;
    }
    
    public void setcEAN(final String cEAN) {
        this.cEAN = cEAN;
    }
    
    public String getxProd() {
        return this.xProd;
    }
    
    public void setxProd(final String xProd) {
        this.xProd = xProd;
    }
    
    public String getNCM() {
        return this.NCM;
    }
    
    public void setNCM(final String nCM) {
        this.NCM = nCM;
    }
    
    public String getCFOP() {
        return this.CFOP;
    }
    
    public void setCFOP(final String cFOP) {
        this.CFOP = cFOP;
    }
    
    public String getuCom() {
        return this.uCom;
    }
    
    public void setuCom(final String uCom) {
        this.uCom = uCom;
    }
    
    public String getqCom() {
        return this.qCom;
    }
    
    public void setqCom(final String qCom) {
        this.qCom = qCom;
    }
    
    public String getvUnCom() {
        return this.vUnCom;
    }
    
    public void setvUnCom(final String vUnCom) {
        this.vUnCom = vUnCom;
    }
    
    public String getvProd() {
        return this.vProd;
    }
    
    public void setvProd(final String vProd) {
        this.vProd = vProd;
    }
    
    public String getIndRegra() {
        return this.indRegra;
    }
    
    public void setIndRegra(final String indRegra) {
        this.indRegra = indRegra;
    }
    
    public String getvDesc() {
        return this.vDesc;
    }
    
    public void setvDesc(final String vDesc) {
        this.vDesc = vDesc;
    }
    
    public String getvOutro() {
        return this.vOutro;
    }
    
    public void setvOutro(final String vOutro) {
        this.vOutro = vOutro;
    }
    
    public String getvItem() {
        return this.vItem;
    }
    
    public void setvItem(final String vItem) {
        this.vItem = vItem;
    }
    
    public List getObsFiscoDet() {
        return this.obsFiscoDet;
    }
    
    public void setvRatAcr(final String vRatAcr) {
        this.vRatAcr = vRatAcr;
    }
    
    public void setvRatDesc(final String vRatDesc) {
        this.vRatDesc = vRatDesc;
    }
    
    public String getvRatAcr() {
        return this.vRatAcr;
    }
    
    public String getvRatDesc() {
        return this.vRatDesc;
    }
    
    public String validar() {
        String resultado = "1000";
        try {
            if (this.cProd == null) {
                ControleLogs.logar("Campo 'cProd' inexistente");
                resultado = "1999";
            }
            else if (this.cEAN != null && !ControleDados.validarGTIN8(this.cEAN) && !ControleDados.validarGTIN12(this.cEAN) && !ControleDados.validarGTIN13(this.cEAN) && !ControleDados.validarGTIN14(this.cEAN)) {
                ControleLogs.logar("Erro no campo 'cEAN'");
                resultado = "1460";
            }
            else if (this.xProd == null) {
                ControleLogs.logar("Campo 'xProd' inexistente");
                resultado = "1999";
            }
            else if (this.NCM != null && this.NCM.length() != 2 && this.NCM.length() != 8) {
                ControleLogs.logar("Erro no campo 'NCM' : Campo inv\u00e1lido.");
                resultado = "1999";
            }
            else if (this.CFOP == null) {
                ControleLogs.logar("Campo 'CFOP' inexistente");
                resultado = "1999";
            }
            else if (this.uCom == null) {
                ControleLogs.logar("Campo 'uCom' inexistente");
                resultado = "1999";
            }
            else if (this.qCom == null) {
                ControleLogs.logar("Campo 'qCom' inexistente");
                resultado = "1999";
            }
            else if (this.vUnCom == null) {
                ControleLogs.logar("Campo 'vUnCom' inexistente");
                resultado = "1999";
            }
            else if (this.indRegra == null) {
                ControleLogs.logar("Campo 'indRegra' inexistente");
                resultado = "1999";
            }
            else if (this.vDesc != null && !ControleDados.validarDouble(1, 15, 2, this.vDesc)) {
                ControleLogs.logar("Erro no campo 'vDesc' : Campo inv\u00e1lido.");
                resultado = "1999";
            }
            else if (this.vOutro != null && !ControleDados.validarDouble(1, 15, 2, this.vOutro)) {
                ControleLogs.logar("Erro no campo 'vOutro' : Campo inv\u00e1lido.");
                resultado = "1999";
            }
            else if (this.cProd.length() < 1 || this.cProd.length() > 60) {
                ControleLogs.logar("Erro no campo 'cProd' : campo inv\u00e1lido.");
                resultado = "1999";
            }
            else if (this.cProd.trim().length() == 0) {
                ControleLogs.logar("Erro no campo 'cProd' : Campo em branco.");
                resultado = "1459";
            }
            else if (this.xProd.length() < 1 || this.xProd.length() > 120) {
                ControleLogs.logar("Erro no campo 'xProd' : Campo com tamanho inv\u00e1lido");
                resultado = "1999";
            }
            else if (this.xProd.trim().length() == 0) {
                ControleLogs.logar("Erro no campo 'xProd' : Campo em branco");
                resultado = "1461";
            }
            else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && this.CEST != null && this.CEST.length() != 7) {
                ControleLogs.logar("Erro no campo 'CEST' : campo inv\u00e1lido.");
                resultado = "1999";
            }
            else if (this.CFOP.length() != 4 || this.CFOP.charAt(0) != '5') {
                ControleLogs.logar("Erro no campo 'CFOP' : CFOP n\u00e3o \u00e9 v\u00e1lido para CF-e-SAT  (diferente de 5xxx)");
                resultado = "1462";
            }
            else if (this.uCom.length() < 1 || this.uCom.length() > 6) {
                ControleLogs.logar("Erro no campo 'uCom' : Campo com tamanho inv\u00e1lido.");
                resultado = "1999";
            }
            else if (this.uCom.trim().length() == 0) {
                ControleLogs.logar("Erro no campo 'uCom' : Campo em branco.");
                resultado = "1463";
            }
            else if (!ControleDados.validarDouble(1, 15, 4, this.qCom)) {
                ControleLogs.logar("Erro no campo 'qCom' : Campo inv\u00e1lido.");
                resultado = "1999";
            }
            else if (!ControleDados.validarDouble(1, 15, 3, this.vUnCom) && !ControleDados.validarDouble(1, 15, 2, this.vUnCom)) {
                ControleLogs.logar("Erro no campo 'vUnCom' : campo inv\u00e1lido.");
                resultado = "1999";
            }
            else if (!this.indRegra.equals("A") && !this.indRegra.equals("T")) {
                ControleLogs.logar("Erro no campo 'indRegra' : Campo diferente de 'A' e 'T'");
                resultado = "1467";
            }
            else if (Double.parseDouble(this.qCom) < 0.0) {
                ControleLogs.logar("Erro no campo 'qCom' : Campo menor que 0 (zero).");
                resultado = "1464";
            }
            else if (Double.parseDouble(this.vUnCom) < 0.0) {
                ControleLogs.logar("Erro no campo 'vUnCom' : Campo menor que 0 (zero)");
                resultado = "1465";
            }
            else if (this.vDesc != null && Double.parseDouble(this.vDesc) < 0.0) {
                ControleLogs.logar("Erro no campo 'vDesc' : Campo menor que 0 (zero)");
                resultado = "1468";
            }
            else if (this.vOutro != null && Double.parseDouble(this.vOutro) < 0.0) {
                ControleLogs.logar("Erro no campo 'vOutro' : Campo menor que 0 (zero)");
                resultado = "1469";
            }
            else if (this.obsFiscoDet != null) {
                for (int i = 0; i < this.obsFiscoDet.size(); ++i) {
                   /*FEITO CAST*/ final ObservacaoFiscoDet obsFD = (ObservacaoFiscoDet) this.obsFiscoDet.get(i);
                    resultado = obsFD.validar();
                    if (!resultado.equals("1000")) {
                        break;
                    }
                    if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08) {
                        if (!this.CFOP.equals("5656")) {
                            ControleLogs.logar("Erro no campo 'xTextoDet': N\u00e3o informado c\u00f3digo do produto com CFOP 5656");
                            resultado = "1751";
                        }
                        else if (this.CFOP.equals("5656") && obsFD.getxTextoDet() != null) {
                            if (Parametrizacoes.tabelaCodigosAnp != null && !Parametrizacoes.tabelaCodigosAnp.isCodigoAnpValido(obsFD.getxTextoDet())) {
                                ControleLogs.logar("Erro no campo 'xTextoDet': C\u00f3digo do produto CFOP 5656 fora do padr\u00e3o ANP.");
                                resultado = "1752";
                            }
                        }
                        else {
                            ControleLogs.logar("Erro no campo 'xTextoDet': Erro Desconhecido.");
                            resultado = "1999";
                        }
                        if (!ControleDados.validarDouble(1, 15, 3, this.vUnCom)) {
                            ControleLogs.logar("Alerta no campo 'vUnCom': Venda de Combust\u00edvel deve possuir 3 casas decimais.");
                        }
                        if (!this.indRegra.equals("T")) {
                            ControleLogs.logar("Alerta no campo 'indRegra': Opera\u00e7\u00e3o com Combust\u00edveis deve ser truncado.");
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            resultado = "1999";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.cProd != null) {
            retorno.append("<cProd>").append(this.cProd).append("</cProd>");
        }
        if (this.cEAN != null) {
            retorno.append("<cEAN>").append(this.cEAN).append("</cEAN>");
        }
        if (this.xProd != null) {
            retorno.append("<xProd>").append(this.xProd).append("</xProd>");
        }
        if (this.NCM != null) {
            retorno.append("<NCM>").append(this.NCM).append("</NCM>");
        }
        if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && this.CEST != null) {
            retorno.append("<CEST>").append(this.CEST).append("</CEST>");
        }
        if (this.CFOP != null) {
            retorno.append("<CFOP>").append(this.CFOP).append("</CFOP>");
        }
        if (this.uCom != null) {
            retorno.append("<uCom>").append(this.uCom).append("</uCom>");
        }
        if (this.qCom != null) {
            retorno.append("<qCom>").append(this.qCom).append("</qCom>");
        }
        if (this.vUnCom != null) {
            retorno.append("<vUnCom>").append(this.vUnCom).append("</vUnCom>");
        }
        if (this.vProd != null) {
            retorno.append("<vProd>").append(this.vProd).append("</vProd>");
        }
        if (this.indRegra != null) {
            retorno.append("<indRegra>").append(this.indRegra).append("</indRegra>");
        }
        if (this.vDesc != null && Double.parseDouble(this.vDesc) > 0.0) {
            retorno.append("<vDesc>").append(this.vDesc).append("</vDesc>");
        }
        if (this.vOutro != null && Double.parseDouble(this.vOutro) > 0.0) {
            retorno.append("<vOutro>").append(this.vOutro).append("</vOutro>");
        }
        if (this.vItem != null) {
            retorno.append("<vItem>").append(this.vItem).append("</vItem>");
        }
        if (this.vRatDesc != null && Double.parseDouble(this.vRatDesc) > 0.0) {
            retorno.append("<vRatDesc>").append(this.vRatDesc).append("</vRatDesc>");
        }
        if (this.vRatAcr != null && Double.parseDouble(this.vRatAcr) > 0.0) {
            retorno.append("<vRatAcr>").append(this.vRatAcr).append("</vRatAcr>");
        }
        for (int i = 0; i < this.obsFiscoDet.size(); ++i) {
     /*FEITO CAST*/       retorno.append("<obsFiscoDet xCampoDet=\"").append(((ObservacaoFiscoDet) this.obsFiscoDet.get(i)).getxCampoDet()).append("\">");
            this.obsFiscoDet.get(i); //removido o .toString(retorn)
            retorno.append("</obsFiscoDet>");
        }
    }
}
