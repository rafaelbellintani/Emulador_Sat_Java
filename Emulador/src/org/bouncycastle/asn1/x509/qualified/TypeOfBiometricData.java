// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509.qualified;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class TypeOfBiometricData extends ASN1Encodable implements ASN1Choice
{
    public static final int PICTURE = 0;
    public static final int HANDWRITTEN_SIGNATURE = 1;
    DEREncodable obj;
    
    public static TypeOfBiometricData getInstance(final Object o) {
        if (o == null || o instanceof TypeOfBiometricData) {
            return (TypeOfBiometricData)o;
        }
        if (o instanceof DERInteger) {
            return new TypeOfBiometricData(DERInteger.getInstance(o).getValue().intValue());
        }
        if (o instanceof DERObjectIdentifier) {
            return new TypeOfBiometricData(DERObjectIdentifier.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    public TypeOfBiometricData(final int i) {
        if (i == 0 || i == 1) {
            this.obj = new DERInteger(i);
            return;
        }
        throw new IllegalArgumentException("unknow PredefinedBiometricType : " + i);
    }
    
    public TypeOfBiometricData(final DERObjectIdentifier obj) {
        this.obj = obj;
    }
    
    public boolean isPredefined() {
        return this.obj instanceof DERInteger;
    }
    
    public int getPredefinedBiometricType() {
        return ((DERInteger)this.obj).getValue().intValue();
    }
    
    public DERObjectIdentifier getBiometricDataOid() {
        return (DERObjectIdentifier)this.obj;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.obj.getDERObject();
    }
}
