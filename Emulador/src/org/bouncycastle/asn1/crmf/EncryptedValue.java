// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class EncryptedValue extends ASN1Encodable
{
    private AlgorithmIdentifier intendedAlg;
    private AlgorithmIdentifier symmAlg;
    private DERBitString encSymmKey;
    private AlgorithmIdentifier keyAlg;
    private ASN1OctetString valueHint;
    private DERBitString encValue;
    
    private EncryptedValue(final ASN1Sequence asn1Sequence) {
        int n;
        for (n = 0; asn1Sequence.getObjectAt(n) instanceof ASN1TaggedObject; ++n) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(n);
            switch (asn1TaggedObject.getTagNo()) {
                case 0: {
                    this.intendedAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, false);
                    break;
                }
                case 1: {
                    this.symmAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, false);
                    break;
                }
                case 2: {
                    this.encSymmKey = DERBitString.getInstance(asn1TaggedObject, false);
                    break;
                }
                case 3: {
                    this.keyAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, false);
                    break;
                }
                case 4: {
                    this.valueHint = ASN1OctetString.getInstance(asn1TaggedObject, false);
                    break;
                }
            }
        }
        this.encValue = DERBitString.getInstance(asn1Sequence.getObjectAt(n));
    }
    
    public static EncryptedValue getInstance(final Object o) {
        if (o instanceof EncryptedValue) {
            return (EncryptedValue)o;
        }
        if (o instanceof ASN1Sequence) {
            return new EncryptedValue((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        this.addOptional(asn1EncodableVector, 0, this.intendedAlg);
        this.addOptional(asn1EncodableVector, 1, this.symmAlg);
        this.addOptional(asn1EncodableVector, 2, this.encSymmKey);
        this.addOptional(asn1EncodableVector, 3, this.keyAlg);
        this.addOptional(asn1EncodableVector, 4, this.valueHint);
        asn1EncodableVector.add(this.encValue);
        return new DERSequence(asn1EncodableVector);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, n, asn1Encodable));
        }
    }
}
