// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.asymmetric.ec;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.jce.provider.JCEECPrivateKey;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import java.security.KeyPair;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.jce.provider.ProviderUtil;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.teletrust.TeleTrusTNamedCurves;
import org.bouncycastle.asn1.nist.NISTNamedCurves;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X962NamedCurves;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.asn1.cryptopro.ECGOST3410NamedCurves;
import java.math.BigInteger;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.spec.ECParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.util.Hashtable;
import java.security.SecureRandom;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.jce.provider.JDKKeyPairGenerator;

public abstract class KeyPairGenerator extends JDKKeyPairGenerator
{
    public KeyPairGenerator(final String s) {
        super(s);
    }
    
    public static class EC extends KeyPairGenerator
    {
        ECKeyGenerationParameters param;
        ECKeyPairGenerator engine;
        Object ecParams;
        int strength;
        int certainty;
        SecureRandom random;
        boolean initialised;
        String algorithm;
        private static Hashtable ecParameters;
        
        public EC() {
            super("EC");
            this.engine = new ECKeyPairGenerator();
            this.ecParams = null;
            this.strength = 239;
            this.certainty = 50;
            this.random = new SecureRandom();
            this.initialised = false;
            this.algorithm = "EC";
        }
        
        public EC(final String algorithm) {
            super(algorithm);
            this.engine = new ECKeyPairGenerator();
            this.ecParams = null;
            this.strength = 239;
            this.certainty = 50;
            this.random = new SecureRandom();
            this.initialised = false;
            this.algorithm = algorithm;
        }
        
        @Override
        public void initialize(final int n, final SecureRandom random) {
            this.strength = n;
            this.random = random;
            this.ecParams = EC.ecParameters.get(new Integer(n));
            if (this.ecParams != null) {
                try {
                    this.initialize((AlgorithmParameterSpec)this.ecParams, random);
                    return;
                }
                catch (InvalidAlgorithmParameterException ex) {
                    throw new InvalidParameterException("key size not configurable.");
                }
                throw new InvalidParameterException("unknown key size.");
            }
            throw new InvalidParameterException("unknown key size.");
        }
        
        @Override
        public void initialize(final AlgorithmParameterSpec ecParams, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (ecParams instanceof ECParameterSpec) {
                final ECParameterSpec ecParameterSpec = (ECParameterSpec)ecParams;
                this.ecParams = ecParams;
                this.param = new ECKeyGenerationParameters(new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN()), secureRandom);
                this.engine.init(this.param);
                this.initialised = true;
            }
            else if (ecParams instanceof java.security.spec.ECParameterSpec) {
                final java.security.spec.ECParameterSpec ecParameterSpec2 = (java.security.spec.ECParameterSpec)ecParams;
                this.ecParams = ecParams;
                final ECCurve convertCurve = EC5Util.convertCurve(ecParameterSpec2.getCurve());
                this.param = new ECKeyGenerationParameters(new ECDomainParameters(convertCurve, EC5Util.convertPoint(convertCurve, ecParameterSpec2.getGenerator(), false), ecParameterSpec2.getOrder(), BigInteger.valueOf(ecParameterSpec2.getCofactor())), secureRandom);
                this.engine.init(this.param);
                this.initialised = true;
            }
            else if (ecParams instanceof ECGenParameterSpec) {
                final String name = ((ECGenParameterSpec)ecParams).getName();
                if (this.algorithm.equals("ECGOST3410")) {
                    final ECDomainParameters byName = ECGOST3410NamedCurves.getByName(name);
                    if (byName == null) {
                        throw new InvalidAlgorithmParameterException("unknown curve name: " + name);
                    }
                    this.ecParams = new ECNamedCurveSpec(name, byName.getCurve(), byName.getG(), byName.getN(), byName.getH(), byName.getSeed());
                }
                else {
                    X9ECParameters x9ECParameters = X962NamedCurves.getByName(name);
                    if (x9ECParameters == null) {
                        x9ECParameters = SECNamedCurves.getByName(name);
                        if (x9ECParameters == null) {
                            x9ECParameters = NISTNamedCurves.getByName(name);
                        }
                        if (x9ECParameters == null) {
                            x9ECParameters = TeleTrusTNamedCurves.getByName(name);
                        }
                        if (x9ECParameters == null) {
                            try {
                                final DERObjectIdentifier derObjectIdentifier = new DERObjectIdentifier(name);
                                x9ECParameters = X962NamedCurves.getByOID(derObjectIdentifier);
                                if (x9ECParameters == null) {
                                    x9ECParameters = SECNamedCurves.getByOID(derObjectIdentifier);
                                }
                                if (x9ECParameters == null) {
                                    x9ECParameters = NISTNamedCurves.getByOID(derObjectIdentifier);
                                }
                                if (x9ECParameters == null) {
                                    x9ECParameters = TeleTrusTNamedCurves.getByOID(derObjectIdentifier);
                                }
                                if (x9ECParameters == null) {
                                    throw new InvalidAlgorithmParameterException("unknown curve OID: " + name);
                                }
                            }
                            catch (IllegalArgumentException ex) {
                                throw new InvalidAlgorithmParameterException("unknown curve name: " + name);
                            }
                        }
                    }
                    this.ecParams = new ECNamedCurveSpec(name, x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), null);
                }
                final java.security.spec.ECParameterSpec ecParameterSpec3 = (java.security.spec.ECParameterSpec)this.ecParams;
                final ECCurve convertCurve2 = EC5Util.convertCurve(ecParameterSpec3.getCurve());
                this.param = new ECKeyGenerationParameters(new ECDomainParameters(convertCurve2, EC5Util.convertPoint(convertCurve2, ecParameterSpec3.getGenerator(), false), ecParameterSpec3.getOrder(), BigInteger.valueOf(ecParameterSpec3.getCofactor())), secureRandom);
                this.engine.init(this.param);
                this.initialised = true;
            }
            else if (ecParams == null && ProviderUtil.getEcImplicitlyCa() != null) {
                final ECParameterSpec ecImplicitlyCa = ProviderUtil.getEcImplicitlyCa();
                this.ecParams = ecParams;
                this.param = new ECKeyGenerationParameters(new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN()), secureRandom);
                this.engine.init(this.param);
                this.initialised = true;
            }
            else {
                if (ecParams == null && ProviderUtil.getEcImplicitlyCa() == null) {
                    throw new InvalidAlgorithmParameterException("null parameter passed but no implicitCA set");
                }
                throw new InvalidAlgorithmParameterException("parameter object not a ECParameterSpec");
            }
        }
        
        @Override
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                throw new IllegalStateException("EC Key Pair Generator not initialised");
            }
            final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
            final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)generateKeyPair.getPublic();
            final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)generateKeyPair.getPrivate();
            if (this.ecParams instanceof ECParameterSpec) {
                final ECParameterSpec ecParameterSpec = (ECParameterSpec)this.ecParams;
                final JCEECPublicKey publicKey = new JCEECPublicKey(this.algorithm, ecPublicKeyParameters, ecParameterSpec);
                return new KeyPair(publicKey, new JCEECPrivateKey(this.algorithm, ecPrivateKeyParameters, publicKey, ecParameterSpec));
            }
            if (this.ecParams == null) {
                return new KeyPair(new JCEECPublicKey(this.algorithm, ecPublicKeyParameters), new JCEECPrivateKey(this.algorithm, ecPrivateKeyParameters));
            }
            final java.security.spec.ECParameterSpec ecParameterSpec2 = (java.security.spec.ECParameterSpec)this.ecParams;
            final JCEECPublicKey publicKey2 = new JCEECPublicKey(this.algorithm, ecPublicKeyParameters, ecParameterSpec2);
            return new KeyPair(publicKey2, new JCEECPrivateKey(this.algorithm, ecPrivateKeyParameters, publicKey2, ecParameterSpec2));
        }
        
        static {
            (EC.ecParameters = new Hashtable()).put(new Integer(192), new ECGenParameterSpec("prime192v1"));
            EC.ecParameters.put(new Integer(239), new ECGenParameterSpec("prime239v1"));
            EC.ecParameters.put(new Integer(256), new ECGenParameterSpec("prime256v1"));
            EC.ecParameters.put(new Integer(224), new ECGenParameterSpec("P-224"));
            EC.ecParameters.put(new Integer(384), new ECGenParameterSpec("P-384"));
            EC.ecParameters.put(new Integer(521), new ECGenParameterSpec("P-521"));
        }
    }
    
    public static class ECDH extends EC
    {
        public ECDH() {
            super("ECDH");
        }
    }
    
    public static class ECDHC extends EC
    {
        public ECDHC() {
            super("ECDHC");
        }
    }
    
    public static class ECDSA extends EC
    {
        public ECDSA() {
            super("ECDSA");
        }
    }
    
    public static class ECGOST3410 extends EC
    {
        public ECGOST3410() {
            super("ECGOST3410");
        }
    }
    
    public static class ECMQV extends EC
    {
        public ECMQV() {
            super("ECMQV");
        }
    }
}
