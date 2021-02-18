// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.BERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class EncryptedContentInfo extends ASN1Encodable
{
    private DERObjectIdentifier contentType;
    private AlgorithmIdentifier contentEncryptionAlgorithm;
    private ASN1OctetString encryptedContent;
    
    public EncryptedContentInfo(final DERObjectIdentifier contentType, final AlgorithmIdentifier contentEncryptionAlgorithm, final ASN1OctetString encryptedContent) {
        this.contentType = contentType;
        this.contentEncryptionAlgorithm = contentEncryptionAlgorithm;
        this.encryptedContent = encryptedContent;
    }
    
    public EncryptedContentInfo(final ASN1Sequence asn1Sequence) {
        this.contentType = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.contentEncryptionAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() > 2) {
            this.encryptedContent = ASN1OctetString.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(2), false);
        }
    }
    
    public static EncryptedContentInfo getInstance(final Object o) {
        if (o == null || o instanceof EncryptedContentInfo) {
            return (EncryptedContentInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new EncryptedContentInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid EncryptedContentInfo: " + o.getClass().getName());
    }
    
    public DERObjectIdentifier getContentType() {
        return this.contentType;
    }
    
    public AlgorithmIdentifier getContentEncryptionAlgorithm() {
        return this.contentEncryptionAlgorithm;
    }
    
    public ASN1OctetString getEncryptedContent() {
        return this.encryptedContent;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.contentType);
        asn1EncodableVector.add(this.contentEncryptionAlgorithm);
        if (this.encryptedContent != null) {
            asn1EncodableVector.add(new BERTaggedObject(false, 0, this.encryptedContent));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
