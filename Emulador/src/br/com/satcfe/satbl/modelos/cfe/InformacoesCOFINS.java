// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.modelos.ABNT5891;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class InformacoesCOFINS
{
    private COFINSAliq COFINSAliq;
    private COFINSQtde COFINSQtde;
    private COFINSNT COFINSNT;
    private COFINSSN COFINSSN;
    private COFINSOutr COFINSOutr;
    
    public InformacoesCOFINS(final Node no) {
        this.COFINSAliq = null;
        this.COFINSQtde = null;
        this.COFINSNT = null;
        this.COFINSSN = null;
        this.COFINSOutr = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("COFINSAliq")) {
                this.COFINSAliq = new COFINSAliq(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("COFINSQtde")) {
                this.COFINSQtde = new COFINSQtde(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("COFINSNT")) {
                this.COFINSNT = new COFINSNT(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("COFINSSN")) {
                this.COFINSSN = new COFINSSN(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("COFINSOutr")) {
                this.COFINSOutr = new COFINSOutr(filhoAtual);
            }
        }
    }
    
    public InformacoesCOFINS() {
        this.COFINSAliq = null;
        this.COFINSQtde = null;
        this.COFINSNT = null;
        this.COFINSSN = null;
        this.COFINSOutr = null;
        this.COFINSAliq = new COFINSAliq();
    }
    
    public COFINSAliq getCOFINSAliq() {
        return this.COFINSAliq;
    }
    
    public COFINSQtde getCOFINSQTde() {
        return this.COFINSQtde;
    }
    
    public COFINSNT getCOFINSNT() {
        return this.COFINSNT;
    }
    
    public COFINSSN getCOFINSSN() {
        return this.COFINSSN;
    }
    
    public COFINSOutr getCOFINSOutr() {
        return this.COFINSOutr;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.COFINSAliq == null && this.COFINSQtde == null && this.COFINSNT == null && this.COFINSSN == null && this.COFINSOutr == null) {
            ControleLogs.logar("Campos 'COFINSAliq', 'COFINSQtde', 'COFINSNT', 'COFINSSN' e 'COFINSOutr' inexistentes");
            resultado = "1999";
        }
        else if (this.COFINSAliq != null && (this.COFINSQtde != null || this.COFINSNT != null || this.COFINSSN != null || this.COFINSOutr != null)) {
            ControleLogs.logar("Informar apenas um dos grupos com base valor atribu\u00eddo ao campo CST do COFINS");
            resultado = "1999";
        }
        else if (this.COFINSQtde != null && (this.COFINSAliq != null || this.COFINSNT != null || this.COFINSSN != null || this.COFINSOutr != null)) {
            ControleLogs.logar("Informar apenas um dos grupos com base valor atribu\u00eddo ao campo CST do COFINS");
            resultado = "1999";
        }
        else if (this.COFINSNT != null && (this.COFINSAliq != null || this.COFINSQtde != null || this.COFINSSN != null || this.COFINSOutr != null)) {
            ControleLogs.logar("Informar apenas um dos grupos com base valor atribu\u00eddo ao campo CST do COFINS");
            resultado = "1999";
        }
        else if (this.COFINSSN != null && (this.COFINSAliq != null || this.COFINSQtde != null || this.COFINSNT != null || this.COFINSOutr != null)) {
            ControleLogs.logar("Informar apenas um dos grupos com base valor atribu\u00eddo ao campo CST do COFINS");
            resultado = "1999";
        }
        else if (this.COFINSOutr != null && (this.COFINSAliq != null || this.COFINSQtde != null || this.COFINSNT != null || this.COFINSSN != null)) {
            ControleLogs.logar("Informar apenas um dos grupos com base valor atribu\u00eddo ao campo CST do COFINS");
            resultado = "1999";
        }
        else if ((this.COFINSAliq == null || (resultado = this.COFINSAliq.validar()).equals("1000")) && (this.COFINSQtde == null || (resultado = this.COFINSQtde.validar()).equals("1000")) && (this.COFINSNT == null || (resultado = this.COFINSNT.validar()).equals("1000")) && (this.COFINSSN == null || (resultado = this.COFINSSN.validar()).equals("1000")) && this.COFINSOutr != null) {
            (resultado = this.COFINSOutr.validar()).equals("1000");
        }
        return resultado;
    }
    
    public void completar() {
        if (this.COFINSAliq != null) {
            if (this.COFINSAliq.getvBC() != null) {
                this.COFINSAliq.setvBC(new ABNT5891(this.COFINSAliq.getvBC()).arredondarNBR(2).toString());
            }
            if (this.COFINSAliq.getpCOFINS() != null) {
                this.COFINSAliq.setpCOFINS(new ABNT5891(this.COFINSAliq.getpCOFINS()).arredondarNBR(4).toString());
            }
            if (this.COFINSAliq.getvCOFINS() != null) {
                this.COFINSAliq.setvCOFINS(new ABNT5891(this.COFINSAliq.getvCOFINS()).arredondarNBR(2).toString());
            }
        }
        if (this.COFINSQtde != null) {
            if (this.COFINSQtde.getqBCProd() != null) {
                this.COFINSQtde.setqBCProd(new ABNT5891(this.COFINSQtde.getqBCProd()).arredondarNBR(4).toString());
            }
            if (this.COFINSQtde.getvAliqProd() != null) {
                this.COFINSQtde.setvAliqProd(new ABNT5891(this.COFINSQtde.getvAliqProd()).arredondarNBR(4).toString());
            }
            if (this.COFINSQtde.getvCOFINS() != null) {
                this.COFINSQtde.setvCOFINS(new ABNT5891(this.COFINSQtde.getvCOFINS()).arredondarNBR(2).toString());
            }
        }
        if (this.COFINSOutr != null) {
            if (this.COFINSOutr.getvBC() != null) {
                this.COFINSOutr.setvBC(new ABNT5891(this.COFINSOutr.getvBC()).arredondarNBR(2).toString());
            }
            if (this.COFINSOutr.getpCOFINS() != null) {
                this.COFINSOutr.setpCOFINS(new ABNT5891(this.COFINSOutr.getpCOFINS()).arredondarNBR(4).toString());
            }
            if (this.COFINSOutr.getqBCProd() != null) {
                this.COFINSOutr.setqBCProd(new ABNT5891(this.COFINSOutr.getqBCProd()).arredondarNBR(4).toString());
            }
            if (this.COFINSOutr.getvAliqProd() != null) {
                this.COFINSOutr.setvAliqProd(new ABNT5891(this.COFINSOutr.getvAliqProd()).arredondarNBR(4).toString());
            }
            if (this.COFINSOutr.getvCOFINS() != null) {
                this.COFINSOutr.setvCOFINS(new ABNT5891(this.COFINSOutr.getvCOFINS()).arredondarNBR(2).toString());
            }
        }
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.COFINSAliq != null) {
            retorno.append("<COFINSAliq>");
            this.COFINSAliq.toString(retorno);
            retorno.append("</COFINSAliq>");
        }
        if (this.COFINSQtde != null) {
            retorno.append("<COFINSQtde>");
            this.COFINSQtde.toString(retorno);
            retorno.append("</COFINSQtde>");
        }
        if (this.COFINSNT != null) {
            retorno.append("<COFINSNT>");
            this.COFINSNT.toString(retorno);
            retorno.append("</COFINSNT>");
        }
        if (this.COFINSSN != null) {
            retorno.append("<COFINSSN>");
            this.COFINSSN.toString(retorno);
            retorno.append("</COFINSSN>");
        }
        if (this.COFINSOutr != null) {
            retorno.append("<COFINSOutr>");
            this.COFINSOutr.toString(retorno);
            retorno.append("</COFINSOutr>");
        }
    }
}
