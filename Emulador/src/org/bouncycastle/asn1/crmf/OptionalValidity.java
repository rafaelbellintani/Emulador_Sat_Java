// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.asn1.ASN1Encodable;

public class OptionalValidity extends ASN1Encodable
{
    private Time notBefore;
    private Time notAfter;
    
    private OptionalValidity(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            if (asn1TaggedObject.getTagNo() == 0) {
                this.notBefore = Time.getInstance(asn1TaggedObject, true);
            }
            else {
                this.notAfter = Time.getInstance(asn1TaggedObject, true);
            }
        }
    }
    
    public static OptionalValidity getInstance(final Object o) {
        if (o instanceof OptionalValidity) {
            return (OptionalValidity)o;
        }
        if (o instanceof ASN1Sequence) {
            return new OptionalValidity((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.notBefore != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.notBefore));
        }
        if (this.notAfter != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.notAfter));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
