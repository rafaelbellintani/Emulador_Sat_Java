// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.satcfe.satbl.Parametrizacoes;
import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.controles.ControleLogs;
import br.com.um.controles.ControleDados;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class IdentificacaoDestinatarioCFe
{
    private String CNPJ;
    private String CPF;
    private String xNome;
    
    public IdentificacaoDestinatarioCFe(final Node no) {
        this.CNPJ = null;
        this.CPF = null;
        this.xNome = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("CPF")) {
                this.CPF = filhoAtual.getTextContent();
            }
            if (filhoAtual.getNodeName().equalsIgnoreCase("CNPJ")) {
                this.CNPJ = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("xNome")) {
                this.xNome = filhoAtual.getTextContent();
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
    
    public String getxNome() {
        return this.xNome;
    }
    
    public void setxNome(final String xNome) {
        this.xNome = xNome;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.CNPJ != null && (!ControleDados.validarCNPJCPF(this.CNPJ) || this.CNPJ.equals("00000000000000"))) {
            ControleLogs.logar("Campo 'CNPJ' invalido");
            resultado = "1235";
        }
        else if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) >= 0.07 && this.CNPJ != null && this.CNPJ.equals(Parametrizacoes.CNPJ)) {
            ControleLogs.logar("Campo 'CNPJ' invalido");
            resultado = "1235";
        }
        else if (this.CPF != null && (!ControleDados.validarCNPJCPF(this.CPF) || this.CPF.equals("00000000000"))) {
            ControleLogs.logar("Campo 'CNPJ' invalido");
            resultado = "1237";
        }
        else if (this.CPF != null && this.CNPJ != null) {
            ControleLogs.logar("Campo 'CPF' e 'CNPJ' invalido");
            resultado = "1237";
        }
        else if (this.xNome != null && (this.xNome.length() < 2 || this.xNome.length() > 60)) {
            ControleLogs.logar("Campo 'xNome' invalido");
            resultado = "1999";
        }
        else if (this.xNome != null && this.xNome.trim().length() == 0) {
            ControleLogs.logar("Campo 'xNome' em branco");
            resultado = "1000";
            CFe.setFalhaCFe("1234");
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
        if (this.xNome != null) {
            retorno.append("<xNome>").append(this.xNome).append("</xNome>");
        }
    }
}
