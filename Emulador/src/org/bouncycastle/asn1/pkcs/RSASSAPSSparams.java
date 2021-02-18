// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.ASN1Encodable;

public class RSASSAPSSparams extends ASN1Encodable
{
    private AlgorithmIdentifier hashAlgorithm;
    private AlgorithmIdentifier maskGenAlgorithm;
    private DERInteger saltLength;
    private DERInteger trailerField;
    public static final AlgorithmIdentifier DEFAULT_HASH_ALGORITHM;
    public static final AlgorithmIdentifier DEFAULT_MASK_GEN_FUNCTION;
    public static final DERInteger DEFAULT_SALT_LENGTH;
    public static final DERInteger DEFAULT_TRAILER_FIELD;
    
    public static RSASSAPSSparams getInstance(final Object o) {
        if (o == null || o instanceof RSASSAPSSparams) {
            return (RSASSAPSSparams)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RSASSAPSSparams((ASN1Sequence)o);
        }
        throw new IllegalArgumentException("unknown object in factory: " + o.getClass().getName());
    }
    
    public RSASSAPSSparams() {
        this.hashAlgorithm = RSASSAPSSparams.DEFAULT_HASH_ALGORITHM;
        this.maskGenAlgorithm = RSASSAPSSparams.DEFAULT_MASK_GEN_FUNCTION;
        this.saltLength = RSASSAPSSparams.DEFAULT_SALT_LENGTH;
        this.trailerField = RSASSAPSSparams.DEFAULT_TRAILER_FIELD;
    }
    
    public RSASSAPSSparams(final AlgorithmIdentifier hashAlgorithm, final AlgorithmIdentifier maskGenAlgorithm, final DERInteger saltLength, final DERInteger trailerField) {
        this.hashAlgorithm = hashAlgorithm;
        this.maskGenAlgorithm = maskGenAlgorithm;
        this.saltLength = saltLength;
        this.trailerField = trailerField;
    }
    
    public RSASSAPSSparams(final ASN1Sequence asn1Sequence) {
        this.hashAlgorithm = RSASSAPSSparams.DEFAULT_HASH_ALGORITHM;
        this.maskGenAlgorithm = RSASSAPSSparams.DEFAULT_MASK_GEN_FUNCTION;
        this.saltLength = RSASSAPSSparams.DEFAULT_SALT_LENGTH;
        this.trailerField = RSASSAPSSparams.DEFAULT_TRAILER_FIELD;
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(i);
            switch (asn1TaggedObject.getTagNo()) {
                case 0: {
                    this.hashAlgorithm = AlgorithmIdentifier.getInstance(asn1TaggedObject, true);
                    break;
                }
                case 1: {
                    this.maskGenAlgorithm = AlgorithmIdentifier.getInstance(asn1TaggedObject, true);
                    break;
                }
                case 2: {
                    this.saltLength = DERInteger.getInstance(asn1TaggedObject, true);
                    break;
                }
                case 3: {
                    this.trailerField = DERInteger.getInstance(asn1TaggedObject, true);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("unknown tag");
                }
            }
        }
    }
    
    public AlgorithmIdentifier getHashAlgorithm() {
        return this.hashAlgorithm;
    }
    
    public AlgorithmIdentifier getMaskGenAlgorithm() {
        return this.maskGenAlgorithm;
    }
    
    public DERInteger getSaltLength() {
        return this.saltLength;
    }
    
    public DERInteger getTrailerField() {
        return this.trailerField;
    }
    
    @Override
    public DERObject toASN1Object() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (!this.hashAlgorithm.equals(RSASSAPSSparams.DEFAULT_HASH_ALGORITHM)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.hashAlgorithm));
        }
        if (!this.maskGenAlgorithm.equals(RSASSAPSSparams.DEFAULT_MASK_GEN_FUNCTION)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.maskGenAlgorithm));
        }
        if (!this.saltLength.equals(RSASSAPSSparams.DEFAULT_SALT_LENGTH)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.saltLength));
        }
        if (!this.trailerField.equals(RSASSAPSSparams.DEFAULT_TRAILER_FIELD)) {
            asn1EncodableVector.add(new DERTaggedObject(true, 3, this.trailerField));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    static {
        DEFAULT_HASH_ALGORITHM = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, new DERNull());
        DEFAULT_MASK_GEN_FUNCTION = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, RSASSAPSSparams.DEFAULT_HASH_ALGORITHM);
        DEFAULT_SALT_LENGTH = new DERInteger(20);
        DEFAULT_TRAILER_FIELD = new DERInteger(1);
    }
}
