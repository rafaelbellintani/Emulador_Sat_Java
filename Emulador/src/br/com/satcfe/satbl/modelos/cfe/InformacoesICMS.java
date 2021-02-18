// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.modelos.ABNT5891;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class InformacoesICMS
{
    private ICMS00 ICMS00;
    private ICMS40 ICMS40;
    private ICMSSN102 ICMSSN102;
    private ICMSSN900 ICMSSN900;
    
    public InformacoesICMS(final Node no) {
        this.ICMS00 = null;
        this.ICMS40 = null;
        this.ICMSSN102 = null;
        this.ICMSSN900 = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("ICMS00")) {
                this.ICMS00 = new ICMS00(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("ICMS40")) {
                this.ICMS40 = new ICMS40(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("ICMSSN102")) {
                this.ICMSSN102 = new ICMSSN102(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("ICMSSN900")) {
                this.ICMSSN900 = new ICMSSN900(filhoAtual);
            }
        }
    }
    
    public ICMS00 getICMS00() {
        return this.ICMS00;
    }
    
    public void setICMS00(final ICMS00 ICMS00CFe) {
        this.ICMS00 = ICMS00CFe;
    }
    
    public ICMS40 getICMS40() {
        return this.ICMS40;
    }
    
    public void setICMS40(final ICMS40 ICMS40CFe) {
        this.ICMS40 = ICMS40CFe;
    }
    
    public ICMSSN102 getICMSSN102() {
        return this.ICMSSN102;
    }
    
    public void getICMSSN102(final ICMSSN102 ICMSSN102CFe) {
        this.ICMSSN102 = ICMSSN102CFe;
    }
    
    public ICMSSN900 getICMSSN900() {
        return this.ICMSSN900;
    }
    
    public void getICMSSN900(final ICMSSN900 ICMSSN900CFe) {
        this.ICMSSN900 = ICMSSN900CFe;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.ICMS00 == null && this.ICMS40 == null && this.ICMSSN102 == null && this.ICMSSN900 == null) {
            ControleLogs.logar("Campo 'ICMS00', 'ICMS40', 'ICMSSN102' ou 'ICMSSN900' n\u00e3o informado.");
            resultado = "1999";
        }
        else if (this.ICMS00 != null && (this.ICMS40 != null || this.ICMSSN102 != null || this.ICMSSN900 != null)) {
            ControleLogs.logar("Somente um dos campos 'ICMS00', 'ICMS40', 'ICMSSN102' ou 'ICMSSN900' deve ser informado.");
            resultado = "1999";
        }
        else if (this.ICMS40 != null && (this.ICMS00 != null || this.ICMSSN102 != null || this.ICMSSN900 != null)) {
            ControleLogs.logar("Somente um dos campos 'ICMS00', 'ICMS40', 'ICMSSN102' ou 'ICMSSN900' deve ser informado.");
            resultado = "1999";
        }
        else if (this.ICMSSN102 != null && (this.ICMS00 != null || this.ICMS40 != null || this.ICMSSN900 != null)) {
            ControleLogs.logar("Somente um dos campos 'ICMS00', 'ICMS40', 'ICMSSN102' ou 'ICMSSN900' deve ser informado.");
            resultado = "1999";
        }
        else if (this.ICMSSN900 != null && (this.ICMS00 != null || this.ICMS40 != null || this.ICMSSN102 != null)) {
            ControleLogs.logar("Um dos campos 'ICMS00', 'ICMS40', 'ICMSSN102' ou 'ICMSSN900' deve ser informado.");
            resultado = "1999";
        }
        else if ((this.ICMS00 == null || (resultado = this.ICMS00.validar()).equals("1000")) && (this.ICMS40 == null || (resultado = this.ICMS40.validar()).equals("1000")) && (this.ICMSSN102 == null || (resultado = this.ICMSSN102.validar()).equals("1000")) && this.ICMSSN900 != null) {
            (resultado = this.ICMSSN900.validar()).equals("1000");
        }
        return resultado;
    }
    
    public void completar() {
        if (this.ICMS00 != null) {
            if (this.ICMS00.getpICMS() != null) {
                this.ICMS00.setpICMS(new ABNT5891(this.ICMS00.getpICMS()).arredondarNBR(2).toString());
            }
            if (this.ICMS00.getvICMS() != null) {
                this.ICMS00.setvICMS(new ABNT5891(this.ICMS00.getvICMS()).arredondarNBR(2).toString());
            }
        }
        if (this.ICMSSN900 != null) {
            if (this.ICMSSN900.getpICMS() != null) {
                this.ICMSSN900.setpICMS(new ABNT5891(this.ICMSSN900.getpICMS()).arredondarNBR(2).toString());
            }
            if (this.ICMSSN900.getvICMS() != null) {
                this.ICMSSN900.setvICMS(new ABNT5891(this.ICMSSN900.getvICMS()).arredondarNBR(2).toString());
            }
        }
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.ICMS00 != null) {
            retorno.append("<ICMS00>");
            this.ICMS00.toString(retorno);
            retorno.append("</ICMS00>");
        }
        if (this.ICMS40 != null) {
            retorno.append("<ICMS40>");
            this.ICMS40.toString(retorno);
            retorno.append("</ICMS40>");
        }
        if (this.ICMSSN102 != null) {
            retorno.append("<ICMSSN102>");
            this.ICMSSN102.toString(retorno);
            retorno.append("</ICMSSN102>");
        }
        if (this.ICMSSN900 != null) {
            retorno.append("<ICMSSN900>");
            this.ICMSSN900.toString(retorno);
            retorno.append("</ICMSSN900>");
        }
    }
}
