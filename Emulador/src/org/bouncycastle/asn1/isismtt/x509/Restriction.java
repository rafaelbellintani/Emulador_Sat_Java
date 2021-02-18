// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.isismtt.x509;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.x500.DirectoryString;
import org.bouncycastle.asn1.ASN1Encodable;

public class Restriction extends ASN1Encodable
{
    private DirectoryString restriction;
    
    public static Restriction getInstance(final Object o) {
        if (o == null || o instanceof Restriction) {
            return (Restriction)o;
        }
        if (o instanceof DERString) {
            return new Restriction(DirectoryString.getInstance(o));
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    private Restriction(final DirectoryString restriction) {
        this.restriction = restriction;
    }
    
    public Restriction(final String s) {
        this.restriction = new DirectoryString(s);
    }
    
    public DirectoryString getRestriction() {
        return this.restriction;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.restriction.toASN1Object();
    }
}
