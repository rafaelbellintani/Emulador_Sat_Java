// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Encodable;

public class DistributionPoint extends ASN1Encodable
{
    DistributionPointName distributionPoint;
    ReasonFlags reasons;
    GeneralNames cRLIssuer;
    
    public static DistributionPoint getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static DistributionPoint getInstance(final Object o) {
        if (o == null || o instanceof DistributionPoint) {
            return (DistributionPoint)o;
        }
        if (o instanceof ASN1Sequence) {
            return new DistributionPoint((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid DistributionPoint: " + o.getClass().getName());
    }
    
    public DistributionPoint(final ASN1Sequence asn1Sequence) {
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(i));
            switch (instance.getTagNo()) {
                case 0: {
                    this.distributionPoint = DistributionPointName.getInstance(instance, true);
                    break;
                }
                case 1: {
                    this.reasons = new ReasonFlags(DERBitString.getInstance(instance, false));
                    break;
                }
                case 2: {
                    this.cRLIssuer = GeneralNames.getInstance(instance, false);
                    break;
                }
            }
        }
    }
    
    public DistributionPoint(final DistributionPointName distributionPoint, final ReasonFlags reasons, final GeneralNames crlIssuer) {
        this.distributionPoint = distributionPoint;
        this.reasons = reasons;
        this.cRLIssuer = crlIssuer;
    }
    
    public DistributionPointName getDistributionPoint() {
        return this.distributionPoint;
    }
    
    public ReasonFlags getReasons() {
        return this.reasons;
    }
    
    public GeneralNames getCRLIssuer() {
        return this.cRLIssuer;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.distributionPoint != null) {
            asn1EncodableVector.add(new DERTaggedObject(0, this.distributionPoint));
        }
        if (this.reasons != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.reasons));
        }
        if (this.cRLIssuer != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.cRLIssuer));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final String property = System.getProperty("line.separator");
        final StringBuffer sb = new StringBuffer();
        sb.append("DistributionPoint: [");
        sb.append(property);
        if (this.distributionPoint != null) {
            this.appendObject(sb, property, "distributionPoint", this.distributionPoint.toString());
        }
        if (this.reasons != null) {
            this.appendObject(sb, property, "reasons", this.reasons.toString());
        }
        if (this.cRLIssuer != null) {
            this.appendObject(sb, property, "cRLIssuer", this.cRLIssuer.toString());
        }
        sb.append("]");
        sb.append(property);
        return sb.toString();
    }
    
    private void appendObject(final StringBuffer sb, final String s, final String str, final String str2) {
        final String str3 = "    ";
        sb.append(str3);
        sb.append(str);
        sb.append(":");
        sb.append(s);
        sb.append(str3);
        sb.append(str3);
        sb.append(str2);
        sb.append(s);
    }
}
