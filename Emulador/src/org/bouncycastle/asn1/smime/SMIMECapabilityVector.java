// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.smime;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1EncodableVector;

public class SMIMECapabilityVector
{
    private ASN1EncodableVector capabilities;
    
    public SMIMECapabilityVector() {
        this.capabilities = new ASN1EncodableVector();
    }
    
    public void addCapability(final DERObjectIdentifier derObjectIdentifier) {
        this.capabilities.add(new DERSequence(derObjectIdentifier));
    }
    
    public void addCapability(final DERObjectIdentifier derObjectIdentifier, final int n) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(derObjectIdentifier);
        asn1EncodableVector.add(new DERInteger(n));
        this.capabilities.add(new DERSequence(asn1EncodableVector));
    }
    
    public void addCapability(final DERObjectIdentifier derObjectIdentifier, final DEREncodable derEncodable) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(derObjectIdentifier);
        asn1EncodableVector.add(derEncodable);
        this.capabilities.add(new DERSequence(asn1EncodableVector));
    }
    
    public DEREncodableVector toDEREncodableVector() {
        return this.capabilities;
    }
}
