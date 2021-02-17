// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.mozilla;

import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class PublicKeyAndChallenge extends ASN1Encodable
{
    private ASN1Sequence pkacSeq;
    private SubjectPublicKeyInfo spki;
    private DERIA5String challenge;
    
    public static PublicKeyAndChallenge getInstance(final Object o) {
        if (o instanceof PublicKeyAndChallenge) {
            return (PublicKeyAndChallenge)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PublicKeyAndChallenge((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unkown object in factory: " + o.getClass().getName());
    }
    
    public PublicKeyAndChallenge(final ASN1Sequence pkacSeq) {
        this.pkacSeq = pkacSeq;
        this.spki = SubjectPublicKeyInfo.getInstance(pkacSeq.getObjectAt(0));
        this.challenge = DERIA5String.getInstance(pkacSeq.getObjectAt(1));
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.pkacSeq;
    }
    
    public SubjectPublicKeyInfo getSubjectPublicKeyInfo() {
        return this.spki;
    }
    
    public DERIA5String getChallenge() {
        return this.challenge;
    }
}
