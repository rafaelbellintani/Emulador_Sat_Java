// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1Encodable;

public class AttributeCertificate extends ASN1Encodable
{
    AttributeCertificateInfo acinfo;
    AlgorithmIdentifier signatureAlgorithm;
    DERBitString signatureValue;
    
    public static AttributeCertificate getInstance(final Object o) {
        if (o instanceof AttributeCertificate) {
            return (AttributeCertificate)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AttributeCertificate((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public AttributeCertificate(final AttributeCertificateInfo acinfo, final AlgorithmIdentifier signatureAlgorithm, final DERBitString signatureValue) {
        this.acinfo = acinfo;
        this.signatureAlgorithm = signatureAlgorithm;
        this.signatureValue = signatureValue;
    }
    
    public AttributeCertificate(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 3) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.acinfo = AttributeCertificateInfo.getInstance(asn1Sequence.getObjectAt(0));
        this.signatureAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.signatureValue = DERBitString.getInstance(asn1Sequence.getObjectAt(2));
    }
    
    public AttributeCertificateInfo getAcinfo() {
        return this.acinfo;
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }
    
    public DERBitString getSignatureValue() {
        return this.signatureValue;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.acinfo);
        asn1EncodableVector.add(this.signatureAlgorithm);
        asn1EncodableVector.add(this.signatureValue);
        return new DERSequence(asn1EncodableVector);
    }
}
