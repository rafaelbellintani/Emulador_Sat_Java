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
import org.bouncycastle.asn1.crmf.CertTemplate;
import org.bouncycastle.asn1.ASN1Encodable;

public class RevDetails extends ASN1Encodable
{
    private CertTemplate certDetails;
    private X509Extensions crlEntryDetails;
    
    private RevDetails(final ASN1Sequence asn1Sequence) {
        this.certDetails = CertTemplate.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.crlEntryDetails = X509Extensions.getInstance(asn1Sequence.getObjectAt(1));
        }
    }
    
    public static RevDetails getInstance(final Object o) {
        if (o instanceof RevDetails) {
            return (RevDetails)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RevDetails((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public CertTemplate getCertDetails() {
        return this.certDetails;
    }
    
    public X509Extensions getCrlEntryDetails() {
        return this.crlEntryDetails;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certDetails);
        if (this.crlEntryDetails != null) {
            asn1EncodableVector.add(this.crlEntryDetails);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
