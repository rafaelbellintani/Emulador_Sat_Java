// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.ASN1Encodable;

public class IssuerAndSerialNumber extends ASN1Encodable
{
    X509Name name;
    DERInteger certSerialNumber;
    
    public static IssuerAndSerialNumber getInstance(final Object o) {
        if (o instanceof IssuerAndSerialNumber) {
            return (IssuerAndSerialNumber)o;
        }
        if (o instanceof ASN1Sequence) {
            return new IssuerAndSerialNumber((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public IssuerAndSerialNumber(final ASN1Sequence asn1Sequence) {
        this.name = X509Name.getInstance(asn1Sequence.getObjectAt(0));
        this.certSerialNumber = (DERInteger)asn1Sequence.getObjectAt(1);
    }
    
    public IssuerAndSerialNumber(final X509Name name, final BigInteger bigInteger) {
        this.name = name;
        this.certSerialNumber = new DERInteger(bigInteger);
    }
    
    public IssuerAndSerialNumber(final X509Name name, final DERInteger certSerialNumber) {
        this.name = name;
        this.certSerialNumber = certSerialNumber;
    }
    
    public X509Name getName() {
        return this.name;
    }
    
    public DERInteger getCertificateSerialNumber() {
        return this.certSerialNumber;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.name);
        asn1EncodableVector.add(this.certSerialNumber);
        return new DERSequence(asn1EncodableVector);
    }
}
