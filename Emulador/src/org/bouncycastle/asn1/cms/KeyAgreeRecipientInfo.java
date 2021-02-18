// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class KeyAgreeRecipientInfo extends ASN1Encodable
{
    private DERInteger version;
    private OriginatorIdentifierOrKey originator;
    private ASN1OctetString ukm;
    private AlgorithmIdentifier keyEncryptionAlgorithm;
    private ASN1Sequence recipientEncryptedKeys;
    
    public KeyAgreeRecipientInfo(final OriginatorIdentifierOrKey originator, final ASN1OctetString ukm, final AlgorithmIdentifier keyEncryptionAlgorithm, final ASN1Sequence recipientEncryptedKeys) {
        this.version = new DERInteger(3);
        this.originator = originator;
        this.ukm = ukm;
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        this.recipientEncryptedKeys = recipientEncryptedKeys;
    }
    
    public KeyAgreeRecipientInfo(final ASN1Sequence asn1Sequence) {
        int n = 0;
        this.version = (DERInteger)asn1Sequence.getObjectAt(n++);
        this.originator = OriginatorIdentifierOrKey.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n++), true);
        if (asn1Sequence.getObjectAt(n) instanceof ASN1TaggedObject) {
            this.ukm = ASN1OctetString.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n++), true);
        }
        this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n++));
        this.recipientEncryptedKeys = (ASN1Sequence)asn1Sequence.getObjectAt(n++);
    }
    
    public static KeyAgreeRecipientInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static KeyAgreeRecipientInfo getInstance(final Object o) {
        if (o == null || o instanceof KeyAgreeRecipientInfo) {
            return (KeyAgreeRecipientInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new KeyAgreeRecipientInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Illegal object in KeyAgreeRecipientInfo: " + o.getClass().getName());
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public OriginatorIdentifierOrKey getOriginator() {
        return this.originator;
    }
    
    public ASN1OctetString getUserKeyingMaterial() {
        return this.ukm;
    }
    
    public AlgorithmIdentifier getKeyEncryptionAlgorithm() {
        return this.keyEncryptionAlgorithm;
    }
    
    public ASN1Sequence getRecipientEncryptedKeys() {
        return this.recipientEncryptedKeys;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(new DERTaggedObject(true, 0, this.originator));
        if (this.ukm != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.ukm));
        }
        asn1EncodableVector.add(this.keyEncryptionAlgorithm);
        asn1EncodableVector.add(this.recipientEncryptedKeys);
        return new DERSequence(asn1EncodableVector);
    }
}
