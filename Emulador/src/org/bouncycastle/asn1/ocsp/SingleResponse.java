// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.ASN1Encodable;

public class SingleResponse extends ASN1Encodable
{
    private CertID certID;
    private CertStatus certStatus;
    private DERGeneralizedTime thisUpdate;
    private DERGeneralizedTime nextUpdate;
    private X509Extensions singleExtensions;
    
    public SingleResponse(final CertID certID, final CertStatus certStatus, final DERGeneralizedTime thisUpdate, final DERGeneralizedTime nextUpdate, final X509Extensions singleExtensions) {
        this.certID = certID;
        this.certStatus = certStatus;
        this.thisUpdate = thisUpdate;
        this.nextUpdate = nextUpdate;
        this.singleExtensions = singleExtensions;
    }
    
    public SingleResponse(final ASN1Sequence asn1Sequence) {
        this.certID = CertID.getInstance(asn1Sequence.getObjectAt(0));
        this.certStatus = CertStatus.getInstance(asn1Sequence.getObjectAt(1));
        this.thisUpdate = (DERGeneralizedTime)asn1Sequence.getObjectAt(2);
        if (asn1Sequence.size() > 4) {
            this.nextUpdate = DERGeneralizedTime.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(3), true);
            this.singleExtensions = X509Extensions.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(4), true);
        }
        else if (asn1Sequence.size() > 3) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(3);
            if (asn1TaggedObject.getTagNo() == 0) {
                this.nextUpdate = DERGeneralizedTime.getInstance(asn1TaggedObject, true);
            }
            else {
                this.singleExtensions = X509Extensions.getInstance(asn1TaggedObject, true);
            }
        }
    }
    
    public static SingleResponse getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static SingleResponse getInstance(final Object o) {
        if (o == null || o instanceof SingleResponse) {
            return (SingleResponse)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SingleResponse((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public CertID getCertID() {
        return this.certID;
    }
    
    public CertStatus getCertStatus() {
        return this.certStatus;
    }
    
    public DERGeneralizedTime getThisUpdate() {
        return this.thisUpdate;
    }
    
    public DERGeneralizedTime getNextUpdate() {
        return this.nextUpdate;
    }
    
    public X509Extensions getSingleExtensions() {
        return this.singleExtensions;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certID);
        asn1EncodableVector.add(this.certStatus);
        asn1EncodableVector.add(this.thisUpdate);
        if (this.nextUpdate != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.nextUpdate));
        }
        if (this.singleExtensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.singleExtensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
