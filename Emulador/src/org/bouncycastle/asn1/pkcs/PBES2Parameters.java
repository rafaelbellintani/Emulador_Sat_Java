// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class PBES2Parameters extends ASN1Encodable implements PKCSObjectIdentifiers
{
    private KeyDerivationFunc func;
    private EncryptionScheme scheme;
    
    public PBES2Parameters(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        final ASN1Sequence asn1Sequence2 = objects.nextElement();
        if (asn1Sequence2.getObjectAt(0).equals(PBES2Parameters.id_PBKDF2)) {
            this.func = new KeyDerivationFunc(PBES2Parameters.id_PBKDF2, PBKDF2Params.getInstance(asn1Sequence2.getObjectAt(1)));
        }
        else {
            this.func = new KeyDerivationFunc(asn1Sequence2);
        }
        this.scheme = new EncryptionScheme(objects.nextElement());
    }
    
    public KeyDerivationFunc getKeyDerivationFunc() {
        return this.func;
    }
    
    public EncryptionScheme getEncryptionScheme() {
        return this.scheme;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.func);
        asn1EncodableVector.add(this.scheme);
        return new DERSequence(asn1EncodableVector);
    }
}
