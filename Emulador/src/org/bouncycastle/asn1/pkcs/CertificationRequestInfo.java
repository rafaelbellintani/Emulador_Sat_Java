// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertificationRequestInfo extends ASN1Encodable
{
    DERInteger version;
    X509Name subject;
    SubjectPublicKeyInfo subjectPKInfo;
    ASN1Set attributes;
    
    public static CertificationRequestInfo getInstance(final Object o) {
        if (o instanceof CertificationRequestInfo) {
            return (CertificationRequestInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertificationRequestInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public CertificationRequestInfo(final X509Name subject, final SubjectPublicKeyInfo subjectPKInfo, final ASN1Set attributes) {
        this.version = new DERInteger(0);
        this.attributes = null;
        this.subject = subject;
        this.subjectPKInfo = subjectPKInfo;
        this.attributes = attributes;
        if (subject == null || this.version == null || this.subjectPKInfo == null) {
            throw new IllegalArgumentException("Not all mandatory fields set in CertificationRequestInfo generator.");
        }
    }
    
    public CertificationRequestInfo(final ASN1Sequence asn1Sequence) {
        this.version = new DERInteger(0);
        this.attributes = null;
        this.version = (DERInteger)asn1Sequence.getObjectAt(0);
        this.subject = X509Name.getInstance(asn1Sequence.getObjectAt(1));
        this.subjectPKInfo = SubjectPublicKeyInfo.getInstance(asn1Sequence.getObjectAt(2));
        if (asn1Sequence.size() > 3) {
            this.attributes = ASN1Set.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(3), false);
        }
        if (this.subject == null || this.version == null || this.subjectPKInfo == null) {
            throw new IllegalArgumentException("Not all mandatory fields set in CertificationRequestInfo generator.");
        }
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public X509Name getSubject() {
        return this.subject;
    }
    
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.subjectPKInfo;
    }
    
    public ASN1Set getAttributes() {
        return this.attributes;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.subject);
        asn1EncodableVector.add(this.subjectPKInfo);
        if (this.attributes != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.attributes));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
