// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.excecoes;

public class TimeoutSATException extends Exception
{
    private static final long serialVersionUID = 5510964678951360883L;
    
    public TimeoutSATException() {
    }
    
    public TimeoutSATException(final String msg) {
        super(msg);
    }
}
