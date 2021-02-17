// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERSequence;
import java.util.Vector;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class NameConstraints extends ASN1Encodable
{
    private ASN1Sequence permitted;
    private ASN1Sequence excluded;
    
    public NameConstraints(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            switch (instance.getTagNo()) {
                case 0: {
                    this.permitted = ASN1Sequence.getInstance(instance, false);
                    continue;
                }
                case 1: {
                    this.excluded = ASN1Sequence.getInstance(instance, false);
                    continue;
                }
            }
        }
    }
    
    public NameConstraints(final Vector vector, final Vector vector2) {
        if (vector != null) {
            this.permitted = this.createSequence(vector);
        }
        if (vector2 != null) {
            this.excluded = this.createSequence(vector2);
        }
    }
    
    private DERSequence createSequence(final Vector vector) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<GeneralSubtree> elements = vector.elements();
        while (elements.hasMoreElements()) {
            asn1EncodableVector.add(elements.nextElement());
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    public ASN1Sequence getPermittedSubtrees() {
        return this.permitted;
    }
    
    public ASN1Sequence getExcludedSubtrees() {
        return this.excluded;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.permitted != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.permitted));
        }
        if (this.excluded != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.excluded));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
