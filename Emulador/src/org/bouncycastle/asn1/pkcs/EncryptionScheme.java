// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class EncryptionScheme extends AlgorithmIdentifier
{
    DERObject objectId;
    DERObject obj;
    
    EncryptionScheme(final ASN1Sequence asn1Sequence) {
        super(asn1Sequence);
        this.objectId = (DERObject)asn1Sequence.getObjectAt(0);
        this.obj = (DERObject)asn1Sequence.getObjectAt(1);
    }
    
    public DERObject getObject() {
        return this.obj;
    }
    
    @Override
    public DERObject getDERObject() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.objectId);
        asn1EncodableVector.add(this.obj);
        return new DERSequence(asn1EncodableVector);
    }
}
