// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.isismtt.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERPrintableString;
import org.bouncycastle.asn1.ASN1Encodable;

public class MonetaryLimit extends ASN1Encodable
{
    DERPrintableString currency;
    DERInteger amount;
    DERInteger exponent;
    
    public static MonetaryLimit getInstance(final Object o) {
        if (o == null || o instanceof MonetaryLimit) {
            return (MonetaryLimit)o;
        }
        if (o instanceof ASN1Sequence) {
            return new MonetaryLimit(ASN1Sequence.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    private MonetaryLimit(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 3) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        this.currency = DERPrintableString.getInstance(objects.nextElement());
        this.amount = DERInteger.getInstance(objects.nextElement());
        this.exponent = DERInteger.getInstance(objects.nextElement());
    }
    
    public MonetaryLimit(final String s, final int n, final int n2) {
        this.currency = new DERPrintableString(s, true);
        this.amount = new DERInteger(n);
        this.exponent = new DERInteger(n2);
    }
    
    public String getCurrency() {
        return this.currency.getString();
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
