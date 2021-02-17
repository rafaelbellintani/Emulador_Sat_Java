// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class OtherRecipientInfo extends ASN1Encodable
{
    private DERObjectIdentifier oriType;
    private DEREncodable oriValue;
    
    public OtherRecipientInfo(final DERObjectIdentifier oriType, final DEREncodable oriValue) {
        this.oriType = oriType;
        this.oriValue = oriValue;
    }
    
    public OtherRecipientInfo(final ASN1Sequence asn1Sequence) {
        this.oriType = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.oriValue = asn1Sequence.getObjectAt(1);
    }
    
    public static OtherRecipientInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static OtherRecipientInfo getInstance(final Object o) {
        if (o == null || o instanceof OtherRecipientInfo) {
            return (OtherRecipientInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new OtherRecipientInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid OtherRecipientInfo: " + o.getClass().getName());
    }
    
    public DERObjectIdentifier getType() {
        return this.oriType;
    }
    
    public DEREncodable getValue() {
        return this.oriValue;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.oriType);
        asn1EncodableVector.add(this.oriValue);
        return new DERSequence(asn1EncodableVector);
    }
}
