// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509.qualified;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class Iso4217CurrencyCode extends ASN1Encodable implements ASN1Choice
{
    final int ALPHABETIC_MAXSIZE = 3;
    final int NUMERIC_MINSIZE = 1;
    final int NUMERIC_MAXSIZE = 999;
    DEREncodable obj;
    int numeric;
    
    public static Iso4217CurrencyCode getInstance(final Object o) {
        if (o == null || o instanceof Iso4217CurrencyCode) {
            return (Iso4217CurrencyCode)o;
        }
        if (o instanceof DERInteger) {
            return new Iso4217CurrencyCode(DERInteger.getInstance(o).getValue().intValue());
        }
        if (o instanceof DERPrintableString) {
            return new Iso4217CurrencyCode(DERPrintableString.getInstance(o).getString());
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    public Iso4217CurrencyCode(final int n) {
        if (n > 999 || n < 1) {
            throw new IllegalArgumentException("wrong size in numeric code : not in (1..999)");
        }
        this.obj = new DERInteger(n);
    }
    
    public Iso4217CurrencyCode(final String s) {
        if (s.length() > 3) {
            throw new IllegalArgumentException("wrong size in alphabetic code : max size is 3");
        }
        this.obj = new DERPrintableString(s);
    }
    
    public boolean isAlphabetic() {
        return this.obj instanceof DERPrintableString;
    }
    
    public String getAlphabetic() {
        return ((DERPrintableString)this.obj).getString();
    }
    
    public int getNumeric() {
        return ((DERInteger)this.obj).getValue().intValue();
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.obj.getDERObject();
    }
}
