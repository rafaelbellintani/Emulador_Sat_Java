// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.jce.spec.GOST3410PublicKeySpec;
import org.bouncycastle.jce.spec.GOST3410PrivateKeySpec;
import org.bouncycastle.jce.spec.ElGamalPublicKeySpec;
import org.bouncycastle.jce.spec.ElGamalPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.DSAPrivateKeySpec;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.io.IOException;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import java.security.InvalidKeyException;
import org.bouncycastle.jce.interfaces.ElGamalPrivateKey;
import org.bouncycastle.jce.interfaces.ElGamalPublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHPublicKeySpec;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.spec.DHPrivateKeySpec;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.Key;
import java.security.spec.X509EncodedKeySpec;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.KeyFactorySpi;

public abstract class JDKKeyFactory extends KeyFactorySpi
{
    protected boolean elGamalFactory;
    
    public JDKKeyFactory() {
        this.elGamalFactory = false;
    }
    
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                return createPrivateKeyFromDERStream(((PKCS8EncodedKeySpec)keySpec).getEncoded());
            }
            catch (Exception ex) {
                throw new InvalidKeySpecException(ex.toString());
            }
        }
        throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
    }
    
    @Override
    protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof X509EncodedKeySpec) {
            try {
                return createPublicKeyFromDERStream(((X509EncodedKeySpec)keySpec).getEncoded());
            }
            catch (Exception ex) {
                throw new InvalidKeySpecException(ex.toString());
            }
        }
        throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final Key obj, final Class obj2) throws InvalidKeySpecException {
        if (obj2.isAssignableFrom(PKCS8EncodedKeySpec.class) && obj.getFormat().equals("PKCS#8")) {
            return new PKCS8EncodedKeySpec(obj.getEncoded());
        }
        if (obj2.isAssignableFrom(X509EncodedKeySpec.class) && obj.getFormat().equals("X.509")) {
            return new X509EncodedKeySpec(obj.getEncoded());
        }
        if (obj2.isAssignableFrom(RSAPublicKeySpec.class) && obj instanceof RSAPublicKey) {
            final RSAPublicKey rsaPublicKey = (RSAPublicKey)obj;
            return new RSAPublicKeySpec(rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent());
        }
        if (obj2.isAssignableFrom(RSAPrivateKeySpec.class) && obj instanceof RSAPrivateKey) {
            final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)obj;
            return new RSAPrivateKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());
        }
        if (obj2.isAssignableFrom(RSAPrivateCrtKeySpec.class) && obj instanceof RSAPrivateCrtKey) {
            final RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey)obj;
            return new RSAPrivateCrtKeySpec(rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent(), rsaPrivateCrtKey.getPrivateExponent(), rsaPrivateCrtKey.getPrimeP(), rsaPrivateCrtKey.getPrimeQ(), rsaPrivateCrtKey.getPrimeExponentP(), rsaPrivateCrtKey.getPrimeExponentQ(), rsaPrivateCrtKey.getCrtCoefficient());
        }
        if (obj2.isAssignableFrom(DHPrivateKeySpec.class) && obj instanceof DHPrivateKey) {
            final DHPrivateKey dhPrivateKey = (DHPrivateKey)obj;
            return new DHPrivateKeySpec(dhPrivateKey.getX(), dhPrivateKey.getParams().getP(), dhPrivateKey.getParams().getG());
        }
        if (obj2.isAssignableFrom(DHPublicKeySpec.class) && obj instanceof DHPublicKey) {
            final DHPublicKey dhPublicKey = (DHPublicKey)obj;
            return new DHPublicKeySpec(dhPublicKey.getY(), dhPublicKey.getParams().getP(), dhPublicKey.getParams().getG());
        }
        throw new RuntimeException("not implemented yet " + obj + " " + obj2);
    }
    
    @Override
    protected Key engineTranslateKey(final Key key) throws InvalidKeyException {
        if (key instanceof RSAPublicKey) {
            return new JCERSAPublicKey((RSAPublicKey)key);
        }
        if (key instanceof RSAPrivateCrtKey) {
            return new JCERSAPrivateCrtKey((RSAPrivateCrtKey)key);
        }
        if (key instanceof RSAPrivateKey) {
            return new JCERSAPrivateKey((RSAPrivateKey)key);
        }
        if (key instanceof DHPublicKey) {
            if (this.elGamalFactory) {
                return new JCEElGamalPublicKey((DHPublicKey)key);
            }
            return new JCEDHPublicKey((DHPublicKey)key);
        }
        else if (key instanceof DHPrivateKey) {
            if (this.elGamalFactory) {
                return new JCEElGamalPrivateKey((DHPrivateKey)key);
            }
            return new JCEDHPrivateKey((DHPrivateKey)key);
        }
        else {
            if (key instanceof DSAPublicKey) {
                return new JDKDSAPublicKey((DSAPublicKey)key);
            }
            if (key instanceof DSAPrivateKey) {
                return new JDKDSAPrivateKey((DSAPrivateKey)key);
            }
            if (key instanceof ElGamalPublicKey) {
                return new JCEElGamalPublicKey((ElGamalPublicKey)key);
            }
            if (key instanceof ElGamalPrivateKey) {
                return new JCEElGamalPrivateKey((ElGamalPrivateKey)key);
            }
            throw new InvalidKeyException("key type unknown");
        }
    }
    
    public static PublicKey createPublicKeyFromDERStream(final byte[] array) throws IOException {
        return createPublicKeyFromPublicKeyInfo(new SubjectPublicKeyInfo((ASN1Sequence)ASN1Object.fromByteArray(array)));
    }
    
    static PublicKey createPublicKeyFromPublicKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final DERObjectIdentifier objectId = subjectPublicKeyInfo.getAlgorithmId().getObjectId();
        if (RSAUtil.isRsaOid(objectId)) {
            return new JCERSAPublicKey(subjectPublicKeyInfo);
        }
        if (objectId.equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            return new JCEDHPublicKey(subjectPublicKeyInfo);
        }
        if (objectId.equals(X9ObjectIdentifiers.dhpublicnumber)) {
            return new JCEDHPublicKey(subjectPublicKeyInfo);
        }
        if (objectId.equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            return new JCEElGamalPublicKey(subjectPublicKeyInfo);
        }
        if (objectId.equals(X9ObjectIdentifiers.id_dsa)) {
            return new JDKDSAPublicKey(subjectPublicKeyInfo);
        }
        if (objectId.equals(OIWObjectIdentifiers.dsaWithSHA1)) {
            return new JDKDSAPublicKey(subjectPublicKeyInfo);
        }
        if (objectId.equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            return new JCEECPublicKey(subjectPublicKeyInfo);
        }
        if (objectId.equals(CryptoProObjectIdentifiers.gostR3410_94)) {
            return new JDKGOST3410PublicKey(subjectPublicKeyInfo);
        }
        if (objectId.equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
            return new JCEECPublicKey(subjectPublicKeyInfo);
        }
        throw new RuntimeException("algorithm identifier " + objectId + " in key not recognised");
    }
    
    protected static PrivateKey createPrivateKeyFromDERStream(final byte[] array) throws IOException {
        return createPrivateKeyFromPrivateKeyInfo(new PrivateKeyInfo((ASN1Sequence)ASN1Object.fromByteArray(array)));
    }
    
    static PrivateKey createPrivateKeyFromPrivateKeyInfo(final PrivateKeyInfo privateKeyInfo) {
        final DERObjectIdentifier objectId = privateKeyInfo.getAlgorithmId().getObjectId();
        if (RSAUtil.isRsaOid(objectId)) {
            return new JCERSAPrivateCrtKey(privateKeyInfo);
        }
        if (objectId.equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            return new JCEDHPrivateKey(privateKeyInfo);
        }
        if (objectId.equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            return new JCEElGamalPrivateKey(privateKeyInfo);
        }
        if (objectId.equals(X9ObjectIdentifiers.id_dsa)) {
            return new JDKDSAPrivateKey(privateKeyInfo);
        }
        if (objectId.equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            return new JCEECPrivateKey(privateKeyInfo);
        }
        if (objectId.equals(CryptoProObjectIdentifiers.gostR3410_94)) {
            return new JDKGOST3410PrivateKey(privateKeyInfo);
        }
        if (objectId.equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
            return new JCEECPrivateKey(privateKeyInfo);
        }
        throw new RuntimeException("algorithm identifier " + objectId + " in key not recognised");
    }
    
    public static class DH extends JDKKeyFactory
    {
        @Override
        protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof DHPrivateKeySpec) {
                return new JCEDHPrivateKey((DHPrivateKeySpec)keySpec);
            }
            return super.engineGeneratePrivate(keySpec);
        }
        
        @Override
        protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof DHPublicKeySpec) {
                return new JCEDHPublicKey((DHPublicKeySpec)keySpec);
            }
            return super.engineGeneratePublic(keySpec);
        }
    }
    
    public static class DSA extends JDKKeyFactory
    {
        @Override
        protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof DSAPrivateKeySpec) {
                return new JDKDSAPrivateKey((DSAPrivateKeySpec)keySpec);
            }
            return super.engineGeneratePrivate(keySpec);
        }
        
        @Override
        protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof DSAPublicKeySpec) {
                return new JDKDSAPublicKey((DSAPublicKeySpec)keySpec);
            }
            return super.engineGeneratePublic(keySpec);
        }
    }
    
    public static class ElGamal extends JDKKeyFactory
    {
        public ElGamal() {
            this.elGamalFactory = true;
        }
        
        @Override
        protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof ElGamalPrivateKeySpec) {
                return new JCEElGamalPrivateKey((ElGamalPrivateKeySpec)keySpec);
            }
            if (keySpec instanceof DHPrivateKeySpec) {
                return new JCEElGamalPrivateKey((DHPrivateKeySpec)keySpec);
            }
            return super.engineGeneratePrivate(keySpec);
        }
        
        @Override
        protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof ElGamalPublicKeySpec) {
                return new JCEElGamalPublicKey((ElGamalPublicKeySpec)keySpec);
            }
            if (keySpec instanceof DHPublicKeySpec) {
                return new JCEElGamalPublicKey((DHPublicKeySpec)keySpec);
            }
            return super.engineGeneratePublic(keySpec);
        }
    }
    
    public static class GOST3410 extends JDKKeyFactory
    {
        @Override
        protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof GOST3410PrivateKeySpec) {
                return new JDKGOST3410PrivateKey((GOST3410PrivateKeySpec)keySpec);
            }
            return super.engineGeneratePrivate(keySpec);
        }
        
        @Override
        protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof GOST3410PublicKeySpec) {
                return new JDKGOST3410PublicKey((GOST3410PublicKeySpec)keySpec);
            }
            return super.engineGeneratePublic(keySpec);
        }
    }
    
    public static class RSA extends JDKKeyFactory
    {
        @Override
        protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof PKCS8EncodedKeySpec) {
                try {
                    return JDKKeyFactory.createPrivateKeyFromDERStream(((PKCS8EncodedKeySpec)keySpec).getEncoded());
                }
                catch (Exception ex2) {
                    try {
                        return new JCERSAPrivateCrtKey(new RSAPrivateKeyStructure((ASN1Sequence)ASN1Object.fromByteArray(((PKCS8EncodedKeySpec)keySpec).getEncoded())));
                    }
                    catch (Exception ex) {
                        throw new InvalidKeySpecException(ex.toString());
                    }
                }
            }
            if (keySpec instanceof RSAPrivateCrtKeySpec) {
                return new JCERSAPrivateCrtKey((RSAPrivateCrtKeySpec)keySpec);
            }
            if (keySpec instanceof RSAPrivateKeySpec) {
                return new JCERSAPrivateKey((RSAPrivateKeySpec)keySpec);
            }
            throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
        }
        
        @Override
        protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
            if (keySpec instanceof RSAPublicKeySpec) {
                return new JCERSAPublicKey((RSAPublicKeySpec)keySpec);
            }
            return super.engineGeneratePublic(keySpec);
        }
    }
    
    public static class X509 extends JDKKeyFactory
    {
    }
}
