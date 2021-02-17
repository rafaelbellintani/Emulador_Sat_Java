// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERIA5String;

public class SPuri
{
    private DERIA5String uri;
    
    public static SPuri getInstance(final Object o) {
        if (o instanceof SPuri) {
            return (SPuri)o;
        }
        if (o instanceof DERIA5String) {
            return new SPuri((DERIA5String)o);
        }
        throw new IllegalArgumentException("unknown object in 'SPuri' factory: " + o.getClass().getName() + ".");
    }
    
    public SPuri(final DERIA5String uri) {
        this.uri = uri;
    }
    
    public DERIA5String getUri() {
        return this.uri;
    }
    
    public DERObject toASN1Object() {
        return this.uri.getDERObject();
    }
}
