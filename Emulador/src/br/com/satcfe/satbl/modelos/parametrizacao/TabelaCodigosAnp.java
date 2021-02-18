// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import java.util.Iterator;
import java.util.Date;
import br.com.um.controles.ControleTempo;
import java.util.ArrayList;
import java.util.List;

public class TabelaCodigosAnp
{
    private String ambiente;
    private String ativarTabela;
    private List<CodigoAnp> tabela;
    
    public TabelaCodigosAnp() {
        this.ambiente = null;
        this.ativarTabela = null;
        this.tabela = null;
    }
    
    public String getAmbiente() {
        return this.ambiente;
    }
    
    public void setAmbiente(final String ambiente) {
        this.ambiente = ambiente;
    }
    
    public String getAtivarTabela() {
        return this.ativarTabela;
    }
    
    public void setAtivarTabela(final String ativarTabela) {
        this.ativarTabela = ativarTabela;
    }
    
    public List<CodigoAnp> getTabela() {
        return this.tabela;
    }
    
    public void setTabela(final List<CodigoAnp> tabela) {
        this.tabela = tabela;
    }
    
    public void addCodigoAnp(final CodigoAnp codigoAnp) {
        if (this.tabela == null) {
            this.tabela = new ArrayList<CodigoAnp>();
        }
        this.tabela.add(codigoAnp);
    }
    
    public boolean isCodigoAnpValido(final String codigoAnp) {
        final Date today = new Date(ControleTempo.getCurrentTime());
        for (final CodigoAnp codAnp : this.tabela) {
            if (codAnp != null && codAnp.getcProdANP() != null && codAnp.getcProdANP().equals(codigoAnp)) {
                try {
                    final Date inicio = new Date(ControleTempo.parseTimeStamp(codAnp.getDataInicio()));
                    final Date fim = new Date(ControleTempo.parseTimeStamp(codAnp.getDataFim()));
                    if (inicio.before(today) && fim.after(today)) {
                        return true;
                    }
                    continue;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
