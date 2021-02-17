// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;

public class SubjectDirectoryAttributes extends ASN1Encodable
{
    private Vector attributes;
    
    public static SubjectDirectoryAttributes getInstance(final Object o) {
        if (o == null || o instanceof SubjectDirectoryAttributes) {
            return (SubjectDirectoryAttributes)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SubjectDirectoryAttributes((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public SubjectDirectoryAttributes(final ASN1Sequence asn1Sequence) {
        this.attributes = new Vector();
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            this.attributes.addElement(new Attribute(ASN1Sequence.getInstance(objects.nextElement())));
        }
    }
    
    public SubjectDirectoryAttributes(final Vector vector) {
        this.attributes = new Vector();
        final Enumeration<Object> elements = vector.elements();
        while (elements.hasMoreElements()) {
            this.attributes.addElement(elements.nextElement());
        }
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<Attribute> elements = this.attributes.elements();
        while (elements.hasMoreElements()) {
            asn1EncodableVector.add(elements.nextElement());
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    public Vector getAttributes() {
        return this.attributes;
    }
}
