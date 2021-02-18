// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class TBSCertList extends ASN1Encodable
{
    ASN1Sequence seq;
    DERInteger version;
    AlgorithmIdentifier signature;
    X509Name issuer;
    Time thisUpdate;
    Time nextUpdate;
    ASN1Sequence revokedCertificates;
    X509Extensions crlExtensions;
    
    public static TBSCertList getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static TBSCertList getInstance(final Object o) {
        if (o instanceof TBSCertList) {
            return (TBSCertList)o;
        }
        if (o instanceof ASN1Sequence) {
            return new TBSCertList((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public TBSCertList(final ASN1Sequence seq) {
        if (seq.size() < 3 || seq.size() > 7) {
            throw new IllegalArgumentException("Bad sequence size: " + seq.size());
        }
        int n = 0;
        this.seq = seq;
        if (seq.getObjectAt(n) instanceof DERInteger) {
            this.version = DERInteger.getInstance(seq.getObjectAt(n++));
        }
        else {
            this.version = new DERInteger(0);
        }
        this.signature = AlgorithmIdentifier.getInstance(seq.getObjectAt(n++));
        this.issuer = X509Name.getInstance(seq.getObjectAt(n++));
        this.thisUpdate = Time.getInstance(seq.getObjectAt(n++));
        if (n < seq.size() && (seq.getObjectAt(n) instanceof DERUTCTime || seq.getObjectAt(n) instanceof DERGeneralizedTime || seq.getObjectAt(n) instanceof Time)) {
            this.nextUpdate = Time.getInstance(seq.getObjectAt(n++));
        }
        if (n < seq.size() && !(seq.getObjectAt(n) instanceof DERTaggedObject)) {
            this.revokedCertificates = ASN1Sequence.getInstance(seq.getObjectAt(n++));
        }
        if (n < seq.size() && seq.getObjectAt(n) instanceof DERTaggedObject) {
            this.crlExtensions = X509Extensions.getInstance(seq.getObjectAt(n));
        }
    }
    
    public int getVersion() {
        return this.version.getValue().intValue() + 1;
    }
    
    public DERInteger getVersionNumber() {
        return this.version;
    }
    
    public AlgorithmIdentifier getSignature() {
        return this.signature;
    }
    
    public X509Name getIssuer() {
        return this.issuer;
    }
    
    public Time getThisUpdate() {
        return this.thisUpdate;
    }
    
    public Time getNextUpdate() {
        return this.nextUpdate;
    }
    
    public CRLEntry[] getRevokedCertificates() {
        if (this.revokedCertificates == null) {
            return new CRLEntry[0];
        }
        final CRLEntry[] array = new CRLEntry[this.revokedCertificates.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new CRLEntry(ASN1Sequence.getInstance(this.revokedCertificates.getObjectAt(i)));
        }
        return array;
    }
    
    public Enumeration getRevokedCertificateEnumeration() {
        if (this.revokedCertificates == null) {
            return new EmptyEnumeration();
        }
        return new RevokedCertificatesEnumeration(this.revokedCertificates.getObjects());
    }
    
    public X509Extensions getExtensions() {
        return this.crlExtensions;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.seq;
    }
    
    public class CRLEntry extends ASN1Encodable
    {
        ASN1Sequence seq;
        DERInteger userCertificate;
        Time revocationDate;
        X509Extensions crlEntryExtensions;
        
        public CRLEntry(final ASN1Sequence seq) {
            if (seq.size() < 2 || seq.size() > 3) {
                throw new IllegalArgumentException("Bad sequence size: " + seq.size());
            }
            this.seq = seq;
            this.userCertificate = DERInteger.getInstance(seq.getObjectAt(0));
            this.revocationDate = Time.getInstance(seq.getObjectAt(1));
        }
        
        public DERInteger getUserCertificate() {
            return this.userCertificate;
        }
        
        public Time getRevocationDate() {
            return this.revocationDate;
        }
        
        public X509Extensions getExtensions() {
            if (this.crlEntryExtensions == null && this.seq.size() == 3) {
                this.crlEntryExtensions = X509Extensions.getInstance(this.seq.getObjectAt(2));
            }
            return this.crlEntryExtensions;
        }
        
        @Override
        public DERObject toASN1Object() {
            return this.seq;
        }
    }
    
    private class EmptyEnumeration implements Enumeration
    {
        @Override
        public boolean hasMoreElements() {
            return false;
        }
        
        @Override
        public Object nextElement() {
            return null;
        }
    }
    
    private class RevokedCertificatesEnumeration implements Enumeration
    {
        private final Enumeration en;
        
        RevokedCertificatesEnumeration(final Enumeration en) {
            this.en = en;
        }
        
        @Override
        public boolean hasMoreElements() {
            return this.en.hasMoreElements();
        }
        
        @Override
        public Object nextElement() {
            return new CRLEntry(ASN1Sequence.getInstance(this.en.nextElement()));
        }
    }
}
