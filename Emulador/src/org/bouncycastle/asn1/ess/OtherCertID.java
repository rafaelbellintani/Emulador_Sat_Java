// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.ASN1Encodable;

public class OtherCertID extends ASN1Encodable
{
    private ASN1Encodable otherCertHash;
    private IssuerSerial issuerSerial;
    
    public static OtherCertID getInstance(final Object o) {
        if (o == null || o instanceof OtherCertID) {
            return (OtherCertID)o;
        }
        if (o instanceof ASN1Sequence) {
            return new OtherCertID((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in 'OtherCertID' factory : " + o.getClass().getName() + ".");
    }
    
    public OtherCertID(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 1 || asn1Sequence.size() > 2) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        if (asn1Sequence.getObjectAt(0).getDERObject() instanceof ASN1OctetString) {
            this.otherCertHash = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0));
        }
        else {
            this.otherCertHash = DigestInfo.getInstance(asn1Sequence.getObjectAt(0));
        }
        if (asn1Sequence.size() > 1) {
            this.issuerSerial = new IssuerSerial(ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1)));
        }
    }
    
    public OtherCertID(final AlgorithmIdentifier algorithmIdentifier, final byte[] array) {
        this.otherCertHash = new DigestInfo(algorithmIdentifier, array);
    }
    
    public OtherCertID(final AlgorithmIdentifier algorithmIdentifier, final byte[] array, final IssuerSerial issuerSerial) {
        this.otherCertHash = new DigestInfo(algorithmIdentifier, array);
        this.issuerSerial = issuerSerial;
    }
    
    public AlgorithmIdentifier getAlgorithmHash() {
        if (this.otherCertHash.getDERObject() instanceof ASN1OctetString) {
            return new AlgorithmIdentifier("1.3.14.3.2.26");
        }
        return DigestInfo.getInstance(this.otherCertHash).getAlgorithmId();
    }
    
    public byte[] getCertHash() {
        if (this.otherCertHash.getDERObject() instanceof ASN1OctetString) {
            return ((ASN1OctetString)this.otherCertHash.getDERObject()).getOctets();
        }
        return DigestInfo.getInstance(this.otherCertHash).getDigest();
    }
    
    public IssuerSerial getIssuerSerial() {
        return this.issuerSerial;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.otherCertHash);
        if (this.issuerSerial != null) {
            asn1EncodableVector.add(this.issuerSerial);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
