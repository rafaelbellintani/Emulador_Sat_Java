// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEROctetString;
import java.io.IOException;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.util.Vector;
import java.util.Hashtable;

public class X509ExtensionsGenerator
{
    private Hashtable extensions;
    private Vector extOrdering;
    
    public X509ExtensionsGenerator() {
        this.extensions = new Hashtable();
        this.extOrdering = new Vector();
    }
    
    public void reset() {
        this.extensions = new Hashtable();
        this.extOrdering = new Vector();
    }
    
    public void addExtension(final DERObjectIdentifier derObjectIdentifier, final boolean b, final DEREncodable derEncodable) {
        try {
            this.addExtension(derObjectIdentifier, b, derEncodable.getDERObject().getEncoded("DER"));
        }
        catch (IOException obj) {
            throw new IllegalArgumentException("error encoding value: " + obj);
        }
    }
    
    public void addExtension(final DERObjectIdentifier derObjectIdentifier, final boolean b, final byte[] array) {
        if (this.extensions.containsKey(derObjectIdentifier)) {
            throw new IllegalArgumentException("extension " + derObjectIdentifier + " already added");
        }
        this.extOrdering.addElement(derObjectIdentifier);
        this.extensions.put(derObjectIdentifier, new X509Extension(b, new DEROctetString(array)));
    }
    
    public boolean isEmpty() {
        return this.extOrdering.isEmpty();
    }
    
    public X509Extensions generate() {
        return new X509Extensions(this.extOrdering, this.extensions);
    }
}
