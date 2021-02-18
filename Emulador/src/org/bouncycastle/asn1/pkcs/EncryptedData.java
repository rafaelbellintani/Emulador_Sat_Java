// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class EncryptedData extends ASN1Encodable
{
    ASN1Sequence data;
    DERObjectIdentifier bagId;
    DERObject bagValue;
    
    public static EncryptedData getInstance(final Object o) {
        if (o instanceof EncryptedData) {
            return (EncryptedData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new EncryptedData((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public EncryptedData(final ASN1Sequence asn1Sequence) {
        if (((DERInteger)asn1Sequence.getObjectAt(0)).getValue().intValue() != 0) {
            throw new IllegalArgumentException("sequence not version 0");
        }
        this.data = (ASN1Sequence)asn1Sequence.getObjectAt(1);
    }
    
    public EncryptedData(final DERObjectIdentifier derObjectIdentifier, final AlgorithmIdentifier algorithmIdentifier, final DEREncodable derEncodable) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(derObjectIdentifier);
        asn1EncodableVector.add(algorithmIdentifier.getDERObject());
        asn1EncodableVector.add(new BERTaggedObject(false, 0, derEncodable));
        this.data = new BERSequence(asn1EncodableVector);
    }
    
    public DERObjectIdentifier getContentType() {
        return (DERObjectIdentifier)this.data.getObjectAt(0);
    }
    
    public AlgorithmIdentifier getEncryptionAlgorithm() {
        return AlgorithmIdentifier.getInstance(this.data.getObjectAt(1));
    }
    
    public ASN1OctetString getContent() {
        if (this.data.size() == 3) {
            return ASN1OctetString.getInstance(((DERTaggedObject)this.data.getObjectAt(2)).getObject());
        }
        return null;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(0));
        asn1EncodableVector.add(this.data);
        return new BERSequence(asn1EncodableVector);
    }
}
