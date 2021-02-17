// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.satcfe.satbl.modelos.CredenciadorasCartao;
import br.com.um.controles.ControleDados;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class MP
{
    private String cMP;
    private String vMP;
    private String cAdmC;
    
    public MP(final Node no) {
        this.cMP = null;
        this.vMP = null;
        this.cAdmC = null;
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("cMP")) {
                this.cMP = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("vMP")) {
                this.vMP = filhoAtual.getTextContent();
            }
            else if (filhoAtual.getNodeName().equalsIgnoreCase("cAdmC")) {
                this.cAdmC = filhoAtual.getTextContent();
            }
        }
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.cMP == null) {
            ControleLogs.logar("Campo 'cMP' inexistente.");
            resultado = "1999";
        }
        else if (this.vMP == null) {
            ControleLogs.logar("Campo 'vMP' inexistente.");
            resultado = "1999";
        }
        else if (this.cMP.length() != 2) {
            ControleLogs.logar("Erro no Campo 'cMP' : Campo inv\u00e1lido.");
            resultado = "1527";
        }
        else if (!ControleDados.isNumerico(this.cMP)) {
            ControleLogs.logar("Erro no Campo 'cMP' : Campo inv\u00e1lido, Campo n\u00e3o \u00e9 numerico.");
            resultado = "1527";
        }
        else if (!this.cMP.equals("01") && !this.cMP.equals("02") && !this.cMP.equals("03") && !this.cMP.equals("04") && !this.cMP.equals("05") && !this.cMP.equals("10") && !this.cMP.equals("11") && !this.cMP.equals("12") && !this.cMP.equals("13") && !this.cMP.equals("99")) {
            ControleLogs.logar("Erro no Campo 'cMP' : Campo inv\u00e1lido, fora do range de codigos aceitos.");
            resultado = "1527";
        }
        else if (!ControleDados.isNumerico(this.vMP)) {
            ControleLogs.logar("Erro no Campo 'vMP' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (!ControleDados.validarDouble(1, 15, 2, this.vMP)) {
            ControleLogs.logar("Erro no Campo 'vMP' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (Double.parseDouble(this.vMP) < 0.0) {
            ControleLogs.logar("Erro no Campo 'vMP' : Campo menor que 0 (zero).");
            resultado = "1528";
        }
        else if (this.cAdmC != null && (this.cAdmC.length() != 3 || !ControleDados.isNumerico(this.cAdmC))) {
            ControleLogs.logar("Erro no Campo 'cAdmC' : Campo inv\u00e1lido.");
            resultado = "1999";
        }
        else if (this.cAdmC != null && this.cAdmC.length() == 3 && !CredenciadorasCartao.existe(this.cAdmC)) {
            ControleLogs.logar("Erro no Campo 'cAdmC' : Campo diferente dos previstos no Anexo 3. ");
            resultado = "1535";
        }
        return resultado;
    }
    
    @Override
    public String toString() {
        final StringBuffer retorno = new StringBuffer();
        if (ControleDados.isNumerico(this.cMP)) {
            retorno.append("<cMP>").append(this.cMP).append("</cMP>");
        }
        if (ControleDados.isNumerico(this.vMP)) {
            retorno.append("<vMP>").append(this.vMP).append("</vMP>");
        }
        if (this.cAdmC != null) {
            retorno.append("<cAdmC>").append(this.cAdmC).append("</cAdmC>");
        }
        return retorno.toString();
    }
    
    public String getvMP() {
        return this.vMP;
    }
    
    public void setvMP(final String vMP) {
        this.vMP = vMP;
    }
}
