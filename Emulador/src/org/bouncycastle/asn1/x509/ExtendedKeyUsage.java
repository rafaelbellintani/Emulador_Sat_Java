// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.util.Vector;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import java.util.Hashtable;
import org.bouncycastle.asn1.ASN1Encodable;

public class ExtendedKeyUsage extends ASN1Encodable
{
    Hashtable usageTable;
    ASN1Sequence seq;
    
    public static ExtendedKeyUsage getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static ExtendedKeyUsage getInstance(final Object o) {
        if (o instanceof ExtendedKeyUsage) {
            return (ExtendedKeyUsage)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ExtendedKeyUsage((ASN1Sequence)o);
        }
        if (o instanceof X509Extension) {
            return getInstance(X509Extension.convertValueToObject((X509Extension)o));
        }
        throw new IllegalArgumentException("Invalid ExtendedKeyUsage: " + o.getClass().getName());
    }
    
    public ExtendedKeyUsage(final KeyPurposeId keyPurposeId) {
        this.usageTable = new Hashtable();
        this.seq = new DERSequence(keyPurposeId);
        this.usageTable.put(keyPurposeId, keyPurposeId);
    }
    
    public ExtendedKeyUsage(final ASN1Sequence seq) {
        this.usageTable = new Hashtable();
        this.seq = seq;
        final Enumeration objects = seq.getObjects();
        while (objects.hasMoreElements()) {
            final Object nextElement = objects.nextElement();
            if (!(nextElement instanceof DERObjectIdentifier)) {
                throw new IllegalArgumentException("Only DERObjectIdentifiers allowed in ExtendedKeyUsage.");
            }
            this.usageTable.put(nextElement, nextElement);
        }
    }
    
    public ExtendedKeyUsage(final Vector vector) {
        this.usageTable = new Hashtable();
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<DERObject> elements = vector.elements();
        while (elements.hasMoreElements()) {
            final DERObject derObject = elements.nextElement();
            asn1EncodableVector.add(derObject);
            this.usageTable.put(derObject, derObject);
        }
        this.seq = new DERSequence(asn1EncodableVector);
    }
    
    public boolean hasKeyPurposeId(final KeyPurposeId key) {
        return this.usageTable.get(key) != null;
    }
    
    public Vector getUsages() {
        final Vector<Object> vector = new Vector<Object>();
        final Enumeration<Object> elements = this.usageTable.elements();
        while (elements.hasMoreElements()) {
            vector.addElement(elements.nextElement());
        }
        return vector;
    }
    
    public int size() {
        return this.usageTable.size();
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.seq;
    }
}
