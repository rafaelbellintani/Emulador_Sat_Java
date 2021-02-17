// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.ASN1Encodable;

public class ServiceLocator extends ASN1Encodable
{
    X509Name issuer;
    DERObject locator;
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.issuer);
        if (this.locator != null) {
            asn1EncodableVector.add(this.locator);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
