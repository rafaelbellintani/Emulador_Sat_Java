// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cms;

import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.util.Vector;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEREncodableVector;
import java.util.Hashtable;

public class AttributeTable
{
    private Hashtable attributes;
    
    public AttributeTable(final Hashtable hashtable) {
        this.attributes = new Hashtable();
        this.attributes = this.copyTable(hashtable);
    }
    
    public AttributeTable(final DEREncodableVector derEncodableVector) {
        this.attributes = new Hashtable();
        for (int i = 0; i != derEncodableVector.size(); ++i) {
            final Attribute instance = Attribute.getInstance(derEncodableVector.get(i));
            this.addAttribute(instance.getAttrType(), instance);
        }
    }
    
    public AttributeTable(final ASN1Set set) {
        this.attributes = new Hashtable();
        for (int i = 0; i != set.size(); ++i) {
            final Attribute instance = Attribute.getInstance(set.getObjectAt(i));
            this.addAttribute(instance.getAttrType(), instance);
        }
    }
    
    private void addAttribute(final DERObjectIdentifier key, final Attribute obj) {
        final Attribute value = this.attributes.get(key);
        if (value == null) {
            this.attributes.put(key, obj);
        }
        else {
            Vector<Attribute> value2;
            if (value instanceof Attribute) {
                value2 = new Vector<Attribute>();
                value2.addElement(value);
                value2.addElement(obj);
            }
            else {
                value2 = (Vector<Attribute>)value;
                value2.addElement(obj);
            }
            this.attributes.put(key, value2);
        }
    }
    
    public Attribute get(final DERObjectIdentifier key) {
        final Attribute value = this.attributes.get(key);
        if (value instanceof Vector) {
            return ((Vector<Attribute>)value).elementAt(0);
        }
        return value;
    }
    
    public ASN1EncodableVector getAll(final DERObjectIdentifier key) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Attribute value = this.attributes.get(key);
        if (value instanceof Vector) {
            final Enumeration<Attribute> elements = ((Vector<Attribute>)value).elements();
            while (elements.hasMoreElements()) {
                asn1EncodableVector.add(elements.nextElement());
            }
        }
        else if (value != null) {
            asn1EncodableVector.add(value);
        }
        return asn1EncodableVector;
    }
    
    public Hashtable toHashtable() {
        return this.copyTable(this.attributes);
    }
    
    public ASN1EncodableVector toASN1EncodableVector() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<Vector<Object>> elements = this.attributes.elements();
        while (elements.hasMoreElements()) {
            final Vector<Object> nextElement = elements.nextElement();
            if (nextElement instanceof Vector) {
                final Enumeration<Object> elements2 = nextElement.elements();
                while (elements2.hasMoreElements()) {
                    asn1EncodableVector.add(Attribute.getInstance(elements2.nextElement()));
                }
            }
            else {
                asn1EncodableVector.add(Attribute.getInstance(nextElement));
            }
        }
        return asn1EncodableVector;
    }
    
    private Hashtable copyTable(final Hashtable hashtable) {
        final Hashtable<Object, Object> hashtable2 = new Hashtable<Object, Object>();
        final Enumeration<Object> keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            final Object nextElement = keys.nextElement();
            hashtable2.put(nextElement, hashtable.get(nextElement));
        }
        return hashtable2;
    }
}
