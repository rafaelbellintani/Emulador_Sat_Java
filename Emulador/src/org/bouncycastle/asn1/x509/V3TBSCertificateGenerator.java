// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERTaggedObject;

public class V3TBSCertificateGenerator
{
    DERTaggedObject version;
    DERInteger serialNumber;
    AlgorithmIdentifier signature;
    X509Name issuer;
    Time startDate;
    Time endDate;
    X509Name subject;
    SubjectPublicKeyInfo subjectPublicKeyInfo;
    X509Extensions extensions;
    private boolean altNamePresentAndCritical;
    private DERBitString issuerUniqueID;
    private DERBitString subjectUniqueID;
    
    public V3TBSCertificateGenerator() {
        this.version = new DERTaggedObject(0, new DERInteger(2));
    }
    
    public void setSerialNumber(final DERInteger serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    public void setSignature(final AlgorithmIdentifier signature) {
        this.signature = signature;
    }
    
    public void setIssuer(final X509Name issuer) {
        this.issuer = issuer;
    }
    
    public void setStartDate(final DERUTCTime derutcTime) {
        this.startDate = new Time(derutcTime);
    }
    
    public void setStartDate(final Time startDate) {
        this.startDate = startDate;
    }
    
    public void setEndDate(final DERUTCTime derutcTime) {
        this.endDate = new Time(derutcTime);
    }
    
    public void setEndDate(final Time endDate) {
        this.endDate = endDate;
    }
    
    public void setSubject(final X509Name subject) {
        this.subject = subject;
    }
    
    public void setIssuerUniqueID(final DERBitString issuerUniqueID) {
        this.issuerUniqueID = issuerUniqueID;
    }
    
    public void setSubjectUniqueID(final DERBitString subjectUniqueID) {
        this.subjectUniqueID = subjectUniqueID;
    }
    
    public void setSubjectPublicKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.subjectPublicKeyInfo = subjectPublicKeyInfo;
    }
    
    public void setExtensions(final X509Extensions extensions) {
        this.extensions = extensions;
        if (extensions != null) {
            final X509Extension extension = extensions.getExtension(X509Extensions.SubjectAlternativeName);
            if (extension != null && extension.isCritical()) {
                this.altNamePresentAndCritical = true;
            }
        }
    }
    
    public TBSCertificateStructure generateTBSCertificate() {
        if (this.serialNumber == null || this.signature == null || this.issuer == null || this.startDate == null || this.endDate == null || (this.subject == null && !this.altNamePresentAndCritical) || this.subjectPublicKeyInfo == null) {
            throw new IllegalStateException("not all mandatory fields set in V3 TBScertificate generator");
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.serialNumber);
        asn1EncodableVector.add(this.signature);
        asn1EncodableVector.add(this.issuer);
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        asn1EncodableVector2.add(this.startDate);
        asn1EncodableVector2.add(this.endDate);
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        if (this.subject != null) {
            asn1EncodableVector.add(this.subject);
        }
        else {
            asn1EncodableVector.add(new DERSequence());
        }
        asn1EncodableVector.add(this.subjectPublicKeyInfo);
        if (this.issuerUniqueID != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.issuerUniqueID));
        }
        if (this.subjectUniqueID != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.subjectUniqueID));
        }
        if (this.extensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(3, this.extensions));
        }
        return new TBSCertificateStructure(new DERSequence(asn1EncodableVector));
    }
}
