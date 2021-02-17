// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.ASN1OctetString;

public class X509Extension
{
    boolean critical;
    ASN1OctetString value;
    
    public X509Extension(final DERBoolean derBoolean, final ASN1OctetString value) {
        this.critical = derBoolean.isTrue();
        this.value = value;
    }
    
    public X509Extension(final boolean critical, final ASN1OctetString value) {
        this.critical = critical;
        this.value = value;
    }
    
    public boolean isCritical() {
        return this.critical;
    }
    
    public ASN1OctetString getValue() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        if (this.isCritical()) {
            return this.getValue().hashCode();
        }
        return ~this.getValue().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof X509Extension)) {
            return false;
        }
        final X509Extension x509Extension = (X509Extension)o;
        return x509Extension.getValue().equals(this.getValue()) && x509Extension.isCritical() == this.isCritical();
    }
    
    public static ASN1Object convertValueToObject(final X509Extension x509Extension) throws IllegalArgumentException {
        try {
            return ASN1Object.fromByteArray(x509Extension.getValue().getOctets());
        }
        catch (IOException obj) {
            throw new IllegalArgumentException("can't convert extension: " + obj);
        }
    }
}
