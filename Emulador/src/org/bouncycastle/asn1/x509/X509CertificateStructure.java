// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.ASN1Encodable;

public class X509CertificateStructure extends ASN1Encodable implements X509ObjectIdentifiers, PKCSObjectIdentifiers
{
    ASN1Sequence seq;
    TBSCertificateStructure tbsCert;
    AlgorithmIdentifier sigAlgId;
    DERBitString sig;
    
    public static X509CertificateStructure getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static X509CertificateStructure getInstance(final Object o) {
        if (o instanceof X509CertificateStructure) {
            return (X509CertificateStructure)o;
        }
        if (o instanceof ASN1Sequence) {
            return new X509CertificateStructure((ASN1Sequence)o);
        }
        if (o != null) {
            throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
        }
        throw new IllegalArgumentException("null object in factory");
    }
    
    public X509CertificateStructure(final ASN1Sequence seq) {
        this.seq = seq;
        if (seq.size() == 3) {
            this.tbsCert = TBSCertificateStructure.getInstance(seq.getObjectAt(0));
            this.sigAlgId = AlgorithmIdentifier.getInstance(seq.getObjectAt(1));
            this.sig = DERBitString.getInstance(seq.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("sequence wrong size for a certificate");
    }
    
    public TBSCertificateStructure getTBSCertificate() {
        return this.tbsCert;
    }
    
    public int getVersion() {
        return this.tbsCert.getVersion();
    }
    
    public DERInteger getSerialNumber() {
        return this.tbsCert.getSerialNumber();
    }
    
    public X509Name getIssuer() {
        return this.tbsCert.getIssuer();
    }
    
    public Time getStartDate() {
        return this.tbsCert.getStartDate();
    }
    
    public Time getEndDate() {
        return this.tbsCert.getEndDate();
    }
    
    public X509Name getSubject() {
        return this.tbsCert.getSubject();
    }
    
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.tbsCert.getSubjectPublicKeyInfo();
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.sigAlgId;
    }
    
    public DERBitString getSignature() {
        return this.sig;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.seq;
    }
}
