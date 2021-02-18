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
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class EncryptedData extends ASN1Encodable
{
    private DERInteger version;
    private EncryptedContentInfo encryptedContentInfo;
    private ASN1Set unprotectedAttrs;
    
    public static EncryptedData getInstance(final Object o) {
        if (o instanceof EncryptedData) {
            return (EncryptedData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new EncryptedData((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid EncryptedData: " + o.getClass().getName());
    }
    
    public EncryptedData(final EncryptedContentInfo encryptedContentInfo) {
        this(encryptedContentInfo, null);
    }
    
    public EncryptedData(final EncryptedContentInfo encryptedContentInfo, final ASN1Set unprotectedAttrs) {
        this.version = new DERInteger((unprotectedAttrs == null) ? 0 : 2);
        this.encryptedContentInfo = encryptedContentInfo;
        this.unprotectedAttrs = unprotectedAttrs;
    }
    
    private EncryptedData(final ASN1Sequence asn1Sequence) {
        this.version = DERInteger.getInstance(asn1Sequence.getObjectAt(0));
        this.encryptedContentInfo = EncryptedContentInfo.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() == 3) {
            this.unprotectedAttrs = ASN1Set.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public DERInteger getVersion() {
        return this.version;
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
        asn1EncodableVector.add(this.encryptedContentInfo);
        if (this.unprotectedAttrs != null) {
            asn1EncodableVector.add(new BERTaggedObject(false, 1, this.unprotectedAttrs));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
