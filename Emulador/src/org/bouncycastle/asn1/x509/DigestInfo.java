// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Encodable;

public class DigestInfo extends ASN1Encodable
{
    private byte[] digest;
    private AlgorithmIdentifier algId;
    
    public static DigestInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static DigestInfo getInstance(final Object o) {
        if (o instanceof DigestInfo) {
            return (DigestInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new DigestInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public DigestInfo(final AlgorithmIdentifier algId, final byte[] digest) {
        this.digest = digest;
        this.algId = algId;
    }
    
    public DigestInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.algId = AlgorithmIdentifier.getInstance(objects.nextElement());
        this.digest = ASN1OctetString.getInstance(objects.nextElement()).getOctets();
    }
    
    public AlgorithmIdentifier getAlgorithmId() {
        return this.algId;
    }
    
    public byte[] getDigest() {
        return this.digest;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.algId);
        asn1EncodableVector.add(new DEROctetString(this.digest));
        return new DERSequence(asn1EncodableVector);
    }
}
