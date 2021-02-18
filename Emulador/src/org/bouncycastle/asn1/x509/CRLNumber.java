// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle.asn1.DERInteger;

public class CRLNumber extends DERInteger
{
    public CRLNumber(final BigInteger bigInteger) {
        super(bigInteger);
    }
    
    public BigInteger getCRLNumber() {
        return this.getPositiveValue();
    }
    
    @Override
    public String toString() {
        return "CRLNumber: " + this.getCRLNumber();
    }
}
