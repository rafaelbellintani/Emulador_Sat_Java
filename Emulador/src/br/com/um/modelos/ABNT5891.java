// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.modelos;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.math.MathContext;
import java.math.RoundingMode;
import java.math.BigDecimal;

public class ABNT5891
{
    private static final long serialVersionUID = 7968308675029276046L;
    private BigDecimal value;
    private int NUMERO_ZERO;
    private int NUMERO_IMPAR;
    private int NUMERO_PAR;
    
    public ABNT5891(final String value) {
        this.value = null;
        this.NUMERO_ZERO = 0;
        this.NUMERO_IMPAR = 1;
        this.NUMERO_PAR = 2;
        this.value = new BigDecimal(value);
    }
    
    public ABNT5891(final double value) {
        this.value = null;
        this.NUMERO_ZERO = 0;
        this.NUMERO_IMPAR = 1;
        this.NUMERO_PAR = 2;
        this.value = new BigDecimal(String.valueOf(value));
    }
    
    public ABNT5891(final BigDecimal value) {
        this.value = null;
        this.NUMERO_ZERO = 0;
        this.NUMERO_IMPAR = 1;
        this.NUMERO_PAR = 2;
        this.value = value;
    }
    
    public double doubleValue() {
        return this.value.doubleValue();
    }
    
    public BigDecimal roundValue(final int decimais) {
        return this.arredondarNBR(decimais);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
    
    public BigDecimal arredondar(final int dec) {
        final BigDecimal b = this.value;
        final int v = String.valueOf(b.intValue()).length();
        return b.round(new MathContext(v + dec, RoundingMode.HALF_EVEN));
    }
    
    public BigDecimal arredondarNBR(int dec) {
        if (dec < 0) {
            dec = 0;
        }
        int ULTIMO_CONSERVADO = 0;
        int PRIMEIRO_NAO_CONSERVADO = 0;
        final BigDecimal b = this.value;
        final int nInt = String.valueOf(b.intValue()).length();
        int ponto = 0;
        char[] v = b.toString().toCharArray();
        for (int i = 0; i < v.length; ++i) {
            if (v[i] == '.') {
                ponto = i;
            }
        }
        if (dec == 0) {
            ULTIMO_CONSERVADO = ponto - 1;
        }
        else {
            ULTIMO_CONSERVADO = ponto + dec;
        }
        PRIMEIRO_NAO_CONSERVADO = ponto + dec + 1;
        if (ponto + dec == dec || ponto + dec + 1 >= this.value.toString().length()) {
            final char[] c = new char[dec];
            Arrays.fill(c, '0');
            final DecimalFormat df = new DecimalFormat("0." + new String(c));
            return new BigDecimal(df.format(this.value).replace(",", "."));
        }
        final char c2 = v[PRIMEIRO_NAO_CONSERVADO];
        if (v[PRIMEIRO_NAO_CONSERVADO] >= '5') {
            if (v[PRIMEIRO_NAO_CONSERVADO] > '5') {
                v = Arrays.copyOf(v, PRIMEIRO_NAO_CONSERVADO);
                v = somarDecimo(new BigDecimal(v, 0, v.length), dec).toString().toCharArray();
            }
            else if (v[PRIMEIRO_NAO_CONSERVADO] == '5' && v.length - (PRIMEIRO_NAO_CONSERVADO + 1) > 0 && Long.parseLong(new String(Arrays.copyOfRange(v, PRIMEIRO_NAO_CONSERVADO + 1, v.length))) > 0L) {
                v = Arrays.copyOf(v, PRIMEIRO_NAO_CONSERVADO);
                v = somarDecimo(new BigDecimal(v, 0, v.length), dec).toString().toCharArray();
            }
            else if (v[PRIMEIRO_NAO_CONSERVADO] == '5' && this.isParImpar(v[ULTIMO_CONSERVADO]) == this.NUMERO_IMPAR) {
                v = Arrays.copyOf(v, PRIMEIRO_NAO_CONSERVADO);
                v = somarDecimo(new BigDecimal(v, 0, v.length), dec).toString().toCharArray();
            }
        }
        if (String.valueOf(v).indexOf(".") == -1) {
            return new BigDecimal(String.valueOf(v));
        }
        v = Arrays.copyOf(v, PRIMEIRO_NAO_CONSERVADO);
        if (ponto + dec == dec || ponto + dec + 1 >= v.length) {
            final char[] c = new char[dec];
            Arrays.fill(c, '0');
            final DecimalFormat df = new DecimalFormat("0." + new String(c));
            return new BigDecimal(df.format(new BigDecimal(v, 0, v.length)).replace(",", "."));
        }
        return new BigDecimal(v, 0, v.length);
    }
    
    public static BigDecimal somarDecimo(final BigDecimal b, final int dec) {
        BigDecimal soma;
        if (dec > 0) {
            final char[] zero = new char[dec + 2];
            Arrays.fill(zero, '0');
            zero[zero.length - 1] = '1';
            zero[1] = '.';
            soma = new BigDecimal(new String(zero));
        }
        else {
            soma = new BigDecimal("1");
        }
        return b.add(soma);
    }
    
    public int isParImpar(final char n) {
        return this.isImpar(Integer.valueOf(String.valueOf(n)));
    }
    
    private int charToInt(final char c) {
        return Integer.parseInt(String.valueOf(c));
    }
    
    private char intToChar(final int n) {
        return String.valueOf(n).charAt(0);
    }
    
    public int isImpar(final int n) {
        if (n == 0) {
            return this.NUMERO_ZERO;
        }
        if (n % 2 == 0) {
            return this.NUMERO_PAR;
        }
        return this.NUMERO_IMPAR;
    }
    
    public BigDecimal truncar(final int decimais) {
        final String valor = this.value.toString();
        final int ponto = valor.indexOf(".");
        if (decimais <= 0) {
            return new BigDecimal(valor.substring(0, ponto));
        }
        if (ponto + decimais + 1 > valor.length()) {
            final char[] c = new char[decimais];
            Arrays.fill(c, '0');
            final DecimalFormat df = new DecimalFormat("0." + new String(c));
            return new BigDecimal(df.format(Double.parseDouble(valor)).replace(",", "."));
        }
        return new BigDecimal(valor.substring(0, ponto + decimais + 1));
    }
}
