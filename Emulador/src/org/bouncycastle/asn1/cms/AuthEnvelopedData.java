// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class AuthEnvelopedData extends ASN1Encodable
{
    private DERInteger version;
    private OriginatorInfo originatorInfo;
    private ASN1Set recipientInfos;
    private EncryptedContentInfo authEncryptedContentInfo;
    private ASN1Set authAttrs;
    private ASN1OctetString mac;
    private ASN1Set unauthAttrs;
    
    public AuthEnvelopedData(final OriginatorInfo originatorInfo, final ASN1Set recipientInfos, final EncryptedContentInfo authEncryptedContentInfo, final ASN1Set authAttrs, final ASN1OctetString mac, final ASN1Set unauthAttrs) {
        this.version = new DERInteger(0);
        this.originatorInfo = originatorInfo;
        this.recipientInfos = recipientInfos;
        this.authEncryptedContentInfo = authEncryptedContentInfo;
        this.authAttrs = authAttrs;
        this.mac = mac;
        this.unauthAttrs = unauthAttrs;
    }
    
    public AuthEnvelopedData(final ASN1Sequence asn1Sequence) {
        int n = 0;
        this.version = (DERInteger)asn1Sequence.getObjectAt(n++).getDERObject();
        DERObject derObject = asn1Sequence.getObjectAt(n++).getDERObject();
        if (derObject instanceof ASN1TaggedObject) {
            this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)derObject, false);
            derObject = asn1Sequence.getObjectAt(n++).getDERObject();
        }
        this.recipientInfos = ASN1Set.getInstance(derObject);
        this.authEncryptedContentInfo = EncryptedContentInfo.getInstance(asn1Sequence.getObjectAt(n++).getDERObject());
        DERObject derObject2 = asn1Sequence.getObjectAt(n++).getDERObject();
        if (derObject2 instanceof ASN1TaggedObject) {
            this.authAttrs = ASN1Set.getInstance((ASN1TaggedObject)derObject2, false);
            derObject2 = asn1Sequence.getObjectAt(n++).getDERObject();
        }
        this.mac = ASN1OctetString.getInstance(derObject2);
        if (asn1Sequence.size() > n) {
            this.unauthAttrs = ASN1Set.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n++).getDERObject(), false);
        }
    }
    
    public static AuthEnvelopedData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static AuthEnvelopedData getInstance(final Object o) {
        if (o == null || o instanceof AuthEnvelopedData) {
            return (AuthEnvelopedData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AuthEnvelopedData((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid AuthEnvelopedData: " + o.getClass().getName());
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
    
    public EncryptedContentInfo getAuthEncryptedContentInfo() {
        return this.authEncryptedContentInfo;
    }
    
    public ASN1Set getAuthAttrs() {
        return this.authAttrs;
    }
    
    public ASN1OctetString getMac() {
        return this.mac;
    }
    
    public ASN1Set getUnauthAttrs() {
        return this.unauthAttrs;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        if (this.originatorInfo != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.originatorInfo));
        }
        asn1EncodableVector.add(this.recipientInfos);
        asn1EncodableVector.add(this.authEncryptedContentInfo);
        if (this.authAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.authAttrs));
        }
        asn1EncodableVector.add(this.mac);
        if (this.unauthAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.unauthAttrs));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
