// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1Encodable;

public class POPOSigningKeyInput extends ASN1Encodable
{
    private ASN1Encodable authInfo;
    private SubjectPublicKeyInfo publicKey;
    
    private POPOSigningKeyInput(final ASN1Sequence asn1Sequence) {
        this.authInfo = (ASN1Encodable)asn1Sequence.getObjectAt(0);
        this.publicKey = SubjectPublicKeyInfo.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public static POPOSigningKeyInput getInstance(final Object o) {
        if (o instanceof POPOSigningKeyInput) {
            return (POPOSigningKeyInput)o;
        }
        if (o instanceof ASN1Sequence) {
            return new POPOSigningKeyInput((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public SubjectPublicKeyInfo getPublicKey() {
        return this.publicKey;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.authInfo);
        asn1EncodableVector.add(this.publicKey);
        return new DERSequence(asn1EncodableVector);
    }
}
