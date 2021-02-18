// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.modelos.ABNT5891;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.um.controles.ControleDados;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class ValoresTotaisCFe
{
    private ICMSTotal ICMSTot;
    private ISSQNtot ISSQNtot;
    private DescontosAcrescimosTotal descAcrEntr;
    private String vCFeLei12741;
    private String vCFe;
    
    public ValoresTotaisCFe(final Node no) {
        this.ICMSTot = null;
        this.ISSQNtot = null;
        this.descAcrEntr = null;
        this.vCFeLei12741 = null;
        this.vCFe = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("DescAcrEntr")) {
                this.descAcrEntr = new DescontosAcrescimosTotal(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vCFeLei12741")) {
                this.vCFeLei12741 = filhoAtual.getTextContent();
            }
        }
    }
    
    public ICMSTotal getICMSTot() {
        return this.ICMSTot;
    }
    
    public void setICMSTot(final ICMSTotal ICMSTotDfe) {
        this.ICMSTot = ICMSTotDfe;
    }
    
    public ISSQNtot getISSQNtot() {
        return this.ISSQNtot;
    }
    
    public void setISSQNtot(final ISSQNtot ISSQNtotCfe) {
        this.ISSQNtot = ISSQNtotCfe;
    }
    
    public DescontosAcrescimosTotal getDescAcrEntr() {
        return this.descAcrEntr;
    }
    
    public void setDescAcrEntr(final DescontosAcrescimosTotal descAcrEntrCfe) {
        this.descAcrEntr = descAcrEntrCfe;
    }
    
    public void setvCFe(final String vCFe) {
        this.vCFe = vCFe;
    }
    
    public String getvCFe() {
        return this.vCFe;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.descAcrEntr == null || (resultado = this.descAcrEntr.validar()).equals("1000")) {
            if (this.vCFeLei12741 != null && !ControleDados.validarDouble(1, 15, 2, this.vCFeLei12741)) {
                ControleLogs.logar("Erro no campo 'vCFeLei12741' : Campo inv\u00e1lido.");
                resultado = "1999";
            }
            else if (this.vCFeLei12741 != null && Double.parseDouble(this.vCFeLei12741) < 0.0) {
                ControleLogs.logar("Erro no campo 'vCFeLei12741': Campo menor que 0 (zero).");
                resultado = "1533";
            }
        }
        return resultado;
    }
    
    public void completar() {
        if (this.vCFe != null) {
            this.vCFe = new ABNT5891(this.vCFe).arredondarNBR(2).toString();
        }
        if (this.vCFeLei12741 != null) {
            this.vCFeLei12741 = new ABNT5891(this.vCFeLei12741).arredondarNBR(2).toString();
        }
        if (this.ICMSTot != null) {
            this.ICMSTot.completar();
        }
        if (this.ISSQNtot != null) {
            this.ISSQNtot.completar();
        }
        if (this.descAcrEntr != null) {
            this.descAcrEntr.completar();
        }
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.ICMSTot != null) {
            retorno.append("<ICMSTot>");
            this.ICMSTot.toString(retorno);
            retorno.append("</ICMSTot>");
        }
        if (this.vCFe != null) {
            retorno.append("<vCFe>").append(this.vCFe).append("</vCFe>");
        }
        if (this.ISSQNtot != null) {
            retorno.append("<ISSQNtot>");
            this.ISSQNtot.toString(retorno);
            retorno.append("</ISSQNtot>");
        }
        if (this.descAcrEntr != null) {
            retorno.append("<DescAcrEntr>");
            this.descAcrEntr.toString(retorno);
            retorno.append("</DescAcrEntr>");
        }
        if (this.vCFeLei12741 != null) {
            retorno.append("<vCFeLei12741>");
            retorno.append(this.vCFeLei12741);
            retorno.append("</vCFeLei12741>");
        }
    }
}
