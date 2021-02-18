// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.cfe;

import br.com.satcfe.satbl.Configuracoes;
import br.com.satcfe.satbl.controles.ControleLogs;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import org.w3c.dom.Node;
import java.util.List;

public class InformacoesAdicionaisCFe
{
    private String infCpl;
    private List<ObsFisco> obsFisco;
    
    public InformacoesAdicionaisCFe(final Node no) {
        this.infCpl = null;
        this.obsFisco = new ArrayList<ObsFisco>();
        final NodeList filhos = no.getChildNodes();
        for (int i = 0; i < filhos.getLength(); ++i) {
            final Node filhoAtual = filhos.item(i);
            if (filhoAtual.getNodeName().equalsIgnoreCase("infCpl")) {
                this.infCpl = filhoAtual.getTextContent();
            }
        }
    }
    
    public InformacoesAdicionaisCFe() {
    }
    
    public String getInfCpl() {
        return this.infCpl;
    }
    
    public void setInfCpl(final String infCpl) {
        this.infCpl = infCpl;
    }
    
    public List getObsFisco() {
        return this.obsFisco;
    }
    
    public void setObsFisco(final List<ObsFisco> obsFisco) {
        this.obsFisco = obsFisco;
    }
    
    public String validar() {
        String resultado = "1000";
        if (this.infCpl != null && (this.infCpl.length() < 1 || this.infCpl.length() > 5000)) {
            ControleLogs.logar("campo 'infCpl' inv\u00e1lido");
            resultado = "1999";
        }
        return resultado;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        if (this.infCpl != null) {
            sb.append("<infCpl>").append(this.infCpl).append("</infCpl>");
        }
        if (Double.parseDouble(Configuracoes.SAT.VERSAO_LAYOUT_CFE) < 0.08 && this.obsFisco != null) {
            for (int i = 0; i < this.obsFisco.size(); ++i) {
                sb.append("<obsFisco xCampo=\"").append(this.obsFisco.get(i).getxCampo()).append("\">");
                sb.append("<xTexto>");
                sb.append(this.obsFisco.get(i).getxTexto());
                sb.append("</xTexto>");
                sb.append("</obsFisco>");
            }
        }
        return sb.toString();
    }
}
