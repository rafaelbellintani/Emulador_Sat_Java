// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class Pfx extends ASN1Encodable implements PKCSObjectIdentifiers
{
    private ContentInfo contentInfo;
    private MacData macData;
    
    public Pfx(final ASN1Sequence asn1Sequence) {
        this.macData = null;
        if (((DERInteger)asn1Sequence.getObjectAt(0)).getValue().intValue() != 3) {
            throw new IllegalArgumentException("wrong version for PFX PDU");
        }
        this.contentInfo = ContentInfo.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() == 3) {
            this.macData = MacData.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public Pfx(final ContentInfo contentInfo, final MacData macData) {
        this.macData = null;
        this.contentInfo = contentInfo;
        this.macData = macData;
    }
    
    public ContentInfo getAuthSafe() {
        return this.contentInfo;
    }
    
    public MacData getMacData() {
        return this.macData;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(3));
        asn1EncodableVector.add(this.contentInfo);
        if (this.macData != null) {
            asn1EncodableVector.add(this.macData);
        }
        return new BERSequence(asn1EncodableVector);
    }
}
