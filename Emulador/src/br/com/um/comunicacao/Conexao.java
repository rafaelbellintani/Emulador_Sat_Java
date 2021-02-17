// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.comunicacao;

import java.net.URL;

public interface Conexao
{
    void setRequest(final String p0);
    
    boolean consumir(final URL p0, final String p1);
    
    String getResponse();
}
