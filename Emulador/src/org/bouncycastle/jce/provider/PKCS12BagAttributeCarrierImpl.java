// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.ASN1InputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.Vector;
import java.util.Hashtable;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;

class PKCS12BagAttributeCarrierImpl implements PKCS12BagAttributeCarrier
{
    private Hashtable pkcs12Attributes;
    private Vector pkcs12Ordering;
    
    PKCS12BagAttributeCarrierImpl(final Hashtable pkcs12Attributes, final Vector pkcs12Ordering) {
        this.pkcs12Attributes = pkcs12Attributes;
        this.pkcs12Ordering = pkcs12Ordering;
    }
    
    public PKCS12BagAttributeCarrierImpl() {
        this(new Hashtable(), new Vector());
    }
    
    @Override
    public void setBagAttribute(final DERObjectIdentifier derObjectIdentifier, final DEREncodable derEncodable) {
        if (this.pkcs12Attributes.containsKey(derObjectIdentifier)) {
            this.pkcs12Attributes.put(derObjectIdentifier, derEncodable);
        }
        else {
            this.pkcs12Attributes.put(derObjectIdentifier, derEncodable);
            this.pkcs12Ordering.addElement(derObjectIdentifier);
        }
    }
    
    @Override
    public DEREncodable getBagAttribute(final DERObjectIdentifier key) {
        return this.pkcs12Attributes.get(key);
    }
    
    @Override
    public Enumeration getBagAttributeKeys() {
        return this.pkcs12Ordering.elements();
    }
    
    int size() {
        return this.pkcs12Ordering.size();
    }
    
    Hashtable getAttributes() {
        return this.pkcs12Attributes;
    }
    
    Vector getOrdering() {
        return this.pkcs12Ordering;
    }
    
    public void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        if (this.pkcs12Ordering.size() == 0) {
            objectOutputStream.writeObject(new Hashtable());
            objectOutputStream.writeObject(new Vector());
        }
        else {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final ASN1OutputStream asn1OutputStream = new ASN1OutputStream(byteArrayOutputStream);
            final Enumeration bagAttributeKeys = this.getBagAttributeKeys();
            while (bagAttributeKeys.hasMoreElements()) {
                final DERObjectIdentifier key = bagAttributeKeys.nextElement();
                asn1OutputStream.writeObject(key);
                asn1OutputStream.writeObject(this.pkcs12Attributes.get(key));
            }
            objectOutputStream.writeObject(byteArrayOutputStream.toByteArray());
        }
    }
    
    public void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        final Object object = objectInputStream.readObject();
        if (object instanceof Hashtable) {
            this.pkcs12Attributes = (Hashtable)object;
            this.pkcs12Ordering = (Vector)objectInputStream.readObject();
        }
        else {
            final ASN1InputStream asn1InputStream = new ASN1InputStream((byte[])object);
            DERObjectIdentifier derObjectIdentifier;
            while ((derObjectIdentifier = (DERObjectIdentifier)asn1InputStream.readObject()) != null) {
                this.setBagAttribute(derObjectIdentifier, asn1InputStream.readObject());
            }
        }
    }
}
