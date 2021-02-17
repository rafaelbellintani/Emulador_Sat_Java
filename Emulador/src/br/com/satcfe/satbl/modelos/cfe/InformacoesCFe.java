// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import java.security.PrivateKey;
import br.com.um.controles.ControleKeyStore;
import br.com.satcfe.satbl.controles.ControleArquivos;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;
import br.com.um.modelos.ABNT5891;
import br.com.um.modelos.Decimal;
import java.math.BigDecimal;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import org.w3c.dom.Node;
import java.util.List;

public class InformacoesCFe
{
    private String versao;
    private String versaoDadosEnt;
    private String versaoSB;
    private String id;
    private IdentificacaoCFe ide;
    private IdentificacaoEmitenteCFe emit;
    private IdentificacaoDestinatarioCFe dest;
    private IdentificacaoLocalEntrega entrega;
    private List<DetalhamentoProdutosCFe> det;
    private ValoresTotaisCFe total;
    private Pgto pgto;
    private InformacoesAdicionaisCFe infAdic;
    private List<ObsFisco> obsFisco;
    
    public InformacoesCFe(final Node no) {
        this.versao = null;
        this.versaoDadosEnt = null;
        this.versaoSB = null;
        this.id = null;
        this.ide = null;
        this.emit = null;
        this.dest = null;
        this.entrega = null;
        this.det = null;
        this.total = null;
        this.pgto = null;
        this.infAdic = null;
        this.obsFisco = null;
        this.det = new ArrayList<DetalhamentoProdutosCFe>();
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("ide")) {
                this.ide = new IdentificacaoCFe(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("emit")) {
                this.emit = new IdentificacaoEmitenteCFe(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("dest")) {
                this.dest = new IdentificacaoDestinatarioCFe(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("entrega")) {
                this.entrega = new IdentificacaoLocalEntrega(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("det")) {
                final DetalhamentoProdutosCFe detAtual = new DetalhamentoProdutosCFe(filhoAtual);
                this.det.add(detAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("total")) {
                this.total = new ValoresTotaisCFe(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("pgto")) {
                this.pgto = new Pgto(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("infAdic")) {
                this.infAdic = new InformacoesAdicionaisCFe(filhoAtual);
            }
        }
        final NamedNodeMap atributos = no.getAttributes();
        for (int j = 0; j < atributos.getLength(); ++j) {
            final Node filhoAtual2 = atributos.item(j);
            if (filhoAtual2.getNodeName().equalsIgnoreCase("versaoDadosEnt")) {
                this.versaoDadosEnt = filhoAtual2.getTextContent();
            }
        }
    }
    
    public String getVersao() {
        return this.versao;
    }
    
    public void setVersao(final String versao) {
        this.versao = versao;
    }
    
    public String getVersaoDadosEnt() {
        return this.versaoDadosEnt;
    }
    
    public void setVersaoDadosEnt(final String versaoDadosEnt) {
        this.versaoDadosEnt = versaoDadosEnt;
    }
    
    public String getVersaoSB() {
        return this.versaoSB;
    }
    
    public void setVersaoSB(final String versaoSB) {
        this.versaoSB = versaoSB;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public IdentificacaoCFe getIde() {
        return this.ide;
    }
    
    public void setIde(final IdentificacaoCFe ideCFe) {
        this.ide = ideCFe;
    }
    
    public IdentificacaoEmitenteCFe getEmit() {
        return this.emit;
    }
    
    public void setEmit(final IdentificacaoEmitenteCFe emitCfe) {
        this.emit = emitCfe;
    }
    
    public IdentificacaoDestinatarioCFe getDest() {
        return this.dest;
    }
    
    public void setDest(final IdentificacaoDestinatarioCFe destCfe) {
        this.dest = destCfe;
    }
    
    public IdentificacaoLocalEntrega getEntrega() {
        return this.entrega;
    }
    
    public void setEntrega(final IdentificacaoLocalEntrega entregaCfe) {
        this.entrega = entregaCfe;
    }
    
    public List<DetalhamentoProdutosCFe> getDet() {
        return this.det;
    }
    
    public ValoresTotaisCFe getTotal() {
        return this.total;
    }
    
    public void setTotal(final ValoresTotaisCFe totalCFe) {
        this.total = totalCFe;
    }
    
    public InformacoesAdicionaisCFe getInfAdic() {
        return this.infAdic;
    }
    
    public void setInfAdic(final InformacoesAdicionaisCFe infAdicCfe) {
        this.infAdic = infAdicCfe;
    }
    
    public Pgto getPgto() {
        return this.pgto;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.ide == null) {
            ControleLogs.logar("campo 'ide' inexistente");
            resultado = "1999";
        }
        else if (this.emit == null) {
            ControleLogs.logar("campo 'emit' inexistente");
            resultado = "1999";
        }
        else if (this.dest == null) {
            ControleLogs.logar("campo 'dest' inexistente");
            resultado = "1999";
        }
        else if (this.entrega == null || (resultado = this.entrega.validar()).equals("1000")) {
            if (this.det.size() <= 0) {
                ControleLogs.logar("campo 'det' inv\u00e1lido");
                resultado = "1999";
            }
            else if (this.det.size() > 500) {
                ControleLogs.logar("Erro: Excedido o N\u00famero maximo de itens. ");
                resultado = "1999";
            }
            else if (this.total == null) {
                ControleLogs.logar("campo 'total' inv\u00e1lido");
                resultado = "1999";
            }
            else if (this.pgto == null) {
                ControleLogs.logar("campo 'pgto' inv\u00e1lido");
                resultado = "1999";
            }
            else if (this.infAdic == null || (resultado = this.infAdic.validar()).equals("1000")) {
                if (!ControleDados.isNumerico(this.versaoDadosEnt)) {
                    resultado = "1004";
                    ControleLogs.logar("Erro no campo 'versaoDadosEnt'");
                }
                else if (!(resultado = this.ide.validar()).equals("1000")) {
                    ControleLogs.logar("Erro no campo 'ide': " + resultado);
                }
                else if (!(resultado = this.emit.validar()).equals("1000")) {
                    ControleLogs.logar("Erro no campo 'emit': " + resultado);
                }
                else if (!(resultado = this.dest.validar()).equals("1000")) {
                    ControleLogs.logar("Erro no campo 'dest'");
                }
                else if (!(resultado = this.total.validar()).equals("1000")) {
                    ControleLogs.logar("Erro no campo 'total'");
                }
                else if (!(resultado = this.pgto.validar()).equals("1000")) {
                    ControleLogs.logar("Erro no campo 'pgto'");
                }
                else {
                    for (int i = 0; i < this.det.size() && (resultado = this.det.get(i).validar()).equals("1000"); ++i) {}
                    if (resultado.equals("1000")) {
                        for (int i = 0, n = Integer.parseInt(this.det.get(0).getNItem()); i < this.det.size(); ++i, ++n) {
                            if (this.det.get(i).getNItem() != null && Integer.parseInt(this.det.get(i).getNItem()) != n) {
                                resultado = "1019";
                                break;
                            }
                        }
                    }
                    if (resultado.equals("1000") && this.emit.getIM() == null) {
                        for (int i = 0; i < this.det.size(); ++i) {
                            if (this.det.get(i).getImposto() != null && this.det.get(i).getImposto().getISSQN() != null) {
                                resultado = "1501";
                                break;
                            }
                        }
                    }
                }
            }
        }
        return resultado;
    }
    
    public void completar(final int tipo, final String senhaKeyStore) {
        BigDecimal vTotalSemRateio_desc = new BigDecimal("0");
        BigDecimal vTotalSemRateio_acr = new BigDecimal("0");
        for (int i = 0; i < this.det.size(); ++i) {
            final ProdutoCFe p = this.det.get(i).getProd();
            BigDecimal vProd = null;
            if (p.getIndRegra().equalsIgnoreCase("T")) {
                vProd = new ABNT5891(Decimal.multiplicar(p.getqCom(), p.getvUnCom())).truncar(2);
            }
            else {
                vProd = new ABNT5891(Decimal.multiplicar(p.getqCom(), p.getvUnCom())).arredondarNBR(2);
            }
            final BigDecimal vItem = new ABNT5891(Decimal.somar(Decimal.subtrair(vProd, new BigDecimal((p.getvDesc() == null) ? "0" : p.getvDesc())), new BigDecimal((p.getvOutro() == null) ? "0" : p.getvOutro()))).arredondarNBR(2);
            this.det.get(i).getProd().setvItem(vItem.toString());
            this.det.get(i).getProd().setvProd(vProd.toString());
            vTotalSemRateio_acr = new ABNT5891(Decimal.somar(vTotalSemRateio_acr, vItem)).arredondarNBR(2);
            if (!this.emit.getIndRatISSQN().equalsIgnoreCase("N") || this.det.get(i).getImposto().getISSQN() == null || this.emit.getIndRatISSQN() == null) {
                vTotalSemRateio_desc = new ABNT5891(Decimal.somar(vTotalSemRateio_desc, vItem)).arredondarNBR(2);
            }
        }
        BigDecimal vAcresSubtot = new BigDecimal("0");
        if (this.total.getDescAcrEntr() != null && this.total.getDescAcrEntr().getvAcresSubtot() != null) {
            vAcresSubtot = new BigDecimal(this.total.getDescAcrEntr().getvAcresSubtot());
        }
        BigDecimal vDescSubtot = new BigDecimal("0");
        if (this.total.getDescAcrEntr() != null && this.total.getDescAcrEntr().getvDescSubtot() != null) {
            vDescSubtot = new BigDecimal(this.total.getDescAcrEntr().getvDescSubtot());
        }
        BigDecimal totalRatDesc = new BigDecimal("0");
        BigDecimal maiorDesc = new BigDecimal("0");
        BigDecimal totalRatAcres = new BigDecimal("0");
        BigDecimal maiorAcres = new BigDecimal("0");
        int indiceDesc = 0;
        int indiceAcre = 0;
        boolean ratearISSQN = true;
        boolean existeItemParaRateio = false;
        for (int j = 0; j < this.det.size(); ++j, ratearISSQN = true) {
            if (this.det.get(j).getImposto().getISSQN() != null && this.emit.getIndRatISSQN() != null && this.emit.getIndRatISSQN().equalsIgnoreCase("N")) {
                ratearISSQN = false;
            }
            if (!existeItemParaRateio) {
                existeItemParaRateio = ratearISSQN;
            }
            final ProdutoCFe p2 = this.det.get(j).getProd();
            BigDecimal valor = null;
            if (ratearISSQN) {
                valor = new ABNT5891(Decimal.dividir(Decimal.multiplicar(new BigDecimal(p2.getvItem()), vDescSubtot), vTotalSemRateio_desc)).arredondarNBR(2);
                this.det.get(j).getProd().setvRatDesc(valor.toString());
                totalRatDesc = Decimal.somar(totalRatDesc, valor);
                if (valor.doubleValue() > maiorDesc.doubleValue()) {
                    maiorDesc = new BigDecimal(valor.toString());
                    indiceDesc = j;
                }
            }
            valor = new BigDecimal("0");
            valor = new ABNT5891(Decimal.dividir(Decimal.multiplicar(p2.getvItem(), vAcresSubtot.toString()), vTotalSemRateio_acr)).arredondarNBR(2);
            this.det.get(j).getProd().setvRatAcr(valor.toString());
            totalRatAcres = Decimal.somar(totalRatAcres, valor);
            if (valor.doubleValue() > maiorAcres.doubleValue()) {
                maiorAcres = new BigDecimal(valor.toString());
                indiceAcre = j;
            }
        }
        if (vAcresSubtot != totalRatAcres) {
            BigDecimal v = Decimal.subtrair(vAcresSubtot, totalRatAcres);
            v = Decimal.somar(v.toString(), this.det.get(indiceAcre).getProd().getvRatAcr());
            this.det.get(indiceAcre).getProd().setvRatAcr(new ABNT5891(v).arredondarNBR(2).toString());
        }
        if (vDescSubtot != totalRatDesc && existeItemParaRateio) {
            BigDecimal v = Decimal.subtrair(vDescSubtot, totalRatDesc);
            v = Decimal.somar(v.toString(), this.det.get(indiceDesc).getProd().getvRatDesc());
            this.det.get(indiceDesc).getProd().setvRatDesc(new ABNT5891(v).arredondarNBR(2).toString());
        }
        for (int j = 0; j < this.det.size(); ++j) {
            final ProdutoCFe p2 = this.det.get(j).getProd();
            BigDecimal vProd2 = null;
            if (p2.getIndRegra().equalsIgnoreCase("T")) {
                vProd2 = new ABNT5891(Decimal.multiplicar(p2.getqCom(), p2.getvUnCom())).truncar(2);
            }
            else {
                vProd2 = new ABNT5891(Decimal.multiplicar(p2.getqCom(), p2.getvUnCom())).arredondarNBR(2);
            }
            final BigDecimal vRatDesc = new BigDecimal((p2.getvRatDesc() == null) ? "0" : p2.getvRatDesc());
            final BigDecimal vRatAcr = new BigDecimal((p2.getvRatAcr() == null) ? "0" : p2.getvRatAcr());
            final BigDecimal vDesc = new BigDecimal((p2.getvDesc() == null) ? "0" : p2.getvDesc());
            final BigDecimal vOutro = new BigDecimal((p2.getvOutro() == null) ? "0" : p2.getvOutro());
            final BigDecimal vItem2 = new ABNT5891(Decimal.somar(Decimal.subtrair(Decimal.somar(Decimal.subtrair(vProd2, vDesc), vOutro), vRatDesc), vRatAcr)).arredondarNBR(2);
            this.det.get(j).getProd().setvItem(vItem2.toString());
        }
        this.ide.completar(tipo);
        this.emit.completar();
        for (int j = 0; j < this.det.size(); ++j) {
            this.det.get(j).completar();
        }
        this.versao = Configuracoes.SAT.VERSAO_LAYOUT_CFE;
        this.versaoSB = "000003";
        final String data = this.ide.getdEmi();
        final StringBuffer chave = new StringBuffer("CFe");
        chave.append(this.ide.getcUF()).append(data.substring(2, 4)).append(data.substring(4, 6));
        chave.append(Parametrizacoes.CNPJ).append(this.ide.getMod()).append(this.ide.getNserieSAT());
        chave.append(this.ide.getnCFe()).append(this.ide.getcNF()).append(this.ide.getcDV());
        this.id = chave.toString();
        for (int k = 0; k < this.det.size(); ++k) {
            final TributosCFe tCFe = this.det.get(k).getImposto();
            final ProdutoCFe pCFe = this.det.get(k).getProd();
            final boolean possuiImpostoICMS = tCFe.getICMS() != null;
            if (this.total.getICMSTot() == null && (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS))) {
                this.total.setICMSTot(new ICMSTotal());
                this.total.getICMSTot().setvICMS("0");
                this.total.getICMSTot().setvProd("0");
                this.total.getICMSTot().setvDesc("0");
                this.total.getICMSTot().setvPIS("0");
                this.total.getICMSTot().setvCOFINS("0");
                this.total.getICMSTot().setvPISST("0");
                this.total.getICMSTot().setvCOFINSST("0");
                this.total.getICMSTot().setvOutro("0");
            }
            if (this.total.getvCFe() == null) {
                this.total.setvCFe("0");
            }
            if (this.total.getICMSTot() != null && (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS))) {
                this.total.getICMSTot().setvProd(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvProd(), pCFe.getvProd())).arredondarNBR(2).toString());
                if (pCFe.getvDesc() != null) {
                    this.total.getICMSTot().setvDesc(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvDesc(), pCFe.getvDesc())).arredondarNBR(2).toString());
                }
                if (pCFe.getvOutro() != null) {
                    this.total.getICMSTot().setvOutro(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvOutro(), pCFe.getvOutro())).arredondarNBR(2).toString());
                }
            }
            this.total.setvCFe(new ABNT5891(Decimal.somar(this.total.getvCFe(), pCFe.getvItem())).arredondarNBR(2).toString());
            final InformacoesISSQN infISSQN = tCFe.getISSQN();
            if (infISSQN != null) {
                if (this.total.getISSQNtot() == null) {
                    this.total.setISSQNtot(new ISSQNtot());
                    this.total.getISSQNtot().setvBC("0");
                    this.total.getISSQNtot().setvISS("0");
                    this.total.getISSQNtot().setvPIS("0");
                    this.total.getISSQNtot().setvCOFINS("0");
                    this.total.getISSQNtot().setvCOFINSST("0");
                    this.total.getISSQNtot().setvPISST("0");
                }
                this.total.getISSQNtot().setvBC(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvBC(), infISSQN.getvBC())).arredondarNBR(2).toString());
                this.total.getISSQNtot().setvISS(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvISS(), infISSQN.getvISSQN())).arredondarNBR(2).toString());
            }
            final InformacoesICMS infICMS;
            if ((infICMS = tCFe.getICMS()) != null && (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS))) {
                final ICMS00 icms00;
                if ((icms00 = infICMS.getICMS00()) != null) {
                    this.total.getICMSTot().setvICMS(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvICMS(), icms00.getvICMS())).arredondarNBR(2).toString());
                }
                else {
                    final ICMSSN900 icmssn900;
                    if ((icmssn900 = infICMS.getICMSSN900()) != null) {
                        this.total.getICMSTot().setvICMS(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvICMS(), icmssn900.getvICMS())).arredondarNBR(2).toString());
                    }
                }
            }
            final InformacoesPIS infPIS;
            if ((infPIS = tCFe.getPIS()) != null) {
                final PISAliq pisAliq;
                if ((pisAliq = infPIS.getPISAliq()) != null) {
                    if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                        this.total.getICMSTot().setvPIS(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvPIS(), pisAliq.getvPIS())).arredondarNBR(2).toString());
                    }
                    if (infISSQN != null) {
                        this.total.getISSQNtot().setvPIS(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvPIS(), pisAliq.getvPIS())).arredondarNBR(2).toString());
                    }
                }
                else {
                    final PISQtde pisQtde;
                    if ((pisQtde = infPIS.getPISQTde()) != null) {
                        if (infISSQN != null) {
                            this.total.getISSQNtot().setvPIS(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvPIS(), pisQtde.getvPIS())).arredondarNBR(2).toString());
                        }
                        if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                            this.total.getICMSTot().setvPIS(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvPIS(), pisQtde.getvPIS())).arredondarNBR(2).toString());
                        }
                    }
                    else {
                        final PISOutr pisOutr;
                        if ((pisOutr = infPIS.getPISOutr()) != null) {
                            if (infISSQN != null) {
                                this.total.getISSQNtot().setvPIS(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvPIS(), pisOutr.getvPIS())).arredondarNBR(2).toString());
                            }
                            if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                                this.total.getICMSTot().setvPIS(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvPIS(), pisOutr.getvPIS())).arredondarNBR(2).toString());
                            }
                        }
                    }
                }
            }
            final InformacoesPISST infPISST;
            if ((infPISST = tCFe.getPISST()) != null) {
                if (infPISST.getvBC() != null && infPISST.getpPIS() != null) {
                    if (infISSQN != null) {
                        this.total.getISSQNtot().setvPISST(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvPISST(), infPISST.getvPIS())).arredondarNBR(2).toString());
                    }
                    if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                        this.total.getICMSTot().setvPISST(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvPISST(), infPISST.getvPIS())).arredondarNBR(2).toString());
                    }
                }
                else if (infPISST.getqBCProd() != null && infPISST.getvAliqProd() != null) {
                    if (infISSQN != null) {
                        this.total.getISSQNtot().setvPISST(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvPISST(), infPISST.getvPIS())).arredondarNBR(2).toString());
                    }
                    if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                        this.total.getICMSTot().setvPISST(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvPISST(), infPISST.getvPIS())).arredondarNBR(2).toString());
                    }
                }
            }
            final InformacoesCOFINS infCOFINS;
            if ((infCOFINS = tCFe.getCOFINS()) != null) {
                final COFINSAliq cofinsAliq;
                if ((cofinsAliq = infCOFINS.getCOFINSAliq()) != null) {
                    if (infISSQN != null) {
                        this.total.getISSQNtot().setvCOFINS(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvCOFINS(), cofinsAliq.getvCOFINS())).arredondarNBR(2).toString());
                    }
                    if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                        this.total.getICMSTot().setvCOFINS(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvCOFINS(), cofinsAliq.getvCOFINS())).arredondarNBR(2).toString());
                    }
                }
                else {
                    final COFINSQtde cofinsQtde;
                    if ((cofinsQtde = infCOFINS.getCOFINSQTde()) != null) {
                        if (infISSQN != null) {
                            this.total.getISSQNtot().setvCOFINS(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvCOFINS(), cofinsQtde.getvCOFINS())).arredondarNBR(2).toString());
                        }
                        if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                            this.total.getICMSTot().setvCOFINS(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvCOFINS(), cofinsQtde.getvCOFINS())).arredondarNBR(2).toString());
                        }
                    }
                    else {
                        final COFINSOutr cofinsOutr;
                        if ((cofinsOutr = infCOFINS.getCOFINSOutr()) != null) {
                            if (infISSQN != null) {
                                this.total.getISSQNtot().setvCOFINS(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvCOFINS(), cofinsOutr.getvCOFINS())).arredondarNBR(2).toString());
                            }
                            if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                                this.total.getICMSTot().setvCOFINS(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvCOFINS(), cofinsOutr.getvCOFINS())).arredondarNBR(2).toString());
                            }
                        }
                    }
                }
            }
            final InformacoesCOFINSST infCOFINSST;
            if ((infCOFINSST = tCFe.getCOFINSST()) != null) {
                if (infCOFINSST.getvBC() != null && infCOFINSST.getpCOFINS() != null) {
                    if (infISSQN != null) {
                        this.total.getISSQNtot().setvCOFINSST(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvCOFINSST(), infCOFINSST.getvCOFINS())).arredondarNBR(2).toString());
                    }
                    if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                        this.total.getICMSTot().setvCOFINSST(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvCOFINSST(), infCOFINSST.getvCOFINS())).arredondarNBR(2).toString());
                    }
                }
                else if (infCOFINSST.getqBCProd() != null && infCOFINSST.getvAliqProd() != null) {
                    if (infISSQN != null) {
                        this.total.getISSQNtot().setvCOFINSST(new ABNT5891(Decimal.somar(this.total.getISSQNtot().getvCOFINSST(), infCOFINSST.getvCOFINS())).arredondarNBR(2).toString());
                    }
                    if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 || (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && possuiImpostoICMS)) {
                        this.total.getICMSTot().setvCOFINSST(new ABNT5891(Decimal.somar(this.total.getICMSTot().getvCOFINSST(), infCOFINSST.getvCOFINS())).arredondarNBR(2).toString());
                    }
                }
            }
        }
        this.pgto.calcularVTroco(this.total.getvCFe());
        String cpfCnpj = "";
        if ((cpfCnpj = this.getDest().getCNPJ()) == null) {
            cpfCnpj = this.getDest().getCPF();
        }
        if (cpfCnpj == null || cpfCnpj.length() < 11) {
            cpfCnpj = "";
        }
        final String campoQRCode = this.gerarCampoQRCode(this.id, String.valueOf(this.ide.getdEmi()) + this.ide.gethEmi(), this.total.getvCFe(), cpfCnpj);
        ControleArquivos.escreverCaracteresArquivo(String.valueOf(Configuracoes.SistemaArquivos.DIRETORIO_DEBUG) + "qrCode.txt", campoQRCode.toCharArray());
        try {
            final ControleKeyStore keyStore = new ControleKeyStore(Configuracoes.SistemaArquivos.CAMINHO_ARQUIVO_KEYSTORE, senhaKeyStore);
            keyStore.carregarKeyStore();
            String assinaturaQRCode = keyStore.gerarAssinaturaSHA256((PrivateKey)keyStore.getPrivateKey(), campoQRCode.getBytes());
            if (assinaturaQRCode.length() == 344 && Configuracoes.SAT.VERSAO_LAYOUT_CFE.equals("0.03")) {
                assinaturaQRCode = String.valueOf(assinaturaQRCode) + "hygljwuohmmoewarfnmighlxzke7k2bjlto4sb2vltorgm26khhangknnfvpzydt5terudyw5vuvtzlhlqs3qrzvplfnlvw==";
            }
            this.ide.setQrCode(assinaturaQRCode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.total.completar();
        this.pgto.completar();
        if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 && Parametrizacoes.obsFisco != null) {
            if (this.infAdic == null) {
                this.infAdic = new InformacoesAdicionaisCFe();
            }
            this.infAdic.setObsFisco(this.obsFisco = Parametrizacoes.obsFisco);
        }
        if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && Parametrizacoes.obsFisco != null) {
            this.obsFisco = Parametrizacoes.obsFisco;
        }
    }
    
    private String gerarCampoQRCode(final String chave, final String timeStamp, final String valor, final String cpfCnpj) {
        final StringBuffer s = new StringBuffer();
        s.append(chave.replace("CFe", "")).append("|");
        s.append(timeStamp).append("|");
        s.append(valor).append("|");
        s.append(cpfCnpj);
        return s.toString();
    }
    
    public void toString(final StringBuffer sb) {
        if (this.versao != null) {
            sb.append("<infCFe versao=\"").append(ControleDados.formatarDouble(this.versao, 1, 4, 2)).append("\" ");
        }
        else {
            sb.append("<infCFe ");
        }
        if (this.versaoDadosEnt != null) {
            sb.append("versaoDadosEnt=\"").append(this.versaoDadosEnt).append("\" ");
        }
        if (this.versaoSB != null) {
            sb.append("versaoSB=\"").append("000003").append("\" ");
        }
        if (this.id != null) {
            sb.append("Id=\"").append(this.id).append("\">");
        }
        else {
            sb.append(">");
        }
        if (this.ide != null) {
            sb.append("<ide>");
            this.ide.toString(sb);
            sb.append("</ide>");
        }
        if (this.emit != null) {
            sb.append("<emit>");
            this.emit.toString(sb);
            sb.append("</emit>");
        }
        if (this.dest != null) {
            sb.append("<dest>");
            this.dest.toString(sb);
            sb.append("</dest>");
        }
        if (this.entrega != null) {
            sb.append("<entrega>");
            this.entrega.toString(sb);
            sb.append("</entrega>");
        }
        for (int i = 0; i < this.det.size(); ++i) {
            sb.append("<det nItem=\"").append(this.det.get(i).getNItem()).append("\">");
            this.det.get(i).toString(sb);
            sb.append("</det>");
        }
        if (this.total != null) {
            sb.append("<total>");
            this.total.toString(sb);
            sb.append("</total>");
        }
        if (this.pgto != null) {
            sb.append("<pgto>");
            sb.append(this.pgto.toString());
            sb.append("</pgto>");
        }
        if (this.infAdic != null) {
            sb.append("<infAdic>");
            sb.append(this.infAdic.toString());
            sb.append("</infAdic>");
        }
        if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.08 && this.obsFisco != null) {
            for (int i = 0; i < this.obsFisco.size(); ++i) {
                sb.append("<obsFisco xCampo=\"").append(this.obsFisco.get(i).getxCampo()).append("\">");
                sb.append("<xTexto>");
                sb.append(this.obsFisco.get(i).getxTexto());
                sb.append("</xTexto>");
                sb.append("</obsFisco>");
            }
        }
        sb.append("</infCFe>");
    }
}
