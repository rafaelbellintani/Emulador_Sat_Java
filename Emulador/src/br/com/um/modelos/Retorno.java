// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.modelos;

public class Retorno
{
    private String mensagem;
    private String codigo;
    
    public Retorno() {
        this.mensagem = null;
        this.codigo = null;
    }
    
    public Retorno(final String codigo) {
        this.mensagem = null;
        this.codigo = null;
        this.codigo = codigo;
    }
    
    public String getMensagem() {
        return this.mensagem;
    }
    
    public void setMensagem(final String mensagem) {
        this.mensagem = mensagem;
    }
    
    public String getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(final String codigo) {
        this.codigo = codigo;
    }
}
