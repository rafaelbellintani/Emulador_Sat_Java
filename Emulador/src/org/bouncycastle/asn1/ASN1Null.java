// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1;

import java.io.IOException;

public abstract class ASN1Null extends ASN1Object
{
    @Override
    public int hashCode() {
        return -1;
    }
    
    @Override
    boolean asn1Equals(final DERObject derObject) {
        return derObject instanceof ASN1Null;
    }
    
    @Override
    abstract void encode(final DEROutputStream p0) throws IOException;
    
    @Override
    public String toString() {
        return "NULL";
    }
}
