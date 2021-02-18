// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class POPOSigningKey extends ASN1Encodable
{
    private POPOSigningKeyInput poposkInput;
    private AlgorithmIdentifier algorithmIdentifier;
    private DERBitString signature;
    
    private POPOSigningKey(final ASN1Sequence asn1Sequence) {
        int n = 0;
        if (asn1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
            this.poposkInput = POPOSigningKeyInput.getInstance(asn1Sequence.getObjectAt(n++));
        }
        this.algorithmIdentifier = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n++));
        this.signature = DERBitString.getInstance(asn1Sequence.getObjectAt(n));
    }
    
    public static POPOSigningKey getInstance(final Object o) {
        if (o instanceof POPOSigningKey) {
            return (POPOSigningKey)o;
        }
        if (o instanceof ASN1Sequence) {
            return new POPOSigningKey((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public static POPOSigningKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.poposkInput != null) {
            asn1EncodableVector.add(this.poposkInput);
        }
        asn1EncodableVector.add(this.algorithmIdentifier);
        asn1EncodableVector.add(this.signature);
        return new DERSequence(asn1EncodableVector);
    }
}
