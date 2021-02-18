// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.ASN1Encodable;

public class TBSCertificateStructure extends ASN1Encodable implements X509ObjectIdentifiers, PKCSObjectIdentifiers
{
    ASN1Sequence seq;
    DERInteger version;
    DERInteger serialNumber;
    AlgorithmIdentifier signature;
    X509Name issuer;
    Time startDate;
    Time endDate;
    X509Name subject;
    SubjectPublicKeyInfo subjectPublicKeyInfo;
    DERBitString issuerUniqueId;
    DERBitString subjectUniqueId;
    X509Extensions extensions;
    
    public static TBSCertificateStructure getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static TBSCertificateStructure getInstance(final Object o) {
        if (o instanceof TBSCertificateStructure) {
            return (TBSCertificateStructure)o;
        }
        if (o instanceof ASN1Sequence) {
            return new TBSCertificateStructure((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public TBSCertificateStructure(final ASN1Sequence seq) {
        int n = 0;
        this.seq = seq;
        if (seq.getObjectAt(0) instanceof DERTaggedObject) {
            this.version = DERInteger.getInstance(seq.getObjectAt(0));
        }
        else {
            n = -1;
            this.version = new DERInteger(0);
        }
        this.serialNumber = DERInteger.getInstance(seq.getObjectAt(n + 1));
        this.signature = AlgorithmIdentifier.getInstance(seq.getObjectAt(n + 2));
        this.issuer = X509Name.getInstance(seq.getObjectAt(n + 3));
        final ASN1Sequence asn1Sequence = (ASN1Sequence)seq.getObjectAt(n + 4);
        this.startDate = Time.getInstance(asn1Sequence.getObjectAt(0));
        this.endDate = Time.getInstance(asn1Sequence.getObjectAt(1));
        this.subject = X509Name.getInstance(seq.getObjectAt(n + 5));
        this.subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(seq.getObjectAt(n + 6));
        for (int i = seq.size() - (n + 6) - 1; i > 0; --i) {
            final DERTaggedObject derTaggedObject = (DERTaggedObject)seq.getObjectAt(n + 6 + i);
            switch (derTaggedObject.getTagNo()) {
                case 1: {
                    this.issuerUniqueId = DERBitString.getInstance(derTaggedObject, false);
                    break;
                }
                case 2: {
                    this.subjectUniqueId = DERBitString.getInstance(derTaggedObject, false);
                    break;
                }
                case 3: {
                    this.extensions = X509Extensions.getInstance(derTaggedObject);
                    break;
                }
            }
        }
    }
    
    public int getVersion() {
        return this.version.getValue().intValue() + 1;
    }
    
    public DERInteger getVersionNumber() {
        return this.version;
    }
    
    public DERInteger getSerialNumber() {
        return this.serialNumber;
    }
    
    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }
    
    public X509Name getIssuer() {
        return this.issuer;
    }
    
    public Time getStartDate() {
        return this.startDate;
    }
    
    public Time getEndDate() {
        return this.endDate;
    }
    
    public X509Name getSubject() {
        return this.subject;
    }
    
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.subjectPublicKeyInfo;
    }
    
    public DERBitString getIssuerUniqueId() {
        return this.issuerUniqueId;
    }
    
    public DERBitString getSubjectUniqueId() {
        return this.subjectUniqueId;
    }
    
    public X509Extensions getExtensions() {
        return this.extensions;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.seq;
    }
}
