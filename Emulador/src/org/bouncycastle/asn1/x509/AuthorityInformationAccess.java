// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class AuthorityInformationAccess extends ASN1Encodable
{
    private AccessDescription[] descriptions;
    
    public static AuthorityInformationAccess getInstance(final Object o) {
        if (o instanceof AuthorityInformationAccess) {
            return (AuthorityInformationAccess)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AuthorityInformationAccess((ASN1Sequence)o);
        }
        if (o instanceof X509Extension) {
            return getInstance(X509Extension.convertValueToObject((X509Extension)o));
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public AuthorityInformationAccess(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 1) {
            throw new IllegalArgumentException("sequence may not be empty");
        }
        this.descriptions = new AccessDescription[asn1Sequence.size()];
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            this.descriptions[i] = AccessDescription.getInstance(asn1Sequence.getObjectAt(i));
        }
    }
    
    public AuthorityInformationAccess(final DERObjectIdentifier derObjectIdentifier, final GeneralName generalName) {
        (this.descriptions = new AccessDescription[1])[0] = new AccessDescription(derObjectIdentifier, generalName);
    }
    
    public AccessDescription[] getAccessDescriptions() {
        return this.descriptions;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != this.descriptions.length; ++i) {
            asn1EncodableVector.add(this.descriptions[i]);
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        return "AuthorityInformationAccess: Oid(" + this.descriptions[0].getAccessMethod().getId() + ")";
    }
}
