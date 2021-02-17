// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.mozilla;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.security.PublicKey;
import org.bouncycastle.asn1.DEREncodable;
import java.security.Signature;
import java.security.InvalidKeyException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.asn1.DERObject;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1InputStream;
import java.io.ByteArrayInputStream;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.mozilla.PublicKeyAndChallenge;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Encodable;

public class SignedPublicKeyAndChallenge extends ASN1Encodable
{
    private ASN1Sequence spkacSeq;
    private PublicKeyAndChallenge pkac;
    private AlgorithmIdentifier signatureAlgorithm;
    private DERBitString signature;
    
    private static ASN1Sequence toDERSequence(final byte[] buf) {
        try {
            return (ASN1Sequence)new ASN1InputStream(new ByteArrayInputStream(buf)).readObject();
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("badly encoded request");
        }
    }
    
    public SignedPublicKeyAndChallenge(final byte[] array) {
        this.spkacSeq = toDERSequence(array);
        this.pkac = PublicKeyAndChallenge.getInstance(this.spkacSeq.getObjectAt(0));
        this.signatureAlgorithm = AlgorithmIdentifier.getInstance(this.spkacSeq.getObjectAt(1));
        this.signature = (DERBitString)this.spkacSeq.getObjectAt(2);
    }
    
    @Override
    public DERObject toASN1Object() {
        return this.spkacSeq;
    }
    
    public PublicKeyAndChallenge getPublicKeyAndChallenge() {
        return this.pkac;
    }
    
    public boolean verify() throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        return this.verify(null);
    }
    
    public boolean verify(final String provider) throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        Signature signature;
        if (provider == null) {
            signature = Signature.getInstance(this.signatureAlgorithm.getObjectId().getId());
        }
        else {
            signature = Signature.getInstance(this.signatureAlgorithm.getObjectId().getId(), provider);
        }
        signature.initVerify(this.getPublicKey(provider));
        signature.update(new DERBitString(this.pkac).getBytes());
        return signature.verify(this.signature.getBytes());
    }
    
    public PublicKey getPublicKey(final String provider) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        final SubjectPublicKeyInfo subjectPublicKeyInfo = this.pkac.getSubjectPublicKeyInfo();
        try {
            return KeyFactory.getInstance(subjectPublicKeyInfo.getAlgorithmId().getObjectId().getId(), provider).generatePublic(new X509EncodedKeySpec(new DERBitString(subjectPublicKeyInfo).getBytes()));
        }
        catch (InvalidKeySpecException ex) {
            throw new InvalidKeyException("error encoding public key");
        }
    }
}
