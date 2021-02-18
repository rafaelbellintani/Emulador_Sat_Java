// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.x509;

import java.util.ArrayList;
import java.util.Collection;

public class X509CollectionStoreParameters implements X509StoreParameters
{
    private Collection collection;
    
    public X509CollectionStoreParameters(final Collection collection) {
        if (collection == null) {
            throw new NullPointerException("collection cannot be null");
        }
        this.collection = collection;
    }
    
    public Object clone() {
        return new X509CollectionStoreParameters(this.collection);
    }
    
    public Collection getCollection() {
        return new ArrayList(this.collection);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("X509CollectionStoreParameters: [\n");
        sb.append("  collection: " + this.collection + "\n");
        sb.append("]");
        return sb.toString();
    }
}
