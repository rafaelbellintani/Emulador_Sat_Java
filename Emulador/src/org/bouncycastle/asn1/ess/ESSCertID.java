// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Encodable;

public class ESSCertID extends ASN1Encodable
{
    private ASN1OctetString certHash;
    private IssuerSerial issuerSerial;
    
    public static ESSCertID getInstance(final Object o) {
        if (o == null || o instanceof ESSCertID) {
            return (ESSCertID)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ESSCertID((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in 'ESSCertID' factory : " + o.getClass().getName() + ".");
    }
    
    public ESSCertID(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 1 || asn1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        this.certHash = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.issuerSerial = IssuerSerial.getInstance(asn1Sequence.getObjectAt(1));
        }
    }
    
    public ESSCertID(final byte[] array) {
        this.certHash = new DEROctetString(array);
    }
    
    public ESSCertID(final byte[] array, final IssuerSerial issuerSerial) {
        this.certHash = new DEROctetString(array);
        this.issuerSerial = issuerSerial;
    }
    
    public byte[] getCertHash() {
        return this.certHash.getOctets();
    }
    
    public IssuerSerial getIssuerSerial() {
        return this.issuerSerial;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certHash);
        if (this.issuerSerial != null) {
            asn1EncodableVector.add(this.issuerSerial);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
