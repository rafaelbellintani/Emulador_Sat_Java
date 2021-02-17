// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos;

public class ComandoSefaz
{
    private String nome;
    private String idCmd;
    private String cod;
    private String xMsg;
    
  //CONSTRUTOR COMANDO SEFAZ
    public ComandoSefaz(final String nome) {
        this.nome = null;
        this.idCmd = null;
        this.cod = null;
        this.xMsg = null;
        this.nome = nome;
    }
    
    //CONSTRUTOR COMANDO SEFAZ (PADRÃO)
    public ComandoSefaz() {
        this.nome = null;
        this.idCmd = null;
        this.cod = null;
        this.xMsg = null;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(final String nome) {
        this.nome = nome;
    }
    
    public String getIdCmd() {
        return this.idCmd;
    }
    
    public void setIdCmd(final String idCmd) {
        this.idCmd = idCmd;
    }
    
    public String getCod() {
        return this.cod;
    }
    
    public void setCod(final String cod) {
        this.cod = cod;
    }
    
    public String getxMsg() {
        return this.xMsg;
    }
    
    public void setxMsg(final String xMsg) {
        this.xMsg = xMsg;
    }
    
    public String[] toArray() {
        return new String[] { 
        		this.nome,
        		this.idCmd,
        		this.cod,
        		this.xMsg 
        	};
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.nome) + "|" + this.idCmd + "|" + this.cod + "|" + this.xMsg;
    }
}
