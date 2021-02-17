// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.math.BigInteger;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.asn1.ASN1Encodable;

public class X9Curve extends ASN1Encodable implements X9ObjectIdentifiers
{
    private ECCurve curve;
    private byte[] seed;
    private DERObjectIdentifier fieldIdentifier;
    
    public X9Curve(final ECCurve curve) {
        this.fieldIdentifier = null;
        this.curve = curve;
        this.seed = null;
        this.setFieldIdentifier();
    }
    
    public X9Curve(final ECCurve curve, final byte[] seed) {
        this.fieldIdentifier = null;
        this.curve = curve;
        this.seed = seed;
        this.setFieldIdentifier();
    }
    
    public X9Curve(final X9FieldID x9FieldID, final ASN1Sequence asn1Sequence) {
        this.fieldIdentifier = null;
        this.fieldIdentifier = x9FieldID.getIdentifier();
        if (this.fieldIdentifier.equals(X9Curve.prime_field)) {
            final BigInteger value = ((DERInteger)x9FieldID.getParameters()).getValue();
            this.curve = new ECCurve.Fp(value, new X9FieldElement(value, (ASN1OctetString)asn1Sequence.getObjectAt(0)).getValue().toBigInteger(), new X9FieldElement(value, (ASN1OctetString)asn1Sequence.getObjectAt(1)).getValue().toBigInteger());
        }
        else if (this.fieldIdentifier.equals(X9Curve.characteristic_two_field)) {
            final DERSequence derSequence = (DERSequence)x9FieldID.getParameters();
            final int intValue = ((DERInteger)derSequence.getObjectAt(0)).getValue().intValue();
            final DERObjectIdentifier derObjectIdentifier = (DERObjectIdentifier)derSequence.getObjectAt(1);
            int intValue2 = 0;
            int intValue3 = 0;
            int n;
            if (derObjectIdentifier.equals(X9Curve.tpBasis)) {
                n = ((DERInteger)derSequence.getObjectAt(2)).getValue().intValue();
            }
            else {
                final DERSequence derSequence2 = (DERSequence)derSequence.getObjectAt(2);
                n = ((DERInteger)derSequence2.getObjectAt(0)).getValue().intValue();
                intValue2 = ((DERInteger)derSequence2.getObjectAt(1)).getValue().intValue();
                intValue3 = ((DERInteger)derSequence2.getObjectAt(2)).getValue().intValue();
            }
            this.curve = new ECCurve.F2m(intValue, n, intValue2, intValue3, new X9FieldElement(intValue, n, intValue2, intValue3, (ASN1OctetString)asn1Sequence.getObjectAt(0)).getValue().toBigInteger(), new X9FieldElement(intValue, n, intValue2, intValue3, (ASN1OctetString)asn1Sequence.getObjectAt(1)).getValue().toBigInteger());
        }
        if (asn1Sequence.size() == 3) {
            this.seed = ((DERBitString)asn1Sequence.getObjectAt(2)).getBytes();
        }
    }
    
    private void setFieldIdentifier() {
        if (this.curve instanceof ECCurve.Fp) {
            this.fieldIdentifier = X9Curve.prime_field;
        }
        else {
            if (!(this.curve instanceof ECCurve.F2m)) {
                throw new IllegalArgumentException("This type of ECCurve is not implemented");
            }
            this.fieldIdentifier = X9Curve.characteristic_two_field;
        }
    }
    
    public ECCurve getCurve() {
        return this.curve;
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.fieldIdentifier.equals(X9Curve.prime_field)) {
            asn1EncodableVector.add(new X9FieldElement(this.curve.getA()).getDERObject());
            asn1EncodableVector.add(new X9FieldElement(this.curve.getB()).getDERObject());
        }
        else if (this.fieldIdentifier.equals(X9Curve.characteristic_two_field)) {
            asn1EncodableVector.add(new X9FieldElement(this.curve.getA()).getDERObject());
            asn1EncodableVector.add(new X9FieldElement(this.curve.getB()).getDERObject());
        }
        if (this.seed != null) {
            asn1EncodableVector.add(new DERBitString(this.seed));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
