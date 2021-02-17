// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class IssuingDistributionPoint extends ASN1Encodable
{
    private DistributionPointName distributionPoint;
    private boolean onlyContainsUserCerts;
    private boolean onlyContainsCACerts;
    private ReasonFlags onlySomeReasons;
    private boolean indirectCRL;
    private boolean onlyContainsAttributeCerts;
    private ASN1Sequence seq;
    
    public static IssuingDistributionPoint getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static IssuingDistributionPoint getInstance(final Object o) {
        if (o == null || o instanceof IssuingDistributionPoint) {
            return (IssuingDistributionPoint)o;
        }
        if (o instanceof ASN1Sequence) {
            return new IssuingDistributionPoint((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public IssuingDistributionPoint(final DistributionPointName distributionPoint, final boolean onlyContainsUserCerts, final boolean onlyContainsCACerts, final ReasonFlags onlySomeReasons, final boolean indirectCRL, final boolean onlyContainsAttributeCerts) {
        this.distributionPoint = distributionPoint;
        this.indirectCRL = indirectCRL;
        this.onlyContainsAttributeCerts = onlyContainsAttributeCerts;
        this.onlyContainsCACerts = onlyContainsCACerts;
        this.onlyContainsUserCerts = onlyContainsUserCerts;
        this.onlySomeReasons = onlySomeReasons;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (distributionPoint != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, distributionPoint));
        }
        if (onlyContainsUserCerts) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, new DERBoolean(true)));
        }
        if (onlyContainsCACerts) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, new DERBoolean(true)));
        }
        if (onlySomeReasons != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 3, onlySomeReasons));
        }
        if (indirectCRL) {
            asn1EncodableVector.add(new DERTaggedObject(false, 4, new DERBoolean(true)));
        }
        if (onlyContainsAttributeCerts) {
            asn1EncodableVector.add(new DERTaggedObject(false, 5, new DERBoolean(true)));
        }
        this.seq = new DERSequence(asn1EncodableVector);
    }
    
    public IssuingDistributionPoint(final ASN1Sequence seq) {
        this.seq = seq;
        for (int i = 0; i != seq.size(); ++i) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(seq.getObjectAt(i));
            switch (instance.getTagNo()) {
                case 0: {
                    this.distributionPoint = DistributionPointName.getInstance(instance, true);
                    break;
                }
                case 1: {
                    this.onlyContainsUserCerts = DERBoolean.getInstance(instance, false).isTrue();
                    break;
                }
                case 2: {
                    this.onlyContainsCACerts = DERBoolean.getInstance(instance, false).isTrue();
                    break;
                }
                case 3: {
                    this.onlySomeReasons = new ReasonFlags(DERBitString.getInstance(instance, false));
                    break;
                }
                case 4: {
                    this.indirectCRL = DERBoolean.getInstance(instance, false).isTrue();
                    break;
                }
                case 5: {
                    this.onlyContainsAttributeCerts = DERBoolean.getInstance(instance, false).isTrue();
                    break;
                }
                default: {
                    throw new IllegalArgumentException("unknown tag in IssuingDistributionPoint");
                }
            }
        }
    }
    
    public boolean onlyContainsUserCerts() {
        return this.onlyContainsUserCerts;
    }
    
    public boolean onlyContainsCACerts() {
        return this.onlyContainsCACerts;
    }
    
    public boolean isIndirectCRL() {
        return this.indirectCRL;
    }
    
    public boolean onlyContainsAttributeCerts() {
        return this.onlyContainsAttributeCerts;
    }
    
    public DistributionPointName getDistributionPoint() {
        return this.distributionPoint;
    }
    
    public ReasonFlags getOnlySomeReasons() {
        return this.onlySomeReasons;
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.seq;
    }
    
    @Override
    public String toString() {
        final String property = System.getProperty("line.separator");
        final StringBuffer sb = new StringBuffer();
        sb.append("IssuingDistributionPoint: [");
        sb.append(property);
        if (this.distributionPoint != null) {
            this.appendObject(sb, property, "distributionPoint", this.distributionPoint.toString());
        }
        if (this.onlyContainsUserCerts) {
            this.appendObject(sb, property, "onlyContainsUserCerts", this.booleanToString(this.onlyContainsUserCerts));
        }
        if (this.onlyContainsCACerts) {
            this.appendObject(sb, property, "onlyContainsCACerts", this.booleanToString(this.onlyContainsCACerts));
        }
        if (this.onlySomeReasons != null) {
            this.appendObject(sb, property, "onlySomeReasons", this.onlySomeReasons.toString());
        }
        if (this.onlyContainsAttributeCerts) {
            this.appendObject(sb, property, "onlyContainsAttributeCerts", this.booleanToString(this.onlyContainsAttributeCerts));
        }
        if (this.indirectCRL) {
            this.appendObject(sb, property, "indirectCRL", this.booleanToString(this.indirectCRL));
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
    
    private String booleanToString(final boolean b) {
        return b ? "true" : "false";
    }
}
