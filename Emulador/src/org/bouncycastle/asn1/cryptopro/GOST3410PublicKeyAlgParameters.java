// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.cryptopro;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class GOST3410PublicKeyAlgParameters extends ASN1Encodable
{
    private DERObjectIdentifier publicKeyParamSet;
    private DERObjectIdentifier digestParamSet;
    private DERObjectIdentifier encryptionParamSet;
    
    public static GOST3410PublicKeyAlgParameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public static GOST3410PublicKeyAlgParameters getInstance(final Object o) {
        if (o == null || o instanceof GOST3410PublicKeyAlgParameters) {
            return (GOST3410PublicKeyAlgParameters)o;
        }
        if (o instanceof ASN1Sequence) {
            return new GOST3410PublicKeyAlgParameters((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("Invalid GOST3410Parameter: " + o.getClass().getName());
    }
    
    public GOST3410PublicKeyAlgParameters(final DERObjectIdentifier publicKeyParamSet, final DERObjectIdentifier digestParamSet) {
        this.publicKeyParamSet = publicKeyParamSet;
        this.digestParamSet = digestParamSet;
        this.encryptionParamSet = null;
    }
    
    public GOST3410PublicKeyAlgParameters(final DERObjectIdentifier publicKeyParamSet, final DERObjectIdentifier digestParamSet, final DERObjectIdentifier encryptionParamSet) {
        this.publicKeyParamSet = publicKeyParamSet;
        this.digestParamSet = digestParamSet;
        this.encryptionParamSet = encryptionParamSet;
    }
    
    public GOST3410PublicKeyAlgParameters(final ASN1Sequence asn1Sequence) {
        this.publicKeyParamSet = (DERObjectIdentifier)asn1Sequence.getObjectAt(0);
        this.digestParamSet = (DERObjectIdentifier)asn1Sequence.getObjectAt(1);
        if (asn1Sequence.size() > 2) {
            this.encryptionParamSet = (DERObjectIdentifier)asn1Sequence.getObjectAt(2);
        }
    }
    
    public DERObjectIdentifier getPublicKeyParamSet() {
        return this.publicKeyParamSet;
    }
    
    public DERObjectIdentifier getDigestParamSet() {
        return this.digestParamSet;
    }
    
    public DERObjectIdentifier getEncryptionParamSet() {
        return this.encryptionParamSet;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.publicKeyParamSet);
        asn1EncodableVector.add(this.digestParamSet);
        if (this.encryptionParamSet != null) {
            asn1EncodableVector.add(this.encryptionParamSet);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
