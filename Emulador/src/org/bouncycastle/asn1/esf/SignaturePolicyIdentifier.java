// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class SignaturePolicyIdentifier extends ASN1Encodable
{
    private SignaturePolicyId signaturePolicyId;
    private boolean isSignaturePolicyImplied;
    
    public static SignaturePolicyIdentifier getInstance(final Object o) {
        if (o == null || o instanceof SignaturePolicyIdentifier) {
            return (SignaturePolicyIdentifier)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SignaturePolicyIdentifier(SignaturePolicyId.getInstance(o));
        }
        if (o instanceof ASN1Null) {
            return new SignaturePolicyIdentifier();
        }
        throw new IllegalArgumentException("unknown object in 'SignaturePolicyIdentifier' factory: " + o.getClass().getName() + ".");
    }
    
    public SignaturePolicyIdentifier() {
        this.isSignaturePolicyImplied = true;
    }
    
    public SignaturePolicyIdentifier(final SignaturePolicyId signaturePolicyId) {
        this.signaturePolicyId = signaturePolicyId;
        this.isSignaturePolicyImplied = false;
    }
    
    public SignaturePolicyId getSignaturePolicyId() {
        return this.signaturePolicyId;
    }
    
    public boolean isSignaturePolicyImplied() {
        return this.isSignaturePolicyImplied;
    }
    
    @Override
    public DERObject toASN1Object() {
        if (this.isSignaturePolicyImplied) {
            return new DERNull();
        }
        return this.signaturePolicyId.getDERObject();
    }
}
