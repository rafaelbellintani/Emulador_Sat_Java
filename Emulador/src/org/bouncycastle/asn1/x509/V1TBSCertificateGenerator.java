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
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERTaggedObject;

public class V1TBSCertificateGenerator
{
    DERTaggedObject version;
    DERInteger serialNumber;
    AlgorithmIdentifier signature;
    X509Name issuer;
    Time startDate;
    Time endDate;
    X509Name subject;
    SubjectPublicKeyInfo subjectPublicKeyInfo;
    
    public V1TBSCertificateGenerator() {
        this.version = new DERTaggedObject(0, new DERInteger(0));
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
    
    public void setStartDate(final Time startDate) {
        this.startDate = startDate;
    }
    
    public void setStartDate(final DERUTCTime derutcTime) {
        this.startDate = new Time(derutcTime);
    }
    
    public void setEndDate(final Time endDate) {
        this.endDate = endDate;
    }
    
    public void setEndDate(final DERUTCTime derutcTime) {
        this.endDate = new Time(derutcTime);
    }
    
    public void setSubject(final X509Name subject) {
        this.subject = subject;
    }
    
    public void setSubjectPublicKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.subjectPublicKeyInfo = subjectPublicKeyInfo;
    }
    
    public TBSCertificateStructure generateTBSCertificate() {
        if (this.serialNumber == null || this.signature == null || this.issuer == null || this.startDate == null || this.endDate == null || this.subject == null || this.subjectPublicKeyInfo == null) {
            throw new IllegalStateException("not all mandatory fields set in V1 TBScertificate generator");
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.serialNumber);
        asn1EncodableVector.add(this.signature);
        asn1EncodableVector.add(this.issuer);
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        asn1EncodableVector2.add(this.startDate);
        asn1EncodableVector2.add(this.endDate);
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        asn1EncodableVector.add(this.subject);
        asn1EncodableVector.add(this.subjectPublicKeyInfo);
        return new TBSCertificateStructure(new DERSequence(asn1EncodableVector));
    }
}
