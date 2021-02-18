// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class AccessDescription extends ASN1Encodable
{
    public static final DERObjectIdentifier id_ad_caIssuers;
    public static final DERObjectIdentifier id_ad_ocsp;
    DERObjectIdentifier accessMethod;
    GeneralName accessLocation;
    
    public static AccessDescription getInstance(final Object o) {
        if (o instanceof AccessDescription) {
            return (AccessDescription)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AccessDescription((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public AccessDescription(final ASN1Sequence asn1Sequence) {
        this.accessMethod = null;
        this.accessLocation = null;
        if (asn1Sequence.size() != 2) {
            throw new IllegalArgumentException("wrong number of elements in sequence");
        }
        this.accessMethod = DERObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.accessLocation = GeneralName.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public AccessDescription(final DERObjectIdentifier accessMethod, final GeneralName accessLocation) {
        this.accessMethod = null;
        this.accessLocation = null;
        this.accessMethod = accessMethod;
        this.accessLocation = accessLocation;
    }
    
    public DERObjectIdentifier getAccessMethod() {
        return this.accessMethod;
    }
    
    public GeneralName getAccessLocation() {
        return this.accessLocation;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.accessMethod);
        asn1EncodableVector.add(this.accessLocation);
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        return "AccessDescription: Oid(" + this.accessMethod.getId() + ")";
    }
    
    static {
        id_ad_caIssuers = new DERObjectIdentifier("1.3.6.1.5.5.7.48.2");
        id_ad_ocsp = new DERObjectIdentifier("1.3.6.1.5.5.7.48.1");
    }
}
