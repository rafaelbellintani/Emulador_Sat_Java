// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.misc;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class CAST5CBCParameters extends ASN1Encodable
{
    DERInteger keyLength;
    ASN1OctetString iv;
    
    public static CAST5CBCParameters getInstance(final Object o) {
        if (o instanceof CAST5CBCParameters) {
            return (CAST5CBCParameters)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CAST5CBCParameters((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in CAST5CBCParameter factory");
    }
    
    public CAST5CBCParameters(final byte[] array, final int n) {
        this.iv = new DEROctetString(array);
        this.keyLength = new DERInteger(n);
    }
    
    public CAST5CBCParameters(final ASN1Sequence asn1Sequence) {
        this.iv = (ASN1OctetString)asn1Sequence.getObjectAt(0);
        this.keyLength = (DERInteger)asn1Sequence.getObjectAt(1);
    }
    
    public byte[] getIV() {
        return this.iv.getOctets();
    }
    
    public int getKeyLength() {
        return this.keyLength.getValue().intValue();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.iv);
        asn1EncodableVector.add(this.keyLength);
        return new DERSequence(asn1EncodableVector);
    }
}
