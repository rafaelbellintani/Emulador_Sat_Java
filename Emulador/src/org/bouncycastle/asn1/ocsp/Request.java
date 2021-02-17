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
import org.bouncycastle.asn1.ASN1Encodable;

public class Request extends ASN1Encodable
{
    CertID reqCert;
    X509Extensions singleRequestExtensions;
    
    public Request(final CertID reqCert, final X509Extensions singleRequestExtensions) {
        this.reqCert = reqCert;
        this.singleRequestExtensions = singleRequestExtensions;
    }
    
    public Request(final ASN1Sequence asn1Sequence) {
        this.reqCert = CertID.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() == 2) {
            this.singleRequestExtensions = X509Extensions.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true);
        }
    }
    
    public static Request getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static Request getInstance(final Object o) {
        if (o == null || o instanceof Request) {
            return (Request)o;
        }
        if (o instanceof ASN1Sequence) {
            return new Request((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public CertID getReqCert() {
        return this.reqCert;
    }
    
    public X509Extensions getSingleRequestExtensions() {
        return this.singleRequestExtensions;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.reqCert);
        if (this.singleRequestExtensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.singleRequestExtensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
