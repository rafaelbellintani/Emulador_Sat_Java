// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class InfoTypeAndValue extends ASN1Encodable
{
    private DERObjectIdentifier infoType;
    private ASN1Encodable infoValue;
    
    private InfoTypeAndValue(final ASN1Sequence asn1Sequence) {
        this.infoType = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.infoValue = (ASN1Encodable)asn1Sequence.getObjectAt(1);
        }
    }
    
    public static InfoTypeAndValue getInstance(final Object o) {
        if (o instanceof InfoTypeAndValue) {
            return (InfoTypeAndValue)o;
        }
        if (o instanceof ASN1Sequence) {
            return new InfoTypeAndValue((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public DERObjectIdentifier getInfoType() {
        return this.infoType;
    }
    
    public ASN1Encodable getInfoValue() {
        return this.infoValue;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.infoType);
        if (this.infoValue != null) {
            asn1EncodableVector.add(this.infoValue);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
