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
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class KEKRecipientInfo extends ASN1Encodable
{
    private DERInteger version;
    private KEKIdentifier kekid;
    private AlgorithmIdentifier keyEncryptionAlgorithm;
    private ASN1OctetString encryptedKey;
    
    public KEKRecipientInfo(final KEKIdentifier kekid, final AlgorithmIdentifier keyEncryptionAlgorithm, final ASN1OctetString encryptedKey) {
        this.version = new DERInteger(4);
        this.kekid = kekid;
        this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
        this.encryptedKey = encryptedKey;
    }
    
    public KEKRecipientInfo(final ASN1Sequence asn1Sequence) {
        this.version = (DERInteger)asn1Sequence.getObjectAt(0);
        this.kekid = KEKIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.keyEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(2));
        this.encryptedKey = (ASN1OctetString)asn1Sequence.getObjectAt(3);
    }
    
    public static KEKRecipientInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static KEKRecipientInfo getInstance(final Object o) {
        if (o == null || o instanceof KEKRecipientInfo) {
            return (KEKRecipientInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new KEKRecipientInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid KEKRecipientInfo: " + o.getClass().getName());
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public KEKIdentifier getKekid() {
        return this.kekid;
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
        asn1EncodableVector.add(this.kekid);
        asn1EncodableVector.add(this.keyEncryptionAlgorithm);
        asn1EncodableVector.add(this.encryptedKey);
        return new DERSequence(asn1EncodableVector);
    }
}
