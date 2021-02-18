// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.asn1.ASN1Encodable;

public class X9ECPoint extends ASN1Encodable
{
    ECPoint p;
    
    public X9ECPoint(final ECPoint p) {
        this.p = p;
    }
    
    public X9ECPoint(final ECCurve ecCurve, final ASN1OctetString asn1OctetString) {
        this.p = ecCurve.decodePoint(asn1OctetString.getOctets());
    }
    
    public ECPoint getPoint() {
        return this.p;
    }
    
    @Override
    public DERObject toASN1Object() {
        return new DEROctetString(this.p.getEncoded());
    }
}
