// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import java.math.BigInteger;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class DHParameter extends ASN1Encodable
{
    DERInteger p;
    DERInteger g;
    DERInteger l;
    
    public DHParameter(final BigInteger bigInteger, final BigInteger bigInteger2, final int n) {
        this.p = new DERInteger(bigInteger);
        this.g = new DERInteger(bigInteger2);
        if (n != 0) {
            this.l = new DERInteger(n);
        }
        else {
            this.l = null;
        }
    }
    
    public DHParameter(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.p = objects.nextElement();
        this.g = objects.nextElement();
        if (objects.hasMoreElements()) {
            this.l = objects.nextElement();
        }
        else {
            this.l = null;
        }
    }
    
    public BigInteger getP() {
        return this.p.getPositiveValue();
    }
    
    public BigInteger getG() {
        return this.g.getPositiveValue();
    }
    
    public BigInteger getL() {
        if (this.l == null) {
            return null;
        }
        return this.l.getPositiveValue();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.g);
        if (this.getL() != null) {
            asn1EncodableVector.add(this.l);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
