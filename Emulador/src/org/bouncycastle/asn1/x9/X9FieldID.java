// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERInteger;
import java.math.BigInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class X9FieldID extends ASN1Encodable implements X9ObjectIdentifiers
{
    private DERObjectIdentifier id;
    private DERObject parameters;
    
    public X9FieldID(final BigInteger bigInteger) {
        this.id = X9FieldID.prime_field;
        this.parameters = new DERInteger(bigInteger);
    }
    
    public X9FieldID(final int n, final int n2, final int n3, final int n4) {
        this.id = X9FieldID.characteristic_two_field;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(n));
        if (n3 == 0) {
            asn1EncodableVector.add(X9FieldID.tpBasis);
            asn1EncodableVector.add(new DERInteger(n2));
        }
        else {
            asn1EncodableVector.add(X9FieldID.ppBasis);
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(new DERInteger(n2));
            asn1EncodableVector2.add(new DERInteger(n3));
            asn1EncodableVector2.add(new DERInteger(n4));
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        this.parameters = new DERSequence(asn1EncodableVector);
    }
    
    public X9FieldID(final ASN1Sequence asn1Sequence) {
        this.id = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.parameters = (DERObject)asn1Sequence.getObjectAt(1);
    }
    
    public DERObjectIdentifier getIdentifier() {
        return this.id;
    }
    
    public DERObject getParameters() {
        return this.parameters;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.id);
        asn1EncodableVector.add(this.parameters);
        return new DERSequence(asn1EncodableVector);
    }
}
