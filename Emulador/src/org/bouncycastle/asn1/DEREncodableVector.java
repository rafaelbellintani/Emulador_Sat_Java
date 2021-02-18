// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.util.Vector;

public class DEREncodableVector
{
    Vector v;
    
    @Deprecated
    public DEREncodableVector() {
        this.v = new Vector();
    }
    
    public void add(final DEREncodable obj) {
        this.v.addElement(obj);
    }
    
    public DEREncodable get(final int index) {
        return this.v.elementAt(index);
    }
    
    public int size() {
        return this.v.size();
    }
}
