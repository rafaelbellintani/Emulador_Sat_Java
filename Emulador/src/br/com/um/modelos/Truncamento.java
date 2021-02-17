// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.modelos;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.math.BigDecimal;

public class Truncamento
{
    private String value;
    
    public Truncamento(final String value) {
        this.value = null;
        this.value = value;
    }
    
    public Truncamento(final double value) {
        this.value = null;
        this.value = String.valueOf(value);
    }
    
    public Truncamento(final BigDecimal value) {
        this.value = null;
        this.value = value.toString();
    }
    
    public String truncar(final int decimais) {
        final int ponto = this.value.indexOf(".");
        if (decimais <= 0) {
            return this.value.substring(0, ponto);
        }
        if (ponto + decimais + 1 > this.value.length()) {
            final char[] c = new char[decimais];
            Arrays.fill(c, '0');
            final DecimalFormat df = new DecimalFormat("0." + new String(c));
            return df.format(Double.parseDouble(this.value)).replace(",", ".");
        }
        return this.value.substring(0, ponto + decimais + 1);
    }
    
    @Override
    public String toString() {
        return this.value;
    }
}
