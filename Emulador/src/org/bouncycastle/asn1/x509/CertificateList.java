// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertificateList extends ASN1Encodable
{
    TBSCertList tbsCertList;
    AlgorithmIdentifier sigAlgId;
    DERBitString sig;
    
    public static CertificateList getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static CertificateList getInstance(final Object o) {
        if (o instanceof CertificateList) {
            return (CertificateList)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertificateList((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public CertificateList(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 3) {
            this.tbsCertList = TBSCertList.getInstance(asn1Sequence.getObjectAt(0));
            this.sigAlgId = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.sig = DERBitString.getInstance(asn1Sequence.getObjectAt(2));
            return;
        }
        throw new IllegalArgumentException("sequence wrong size for CertificateList");
    }
    
    public TBSCertList getTBSCertList() {
        return this.tbsCertList;
    }
    
    public TBSCertList.CRLEntry[] getRevokedCertificates() {
        return this.tbsCertList.getRevokedCertificates();
    }
    
    public Enumeration getRevokedCertificateEnumeration() {
        return this.tbsCertList.getRevokedCertificateEnumeration();
    }
    
    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.sigAlgId;
    }
    
    public DERBitString getSignature() {
        return this.sig;
    }
    
    public int getVersion() {
        return this.tbsCertList.getVersion();
    }
    
    public X509Name getIssuer() {
        return this.tbsCertList.getIssuer();
    }
    
    public Time getThisUpdate() {
        return this.tbsCertList.getThisUpdate();
    }
    
    public Time getNextUpdate() {
        return this.tbsCertList.getNextUpdate();
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.tbsCertList);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(this.sig);
        return new DERSequence(asn1EncodableVector);
    }
}
