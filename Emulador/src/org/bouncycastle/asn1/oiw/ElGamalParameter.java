// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.oiw;

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

public class ElGamalParameter extends ASN1Encodable
{
    DERInteger p;
    DERInteger g;
    
    public ElGamalParameter(final BigInteger bigInteger, final BigInteger bigInteger2) {
        this.p = new DERInteger(bigInteger);
        this.g = new DERInteger(bigInteger2);
    }
    
    public ElGamalParameter(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.p = objects.nextElement();
        this.g = objects.nextElement();
    }
    
    public BigInteger getP() {
        return this.p.getPositiveValue();
    }
    
    public BigInteger getG() {
        return this.g.getPositiveValue();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.g);
        return new DERSequence(asn1EncodableVector);
    }
}
