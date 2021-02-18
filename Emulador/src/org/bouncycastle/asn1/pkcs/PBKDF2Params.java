// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Encodable;

public class PBKDF2Params extends ASN1Encodable
{
    ASN1OctetString octStr;
    DERInteger iterationCount;
    DERInteger keyLength;
    
    public static PBKDF2Params getInstance(final Object o) {
        if (o instanceof PBKDF2Params) {
            return (PBKDF2Params)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PBKDF2Params((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public PBKDF2Params(final byte[] array, final int n) {
        this.octStr = new DEROctetString(array);
        this.iterationCount = new DERInteger(n);
    }
    
    public PBKDF2Params(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.octStr = objects.nextElement();
        this.iterationCount = (DERInteger)objects.nextElement();
        if (objects.hasMoreElements()) {
            this.keyLength = (DERInteger)objects.nextElement();
        }
        else {
            this.keyLength = null;
        }
    }
    
    public byte[] getSalt() {
        return this.octStr.getOctets();
    }
    
    public BigInteger getIterationCount() {
        return this.iterationCount.getValue();
    }
    
    public BigInteger getKeyLength() {
        if (this.keyLength != null) {
            return this.keyLength.getValue();
        }
        return null;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.octStr);
        asn1EncodableVector.add(this.iterationCount);
        if (this.keyLength != null) {
            asn1EncodableVector.add(this.keyLength);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
