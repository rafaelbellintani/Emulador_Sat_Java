// 
// Decompiled by Procyon v0.5.36
// 

package br.com.satcfe.satbl.modelos.parametrizacao;

public class Valor
{
    private long valor;
    
    public Valor(String t) {
        this.valor = 0L;
        t = t.trim();
        t = t.replace(":", "");
        if (t.length() == 8) {
            t = t.substring(2);
        }
        final int horas = Integer.parseInt(t.substring(0, 2));
        t = t.substring(2);
        final int minutos = Integer.parseInt(t.substring(0, 2));
        t = t.substring(2);
        final int segundos = Integer.parseInt(t);
        this.valor = (segundos + 60 * minutos + 3600 * horas) * 1000;
    }
    
    public long getValor() {
        return this.valor;
    }
    
    public void setValor(final long valor) {
        this.valor = valor;
    }
}
