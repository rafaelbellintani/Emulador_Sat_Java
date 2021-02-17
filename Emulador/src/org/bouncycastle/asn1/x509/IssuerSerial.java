// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class IssuerSerial extends ASN1Encodable
{
    GeneralNames issuer;
    DERInteger serial;
    DERBitString issuerUID;
    
    public static IssuerSerial getInstance(final Object o) {
        if (o == null || o instanceof IssuerSerial) {
            return (IssuerSerial)o;
        }
        if (o instanceof ASN1Sequence) {
            return new IssuerSerial((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + o.getClass().getName());
    }
    
    public static IssuerSerial getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public IssuerSerial(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2 && asn1Sequence.size() != 3) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.issuer = GeneralNames.getInstance(asn1Sequence.getObjectAt(0));
        this.serial = DERInteger.getInstance(asn1Sequence.getObjectAt(1));
        if (asn1Sequence.size() == 3) {
            this.issuerUID = DERBitString.getInstance(asn1Sequence.getObjectAt(2));
        }
    }
    
    public IssuerSerial(final GeneralNames issuer, final DERInteger serial) {
        this.issuer = issuer;
        this.serial = serial;
    }
    
    public GeneralNames getIssuer() {
        return this.issuer;
    }
    
    public DERInteger getSerial() {
        return this.serial;
    }
    
    public DERBitString getIssuerUID() {
        return this.issuerUID;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.issuer);
        asn1EncodableVector.add(this.serial);
        if (this.issuerUID != null) {
            asn1EncodableVector.add(this.issuerUID);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
