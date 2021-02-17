// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1Encodable;

public class SinglePubInfo extends ASN1Encodable
{
    private DERInteger pubMethod;
    private GeneralName pubLocation;
    
    private SinglePubInfo(final ASN1Sequence asn1Sequence) {
        this.pubMethod = DERInteger.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() == 2) {
            this.pubLocation = GeneralName.getInstance(asn1Sequence.getObjectAt(1));
        }
    }
    
    public static SinglePubInfo getInstance(final Object o) {
        if (o instanceof SinglePubInfo) {
            return (SinglePubInfo)o;
        }
        if (o instanceof ASN1Sequence) {
            return new SinglePubInfo((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid object: " + o.getClass().getName());
    }
    
    public GeneralName getPubLocation() {
        return this.pubLocation;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pubMethod);
        if (this.pubLocation != null) {
            asn1EncodableVector.add(this.pubLocation);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
