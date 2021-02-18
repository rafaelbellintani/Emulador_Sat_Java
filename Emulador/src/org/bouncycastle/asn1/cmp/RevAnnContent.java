// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.crmf.CertId;
import org.bouncycastle.asn1.ASN1Encodable;

public class RevAnnContent extends ASN1Encodable
{
    private PKIStatus status;
    private CertId certId;
    private DERGeneralizedTime willBeRevokedAt;
    private DERGeneralizedTime badSinceDate;
    private X509Extensions crlDetails;
    
    private RevAnnContent(final ASN1Sequence asn1Sequence) {
        this.status = PKIStatus.getInstance(asn1Sequence.getObjectAt(0));
        this.certId = CertId.getInstance(asn1Sequence.getObjectAt(1));
        this.willBeRevokedAt = DERGeneralizedTime.getInstance(asn1Sequence.getObjectAt(2));
        this.badSinceDate = DERGeneralizedTime.getInstance(asn1Sequence.getObjectAt(3));
        if (asn1Sequence.size() > 4) {
            this.crlDetails = X509Extensions.getInstance(asn1Sequence.getObjectAt(4));
        }
    }
    
    public static RevAnnContent getInstance(final Object o) {
        if (o instanceof RevAnnContent) {
            return (RevAnnContent)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RevAnnContent((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public PKIStatus getStatus() {
        return this.status;
    }
    
    public CertId getCertId() {
        return this.certId;
    }
    
    public DERGeneralizedTime getWillBeRevokedAt() {
        return this.willBeRevokedAt;
    }
    
    public DERGeneralizedTime getBadSinceDate() {
        return this.badSinceDate;
    }
    
    public X509Extensions getCrlDetails() {
        return this.crlDetails;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.status);
        asn1EncodableVector.add(this.certId);
        asn1EncodableVector.add(this.willBeRevokedAt);
        asn1EncodableVector.add(this.badSinceDate);
        if (this.crlDetails != null) {
            asn1EncodableVector.add(this.crlDetails);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
