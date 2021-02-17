// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class PKIPublicationInfo extends ASN1Encodable
{
    private DERInteger action;
    private ASN1Sequence pubInfos;
    
    private PKIPublicationInfo(final ASN1Sequence asn1Sequence) {
        this.action = DERInteger.getInstance(asn1Sequence.getObjectAt(0));
        this.pubInfos = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public static PKIPublicationInfo getInstance(final Object o) {
        if (o instanceof PKIPublicationInfo) {
            return (PKIPublicationInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PKIPublicationInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERInteger getAction() {
        return this.action;
    }
    
    public SinglePubInfo[] getPubInfos() {
        if (this.pubInfos == null) {
            return null;
        }
        final SinglePubInfo[] array = new SinglePubInfo[this.pubInfos.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = SinglePubInfo.getInstance(this.pubInfos.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.action);
        asn1EncodableVector.add(this.pubInfos);
        return new DERSequence(asn1EncodableVector);
    }
}
