// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

public class CodigoAnp
{
    private String cProdANP;
    private String descricao;
    private String dataInicio;
    private String dataFim;
    
    public CodigoAnp() {
        this.cProdANP = null;
        this.descricao = null;
        this.dataInicio = null;
        this.dataFim = null;
    }
    
    public String getcProdANP() {
        return this.cProdANP;
    }
    
    public void setcProdANP(final String cProdANP) {
        this.cProdANP = cProdANP;
    }
    
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(final String descricao) {
        this.descricao = descricao;
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
