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
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Encodable;

public class PBMParameter extends ASN1Encodable
{
    private ASN1OctetString salt;
    private AlgorithmIdentifier owf;
    private DERInteger iterationCount;
    private AlgorithmIdentifier mac;
    
    private PBMParameter(final ASN1Sequence asn1Sequence) {
        this.salt = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0));
        this.owf = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
        this.iterationCount = DERInteger.getInstance(asn1Sequence.getObjectAt(2));
        this.mac = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(3));
    }
    
    public static PBMParameter getInstance(final Object o) {
        if (o instanceof PBMParameter) {
            return (PBMParameter)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PBMParameter((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public AlgorithmIdentifier getOwf() {
        return this.owf;
    }
    
    public DERInteger getIterationCount() {
        return this.iterationCount;
    }
    
    public AlgorithmIdentifier getMac() {
        return this.mac;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.salt);
        asn1EncodableVector.add(this.owf);
        asn1EncodableVector.add(this.iterationCount);
        asn1EncodableVector.add(this.mac);
        return new DERSequence(asn1EncodableVector);
    }
}
