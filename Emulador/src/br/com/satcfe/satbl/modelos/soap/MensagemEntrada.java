// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.soap;

import br.com.satcfe.satbl.Configuracoes;

public abstract class MensagemEntrada
{
    protected String nameSpaceRaiz;
    
    public MensagemEntrada() {
        this.nameSpaceRaiz = "http://www.fazenda.sp.gov.br/sat";
    }
    
    public String getMensagem() {
        final double versao = Double.parseDouble(Configuracoes.SAT.VERSAO_SCHEMA_ATUAL);
        if (versao == 0.03) {
            return this.versao003();
        }
        if (versao == 0.04) {
            return this.versao004();
        }
        return this.versao003();
    }
    
    public abstract String versao003();
    
    public abstract String versao004();
}
