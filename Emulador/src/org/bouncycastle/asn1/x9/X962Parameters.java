// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class X962Parameters extends ASN1Encodable implements ASN1Choice
{
    private DERObject params;
    
    public static X962Parameters getInstance(final Object o) {
        if (o == null || o instanceof X962Parameters) {
            return (X962Parameters)o;
        }
        if (o instanceof DERObject) {
            return new X962Parameters((DERObject)o);
        }
        throw new IllegalArgumentException("unknown object in getInstance()");
    }
    
    public static X962Parameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public X962Parameters(final X9ECParameters x9ECParameters) {
        this.params = null;
        this.params = x9ECParameters.getDERObject();
    }
    
    public X962Parameters(final DERObjectIdentifier params) {
        this.params = null;
        this.params = params;
    }
    
    public X962Parameters(final DERObject params) {
        this.params = null;
        this.params = params;
    }
    
    public boolean isNamedCurve() {
        return this.params instanceof DERObjectIdentifier;
    }
    
    public boolean isImplicitlyCA() {
        return this.params instanceof ASN1Null;
    }
    
    public DERObject getParameters() {
        return this.params;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.params;
    }
}
