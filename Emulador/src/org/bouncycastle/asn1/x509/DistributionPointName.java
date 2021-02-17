// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;

public class DistributionPointName extends ASN1Encodable implements ASN1Choice
{
    DEREncodable name;
    int type;
    public static final int FULL_NAME = 0;
    public static final int NAME_RELATIVE_TO_CRL_ISSUER = 1;
    
    public static DistributionPointName getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1TaggedObject.getInstance(asn1TaggedObject, true));
    }
    
    public static DistributionPointName getInstance(final Object o) {
        if (o == null || o instanceof DistributionPointName) {
            return (DistributionPointName)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new DistributionPointName((ASN1TaggedObject)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public DistributionPointName(final int type, final DEREncodable name) {
        this.type = type;
        this.name = name;
    }
    
    public DistributionPointName(final int type, final ASN1Encodable name) {
        this.type = type;
        this.name = name;
    }
    
    public DistributionPointName(final GeneralNames generalNames) {
        this(0, generalNames);
    }
    
    public int getType() {
        return this.type;
    }
    
    public ASN1Encodable getName() {
        return (ASN1Encodable)this.name;
    }
    
    public DistributionPointName(final ASN1TaggedObject asn1TaggedObject) {
        this.type = asn1TaggedObject.getTagNo();
        if (this.type == 0) {
            this.name = GeneralNames.getInstance(asn1TaggedObject, false);
        }
        else {
            this.name = ASN1Set.getInstance(asn1TaggedObject, false);
        }
    }
    
    @Override
    public DERObject toASN1Object() {
        return new DERTaggedObject(false, this.type, this.name);
    }
    
    @Override
    public String toString() {
        final String property = System.getProperty("line.separator");
        final StringBuffer sb = new StringBuffer();
        sb.append("DistributionPointName: [");
        sb.append(property);
        if (this.type == 0) {
            this.appendObject(sb, property, "fullName", this.name.toString());
        }
        else {
            this.appendObject(sb, property, "nameRelativeToCRLIssuer", this.name.toString());
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
