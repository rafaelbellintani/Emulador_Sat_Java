// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.ASN1Encodable;

public class ObjectDigestInfo extends ASN1Encodable
{
    public static final int publicKey = 0;
    public static final int publicKeyCert = 1;
    public static final int otherObjectDigest = 2;
    DEREnumerated digestedObjectType;
    DERObjectIdentifier otherObjectTypeID;
    AlgorithmIdentifier digestAlgorithm;
    DERBitString objectDigest;
    
    public static ObjectDigestInfo getInstance(final Object o) {
        if (o == null || o instanceof ObjectDigestInfo) {
            return (ObjectDigestInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ObjectDigestInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static ObjectDigestInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ObjectDigestInfo(final int n, final String s, final AlgorithmIdentifier digestAlgorithm, final byte[] array) {
        this.digestedObjectType = new DEREnumerated(n);
        if (n == 2) {
            this.otherObjectTypeID = new DERObjectIdentifier(s);
        }
        this.digestAlgorithm = digestAlgorithm;
        this.objectDigest = new DERBitString(array);
    }
    
    private ObjectDigestInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() > 4 || asn1Sequence.size() < 3) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.digestedObjectType = DEREnumerated.getInstance(asn1Sequence.getObjectAt(0));
        int n = 0;
        if (asn1Sequence.size() == 4) {
            this.otherObjectTypeID = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            ++n;
        }
        this.digestAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1 + n));
        this.objectDigest = DERBitString.getInstance(asn1Sequence.getObjectAt(2 + n));
    }
    
    public DEREnumerated getDigestedObjectType() {
        return this.digestedObjectType;
    }
    
    public DERObjectIdentifier getOtherObjectTypeID() {
        return this.otherObjectTypeID;
    }
    
    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }
    
    public DERBitString getObjectDigest() {
        return this.objectDigest;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.digestedObjectType);
        if (this.otherObjectTypeID != null) {
            asn1EncodableVector.add(this.otherObjectTypeID);
        }
        asn1EncodableVector.add(this.digestAlgorithm);
        asn1EncodableVector.add(this.objectDigest);
        return new DERSequence(asn1EncodableVector);
    }
}
