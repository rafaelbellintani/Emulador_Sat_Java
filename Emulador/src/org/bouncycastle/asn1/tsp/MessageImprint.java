// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class MessageImprint extends ASN1Encodable
{
    AlgorithmIdentifier hashAlgorithm;
    byte[] hashedMessage;
    
    public static MessageImprint getInstance(final Object o) {
        if (o == null || o instanceof MessageImprint) {
            return (MessageImprint)o;
        }
        if (o instanceof ASN1Sequence) {
            return new MessageImprint((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Bad object in factory.");
    }
    
    public MessageImprint(final ASN1Sequence asn1Sequence) {
        this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.hashedMessage = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(1)).getOctets();
    }
    
    public MessageImprint(final AlgorithmIdentifier hashAlgorithm, final byte[] hashedMessage) {
        this.hashAlgorithm = hashAlgorithm;
        this.hashedMessage = hashedMessage;
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public byte[] getHashedMessage() {
        return this.hashedMessage;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.hashAlgorithm);
        asn1EncodableVector.add(new DEROctetString(this.hashedMessage));
        return new DERSequence(asn1EncodableVector);
    }
}
