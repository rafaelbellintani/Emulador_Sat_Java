// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

import java.util.ArrayList;
import java.util.List;

public class TabelaVigenciaLeiaute
{
    private List<VigenciaLeiaute> tabela;
    private String ambiente;
    private String mensagem;
    
    public TabelaVigenciaLeiaute() {
        this.tabela = null;
        this.ambiente = null;
        this.mensagem = null;
    }
    
    public List<VigenciaLeiaute> getTabela() {
        return this.tabela;
    }
    
    public void setTabela(final List<VigenciaLeiaute> tabela) {
        this.tabela = tabela;
    }
    
    public String getMensagem() {
        return this.mensagem;
    }
    
    public void setMensagem(final String mensagem) {
        this.mensagem = mensagem;
    }
    
    public String getAmbiente() {
        return this.ambiente;
    }
    
    public void setAmbiente(final String ambiente) {
        this.ambiente = ambiente;
    }
    
    public void addLeiaute(final VigenciaLeiaute leiaute) {
        if (this.tabela == null) {
            this.tabela = new ArrayList<VigenciaLeiaute>();
        }
        this.tabela.add(leiaute);
    }
}
