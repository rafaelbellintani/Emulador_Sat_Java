// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class Challenge extends ASN1Encodable
{
    private AlgorithmIdentifier owf;
    private ASN1OctetString witness;
    private ASN1OctetString challenge;
    
    private Challenge(final ASN1Sequence asn1Sequence) {
        int n = 0;
        if (asn1Sequence.size() == 3) {
            this.owf = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n++));
        }
        this.witness = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n++));
        this.challenge = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n));
    }
    
    public static Challenge getInstance(final Object o) {
        if (o instanceof Challenge) {
            return (Challenge)o;
        }
        if (o instanceof ASN1Sequence) {
            return new Challenge((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public AlgorithmIdentifier getOwf() {
        return this.owf;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        this.addOptional(asn1EncodableVector, this.owf);
        asn1EncodableVector.add(this.witness);
        asn1EncodableVector.add(this.challenge);
        return new DERSequence(asn1EncodableVector);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(asn1Encodable);
        }
    }
}
