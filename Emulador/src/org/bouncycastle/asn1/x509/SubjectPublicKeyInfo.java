// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1Encodable;

public class SubjectPublicKeyInfo extends ASN1Encodable
{
    private AlgorithmIdentifier algId;
    private DERBitString keyData;
    
    public static SubjectPublicKeyInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static SubjectPublicKeyInfo getInstance(final Object o) {
        if (o instanceof SubjectPublicKeyInfo) {
            return (SubjectPublicKeyInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SubjectPublicKeyInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public SubjectPublicKeyInfo(final AlgorithmIdentifier algId, final DEREncodable derEncodable) {
        this.keyData = new DERBitString(derEncodable);
        this.algId = algId;
    }
    
    public SubjectPublicKeyInfo(final AlgorithmIdentifier algId, final byte[] array) {
        this.keyData = new DERBitString(array);
        this.algId = algId;
    }
    
    public SubjectPublicKeyInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        this.algId = AlgorithmIdentifier.getInstance(objects.nextElement());
        this.keyData = DERBitString.getInstance(objects.nextElement());
    }
    
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }
    
    public DERObject getPublicKey() throws IOException {
        return new ASN1InputStream(this.keyData.getBytes()).readObject();
    }
    
    public DERBitString getPublicKeyData() {
        return this.keyData;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.algId);
        asn1EncodableVector.add(this.keyData);
        return new DERSequence(asn1EncodableVector);
    }
}
