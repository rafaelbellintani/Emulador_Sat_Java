// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1Sequence extends ASN1Object
{
    private Vector seq;
    
    public ASN1Sequence() {
        this.seq = new Vector();
    }
    
    public static ASN1Sequence getInstance(final Object o) {
        if (o == null || o instanceof ASN1Sequence) {
            return (ASN1Sequence)o;
        }
        throw new IllegalArgumentException("unknown object in getInstance: " + o.getClass().getName());
    }
    
    public static ASN1Sequence getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (b) {
            if (!asn1TaggedObject.isExplicit()) {
                throw new IllegalArgumentException("object implicit - explicit expected.");
            }
            return (ASN1Sequence)asn1TaggedObject.getObject();
        }
        else if (asn1TaggedObject.isExplicit()) {
            if (asn1TaggedObject instanceof BERTaggedObject) {
                return new BERSequence(asn1TaggedObject.getObject());
            }
            return new DERSequence(asn1TaggedObject.getObject());
        }
        else {
            if (asn1TaggedObject.getObject() instanceof ASN1Sequence) {
                return (ASN1Sequence)asn1TaggedObject.getObject();
            }
            throw new IllegalArgumentException("unknown object in getInstance: " + asn1TaggedObject.getClass().getName());
        }
    }
    
    public Enumeration getObjects() {
        return this.seq.elements();
    }
    
    public ASN1SequenceParser parser() {
        return new ASN1SequenceParser() {
            private final int max = ASN1Sequence.this.size();
            private int index;
            
            @Override
            public DEREncodable readObject() throws IOException {
                if (this.index == this.max) {
                    return null;
                }
                final DEREncodable object = ASN1Sequence.this.getObjectAt(this.index++);
                if (object instanceof ASN1Sequence) {
                    return ((ASN1Sequence)object).parser();
                }
                if (object instanceof ASN1Set) {
                    return ((ASN1Set)object).parser();
                }
                return object;
            }
            
            @Override
            public DERObject getDERObject() {
                return ASN1Sequence.this;
            }
        };
    }
    
    public DEREncodable getObjectAt(final int index) {
        return this.seq.elementAt(index);
    }
    
    public int size() {
        return this.seq.size();
    }
    
    @Override
    public int hashCode() {
        final Enumeration objects = this.getObjects();
        int size = this.size();
        while (objects.hasMoreElements()) {
            final Object nextElement = objects.nextElement();
            size *= 17;
            if (nextElement != null) {
                size ^= nextElement.hashCode();
            }
        }
        return size;
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        if (!(derObject instanceof ASN1Sequence)) {
            return false;
        }
        final ASN1Sequence asn1Sequence = (ASN1Sequence)derObject;
        if (this.size() != asn1Sequence.size()) {
            return false;
        }
        final Enumeration objects = this.getObjects();
        final Enumeration objects2 = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final DERObject derObject2 = objects.nextElement().getDERObject();
            final DERObject derObject3 = objects2.nextElement().getDERObject();
            if (derObject2 != derObject3) {
                if (derObject2 != null && derObject2.equals(derObject3)) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
    
    protected void addObject(final DEREncodable obj) {
        this.seq.addElement(obj);
    }
    
    @Override
    abstract void encode(final DEROutputStream p0) throws IOException;
    
    @Override
    public String toString() {
        return this.seq.toString();
    }
}
