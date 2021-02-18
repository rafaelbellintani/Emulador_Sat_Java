// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import java.security.spec.RSAKeyGenParameterSpec;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;
import org.bouncycastle.crypto.params.GOST3410Parameters;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.crypto.generators.GOST3410KeyPairGenerator;
import org.bouncycastle.crypto.params.GOST3410KeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.generators.DSAParametersGenerator;
import org.bouncycastle.crypto.params.DSAParameters;
import java.security.spec.DSAParameterSpec;
import java.security.InvalidParameterException;
import org.bouncycastle.crypto.generators.DSAKeyPairGenerator;
import org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.generators.DHParametersGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.DHParameters;
import java.security.InvalidAlgorithmParameterException;
import javax.crypto.spec.DHParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.generators.DHBasicKeyPairGenerator;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import java.util.Hashtable;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.KeyPairGenerator;

public abstract class JDKKeyPairGenerator extends KeyPairGenerator
{
    public JDKKeyPairGenerator(final String algorithm) {
        super(algorithm);
    }
    
    @Override
    public abstract void initialize(final int p0, final SecureRandom p1);
    
    @Override
    public abstract KeyPair generateKeyPair();
    
    public static class DH extends JDKKeyPairGenerator
    {
        private static Hashtable params;
        DHKeyGenerationParameters param;
        DHBasicKeyPairGenerator engine;
        int strength;
        int certainty;
        SecureRandom random;
        boolean initialised;
        
        public DH() {
            super("DH");
            this.engine = new DHBasicKeyPairGenerator();
            this.strength = 1024;
            this.certainty = 20;
            this.random = new SecureRandom();
            this.initialised = false;
        }
        
        @Override
        public void initialize(final int strength, final SecureRandom random) {
            this.strength = strength;
            this.random = random;
        }
        
        @Override
        public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec");
            }
            final DHParameterSpec dhParameterSpec = (DHParameterSpec)algorithmParameterSpec;
            this.param = new DHKeyGenerationParameters(secureRandom, new DHParameters(dhParameterSpec.getP(), dhParameterSpec.getG(), null, dhParameterSpec.getL()));
            this.engine.init(this.param);
            this.initialised = true;
        }
        
        @Override
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                final Integer key = new Integer(this.strength);
                if (DH.params.containsKey(key)) {
                    this.param = (DHKeyGenerationParameters)DH.params.get(key);
                }
                else {
                    final DHParametersGenerator dhParametersGenerator = new DHParametersGenerator();
                    dhParametersGenerator.init(this.strength, this.certainty, this.random);
                    this.param = new DHKeyGenerationParameters(this.random, dhParametersGenerator.generateParameters());
                    DH.params.put(key, this.param);
                }
                this.engine.init(this.param);
                this.initialised = true;
            }
            final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JCEDHPublicKey((DHPublicKeyParameters)generateKeyPair.getPublic()), new JCEDHPrivateKey((DHPrivateKeyParameters)generateKeyPair.getPrivate()));
        }
        
        static {
            DH.params = new Hashtable();
        }
    }
    
    public static class DSA extends JDKKeyPairGenerator
    {
        DSAKeyGenerationParameters param;
        DSAKeyPairGenerator engine;
        int strength;
        int certainty;
        SecureRandom random;
        boolean initialised;
        
        public DSA() {
            super("DSA");
            this.engine = new DSAKeyPairGenerator();
            this.strength = 1024;
            this.certainty = 20;
            this.random = new SecureRandom();
            this.initialised = false;
        }
        
        @Override
        public void initialize(final int strength, final SecureRandom random) {
            if (strength < 512 || strength > 1024 || strength % 64 != 0) {
                throw new InvalidParameterException("strength must be from 512 - 1024 and a multiple of 64");
            }
            this.strength = strength;
            this.random = random;
        }
        
        @Override
        public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof DSAParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a DSAParameterSpec");
            }
            final DSAParameterSpec dsaParameterSpec = (DSAParameterSpec)algorithmParameterSpec;
            this.param = new DSAKeyGenerationParameters(secureRandom, new DSAParameters(dsaParameterSpec.getP(), dsaParameterSpec.getQ(), dsaParameterSpec.getG()));
            this.engine.init(this.param);
            this.initialised = true;
        }
        
        @Override
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                final DSAParametersGenerator dsaParametersGenerator = new DSAParametersGenerator();
                dsaParametersGenerator.init(this.strength, this.certainty, this.random);
                this.param = new DSAKeyGenerationParameters(this.random, dsaParametersGenerator.generateParameters());
                this.engine.init(this.param);
                this.initialised = true;
            }
            final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JDKDSAPublicKey((DSAPublicKeyParameters)generateKeyPair.getPublic()), new JDKDSAPrivateKey((DSAPrivateKeyParameters)generateKeyPair.getPrivate()));
        }
    }
    
    public static class ElGamal extends JDKKeyPairGenerator
    {
        ElGamalKeyGenerationParameters param;
        ElGamalKeyPairGenerator engine;
        int strength;
        int certainty;
        SecureRandom random;
        boolean initialised;
        
        public ElGamal() {
            super("ElGamal");
            this.engine = new ElGamalKeyPairGenerator();
            this.strength = 1024;
            this.certainty = 20;
            this.random = new SecureRandom();
            this.initialised = false;
        }
        
        @Override
        public void initialize(final int strength, final SecureRandom random) {
            this.strength = strength;
            this.random = random;
        }
        
        @Override
        public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof ElGamalParameterSpec) && !(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a DHParameterSpec or an ElGamalParameterSpec");
            }
            if (algorithmParameterSpec instanceof ElGamalParameterSpec) {
                final ElGamalParameterSpec elGamalParameterSpec = (ElGamalParameterSpec)algorithmParameterSpec;
                this.param = new ElGamalKeyGenerationParameters(secureRandom, new ElGamalParameters(elGamalParameterSpec.getP(), elGamalParameterSpec.getG()));
            }
            else {
                final DHParameterSpec dhParameterSpec = (DHParameterSpec)algorithmParameterSpec;
                this.param = new ElGamalKeyGenerationParameters(secureRandom, new ElGamalParameters(dhParameterSpec.getP(), dhParameterSpec.getG(), dhParameterSpec.getL()));
            }
            this.engine.init(this.param);
            this.initialised = true;
        }
        
        @Override
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                final ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
                elGamalParametersGenerator.init(this.strength, this.certainty, this.random);
                this.param = new ElGamalKeyGenerationParameters(this.random, elGamalParametersGenerator.generateParameters());
                this.engine.init(this.param);
                this.initialised = true;
            }
            final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JCEElGamalPublicKey((ElGamalPublicKeyParameters)generateKeyPair.getPublic()), new JCEElGamalPrivateKey((ElGamalPrivateKeyParameters)generateKeyPair.getPrivate()));
        }
    }
    
    public static class GOST3410 extends JDKKeyPairGenerator
    {
        GOST3410KeyGenerationParameters param;
        GOST3410KeyPairGenerator engine;
        GOST3410ParameterSpec gost3410Params;
        int strength;
        SecureRandom random;
        boolean initialised;
        
        public GOST3410() {
            super("GOST3410");
            this.engine = new GOST3410KeyPairGenerator();
            this.strength = 1024;
            this.random = null;
            this.initialised = false;
        }
        
        @Override
        public void initialize(final int strength, final SecureRandom random) {
            this.strength = strength;
            this.random = random;
        }
        
        private void init(final GOST3410ParameterSpec gost3410Params, final SecureRandom secureRandom) {
            final GOST3410PublicKeyParameterSetSpec publicKeyParameters = gost3410Params.getPublicKeyParameters();
            this.param = new GOST3410KeyGenerationParameters(secureRandom, new GOST3410Parameters(publicKeyParameters.getP(), publicKeyParameters.getQ(), publicKeyParameters.getA()));
            this.engine.init(this.param);
            this.initialised = true;
            this.gost3410Params = gost3410Params;
        }
        
        @Override
        public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof GOST3410ParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a GOST3410ParameterSpec");
            }
            this.init((GOST3410ParameterSpec)algorithmParameterSpec, secureRandom);
        }
        
        @Override
        public KeyPair generateKeyPair() {
            if (!this.initialised) {
                this.init(new GOST3410ParameterSpec(CryptoProObjectIdentifiers.gostR3410_94_CryptoPro_A.getId()), new SecureRandom());
            }
            final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JDKGOST3410PublicKey((GOST3410PublicKeyParameters)generateKeyPair.getPublic(), this.gost3410Params), new JDKGOST3410PrivateKey((GOST3410PrivateKeyParameters)generateKeyPair.getPrivate(), this.gost3410Params));
        }
    }
    
    public static class RSA extends JDKKeyPairGenerator
    {
        static final BigInteger defaultPublicExponent;
        static final int defaultTests = 12;
        RSAKeyGenerationParameters param;
        RSAKeyPairGenerator engine;
        
        public RSA() {
            super("RSA");
            this.engine = new RSAKeyPairGenerator();
            this.param = new RSAKeyGenerationParameters(RSA.defaultPublicExponent, new SecureRandom(), 2048, 12);
            this.engine.init(this.param);
        }
        
        @Override
        public void initialize(final int n, final SecureRandom secureRandom) {
            this.param = new RSAKeyGenerationParameters(RSA.defaultPublicExponent, secureRandom, n, 12);
            this.engine.init(this.param);
        }
        
        @Override
        public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (!(algorithmParameterSpec instanceof RSAKeyGenParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a RSAKeyGenParameterSpec");
            }
            final RSAKeyGenParameterSpec rsaKeyGenParameterSpec = (RSAKeyGenParameterSpec)algorithmParameterSpec;
            this.param = new RSAKeyGenerationParameters(rsaKeyGenParameterSpec.getPublicExponent(), secureRandom, rsaKeyGenParameterSpec.getKeysize(), 12);
            this.engine.init(this.param);
        }
        
        @Override
        public KeyPair generateKeyPair() {
            final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
            return new KeyPair(new JCERSAPublicKey((RSAKeyParameters)generateKeyPair.getPublic()), new JCERSAPrivateCrtKey((RSAPrivateCrtKeyParameters)generateKeyPair.getPrivate()));
        }
        
        static {
            defaultPublicExponent = BigInteger.valueOf(65537L);
        }
    }
}
