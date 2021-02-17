// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class ResponseData extends ASN1Encodable
{
    private static final DERInteger V1;
    private boolean versionPresent;
    private DERInteger version;
    private ResponderID responderID;
    private DERGeneralizedTime producedAt;
    private ASN1Sequence responses;
    private X509Extensions responseExtensions;
    
    public ResponseData(final DERInteger version, final ResponderID responderID, final DERGeneralizedTime producedAt, final ASN1Sequence responses, final X509Extensions responseExtensions) {
        this.version = version;
        this.responderID = responderID;
        this.producedAt = producedAt;
        this.responses = responses;
        this.responseExtensions = responseExtensions;
    }
    
    public ResponseData(final ResponderID responderID, final DERGeneralizedTime derGeneralizedTime, final ASN1Sequence asn1Sequence, final X509Extensions x509Extensions) {
        this(ResponseData.V1, responderID, derGeneralizedTime, asn1Sequence, x509Extensions);
    }
    
    public ResponseData(final ASN1Sequence asn1Sequence) {
        int n = 0;
        if (asn1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
            if (((ASN1TaggedObject)asn1Sequence.getObjectAt(0)).getTagNo() == 0) {
                this.versionPresent = true;
                this.version = DERInteger.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(0), true);
                ++n;
            }
            else {
                this.version = ResponseData.V1;
            }
        }
        else {
            this.version = ResponseData.V1;
        }
        this.responderID = ResponderID.getInstance(asn1Sequence.getObjectAt(n++));
        this.producedAt = (DERGeneralizedTime)asn1Sequence.getObjectAt(n++);
        this.responses = (ASN1Sequence)asn1Sequence.getObjectAt(n++);
        if (asn1Sequence.size() > n) {
            this.responseExtensions = X509Extensions.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n), true);
        }
    }
    
    public static ResponseData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static ResponseData getInstance(final Object o) {
        if (o == null || o instanceof ResponseData) {
            return (ResponseData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ResponseData((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public ResponderID getResponderID() {
        return this.responderID;
    }
    
    public DERGeneralizedTime getProducedAt() {
        return this.producedAt;
    }
    
    public ASN1Sequence getResponses() {
        return this.responses;
    }
    
    public X509Extensions getResponseExtensions() {
        return this.responseExtensions;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.versionPresent || !this.version.equals(ResponseData.V1)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.version));
        }
        asn1EncodableVector.add(this.responderID);
        asn1EncodableVector.add(this.producedAt);
        asn1EncodableVector.add(this.responses);
        if (this.responseExtensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.responseExtensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    static {
        V1 = new DERInteger(0);
    }
}
