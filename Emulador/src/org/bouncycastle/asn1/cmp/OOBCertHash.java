// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.crmf.CertId;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class OOBCertHash extends ASN1Encodable
{
    private AlgorithmIdentifier hashAlg;
    private CertId certId;
    private DERBitString hashVal;
    
    private OOBCertHash(final ASN1Sequence asn1Sequence) {
        int n = asn1Sequence.size() - 1;
        this.hashVal = DERBitString.getInstance(asn1Sequence.getObjectAt(n--));
        for (int i = n; i >= 0; --i) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(i);
            if (asn1TaggedObject.getTagNo() == 0) {
                this.hashAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, true);
            }
            else {
                this.certId = CertId.getInstance(asn1TaggedObject, true);
            }
        }
    }
    
    public static OOBCertHash getInstance(final Object o) {
        if (o instanceof OOBCertHash) {
            return (OOBCertHash)o;
        }
        if (o instanceof ASN1Sequence) {
            return new OOBCertHash((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public AlgorithmIdentifier getHashAlg() {
        return this.hashAlg;
    }
    
    public CertId getCertId() {
        return this.certId;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        this.addOptional(asn1EncodableVector, 0, this.hashAlg);
        this.addOptional(asn1EncodableVector, 1, this.certId);
        asn1EncodableVector.add(this.hashVal);
        return new DERSequence(asn1EncodableVector);
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
}
