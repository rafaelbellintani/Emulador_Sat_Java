// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.RFC3211WrapEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.RC2WrapEngine;
import org.bouncycastle.crypto.engines.DESedeWrapEngine;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchProviderException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1InputStream;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.InvalidCipherTextException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import java.security.InvalidKeyException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.KeyParameter;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.Key;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import org.bouncycastle.crypto.Wrapper;
import java.security.AlgorithmParameters;
import javax.crypto.CipherSpi;

public abstract class WrapCipherSpi extends CipherSpi implements PBE
{
    private Class[] availableSpecs;
    protected int pbeType;
    protected int pbeHash;
    protected int pbeKeySize;
    protected int pbeIvSize;
    protected AlgorithmParameters engineParams;
    protected Wrapper wrapEngine;
    private int ivSize;
    private byte[] iv;
    
    protected WrapCipherSpi() {
        this.availableSpecs = new Class[] { IvParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class };
        this.pbeType = 2;
        this.pbeHash = 1;
        this.engineParams = null;
        this.wrapEngine = null;
    }
    
    protected WrapCipherSpi(final Wrapper wrapper) {
        this(wrapper, 0);
    }
    
    protected WrapCipherSpi(final Wrapper wrapEngine, final int ivSize) {
        this.availableSpecs = new Class[] { IvParameterSpec.class, PBEParameterSpec.class, RC2ParameterSpec.class, RC5ParameterSpec.class };
        this.pbeType = 2;
        this.pbeHash = 1;
        this.engineParams = null;
        this.wrapEngine = null;
        this.wrapEngine = wrapEngine;
        this.ivSize = ivSize;
    }
    
    @Override
    protected int engineGetBlockSize() {
        return 0;
    }
    
    @Override
    protected byte[] engineGetIV() {
        return this.iv.clone();
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        return key.getEncoded().length;
    }
    
    @Override
    protected int engineGetOutputSize(final int n) {
        return -1;
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }
    
    @Override
    protected void engineSetMode(final String str) throws NoSuchAlgorithmException {
        throw new NoSuchAlgorithmException("can't support mode " + str);
    }
    
    @Override
    protected void engineSetPadding(final String str) throws NoSuchPaddingException {
        throw new NoSuchPaddingException("Padding " + str + " unknown.");
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        CipherParameters cipherParameters;
        if (key instanceof JCEPBEKey) {
            final JCEPBEKey jcepbeKey = (JCEPBEKey)key;
            if (algorithmParameterSpec instanceof PBEParameterSpec) {
                cipherParameters = Util.makePBEParameters(jcepbeKey, algorithmParameterSpec, this.wrapEngine.getAlgorithmName());
            }
            else {
                if (jcepbeKey.getParam() == null) {
                    throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                }
                cipherParameters = jcepbeKey.getParam();
            }
        }
        else {
            cipherParameters = new KeyParameter(key.getEncoded());
        }
        if (algorithmParameterSpec instanceof IvParameterSpec) {
            cipherParameters = new ParametersWithIV(cipherParameters, ((IvParameterSpec)algorithmParameterSpec).getIV());
        }
        if (cipherParameters instanceof KeyParameter && this.ivSize != 0) {
            secureRandom.nextBytes(this.iv = new byte[this.ivSize]);
            cipherParameters = new ParametersWithIV(cipherParameters, this.iv);
        }
        switch (n) {
            case 3: {
                this.wrapEngine.init(true, cipherParameters);
                break;
            }
            case 4: {
                this.wrapEngine.init(false, cipherParameters);
                break;
            }
            case 1:
            case 2: {
                throw new IllegalArgumentException("engine only valid for wrapping");
            }
            default: {
                System.out.println("eeek!");
                break;
            }
        }
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameters engineParams, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        if (engineParams != null) {
            int i = 0;
            while (i != this.availableSpecs.length) {
                try {
                    parameterSpec = engineParams.getParameterSpec((Class<AlgorithmParameterSpec>)this.availableSpecs[i]);
                }
                catch (Exception ex) {
                    ++i;
                    continue;
                }
                break;
            }
            if (parameterSpec == null) {
                throw new InvalidAlgorithmParameterException("can't handle parameter " + engineParams.toString());
            }
        }
        this.engineParams = engineParams;
        this.engineInit(n, key, parameterSpec, secureRandom);
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
    
    @Override
    protected byte[] engineUpdate(final byte[] array, final int n, final int n2) {
        throw new RuntimeException("not supported for wrapping");
    }
    
    @Override
    protected int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws ShortBufferException {
        throw new RuntimeException("not supported for wrapping");
    }
    
    @Override
    protected byte[] engineDoFinal(final byte[] array, final int n, final int n2) throws IllegalBlockSizeException, BadPaddingException {
        return null;
    }
    
    @Override
    protected int engineDoFinal(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws IllegalBlockSizeException, BadPaddingException {
        return 0;
    }
    
    @Override
    protected byte[] engineWrap(final Key key) throws IllegalBlockSizeException, InvalidKeyException {
        final byte[] encoded = key.getEncoded();
        if (encoded == null) {
            throw new InvalidKeyException("Cannot wrap key, null encoding.");
        }
        try {
            if (this.wrapEngine == null) {
                return this.engineDoFinal(encoded, 0, encoded.length);
            }
            return this.wrapEngine.wrap(encoded, 0, encoded.length);
        }
        catch (BadPaddingException ex) {
            throw new IllegalBlockSizeException(ex.getMessage());
        }
    }
    
    @Override
    protected Key engineUnwrap(final byte[] array, final String s, final int i) throws InvalidKeyException {
        byte[] encodedKey;
        try {
            if (this.wrapEngine == null) {
                encodedKey = this.engineDoFinal(array, 0, array.length);
            }
            else {
                encodedKey = this.wrapEngine.unwrap(array, 0, array.length);
            }
        }
        catch (InvalidCipherTextException ex) {
            throw new InvalidKeyException(ex.getMessage());
        }
        catch (BadPaddingException ex2) {
            throw new InvalidKeyException(ex2.getMessage());
        }
        catch (IllegalBlockSizeException ex3) {
            throw new InvalidKeyException(ex3.getMessage());
        }
        if (i == 3) {
            return new SecretKeySpec(encodedKey, s);
        }
        if (s.equals("") && i == 2) {
            final ASN1InputStream asn1InputStream = new ASN1InputStream(encodedKey);
            PKCS12BagAttributeCarrier pkcs12BagAttributeCarrier;
            try {
                final PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo((ASN1Sequence)asn1InputStream.readObject());
                final DERObjectIdentifier objectId = privateKeyInfo.getAlgorithmId().getObjectId();
                if (objectId.equals(X9ObjectIdentifiers.id_ecPublicKey)) {
                    pkcs12BagAttributeCarrier = new JCEECPrivateKey(privateKeyInfo);
                }
                else if (objectId.equals(CryptoProObjectIdentifiers.gostR3410_94)) {
                    pkcs12BagAttributeCarrier = new JDKGOST3410PrivateKey(privateKeyInfo);
                }
                else if (objectId.equals(X9ObjectIdentifiers.id_dsa)) {
                    pkcs12BagAttributeCarrier = new JDKDSAPrivateKey(privateKeyInfo);
                }
                else if (objectId.equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
                    pkcs12BagAttributeCarrier = new JCEDHPrivateKey(privateKeyInfo);
                }
                else if (objectId.equals(X9ObjectIdentifiers.dhpublicnumber)) {
                    pkcs12BagAttributeCarrier = new JCEDHPrivateKey(privateKeyInfo);
                }
                else {
                    pkcs12BagAttributeCarrier = new JCERSAPrivateCrtKey(privateKeyInfo);
                }
            }
            catch (Exception ex7) {
                throw new InvalidKeyException("Invalid key encoding.");
            }
            return (Key)pkcs12BagAttributeCarrier;
        }
        try {
            final KeyFactory instance = KeyFactory.getInstance(s, "BC");
            if (i == 1) {
                return instance.generatePublic(new X509EncodedKeySpec(encodedKey));
            }
            if (i == 2) {
                return instance.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
            }
        }
        catch (NoSuchProviderException ex4) {
            throw new InvalidKeyException("Unknown key type " + ex4.getMessage());
        }
        catch (NoSuchAlgorithmException ex5) {
            throw new InvalidKeyException("Unknown key type " + ex5.getMessage());
        }
        catch (InvalidKeySpecException ex6) {
            throw new InvalidKeyException("Unknown key type " + ex6.getMessage());
        }
        throw new InvalidKeyException("Unknown key type " + i);
    }
    
    public static class DESEDEWrap extends WrapCipherSpi
    {
        public DESEDEWrap() {
            super(new DESedeWrapEngine());
        }
    }
    
    public static class RC2Wrap extends WrapCipherSpi
    {
        public RC2Wrap() {
            super(new RC2WrapEngine());
        }
    }
    
    public static class RFC3211DESedeWrap extends WrapCipherSpi
    {
        public RFC3211DESedeWrap() {
            super(new RFC3211WrapEngine(new DESedeEngine()), 8);
        }
    }
}
