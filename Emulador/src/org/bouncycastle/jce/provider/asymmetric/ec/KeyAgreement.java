// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.asymmetric.ec;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.agreement.kdf.ECDHKEKGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.agreement.ECDHCBasicAgreement;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import java.security.PrivateKey;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.crypto.params.MQVPrivateParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jce.interfaces.MQVPrivateKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.agreement.kdf.DHKDFParameters;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import org.bouncycastle.crypto.CipherParameters;
import java.security.PublicKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.crypto.params.MQVPublicParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import java.security.InvalidKeyException;
import org.bouncycastle.jce.interfaces.MQVPublicKey;
import org.bouncycastle.crypto.agreement.ECMQVBasicAgreement;
import java.security.Key;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.params.ECDomainParameters;
import java.math.BigInteger;
import java.util.Hashtable;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import javax.crypto.KeyAgreementSpi;

public class KeyAgreement extends KeyAgreementSpi
{
    private static final X9IntegerConverter converter;
    private static final Hashtable algorithms;
    private String kaAlgorithm;
    private BigInteger result;
    private ECDomainParameters parameters;
    private BasicAgreement agreement;
    private DerivationFunction kdf;
    
    private byte[] bigIntToBytes(final BigInteger bigInteger) {
        return KeyAgreement.converter.integerToBytes(bigInteger, KeyAgreement.converter.getByteLength(this.parameters.getG().getX()));
    }
    
    protected KeyAgreement(final String kaAlgorithm, final BasicAgreement agreement, final DerivationFunction kdf) {
        this.kaAlgorithm = kaAlgorithm;
        this.agreement = agreement;
        this.kdf = kdf;
    }
    
    @Override
    protected Key engineDoPhase(final Key key, final boolean b) throws InvalidKeyException, IllegalStateException {
        if (this.parameters == null) {
            throw new IllegalStateException(this.kaAlgorithm + " not initialised.");
        }
        if (!b) {
            throw new IllegalStateException(this.kaAlgorithm + " can only be between two parties.");
        }
        CipherParameters generatePublicKeyParameter;
        if (this.agreement instanceof ECMQVBasicAgreement) {
            if (!(key instanceof MQVPublicKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(MQVPublicKey.class) + " for doPhase");
            }
            final MQVPublicKey mqvPublicKey = (MQVPublicKey)key;
            generatePublicKeyParameter = new MQVPublicParameters((ECPublicKeyParameters)ECUtil.generatePublicKeyParameter(mqvPublicKey.getStaticKey()), (ECPublicKeyParameters)ECUtil.generatePublicKeyParameter(mqvPublicKey.getEphemeralKey()));
        }
        else {
            if (!(key instanceof ECPublicKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(ECPublicKey.class) + " for doPhase");
            }
            generatePublicKeyParameter = ECUtil.generatePublicKeyParameter((PublicKey)key);
        }
        this.result = this.agreement.calculateAgreement(generatePublicKeyParameter);
        return null;
    }
    
    @Override
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        if (this.kdf != null) {
            throw new UnsupportedOperationException("KDF can only be used when algorithm is known");
        }
        return this.bigIntToBytes(this.result);
    }
    
    @Override
    protected int engineGenerateSecret(final byte[] array, final int n) throws IllegalStateException, ShortBufferException {
        final byte[] engineGenerateSecret = this.engineGenerateSecret();
        if (array.length - n < engineGenerateSecret.length) {
            throw new ShortBufferException(this.kaAlgorithm + " key agreement: need " + engineGenerateSecret.length + " bytes");
        }
        System.arraycopy(engineGenerateSecret, 0, array, n, engineGenerateSecret.length);
        return engineGenerateSecret.length;
    }
    
    @Override
    protected SecretKey engineGenerateSecret(final String s) throws NoSuchAlgorithmException {
        byte[] bigIntToBytes = this.bigIntToBytes(this.result);
        if (this.kdf != null) {
            if (!KeyAgreement.algorithms.containsKey(s)) {
                throw new NoSuchAlgorithmException("unknown algorithm encountered: " + s);
            }
            final int intValue = KeyAgreement.algorithms.get(s);
            final DHKDFParameters dhkdfParameters = new DHKDFParameters(new DERObjectIdentifier(s), intValue, bigIntToBytes);
            final byte[] array = new byte[intValue / 8];
            this.kdf.init(dhkdfParameters);
            this.kdf.generateBytes(array, 0, array.length);
            bigIntToBytes = array;
        }
        return new SecretKeySpec(bigIntToBytes, s);
    }
    
    @Override
    protected void engineInit(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.initFromKey(key);
    }
    
    @Override
    protected void engineInit(final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        this.initFromKey(key);
    }
    
    private void initFromKey(final Key key) throws InvalidKeyException {
        if (this.agreement instanceof ECMQVBasicAgreement) {
            if (!(key instanceof MQVPrivateKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(MQVPrivateKey.class) + " for initialisation");
            }
            final MQVPrivateKey mqvPrivateKey = (MQVPrivateKey)key;
            final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter(mqvPrivateKey.getStaticPrivateKey());
            final ECPrivateKeyParameters ecPrivateKeyParameters2 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter(mqvPrivateKey.getEphemeralPrivateKey());
            ECPublicKeyParameters ecPublicKeyParameters = null;
            if (mqvPrivateKey.getEphemeralPublicKey() != null) {
                ecPublicKeyParameters = (ECPublicKeyParameters)ECUtil.generatePublicKeyParameter(mqvPrivateKey.getEphemeralPublicKey());
            }
            final MQVPrivateParameters mqvPrivateParameters = new MQVPrivateParameters(ecPrivateKeyParameters, ecPrivateKeyParameters2, ecPublicKeyParameters);
            this.parameters = ecPrivateKeyParameters.getParameters();
            this.agreement.init(mqvPrivateParameters);
        }
        else {
            if (!(key instanceof ECPrivateKey)) {
                throw new InvalidKeyException(this.kaAlgorithm + " key agreement requires " + getSimpleName(ECPrivateKey.class) + " for initialisation");
            }
            final ECPrivateKeyParameters ecPrivateKeyParameters3 = (ECPrivateKeyParameters)ECUtil.generatePrivateKeyParameter((PrivateKey)key);
            this.parameters = ecPrivateKeyParameters3.getParameters();
            this.agreement.init(ecPrivateKeyParameters3);
        }
    }
    
    private static String getSimpleName(final Class clazz) {
        final String name = clazz.getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }
    
    static {
        converter = new X9IntegerConverter();
        algorithms = new Hashtable();
        final Integer n = new Integer(128);
        final Integer value = new Integer(192);
        final Integer n2 = new Integer(256);
        KeyAgreement.algorithms.put(NISTObjectIdentifiers.id_aes128_CBC.getId(), n);
        KeyAgreement.algorithms.put(NISTObjectIdentifiers.id_aes192_CBC.getId(), value);
        KeyAgreement.algorithms.put(NISTObjectIdentifiers.id_aes256_CBC.getId(), n2);
        KeyAgreement.algorithms.put(NISTObjectIdentifiers.id_aes128_wrap.getId(), n);
        KeyAgreement.algorithms.put(NISTObjectIdentifiers.id_aes192_wrap.getId(), value);
        KeyAgreement.algorithms.put(NISTObjectIdentifiers.id_aes256_wrap.getId(), n2);
        KeyAgreement.algorithms.put(PKCSObjectIdentifiers.id_alg_CMS3DESwrap.getId(), value);
    }
    
    public static class DH extends KeyAgreement
    {
        public DH() {
            super("ECDH", new ECDHBasicAgreement(), null);
        }
    }
    
    public static class DHC extends KeyAgreement
    {
        public DHC() {
            super("ECDHC", new ECDHCBasicAgreement(), null);
        }
    }
    
    public static class DHwithSHA1KDF extends KeyAgreement
    {
        public DHwithSHA1KDF() {
            super("ECDHwithSHA1KDF", new ECDHBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
        }
    }
    
    public static class MQV extends KeyAgreement
    {
        public MQV() {
            super("ECMQV", new ECMQVBasicAgreement(), null);
        }
    }
    
    public static class MQVwithSHA1KDF extends KeyAgreement
    {
        public MQVwithSHA1KDF() {
            super("ECMQVwithSHA1KDF", new ECMQVBasicAgreement(), new ECDHKEKGenerator(new SHA1Digest()));
        }
    }
}
