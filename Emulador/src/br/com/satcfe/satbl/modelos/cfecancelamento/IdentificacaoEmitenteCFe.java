// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfecancelamento;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import br.com.satcfe.satbl.modelos.cfe.EnderecoEmitente;

public class IdentificacaoEmitenteCFe
{
    private String CNPJ;
    private String xFanc;
    private String xNome;
    private String IE;
    private String IM;
    private EnderecoEmitente enderEmit;
    
    public IdentificacaoEmitenteCFe() {
        this.CNPJ = null;
        this.xFanc = null;
        this.xNome = null;
        this.IE = null;
        this.IM = null;
        this.enderEmit = null;
    }
    
    public IdentificacaoEmitenteCFe(final Node no) {
        this.CNPJ = null;
        this.xFanc = null;
        this.xNome = null;
        this.IE = null;
        this.IM = null;
        this.enderEmit = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CNPJ")) {
                this.CNPJ = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xNome")) {
                this.xNome = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xFanc")) {
                this.xFanc = filhoAtual.getTextContent();
            }
        }
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.CNPJ == null) {
            resultado = "1207";
        }
        else if (!this.CNPJ.equals(Configuracoes.SAT.CNPJEstabelecimento)) {
            resultado = "1207";
        }
        else if (this.xNome == null) {
            resultado = "1999";
        }
        return resultado;
    }
    
    public void completar(final String xNome) {
        this.CNPJ = Parametrizacoes.CNPJ;
        this.xNome = xNome;
        if (this.enderEmit == null) {
            this.enderEmit = new EnderecoEmitente();
        }
        this.enderEmit.completar();
        this.IE = ControleDados.preencheCom(Parametrizacoes.IE, "0", 12, 1);
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.CNPJ != null) {
            retorno.append("<CNPJ>").append(this.CNPJ).append("</CNPJ>");
        }
        if (this.xNome != null) {
            retorno.append("<xNome>").append(this.xNome).append("</xNome>");
        }
        if (this.xFanc != null) {
            retorno.append("<xFanc>").append(this.xFanc).append("</xFanc>");
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
    }
    
    public String getCNPJ() {
        return this.CNPJ;
    }
    
    public void setCNPJ(final String CNPJ) {
        this.CNPJ = CNPJ;
    }
    
    public String getxFanc() {
        return this.xFanc;
    }
    
    public void setxFanc(final String xFanc) {
        this.xFanc = xFanc;
    }
    
    public String getxNome() {
        return this.xNome;
    }
    
    public void setxNome(final String xNome) {
        this.xNome = xNome;
    }
    
    public String getIE() {
        return this.IE;
    }
    
    public void setIE(final String iE) {
        this.IE = iE;
    }
    
    public String getIM() {
        return this.IM;
    }
    
    public void setIM(final String iM) {
        this.IM = iM;
    }
    
    public EnderecoEmitente getEnderEmit() {
        return this.enderEmit;
    }
    
    public void setEnderEmit(final EnderecoEmitente enderEmit) {
        this.enderEmit = enderEmit;
    }
}
