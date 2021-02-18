// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class OriginatorPublicKey extends ASN1Encodable
{
    private AlgorithmIdentifier algorithm;
    private DERBitString publicKey;
    
    public OriginatorPublicKey(final AlgorithmIdentifier algorithm, final byte[] array) {
        this.algorithm = algorithm;
        this.publicKey = new DERBitString(array);
    }
    
    public OriginatorPublicKey(final ASN1Sequence asn1Sequence) {
        this.algorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.publicKey = (DERBitString)asn1Sequence.getObjectAt(1);
    }
    
    public static OriginatorPublicKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static OriginatorPublicKey getInstance(final Object o) {
        if (o == null || o instanceof OriginatorPublicKey) {
            return (OriginatorPublicKey)o;
        }
        if (o instanceof ASN1Sequence) {
            return new OriginatorPublicKey((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid OriginatorPublicKey: " + o.getClass().getName());
    }
    
    public AlgorithmIdentifier getAlgorithm() {
        return this.algorithm;
    }
    
    public DERBitString getPublicKey() {
        return this.publicKey;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.algorithm);
        asn1EncodableVector.add(this.publicKey);
        return new DERSequence(asn1EncodableVector);
    }
}
