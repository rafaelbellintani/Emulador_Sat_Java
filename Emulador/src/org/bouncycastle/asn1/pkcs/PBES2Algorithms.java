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
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class PBES2Algorithms extends AlgorithmIdentifier implements PKCSObjectIdentifiers
{
    private DERObjectIdentifier objectId;
    private KeyDerivationFunc func;
    private EncryptionScheme scheme;
    
    public PBES2Algorithms(final ASN1Sequence asn1Sequence) {
        super(asn1Sequence);
        final Enumeration objects = asn1Sequence.getObjects();
        this.objectId = objects.nextElement();
        final Enumeration objects2 = ((ASN1Sequence)objects.nextElement()).getObjects();
        final ASN1Sequence asn1Sequence2 = objects2.nextElement();
        if (asn1Sequence2.getObjectAt(0).equals(PBES2Algorithms.id_PBKDF2)) {
            this.func = new KeyDerivationFunc(PBES2Algorithms.id_PBKDF2, PBKDF2Params.getInstance(asn1Sequence2.getObjectAt(1)));
        }
        else {
            this.func = new KeyDerivationFunc(asn1Sequence2);
        }
        this.scheme = new EncryptionScheme(objects2.nextElement());
    }
    
    @Override
    public DERObjectIdentifier getObjectId() {
        return this.objectId;
    }
    
    public KeyDerivationFunc getKeyDerivationFunc() {
        return this.func;
    }
    
    public EncryptionScheme getEncryptionScheme() {
        return this.scheme;
    }
    
    @Override
    public DERObject getDERObject() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        asn1EncodableVector.add(this.objectId);
        asn1EncodableVector2.add(this.func);
        asn1EncodableVector2.add(this.scheme);
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        return new DERSequence(asn1EncodableVector);
    }
}
