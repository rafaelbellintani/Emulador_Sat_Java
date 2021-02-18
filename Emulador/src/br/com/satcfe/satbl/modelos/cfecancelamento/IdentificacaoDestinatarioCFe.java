// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfecancelamento;

import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class IdentificacaoDestinatarioCFe
{
    private String CNPJ;
    private String CPF;
    
    public IdentificacaoDestinatarioCFe(final Node no) {
        this.CNPJ = null;
        this.CPF = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CPF")) {
                this.CPF = filhoAtual.getTextContent();
            }
            if (filhoAtual.getNodeName().equalsIgnoreCase("CNPJ")) {
                this.CNPJ = filhoAtual.getTextContent();
            }
        }
    }
    
    public String getCNPJ() {
        return this.CNPJ;
    }
    
    public void setCNPJ(final String CNPJ) {
        this.CNPJ = CNPJ;
    }
    
    public String getCPF() {
        return this.CPF;
    }
    
    public void setCPF(final String CPF) {
        this.CPF = CPF;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.CPF != null && this.CNPJ != null) {
            ControleLogs.logar("Erro no campo 'CNPJ' : Campo inv\u00e1lido");
            resultado = "1999";
        }
        if (this.CNPJ != null && (!ControleDados.validarCNPJCPF(this.CNPJ) || this.CNPJ.equals("00000000000000"))) {
            resultado = "1235";
        }
        else if (this.CPF != null && (!ControleDados.validarCNPJCPF(this.CPF) || this.CPF.equals("00000000000"))) {
            resultado = "1237";
        }
        return resultado;
    }
    
    public void toString(final StringBuffer retorno) {
        if (this.CNPJ != null) {
            retorno.append("<CNPJ>").append(this.CNPJ).append("</CNPJ>");
        }
        if (this.CPF != null) {
            retorno.append("<CPF>").append(this.CPF).append("</CPF>");
        }
    }
}
