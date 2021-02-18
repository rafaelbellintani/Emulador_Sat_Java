// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class SafeBag extends ASN1Encodable
{
    DERObjectIdentifier bagId;
    DERObject bagValue;
    ASN1Set bagAttributes;
    
    public SafeBag(final DERObjectIdentifier bagId, final DERObject bagValue) {
        this.bagId = bagId;
        this.bagValue = bagValue;
        this.bagAttributes = null;
    }
    
    public SafeBag(final DERObjectIdentifier bagId, final DERObject bagValue, final ASN1Set bagAttributes) {
        this.bagId = bagId;
        this.bagValue = bagValue;
        this.bagAttributes = bagAttributes;
    }
    
    public SafeBag(final ASN1Sequence asn1Sequence) {
        this.bagId = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.bagValue = ((DERTaggedObject)asn1Sequence.getObjectAt(1)).getObject();
        if (asn1Sequence.size() == 3) {
            this.bagAttributes = (ASN1Set)asn1Sequence.getObjectAt(2);
        }
    }
    
    public DERObjectIdentifier getBagId() {
        return this.bagId;
    }
    
    public DERObject getBagValue() {
        return this.bagValue;
    }
    
    public ASN1Set getBagAttributes() {
        return this.bagAttributes;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.bagId);
        asn1EncodableVector.add(new DERTaggedObject(0, this.bagValue));
        if (this.bagAttributes != null) {
            asn1EncodableVector.add(this.bagAttributes);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
