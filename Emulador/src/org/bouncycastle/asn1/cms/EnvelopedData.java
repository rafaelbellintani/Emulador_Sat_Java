// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class EnvelopedData extends ASN1Encodable
{
    private DERInteger version;
    private OriginatorInfo originatorInfo;
    private ASN1Set recipientInfos;
    private EncryptedContentInfo encryptedContentInfo;
    private ASN1Set unprotectedAttrs;
    
    public EnvelopedData(final OriginatorInfo originatorInfo, final ASN1Set recipientInfos, final EncryptedContentInfo encryptedContentInfo, final ASN1Set unprotectedAttrs) {
        if (originatorInfo != null || unprotectedAttrs != null) {
            this.version = new DERInteger(2);
        }
        else {
            this.version = new DERInteger(0);
            final Enumeration objects = recipientInfos.getObjects();
            while (objects.hasMoreElements()) {
                if (!RecipientInfo.getInstance(objects.nextElement()).getVersion().equals(this.version)) {
                    this.version = new DERInteger(2);
                    break;
                }
            }
        }
        this.originatorInfo = originatorInfo;
        this.recipientInfos = recipientInfos;
        this.encryptedContentInfo = encryptedContentInfo;
        this.unprotectedAttrs = unprotectedAttrs;
    }
    
    public EnvelopedData(final ASN1Sequence asn1Sequence) {
        int n = 0;
        this.version = (DERInteger)asn1Sequence.getObjectAt(n++);
        DEREncodable derEncodable = asn1Sequence.getObjectAt(n++);
        if (derEncodable instanceof ASN1TaggedObject) {
            this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)derEncodable, false);
            derEncodable = asn1Sequence.getObjectAt(n++);
        }
        this.recipientInfos = ASN1Set.getInstance(derEncodable);
        this.encryptedContentInfo = EncryptedContentInfo.getInstance(asn1Sequence.getObjectAt(n++));
        if (asn1Sequence.size() > n) {
            this.unprotectedAttrs = ASN1Set.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n), false);
        }
    }
    
    public static EnvelopedData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static EnvelopedData getInstance(final Object o) {
        if (o == null || o instanceof EnvelopedData) {
            return (EnvelopedData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new EnvelopedData((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid EnvelopedData: " + o.getClass().getName());
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public OriginatorInfo getOriginatorInfo() {
        return this.originatorInfo;
    }
    
    public ASN1Set getRecipientInfos() {
        return this.recipientInfos;
    }
    
    public EncryptedContentInfo getEncryptedContentInfo() {
        return this.encryptedContentInfo;
    }
    
    public ASN1Set getUnprotectedAttrs() {
        return this.unprotectedAttrs;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        if (this.originatorInfo != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.originatorInfo));
        }
        asn1EncodableVector.add(this.recipientInfos);
        asn1EncodableVector.add(this.encryptedContentInfo);
        if (this.unprotectedAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.unprotectedAttrs));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
