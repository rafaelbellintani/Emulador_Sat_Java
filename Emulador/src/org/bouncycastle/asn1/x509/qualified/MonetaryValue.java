// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509.qualified;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class MonetaryValue extends ASN1Encodable
{
    Iso4217CurrencyCode currency;
    DERInteger amount;
    DERInteger exponent;
    
    public static MonetaryValue getInstance(final Object o) {
        if (o == null || o instanceof MonetaryValue) {
            return (MonetaryValue)o;
        }
        if (o instanceof ASN1Sequence) {
            return new MonetaryValue(ASN1Sequence.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    public MonetaryValue(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.currency = Iso4217CurrencyCode.getInstance(objects.nextElement());
        this.amount = DERInteger.getInstance(objects.nextElement());
        this.exponent = DERInteger.getInstance(objects.nextElement());
    }
    
    public MonetaryValue(final Iso4217CurrencyCode currency, final int n, final int n2) {
        this.currency = currency;
        this.amount = new DERInteger(n);
        this.exponent = new DERInteger(n2);
    }
    
    public Iso4217CurrencyCode getCurrency() {
        return this.currency;
    }
    
    public BigInteger getAmount() {
        return this.amount.getValue();
    }
    
    public BigInteger getExponent() {
        return this.exponent.getValue();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.currency);
        asn1EncodableVector.add(this.amount);
        asn1EncodableVector.add(this.exponent);
        return new DERSequence(asn1EncodableVector);
    }
}
