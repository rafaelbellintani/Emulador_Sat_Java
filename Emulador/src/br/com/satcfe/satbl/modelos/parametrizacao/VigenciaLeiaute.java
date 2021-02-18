// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

public class VigenciaLeiaute
{
    private String versao;
    private String dataInicio;
    private String dataFim;
    
    public VigenciaLeiaute() {
        this.versao = null;
        this.dataInicio = null;
        this.dataFim = null;
    }
    
    public String getVersao() {
        return this.versao;
    }
    
    public void setVersao(final String versao) {
        this.versao = versao;
    }
    
    public String getDataInicio() {
        return this.dataInicio;
    }
    
    public void setDataInicio(final String dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public String getDataFim() {
        return this.dataFim;
    }
    
    public void setDataFim(final String dataFim) {
        this.dataFim = dataFim;
    }
}
