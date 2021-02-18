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
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class TBSRequest extends ASN1Encodable
{
    private static final DERInteger V1;
    DERInteger version;
    GeneralName requestorName;
    ASN1Sequence requestList;
    X509Extensions requestExtensions;
    boolean versionSet;
    
    public TBSRequest(final GeneralName requestorName, final ASN1Sequence requestList, final X509Extensions requestExtensions) {
        this.version = TBSRequest.V1;
        this.requestorName = requestorName;
        this.requestList = requestList;
        this.requestExtensions = requestExtensions;
    }
    
    public TBSRequest(final ASN1Sequence asn1Sequence) {
        int n = 0;
        if (asn1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
            if (((ASN1TaggedObject)asn1Sequence.getObjectAt(0)).getTagNo() == 0) {
                this.versionSet = true;
                this.version = DERInteger.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(0), true);
                ++n;
            }
            else {
                this.version = TBSRequest.V1;
            }
        }
        else {
            this.version = TBSRequest.V1;
        }
        if (asn1Sequence.getObjectAt(n) instanceof ASN1TaggedObject) {
            this.requestorName = GeneralName.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n++), true);
        }
        this.requestList = (ASN1Sequence)asn1Sequence.getObjectAt(n++);
        if (asn1Sequence.size() == n + 1) {
            this.requestExtensions = X509Extensions.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n), true);
        }
    }
    
    public static TBSRequest getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static TBSRequest getInstance(final Object o) {
        if (o == null || o instanceof TBSRequest) {
            return (TBSRequest)o;
        }
        if (o instanceof ASN1Sequence) {
            return new TBSRequest((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public DERInteger getVersion() {
        return this.version;
    }
    
    public GeneralName getRequestorName() {
        return this.requestorName;
    }
    
    public ASN1Sequence getRequestList() {
        return this.requestList;
    }
    
    public X509Extensions getRequestExtensions() {
        return this.requestExtensions;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (!this.version.equals(TBSRequest.V1) || this.versionSet) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.version));
        }
        if (this.requestorName != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.requestorName));
        }
        asn1EncodableVector.add(this.requestList);
        if (this.requestExtensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.requestExtensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    static {
        V1 = new DERInteger(0);
    }
}
