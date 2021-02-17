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
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class PasswordRecipientInfo extends ASN1Encodable
{
    private DERInteger version;
    private AlgorithmIdentifier keyDerivationAlgorithm;
    private AlgorithmIdentifier keyEncryptionAlgorithm;
    private ASN1OctetString encryptedKey;
    
    public PasswordRecipientInfo(final AlgorithmIdentifier keyEncryptionAlgorithm, final ASN1OctetString encryptedKey) {
        this.version = new DERInteger(0);
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        this.encryptedKey = encryptedKey;
    }
    
    public PasswordRecipientInfo(final AlgorithmIdentifier keyDerivationAlgorithm, final AlgorithmIdentifier keyEncryptionAlgorithm, final ASN1OctetString encryptedKey) {
        this.version = new DERInteger(0);
        this.keyDerivationAlgorithm = keyDerivationAlgorithm;
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        this.encryptedKey = encryptedKey;
    }
    
    public PasswordRecipientInfo(final ASN1Sequence asn1Sequence) {
        this.version = (DERInteger)asn1Sequence.getObjectAt(0);
        if (asn1Sequence.getObjectAt(1) instanceof ASN1TaggedObject) {
            this.keyDerivationAlgorithm = AlgorithmIdentifier.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), false);
            this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(2));
            this.encryptedKey = (ASN1OctetString)asn1Sequence.getObjectAt(3);
        }
        else {
            this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.encryptedKey = (ASN1OctetString)asn1Sequence.getObjectAt(2);
        }
    }
    
    public static PasswordRecipientInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static PasswordRecipientInfo getInstance(final Object o) {
        if (o == null || o instanceof PasswordRecipientInfo) {
            return (PasswordRecipientInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PasswordRecipientInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid PasswordRecipientInfo: " + o.getClass().getName());
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public AlgorithmIdentifier getKeyDerivationAlgorithm() {
        return this.keyDerivationAlgorithm;
    }
    
    public AlgorithmIdentifier getKeyEncryptionAlgorithm() {
        return this.keyEncryptionAlgorithm;
    }
    
    public ASN1OctetString getEncryptedKey() {
        return this.encryptedKey;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        if (this.keyDerivationAlgorithm != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.keyDerivationAlgorithm));
        }
        asn1EncodableVector.add(this.keyEncryptionAlgorithm);
        asn1EncodableVector.add(this.encryptedKey);
        return new DERSequence(asn1EncodableVector);
    }
}
