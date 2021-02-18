// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.ASN1Encodable;

public class SignerLocation extends ASN1Encodable
{
    private DERUTF8String countryName;
    private DERUTF8String localityName;
    private ASN1Sequence postalAddress;
    
    public SignerLocation(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final DERTaggedObject derTaggedObject = objects.nextElement();
            switch (derTaggedObject.getTagNo()) {
                case 0: {
                    this.countryName = DERUTF8String.getInstance(derTaggedObject, true);
                    continue;
                }
                case 1: {
                    this.localityName = DERUTF8String.getInstance(derTaggedObject, true);
                    continue;
                }
                case 2: {
                    if (derTaggedObject.isExplicit()) {
                        this.postalAddress = ASN1Sequence.getInstance(derTaggedObject, true);
                    }
                    else {
                        this.postalAddress = ASN1Sequence.getInstance(derTaggedObject, false);
                    }
                    if (this.postalAddress != null && this.postalAddress.size() > 6) {
                        throw new IllegalArgumentException("postal address must contain less than 6 strings");
                    }
                    continue;
                }
                default: {
                    throw new IllegalArgumentException("illegal tag");
                }
            }
        }
    }
    
    public SignerLocation(final DERUTF8String derutf8String, final DERUTF8String derutf8String2, final ASN1Sequence asn1Sequence) {
        if (asn1Sequence != null && asn1Sequence.size() > 6) {
            throw new IllegalArgumentException("postal address must contain less than 6 strings");
        }
        if (derutf8String != null) {
            this.countryName = DERUTF8String.getInstance(derutf8String.toASN1Object());
        }
        if (derutf8String2 != null) {
            this.localityName = DERUTF8String.getInstance(derutf8String2.toASN1Object());
        }
        if (asn1Sequence != null) {
            this.postalAddress = ASN1Sequence.getInstance(asn1Sequence.toASN1Object());
        }
    }
    
    public static SignerLocation getInstance(final Object o) {
        if (o == null || o instanceof SignerLocation) {
            return (SignerLocation)o;
        }
        return new SignerLocation(ASN1Sequence.getInstance(o));
    }
    
    public DERUTF8String getCountryName() {
        return this.countryName;
    }
    
    public DERUTF8String getLocalityName() {
        return this.localityName;
    }
    
    public ASN1Sequence getPostalAddress() {
        return this.postalAddress;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.countryName != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.countryName));
        }
        if (this.localityName != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.localityName));
        }
        if (this.postalAddress != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.postalAddress));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
