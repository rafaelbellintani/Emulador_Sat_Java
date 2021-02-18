// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class KeyDerivationFunc extends AlgorithmIdentifier
{
    KeyDerivationFunc(final ASN1Sequence asn1Sequence) {
        super(asn1Sequence);
    }
    
    KeyDerivationFunc(final DERObjectIdentifier derObjectIdentifier, final ASN1Encodable asn1Encodable) {
        super(derObjectIdentifier, asn1Encodable);
    }
}
