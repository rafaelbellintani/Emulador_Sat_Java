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
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class OCSPResponse extends ASN1Encodable
{
    OCSPResponseStatus responseStatus;
    ResponseBytes responseBytes;
    
    public OCSPResponse(final OCSPResponseStatus responseStatus, final ResponseBytes responseBytes) {
        this.responseStatus = responseStatus;
        this.responseBytes = responseBytes;
    }
    
    public OCSPResponse(final ASN1Sequence asn1Sequence) {
        this.responseStatus = new OCSPResponseStatus(DEREnumerated.getInstance(asn1Sequence.getObjectAt(0)));
        if (asn1Sequence.size() == 2) {
            this.responseBytes = ResponseBytes.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true);
        }
    }
    
    public static OCSPResponse getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static OCSPResponse getInstance(final Object o) {
        if (o == null || o instanceof OCSPResponse) {
            return (OCSPResponse)o;
        }
        if (o instanceof ASN1Sequence) {
            return new OCSPResponse((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public OCSPResponseStatus getResponseStatus() {
        return this.responseStatus;
    }
    
    public ResponseBytes getResponseBytes() {
        return this.responseBytes;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.responseStatus);
        if (this.responseBytes != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.responseBytes));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
