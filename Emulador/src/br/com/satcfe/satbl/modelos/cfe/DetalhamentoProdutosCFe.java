// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import java.math.BigDecimal;
import br.com.um.modelos.Decimal;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class DetalhamentoProdutosCFe
{
    private String nItem;
    private ProdutoCFe prod;
    private TributosCFe imposto;
    private String infAdProd;
    
    public DetalhamentoProdutosCFe(final Node no) {
        this.nItem = null;
        this.prod = null;
        this.imposto = null;
        this.infAdProd = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("prod")) {
                this.prod = new ProdutoCFe(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("imposto")) {
                this.imposto = new TributosCFe(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("infAdProd")) {
                this.infAdProd = filhoAtual.getTextContent();
            }
        }
        final NamedNodeMap atributos = no.getAttributes();
        for (int j = 0; j < atributos.getLength(); ++j) {
            final Node filhoAtual2 = atributos.item(j);
            if (filhoAtual2.getNodeName().equalsIgnoreCase("nItem")) {
                this.nItem = filhoAtual2.getTextContent();
            }
        }
    }
    
    public String getNItem() {
        return this.nItem;
    }
    
    public void setnItem(final String nItem) {
        this.nItem = nItem;
    }
    
    public ProdutoCFe getProd() {
        return this.prod;
    }
    
    public TributosCFe getImposto() {
        return this.imposto;
    }
    
    public String getInfAdProd() {
        return this.infAdProd;
    }
    
    public void setInfAdProd(final String infAdProd) {
        this.infAdProd = infAdProd;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.prod == null) {
            ControleLogs.logar("campo 'prod' inexistente");
            resultado = "1999";
        }
        else if (this.nItem == null) {
            ControleLogs.logar("campo 'nIten' inexistente");
            resultado = "1999";
        }
        else if (!ControleDados.isInteger(this.nItem)) {
            ControleLogs.logar("campo 'nIten' invalido.");
            resultado = "1999";
        }
        else if (this.imposto == null) {
            ControleLogs.logar("campo 'imposto' inexistente");
            resultado = "1999";
        }
        else if (this.infAdProd != null && (this.infAdProd.length() < 1 || this.infAdProd.length() > 500)) {
            ControleLogs.logar("Erro no campo 'infAdProd'");
            resultado = "1999";
        }
        else if (!(resultado = this.prod.validar()).equals("1000")) {
            ControleLogs.logar("Erro no campo 'prod'");
        }
        else if (!(resultado = this.imposto.validar()).equals("1000")) {
            ControleLogs.logar("Erro no campo 'imposto'");
        }
        return resultado;
    }
    
    public void completar() {
        final InformacoesICMS infICMS;
        if ((infICMS = this.imposto.getICMS()) != null) {
            final ICMS00 icms00;
            if ((icms00 = infICMS.getICMS00()) != null) {
                icms00.setvICMS(Decimal.dividir(Decimal.multiplicar(this.prod.getvItem(), icms00.getpICMS()), new BigDecimal("100")).toString());
            }
            else {
                final ICMSSN900 icmssn900;
                if ((icmssn900 = infICMS.getICMSSN900()) != null) {
                    icmssn900.setvICMS(Decimal.dividir(Decimal.multiplicar(this.prod.getvItem(), icmssn900.getpICMS()), new BigDecimal("100")).toString());
                }
            }
        }
        final InformacoesPIS infPIS;
        if ((infPIS = this.imposto.getPIS()) != null) {
            final PISAliq pisAliq;
            if ((pisAliq = infPIS.getPISAliq()) != null) {
                pisAliq.setvPIS(Decimal.multiplicar(pisAliq.getvBC(), pisAliq.getpPIS()).toString());
            }
            else {
                final PISQtde pisQtde;
                if ((pisQtde = infPIS.getPISQTde()) != null) {
                    pisQtde.setvPIS(Decimal.multiplicar(pisQtde.getqBCProd(), pisQtde.getvAliqProd()).toString());
                }
                else {
                    final PISOutr pisOutr;
                    if ((pisOutr = infPIS.getPISOutr()) != null) {
                        if (pisOutr.getvBC() != null && pisOutr.getpPIS() != null) {
                            pisOutr.setvPIS(Decimal.multiplicar(pisOutr.getvBC(), pisOutr.getpPIS()).toString());
                        }
                        else if (pisOutr.getqBCProd() != null && pisOutr.getvAliqProd() != null) {
                            pisOutr.setvPIS(Decimal.multiplicar(pisOutr.getqBCProd(), pisOutr.getvAliqProd()).toString());
                        }
                    }
                }
            }
        }
        final InformacoesPISST infPISST;
        if ((infPISST = this.imposto.getPISST()) != null) {
            if (infPISST.getvBC() != null && infPISST.getpPIS() != null) {
                infPISST.setvPIS(Decimal.multiplicar(infPISST.getvBC(), infPISST.getpPIS()).toString());
            }
            else if (infPISST.getqBCProd() != null && infPISST.getvAliqProd() != null) {
                infPISST.setvPIS(Decimal.multiplicar(infPISST.getqBCProd(), infPISST.getvAliqProd()).toString());
            }
        }
        final InformacoesCOFINS infCOFINS;
        if ((infCOFINS = this.imposto.getCOFINS()) != null) {
            final COFINSAliq COFINSAliq;
            if ((COFINSAliq = infCOFINS.getCOFINSAliq()) != null) {
                COFINSAliq.setvCOFINS(Decimal.multiplicar(COFINSAliq.getvBC(), COFINSAliq.getpCOFINS()).toString());
            }
            else {
                final COFINSQtde COFINSQtde;
                if ((COFINSQtde = infCOFINS.getCOFINSQTde()) != null) {
                    COFINSQtde.setvCOFINS(Decimal.multiplicar(COFINSQtde.getqBCProd(), COFINSQtde.getvAliqProd()).toString());
                }
                else {
                    final COFINSOutr COFINSOutr;
                    if ((COFINSOutr = infCOFINS.getCOFINSOutr()) != null) {
                        if (COFINSOutr.getvBC() != null && COFINSOutr.getpCOFINS() != null) {
                            COFINSOutr.setvCOFINS(Decimal.multiplicar(COFINSOutr.getvBC(), COFINSOutr.getpCOFINS()).toString());
                        }
                        else if (COFINSOutr.getqBCProd() != null && COFINSOutr.getvAliqProd() != null) {
                            COFINSOutr.setvCOFINS(Decimal.multiplicar(COFINSOutr.getqBCProd(), COFINSOutr.getvAliqProd()).toString());
                        }
                    }
                }
            }
        }
        final InformacoesCOFINSST infCOFINSST;
        if ((infCOFINSST = this.imposto.getCOFINSST()) != null) {
            if (infCOFINSST.getvBC() != null && infCOFINSST.getpCOFINS() != null) {
                infCOFINSST.setvCOFINS(Decimal.multiplicar(infCOFINSST.getvBC(), infCOFINSST.getpCOFINS()).toString());
            }
            else if (infCOFINSST.getqBCProd() != null && infCOFINSST.getvAliqProd() != null) {
                infCOFINSST.setvCOFINS(Decimal.multiplicar(infCOFINSST.getqBCProd(), infCOFINSST.getvAliqProd()).toString());
            }
        }
        final InformacoesISSQN infISSQN;
        if ((infISSQN = this.imposto.getISSQN()) != null) {
            infISSQN.setvBC(Decimal.subtrair(this.prod.getvItem(), infISSQN.getvDeducISSQN()).toString());
            infISSQN.setvISSQN(Decimal.dividir(Decimal.multiplicar(infISSQN.getvBC(), infISSQN.getvAliq()), new BigDecimal("100")).toString());
        }
        this.imposto.completar();
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.prod != null) {
            retorno.append("<prod>");
            this.prod.toString(retorno);
            retorno.append("</prod>");
        }
        if (this.imposto != null) {
            retorno.append("<imposto>");
            this.imposto.toString(retorno);
            retorno.append("</imposto>");
        }
        if (this.infAdProd != null) {
            retorno.append("<infAdProd>").append(this.infAdProd).append("</infAdProd>");
        }
    }
}
