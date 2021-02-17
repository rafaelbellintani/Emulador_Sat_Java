// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.ASN1Encodable;

public class CertId extends ASN1Encodable
{
    private GeneralName issuer;
    private DERInteger serialNumber;
    
    private CertId(final ASN1Sequence asn1Sequence) {
        this.issuer = GeneralName.getInstance(asn1Sequence.getObjectAt(0));
        this.serialNumber = DERInteger.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public static CertId getInstance(final Object o) {
        if (o instanceof CertId) {
            return (CertId)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertId((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public static CertId getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralName getIssuer() {
        return this.issuer;
    }
    
    public DERInteger getSerialNumber() {
        return this.serialNumber;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.issuer);
        asn1EncodableVector.add(this.serialNumber);
        return new DERSequence(asn1EncodableVector);
    }
}
