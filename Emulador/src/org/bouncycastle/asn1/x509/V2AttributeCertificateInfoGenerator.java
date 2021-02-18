// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERInteger;

public class V2AttributeCertificateInfoGenerator
{
    private DERInteger version;
    private Holder holder;
    private AttCertIssuer issuer;
    private AlgorithmIdentifier signature;
    private DERInteger serialNumber;
    private ASN1EncodableVector attributes;
    private DERBitString issuerUniqueID;
    private X509Extensions extensions;
    private DERGeneralizedTime startDate;
    private DERGeneralizedTime endDate;
    
    public V2AttributeCertificateInfoGenerator() {
        this.version = new DERInteger(1);
        this.attributes = new ASN1EncodableVector();
    }
    
    public void setHolder(final Holder holder) {
        this.holder = holder;
    }
    
    public void addAttribute(final String s, final ASN1Encodable asn1Encodable) {
        this.attributes.add(new Attribute(new DERObjectIdentifier(s), new DERSet(asn1Encodable)));
    }
    
    public void addAttribute(final Attribute attribute) {
        this.attributes.add(attribute);
    }
    
    public void setSerialNumber(final DERInteger serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public void setSignature(final AlgorithmIdentifier signature) {
        this.signature = signature;
    }
    
    public void setIssuer(final AttCertIssuer issuer) {
        this.issuer = issuer;
    }
    
    public void setStartDate(final DERGeneralizedTime startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(final DERGeneralizedTime endDate) {
        this.endDate = endDate;
    }
    
    public void setIssuerUniqueID(final DERBitString issuerUniqueID) {
        this.issuerUniqueID = issuerUniqueID;
    }
    
    public void setExtensions(final X509Extensions extensions) {
        this.extensions = extensions;
    }
    
    public AttributeCertificateInfo generateAttributeCertificateInfo() {
        if (this.serialNumber == null || this.signature == null || this.issuer == null || this.startDate == null || this.endDate == null || this.holder == null || this.attributes == null) {
            throw new IllegalStateException("not all mandatory fields set in V2 AttributeCertificateInfo generator");
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.holder);
        asn1EncodableVector.add(this.issuer);
        asn1EncodableVector.add(this.signature);
        asn1EncodableVector.add(this.serialNumber);
        asn1EncodableVector.add(new AttCertValidityPeriod(this.startDate, this.endDate));
        asn1EncodableVector.add(new DERSequence(this.attributes));
        if (this.issuerUniqueID != null) {
            asn1EncodableVector.add(this.issuerUniqueID);
        }
        if (this.extensions != null) {
            asn1EncodableVector.add(this.extensions);
        }
        return new AttributeCertificateInfo(new DERSequence(asn1EncodableVector));
    }
}
