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
import org.bouncycastle.asn1.ASN1Encodable;

public class OCSPRequest extends ASN1Encodable
{
    TBSRequest tbsRequest;
    Signature optionalSignature;
    
    public OCSPRequest(final TBSRequest tbsRequest, final Signature optionalSignature) {
        this.tbsRequest = tbsRequest;
        this.optionalSignature = optionalSignature;
    }
    
    public OCSPRequest(final ASN1Sequence asn1Sequence) {
        this.tbsRequest = TBSRequest.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() == 2) {
            this.optionalSignature = Signature.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true);
        }
    }
    
    public static OCSPRequest getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static OCSPRequest getInstance(final Object o) {
        if (o == null || o instanceof OCSPRequest) {
            return (OCSPRequest)o;
        }
        if (o instanceof ASN1Sequence) {
            return new OCSPRequest((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public TBSRequest getTbsRequest() {
        return this.tbsRequest;
    }
    
    public Signature getOptionalSignature() {
        return this.optionalSignature;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.tbsRequest);
        if (this.optionalSignature != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.optionalSignature));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
