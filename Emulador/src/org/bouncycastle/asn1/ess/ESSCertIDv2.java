// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class ESSCertIDv2 extends ASN1Encodable
{
    private AlgorithmIdentifier hashAlgorithm;
    private byte[] certHash;
    private IssuerSerial issuerSerial;
    private static final AlgorithmIdentifier DEFAULT_ALG_ID;
    
    public static ESSCertIDv2 getInstance(final Object o) {
        if (o == null || o instanceof ESSCertIDv2) {
            return (ESSCertIDv2)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ESSCertIDv2((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in 'ESSCertIDv2' factory : " + o.getClass().getName() + ".");
    }
    
    public ESSCertIDv2(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2 && asn1Sequence.size() != 3) {
            throw new IllegalArgumentException("Bad sequence size: " + asn1Sequence.size());
        }
        int n = 0;
        if (asn1Sequence.getObjectAt(0) instanceof ASN1OctetString) {
            this.hashAlgorithm = ESSCertIDv2.DEFAULT_ALG_ID;
        }
        else {
            this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(n++).getDERObject());
        }
        this.certHash = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n++).getDERObject()).getOctets();
        if (asn1Sequence.size() > n) {
            this.issuerSerial = new IssuerSerial(ASN1Sequence.getInstance(asn1Sequence.getObjectAt(n).getDERObject()));
        }
    }
    
    public ESSCertIDv2(final AlgorithmIdentifier algorithmIdentifier, final byte[] array) {
        this(algorithmIdentifier, array, null);
    }
    
    public ESSCertIDv2(final AlgorithmIdentifier hashAlgorithm, final byte[] certHash, final IssuerSerial issuerSerial) {
        if (hashAlgorithm == null) {
            this.hashAlgorithm = ESSCertIDv2.DEFAULT_ALG_ID;
        }
        else {
            this.hashAlgorithm = hashAlgorithm;
        }
        this.certHash = certHash;
        this.issuerSerial = issuerSerial;
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public byte[] getCertHash() {
        return this.certHash;
    }
    
    public IssuerSerial getIssuerSerial() {
        return this.issuerSerial;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (!this.hashAlgorithm.equals(ESSCertIDv2.DEFAULT_ALG_ID)) {
            asn1EncodableVector.add(this.hashAlgorithm);
        }
        asn1EncodableVector.add(new DEROctetString(this.certHash).toASN1Object());
        if (this.issuerSerial != null) {
            asn1EncodableVector.add(this.issuerSerial);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    static {
        DEFAULT_ALG_ID = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
    }
}
