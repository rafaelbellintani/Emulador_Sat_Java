// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.asymmetric.ec;

import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import org.bouncycastle.jce.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.provider.ProviderUtil;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.InvalidKeyException;
import org.bouncycastle.jce.provider.JCEECPrivateKey;
import java.security.interfaces.ECPrivateKey;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.Key;
import org.bouncycastle.jce.provider.JDKKeyFactory;

public class KeyFactory extends JDKKeyFactory
{
    String algorithm;
    
    KeyFactory(final String algorithm) {
        this.algorithm = algorithm;
    }
    
    @Override
    protected Key engineTranslateKey(final Key key) throws InvalidKeyException {
        if (key instanceof ECPublicKey) {
            return new JCEECPublicKey((ECPublicKey)key);
        }
        if (key instanceof ECPrivateKey) {
            return new JCEECPrivateKey((ECPrivateKey)key);
        }
        throw new InvalidKeyException("key type unknown");
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final Key obj, final Class obj2) throws InvalidKeySpecException {
        if (obj2.isAssignableFrom(PKCS8EncodedKeySpec.class) && obj.getFormat().equals("PKCS#8")) {
            return new PKCS8EncodedKeySpec(obj.getEncoded());
        }
        if (obj2.isAssignableFrom(X509EncodedKeySpec.class) && obj.getFormat().equals("X.509")) {
            return new X509EncodedKeySpec(obj.getEncoded());
        }
        if (obj2.isAssignableFrom(ECPublicKeySpec.class) && obj instanceof ECPublicKey) {
            final ECPublicKey ecPublicKey = (ECPublicKey)obj;
            if (ecPublicKey.getParams() != null) {
                return new ECPublicKeySpec(ecPublicKey.getW(), ecPublicKey.getParams());
            }
            final ECParameterSpec ecImplicitlyCa = ProviderUtil.getEcImplicitlyCa();
            return new ECPublicKeySpec(ecPublicKey.getW(), EC5Util.convertSpec(EC5Util.convertCurve(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getSeed()), ecImplicitlyCa));
        }
        else {
            if (!obj2.isAssignableFrom(ECPrivateKeySpec.class) || !(obj instanceof ECPrivateKey)) {
                throw new RuntimeException("not implemented yet " + obj + " " + obj2);
            }
            final ECPrivateKey ecPrivateKey = (ECPrivateKey)obj;
            if (ecPrivateKey.getParams() != null) {
                return new ECPrivateKeySpec(ecPrivateKey.getS(), ecPrivateKey.getParams());
            }
            final ECParameterSpec ecImplicitlyCa2 = ProviderUtil.getEcImplicitlyCa();
            return new ECPrivateKeySpec(ecPrivateKey.getS(), EC5Util.convertSpec(EC5Util.convertCurve(ecImplicitlyCa2.getCurve(), ecImplicitlyCa2.getSeed()), ecImplicitlyCa2));
        }
    }
    
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                return new JCEECPrivateKey(this.algorithm, (JCEECPrivateKey)JDKKeyFactory.createPrivateKeyFromDERStream(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
            }
            catch (Exception ex) {
                throw new InvalidKeySpecException(ex.toString());
            }
        }
        if (keySpec instanceof org.bouncycastle.jce.spec.ECPrivateKeySpec) {
            return new JCEECPrivateKey(this.algorithm, (org.bouncycastle.jce.spec.ECPrivateKeySpec)keySpec);
        }
        if (keySpec instanceof ECPrivateKeySpec) {
            return new JCEECPrivateKey(this.algorithm, (ECPrivateKeySpec)keySpec);
        }
        throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
    }
    
    @Override
    protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof X509EncodedKeySpec) {
            try {
                return new JCEECPublicKey(this.algorithm, (JCEECPublicKey)JDKKeyFactory.createPublicKeyFromDERStream(((X509EncodedKeySpec)keySpec).getEncoded()));
            }
            catch (Exception ex) {
                throw new InvalidKeySpecException(ex.toString());
            }
        }
        if (keySpec instanceof org.bouncycastle.jce.spec.ECPublicKeySpec) {
            return new JCEECPublicKey(this.algorithm, (org.bouncycastle.jce.spec.ECPublicKeySpec)keySpec);
        }
        if (keySpec instanceof ECPublicKeySpec) {
            return new JCEECPublicKey(this.algorithm, (ECPublicKeySpec)keySpec);
        }
        throw new InvalidKeySpecException("Unknown KeySpec type: " + keySpec.getClass().getName());
    }
    
    public static class EC extends KeyFactory
    {
        public EC() {
            super("EC");
        }
    }
    
    public static class ECDH extends KeyFactory
    {
        public ECDH() {
            super("ECDH");
        }
    }
    
    public static class ECDHC extends KeyFactory
    {
        public ECDHC() {
            super("ECDHC");
        }
    }
    
    public static class ECDSA extends KeyFactory
    {
        public ECDSA() {
            super("ECDSA");
        }
    }
    
    public static class ECGOST3410 extends KeyFactory
    {
        public ECGOST3410() {
            super("ECGOST3410");
        }
    }
    
    public static class ECMQV extends KeyFactory
    {
        public ECMQV() {
            super("ECMQV");
        }
    }
}
