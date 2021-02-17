// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class CRLDistPoint extends ASN1Encodable
{
    ASN1Sequence seq;
    
    public static CRLDistPoint getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static CRLDistPoint getInstance(final Object o) {
        if (o instanceof CRLDistPoint || o == null) {
            return (CRLDistPoint)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CRLDistPoint((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public CRLDistPoint(final ASN1Sequence seq) {
        this.seq = null;
        this.seq = seq;
    }
    
    public CRLDistPoint(final DistributionPoint[] array) {
        this.seq = null;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != array.length; ++i) {
            asn1EncodableVector.add(array[i]);
        }
        this.seq = new DERSequence(asn1EncodableVector);
    }
    
    public DistributionPoint[] getDistributionPoints() {
        final DistributionPoint[] array = new DistributionPoint[this.seq.size()];
        for (int i = 0; i != this.seq.size(); ++i) {
            array[i] = DistributionPoint.getInstance(this.seq.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.seq;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("CRLDistPoint:");
        sb.append(property);
        final DistributionPoint[] distributionPoints = this.getDistributionPoints();
        for (int i = 0; i != distributionPoints.length; ++i) {
            sb.append("    ");
            sb.append(distributionPoints[i]);
            sb.append(property);
        }
        return sb.toString();
    }
}
