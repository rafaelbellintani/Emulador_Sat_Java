// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.excecoes;

public class ErroDesconhecidoException extends Exception
{
    public static final long serialVersionUID = 1L;
    
    public ErroDesconhecidoException() {
    }
    
    public ErroDesconhecidoException(final String msg) {
        super(msg);
    }
}
