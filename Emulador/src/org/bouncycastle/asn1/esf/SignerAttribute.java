// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class SignerAttribute extends ASN1Encodable
{
    private ASN1Sequence claimedAttributes;
    private AttributeCertificate certifiedAttributes;
    
    public static SignerAttribute getInstance(final Object o) {
        if (o == null || o instanceof SignerAttribute) {
            return (SignerAttribute)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SignerAttribute(o);
        }
        throw new IllegalArgumentException("unknown object in 'SignerAttribute' factory: " + o.getClass().getName() + ".");
    }
    
    private SignerAttribute(final Object o) {
        final DERTaggedObject derTaggedObject = (DERTaggedObject)((ASN1Sequence)o).getObjectAt(0);
        if (derTaggedObject.getTagNo() == 0) {
            this.claimedAttributes = ASN1Sequence.getInstance(derTaggedObject, true);
        }
        else {
            if (derTaggedObject.getTagNo() != 1) {
                throw new IllegalArgumentException("illegal tag.");
            }
            this.certifiedAttributes = AttributeCertificate.getInstance(derTaggedObject);
        }
    }
    
    public SignerAttribute(final ASN1Sequence claimedAttributes) {
        this.claimedAttributes = claimedAttributes;
    }
    
    public SignerAttribute(final AttributeCertificate certifiedAttributes) {
        this.certifiedAttributes = certifiedAttributes;
    }
    
    public ASN1Sequence getClaimedAttributes() {
        return this.claimedAttributes;
    }
    
    public AttributeCertificate getCertifiedAttributes() {
        return this.certifiedAttributes;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.claimedAttributes != null) {
            asn1EncodableVector.add(new DERTaggedObject(0, this.claimedAttributes));
        }
        else {
            asn1EncodableVector.add(new DERTaggedObject(1, this.certifiedAttributes));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
