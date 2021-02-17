// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class ResponseBytes extends ASN1Encodable
{
    DERObjectIdentifier responseType;
    ASN1OctetString response;
    
    public ResponseBytes(final DERObjectIdentifier responseType, final ASN1OctetString response) {
        this.responseType = responseType;
        this.response = response;
    }
    
    public ResponseBytes(final ASN1Sequence asn1Sequence) {
        this.responseType = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.response = (ASN1OctetString)asn1Sequence.getObjectAt(1);
    }
    
    public static ResponseBytes getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static ResponseBytes getInstance(final Object o) {
        if (o == null || o instanceof ResponseBytes) {
            return (ResponseBytes)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ResponseBytes((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public DERObjectIdentifier getResponseType() {
        return this.responseType;
    }
    
    public ASN1OctetString getResponse() {
        return this.response;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.responseType);
        asn1EncodableVector.add(this.response);
        return new DERSequence(asn1EncodableVector);
    }
}
