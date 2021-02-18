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
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertID extends ASN1Encodable
{
    AlgorithmIdentifier hashAlgorithm;
    ASN1OctetString issuerNameHash;
    ASN1OctetString issuerKeyHash;
    DERInteger serialNumber;
    
    public CertID(final AlgorithmIdentifier hashAlgorithm, final ASN1OctetString issuerNameHash, final ASN1OctetString issuerKeyHash, final DERInteger serialNumber) {
        this.hashAlgorithm = hashAlgorithm;
        this.issuerNameHash = issuerNameHash;
        this.issuerKeyHash = issuerKeyHash;
        this.serialNumber = serialNumber;
    }
    
    public CertID(final ASN1Sequence asn1Sequence) {
        this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.issuerNameHash = (ASN1OctetString)asn1Sequence.getObjectAt(1);
        this.issuerKeyHash = (ASN1OctetString)asn1Sequence.getObjectAt(2);
        this.serialNumber = (DERInteger)asn1Sequence.getObjectAt(3);
    }
    
    public static CertID getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static CertID getInstance(final Object o) {
        if (o == null || o instanceof CertID) {
            return (CertID)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertID((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public ASN1OctetString getIssuerNameHash() {
        return this.issuerNameHash;
    }
    
    public ASN1OctetString getIssuerKeyHash() {
        return this.issuerKeyHash;
    }
    
    public DERInteger getSerialNumber() {
        return this.serialNumber;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.hashAlgorithm);
        asn1EncodableVector.add(this.issuerNameHash);
        asn1EncodableVector.add(this.issuerKeyHash);
        asn1EncodableVector.add(this.serialNumber);
        return new DERSequence(asn1EncodableVector);
    }
}
