// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;
import java.util.Enumeration;

class LazyDERConstructionEnumeration implements Enumeration
{
    private ASN1InputStream aIn;
    private Object nextObj;
    
    public LazyDERConstructionEnumeration(final byte[] array) {
        this.aIn = new ASN1InputStream(array, true);
        this.nextObj = this.readObject();
    }
    
    @Override
    public boolean hasMoreElements() {
        return this.nextObj != null;
    }
    
    @Override
    public Object nextElement() {
        final Object nextObj = this.nextObj;
        this.nextObj = this.readObject();
        return nextObj;
    }
    
    private Object readObject() {
        try {
            return this.aIn.readObject();
        }
        catch (IOException obj) {
            throw new ASN1ParsingException("malformed DER construction: " + obj, obj);
        }
    }
}
