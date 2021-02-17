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
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class AttributeCertificateInfo extends ASN1Encodable
{
    private DERInteger version;
    private Holder holder;
    private AttCertIssuer issuer;
    private AlgorithmIdentifier signature;
    private DERInteger serialNumber;
    private AttCertValidityPeriod attrCertValidityPeriod;
    private ASN1Sequence attributes;
    private DERBitString issuerUniqueID;
    private X509Extensions extensions;
    
    public static AttributeCertificateInfo getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static AttributeCertificateInfo getInstance(final Object o) {
        if (o instanceof AttributeCertificateInfo) {
            return (AttributeCertificateInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AttributeCertificateInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public AttributeCertificateInfo(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 7 || asn1Sequence.size() > 9) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.version = DERInteger.getInstance(asn1Sequence.getObjectAt(0));
        this.holder = Holder.getInstance(asn1Sequence.getObjectAt(1));
        this.issuer = AttCertIssuer.getInstance(asn1Sequence.getObjectAt(2));
        this.signature = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(3));
        this.serialNumber = DERInteger.getInstance(asn1Sequence.getObjectAt(4));
        this.attrCertValidityPeriod = AttCertValidityPeriod.getInstance(asn1Sequence.getObjectAt(5));
        this.attributes = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(6));
        for (int i = 7; i < asn1Sequence.size(); ++i) {
            final ASN1Encodable asn1Encodable = (ASN1Encodable)asn1Sequence.getObjectAt(i);
            if (asn1Encodable instanceof DERBitString) {
                this.issuerUniqueID = DERBitString.getInstance(asn1Sequence.getObjectAt(i));
            }
            else if (asn1Encodable instanceof ASN1Sequence || asn1Encodable instanceof X509Extensions) {
                this.extensions = X509Extensions.getInstance(asn1Sequence.getObjectAt(i));
            }
        }
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public Holder getHolder() {
        return this.holder;
    }
    
    public AttCertIssuer getIssuer() {
        return this.issuer;
    }
    
    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }
    
    public DERInteger getSerialNumber() {
        return this.serialNumber;
    }
    
    public AttCertValidityPeriod getAttrCertValidityPeriod() {
        return this.attrCertValidityPeriod;
    }
    
    public ASN1Sequence getAttributes() {
        return this.attributes;
    }
    
    public DERBitString getIssuerUniqueID() {
        return this.issuerUniqueID;
    }
    
    public X509Extensions getExtensions() {
        return this.extensions;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.holder);
        asn1EncodableVector.add(this.issuer);
        asn1EncodableVector.add(this.signature);
        asn1EncodableVector.add(this.serialNumber);
        asn1EncodableVector.add(this.attrCertValidityPeriod);
        asn1EncodableVector.add(this.attributes);
        if (this.issuerUniqueID != null) {
            asn1EncodableVector.add(this.issuerUniqueID);
        }
        if (this.extensions != null) {
            asn1EncodableVector.add(this.extensions);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
