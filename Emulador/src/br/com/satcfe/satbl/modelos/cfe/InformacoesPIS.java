// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.um.modelos.ABNT5891;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class InformacoesPIS
{
    private PISAliq PISAliq;
    private PISQtde PISQtde;
    private PISNT PISNT;
    private PISSN PISSN;
    private PISOutr PISOutr;
    
    public InformacoesPIS(final Node no) {
        this.PISAliq = null;
        this.PISQtde = null;
        this.PISNT = null;
        this.PISSN = null;
        this.PISOutr = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("PISAliq")) {
                this.PISAliq = new PISAliq(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("PISQtde")) {
                this.PISQtde = new PISQtde(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("PISNT")) {
                this.PISNT = new PISNT(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("PISSN")) {
                this.PISSN = new PISSN(filhoAtual);
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("PISOutr")) {
                this.PISOutr = new PISOutr(filhoAtual);
            }
        }
    }
    
    public PISAliq getPISAliq() {
        return this.PISAliq;
    }
    
    public void setPISAliq(final PISAliq PISAliqCFe) {
        this.PISAliq = PISAliqCFe;
    }
    
    public PISQtde getPISQTde() {
        return this.PISQtde;
    }
    
    public void setPISQTde(final PISQtde PISQtdeCFe) {
        this.PISQtde = PISQtdeCFe;
    }
    
    public PISNT getPISNT() {
        return this.PISNT;
    }
    
    public void getPISNT(final PISNT PISNTCFe) {
        this.PISNT = PISNTCFe;
    }
    
    public PISSN getPISSN() {
        return this.PISSN;
    }
    
    public void setPISSN(final PISSN PISSNCFe) {
        this.PISSN = PISSNCFe;
    }
    
    public PISOutr getPISOutr() {
        return this.PISOutr;
    }
    
    public void getPISOutr(final PISOutr PISOutrCFe) {
        this.PISOutr = PISOutrCFe;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.PISAliq == null && this.PISQtde == null && this.PISNT == null && this.PISSN == null && this.PISOutr == null) {
            ControleLogs.logar("Campo 'PISAliq', 'PISQtde', 'PISNT', 'PISSN' ou 'PISOutr' n\u00e3o informado.");
            resultado = "1999";
        }
        else if (this.PISAliq != null && (this.PISQtde != null || this.PISNT != null || this.PISSN != null || this.PISOutr != null)) {
            ControleLogs.logar("Somente um dos campos 'PISAliq', 'PISQtde', 'PISNT', 'PISSN' ou 'PISOutr' deve ser informado.");
            resultado = "1999";
        }
        else if (this.PISQtde != null && (this.PISAliq != null || this.PISNT != null || this.PISSN != null || this.PISOutr != null)) {
            ControleLogs.logar("Somente um dos campos 'PISAliq', 'PISQtde', 'PISNT', 'PISSN' ou 'PISOutr' deve ser informado.");
            resultado = "1999";
        }
        else if (this.PISNT != null && (this.PISAliq != null || this.PISQtde != null || this.PISSN != null || this.PISOutr != null)) {
            ControleLogs.logar("Somente um dos campos 'PISAliq', 'PISQtde', 'PISNT', 'PISSN' ou 'PISOutr' deve ser informado.");
            resultado = "1999";
        }
        else if (this.PISSN != null && (this.PISAliq != null || this.PISQtde != null || this.PISNT != null || this.PISOutr != null)) {
            ControleLogs.logar("Somente um dos campos 'PISAliq', 'PISQtde', 'PISNT', 'PISSN' ou 'PISOutr' deve ser informado.");
            resultado = "1999";
        }
        else if (this.PISOutr != null && (this.PISAliq != null || this.PISQtde != null || this.PISNT != null || this.PISSN != null)) {
            ControleLogs.logar("Um dos campos 'PISAliq', 'PISQtde', 'PISNT', 'PISSN' ou 'PISOutr' deve ser informado.");
            resultado = "1999";
        }
        else if (this.PISAliq != null && !(resultado = this.PISAliq.validar()).equals("1000")) {
            ControleLogs.logar("Erro no campo 'PISAliq'");
        }
        else if (this.PISQtde != null && !(resultado = this.PISQtde.validar()).equals("1000")) {
            ControleLogs.logar("Erro no campo 'PISQtde'");
        }
        else if (this.PISNT != null && !(resultado = this.PISNT.validar()).equals("1000")) {
            ControleLogs.logar("Erro no campo 'PISNT'");
        }
        else if (this.PISSN != null && !(resultado = this.PISSN.validar()).equals("1000")) {
            ControleLogs.logar("Erro no campo 'PISSN'");
        }
        else if (this.PISOutr != null && !(resultado = this.PISOutr.validar()).equals("1000")) {
            ControleLogs.logar("Erro no campo 'PISOutr'");
        }
        return resultado;
    }
    
    public void completar() {
        if (this.PISAliq != null) {
            if (this.PISAliq.getvBC() != null) {
                this.PISAliq.setvBC(new ABNT5891(this.PISAliq.getvBC()).arredondarNBR(2).toString());
            }
            if (this.PISAliq.getvPIS() != null) {
                this.PISAliq.setvPIS(new ABNT5891(this.PISAliq.getvPIS()).arredondarNBR(2).toString());
            }
            if (this.PISAliq.getpPIS() != null) {
                this.PISAliq.setpPIS(new ABNT5891(this.PISAliq.getpPIS()).arredondarNBR(4).toString());
            }
        }
        if (this.PISQtde != null) {
            if (this.PISQtde.getqBCProd() != null) {
                this.PISQtde.setqBCProd(new ABNT5891(this.PISQtde.getqBCProd()).arredondarNBR(4).toString());
            }
            if (this.PISQtde.getvAliqProd() != null) {
                this.PISQtde.setvAliqProd(new ABNT5891(this.PISQtde.getvAliqProd()).arredondarNBR(4).toString());
            }
            if (this.PISQtde.getvPIS() != null) {
                this.PISQtde.setvPIS(new ABNT5891(this.PISQtde.getvPIS()).arredondarNBR(2).toString());
            }
        }
        if (this.PISOutr != null) {
            if (this.PISOutr.getvBC() != null) {
                this.PISOutr.setvBC(new ABNT5891(this.PISOutr.getvBC()).arredondarNBR(2).toString());
            }
            if (this.PISOutr.getpPIS() != null) {
                this.PISOutr.setpPIS(new ABNT5891(this.PISOutr.getpPIS()).arredondarNBR(4).toString());
            }
            if (this.PISOutr.getqBCProd() != null) {
                this.PISOutr.setqBCProd(new ABNT5891(this.PISOutr.getqBCProd()).arredondarNBR(4).toString());
            }
            if (this.PISOutr.getvAliqProd() != null) {
                this.PISOutr.setvAliqProd(new ABNT5891(this.PISOutr.getvAliqProd()).arredondarNBR(4).toString());
            }
            if (this.PISOutr.getvPIS() != null) {
                this.PISOutr.setvPIS(new ABNT5891(this.PISOutr.getvPIS()).arredondarNBR(2).toString());
            }
        }
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.PISAliq != null) {
            retorno.append("<PISAliq>");
            this.PISAliq.toString(retorno);
            retorno.append("</PISAliq>");
        }
        if (this.PISQtde != null) {
            retorno.append("<PISQtde>");
            this.PISQtde.toString(retorno);
            retorno.append("</PISQtde>");
        }
        if (this.PISNT != null) {
            retorno.append("<PISNT>");
            this.PISNT.toString(retorno);
            retorno.append("</PISNT>");
        }
        if (this.PISSN != null) {
            retorno.append("<PISSN>");
            this.PISSN.toString(retorno);
            retorno.append("</PISSN>");
        }
        if (this.PISOutr != null) {
            retorno.append("<PISOutr>");
            this.PISOutr.toString(retorno);
            retorno.append("</PISOutr>");
        }
    }
}
