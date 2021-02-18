// 
// Decompiled by Procyon v0.5.36
// 

package br.com.um.modelos;

import java.math.RoundingMode;
import java.math.BigDecimal;

public class Decimal
{
    private BigDecimal decimal;
    
    public Decimal() {
        this.decimal = null;
        this.decimal = new BigDecimal(0);
    }
    
    public Decimal(final BigDecimal decimal) {
        this.decimal = null;
        this.decimal = decimal;
    }
    
    public Decimal(final double d) {
        this.decimal = null;
        this.decimal = new BigDecimal(String.valueOf(d));
    }
    
    public Decimal(final String d) {
        this.decimal = null;
        this.decimal = new BigDecimal(d);
    }
    
    public static BigDecimal somar(final Decimal d1, final Decimal d2) {
        return d1.getDecimal().add(d2.getDecimal());
    }
    
    public static BigDecimal somar(final double d1, final double d2) {
        return somar(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal somar(final String d1, final String d2) {
        return somar(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal somar(final BigDecimal d1, final BigDecimal d2) {
        return somar(new Decimal(d1), new Decimal(d2));
    }
    
    public BigDecimal incrementar(final BigDecimal d1) {
        return somar(this.decimal, d1);
    }
    
    public BigDecimal incrementar(final Decimal d1) {
        return somar(this.decimal, d1.getDecimal());
    }
    
    public BigDecimal incrementar(final String d1) {
        return somar(this.decimal.toString(), d1);
    }
    
    public BigDecimal incrementar(final double d1) {
        return somar(this.decimal.toString(), String.valueOf(d1));
    }
    
    public static BigDecimal subtrair(final Decimal d1, final Decimal d2) {
        return d1.getDecimal().subtract(d2.getDecimal());
    }
    
    public static BigDecimal subtrair(final double d1, final double d2) {
        return subtrair(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal subtrair(final String d1, final String d2) {
        return subtrair(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal subtrair(final BigDecimal d1, final BigDecimal d2) {
        return subtrair(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal multiplicar(final Decimal d1, final Decimal d2) {
        return d1.getDecimal().multiply(d2.getDecimal());
    }
    
    public static BigDecimal multiplicar(final double d1, final double d2) {
        return multiplicar(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal multiplicar(final String d1, final String d2) {
        return multiplicar(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal multiplicar(final BigDecimal d1, final BigDecimal d2) {
        return multiplicar(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal dividir(final Decimal d1, final Decimal d2) {
        return d1.getDecimal().divide(d2.getDecimal(), 9, RoundingMode.HALF_EVEN);
    }
    
    public static BigDecimal dividir(final double d1, final double d2) {
        return dividir(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal dividir(final String d1, final String d2) {
        return dividir(new Decimal(d1), new Decimal(d2));
    }
    
    public static BigDecimal dividir(final BigDecimal d1, final BigDecimal d2) {
        return dividir(new Decimal(d1), new Decimal(d2));
    }
    
    public BigDecimal getDecimal() {
        return this.decimal;
    }
    
    public double getDouble() {
        return this.decimal.doubleValue();
    }
    
    @Override
    public String toString() {
        return this.getDecimal().toString();
    }
}
