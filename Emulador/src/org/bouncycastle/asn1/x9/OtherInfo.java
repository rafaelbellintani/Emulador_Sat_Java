// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Encodable;

public class OtherInfo extends ASN1Encodable
{
    private KeySpecificInfo keyInfo;
    private ASN1OctetString partyAInfo;
    private ASN1OctetString suppPubInfo;
    
    public OtherInfo(final KeySpecificInfo keyInfo, final ASN1OctetString partyAInfo, final ASN1OctetString suppPubInfo) {
        this.keyInfo = keyInfo;
        this.partyAInfo = partyAInfo;
        this.suppPubInfo = suppPubInfo;
    }
    
    public OtherInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.keyInfo = new KeySpecificInfo(objects.nextElement());
        while (objects.hasMoreElements()) {
            final DERTaggedObject derTaggedObject = (DERTaggedObject)objects.nextElement();
            if (derTaggedObject.getTagNo() == 0) {
                this.partyAInfo = (ASN1OctetString)derTaggedObject.getObject();
            }
            else {
                if (derTaggedObject.getTagNo() != 2) {
                    continue;
                }
                this.suppPubInfo = (ASN1OctetString)derTaggedObject.getObject();
            }
        }
    }
    
    public KeySpecificInfo getKeyInfo() {
        return this.keyInfo;
    }
    
    public ASN1OctetString getPartyAInfo() {
        return this.partyAInfo;
    }
    
    public ASN1OctetString getSuppPubInfo() {
        return this.suppPubInfo;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.keyInfo);
        if (this.partyAInfo != null) {
            asn1EncodableVector.add(new DERTaggedObject(0, this.partyAInfo));
        }
        asn1EncodableVector.add(new DERTaggedObject(2, this.suppPubInfo));
        return new DERSequence(asn1EncodableVector);
    }
}
