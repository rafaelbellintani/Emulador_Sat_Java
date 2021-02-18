// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertRepMessage extends ASN1Encodable
{
    private ASN1Sequence caPubs;
    private ASN1Sequence response;
    
    private CertRepMessage(final ASN1Sequence asn1Sequence) {
        int n = 0;
        if (asn1Sequence.size() > 1) {
            this.caPubs = ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n++), true);
        }
        this.response = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(n));
    }
    
    public static CertRepMessage getInstance(final Object o) {
        if (o instanceof CertRepMessage) {
            return (CertRepMessage)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertRepMessage((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public CMPCertificate[] getCaPubs() {
        if (this.caPubs == null) {
            return null;
        }
        final CMPCertificate[] array = new CMPCertificate[this.caPubs.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = CMPCertificate.getInstance(this.caPubs.getObjectAt(i));
        }
        return array;
    }
    
    public CertResponse[] getResponse() {
        final CertResponse[] array = new CertResponse[this.response.size()];
        for (int i = 0; i != array.length; ++i) {
            array[i] = CertResponse.getInstance(this.response.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.caPubs != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.caPubs));
        }
        asn1EncodableVector.add(this.response);
        return new DERSequence(asn1EncodableVector);
    }
}
