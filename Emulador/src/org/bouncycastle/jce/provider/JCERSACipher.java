// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import javax.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import javax.crypto.BadPaddingException;
import java.security.spec.InvalidParameterSpecException;
import org.bouncycastle.crypto.CipherParameters;
import java.security.InvalidParameterException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import java.security.InvalidAlgorithmParameterException;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import org.bouncycastle.crypto.encodings.ISO9796d1Encoding;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.util.Strings;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.Key;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import javax.crypto.spec.PSource;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import java.io.ByteArrayOutputStream;
import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.AsymmetricBlockCipher;

public class JCERSACipher extends WrapCipherSpi
{
    private AsymmetricBlockCipher cipher;
    private AlgorithmParameterSpec paramSpec;
    private AlgorithmParameters engineParams;
    private boolean publicKeyOnly;
    private boolean privateKeyOnly;
    private ByteArrayOutputStream bOut;
    
    public JCERSACipher(final AsymmetricBlockCipher cipher) {
        this.publicKeyOnly = false;
        this.privateKeyOnly = false;
        this.bOut = new ByteArrayOutputStream();
        this.cipher = cipher;
    }
    
    public JCERSACipher(final OAEPParameterSpec oaepParameterSpec) {
        this.publicKeyOnly = false;
        this.privateKeyOnly = false;
        this.bOut = new ByteArrayOutputStream();
        try {
            this.initFromSpec(oaepParameterSpec);
        }
        catch (NoSuchPaddingException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
    
    public JCERSACipher(final boolean publicKeyOnly, final boolean privateKeyOnly, final AsymmetricBlockCipher cipher) {
        this.publicKeyOnly = false;
        this.privateKeyOnly = false;
        this.bOut = new ByteArrayOutputStream();
        this.publicKeyOnly = publicKeyOnly;
        this.privateKeyOnly = privateKeyOnly;
        this.cipher = cipher;
    }
    
    private void initFromSpec(final OAEPParameterSpec paramSpec) throws NoSuchPaddingException {
        final MGF1ParameterSpec mgf1ParameterSpec = (MGF1ParameterSpec)paramSpec.getMGFParameters();
        final Digest digest = JCEDigestUtil.getDigest(mgf1ParameterSpec.getDigestAlgorithm());
        if (digest == null) {
            throw new NoSuchPaddingException("no match on OAEP constructor for digest algorithm: " + mgf1ParameterSpec.getDigestAlgorithm());
        }
        this.cipher = new OAEPEncoding(new RSABlindedEngine(), digest, ((PSource.PSpecified)paramSpec.getPSource()).getValue());
        this.paramSpec = paramSpec;
    }
    
    @Override
    protected int engineGetBlockSize() {
        try {
            return this.cipher.getInputBlockSize();
        }
        catch (NullPointerException ex) {
            throw new IllegalStateException("RSA Cipher not initialised");
        }
    }
    
    @Override
    protected byte[] engineGetIV() {
        return null;
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        if (key instanceof RSAPrivateKey) {
            return ((RSAPrivateKey)key).getModulus().bitLength();
        }
        if (key instanceof RSAPublicKey) {
            return ((RSAPublicKey)key).getModulus().bitLength();
        }
        throw new IllegalArgumentException("not an RSA key!");
    }
    
    @Override
    protected int engineGetOutputSize(final int n) {
        try {
            return this.cipher.getOutputBlockSize();
        }
        catch (NullPointerException ex) {
            throw new IllegalStateException("RSA Cipher not initialised");
        }
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.paramSpec != null) {
            try {
                (this.engineParams = AlgorithmParameters.getInstance("OAEP", "BC")).init(this.paramSpec);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.toString());
            }
        }
        return this.engineParams;
    }
    
    @Override
    protected void engineSetMode(final String str) throws NoSuchAlgorithmException {
        final String upperCase = Strings.toUpperCase(str);
        if (upperCase.equals("NONE") || upperCase.equals("ECB")) {
            return;
        }
        if (upperCase.equals("1")) {
            this.privateKeyOnly = true;
            this.publicKeyOnly = false;
            return;
        }
        if (upperCase.equals("2")) {
            this.privateKeyOnly = false;
            this.publicKeyOnly = true;
            return;
        }
        throw new NoSuchAlgorithmException("can't support mode " + str);
    }
    
    @Override
    protected void engineSetPadding(final String str) throws NoSuchPaddingException {
        final String upperCase = Strings.toUpperCase(str);
        if (upperCase.equals("NOPADDING")) {
            this.cipher = new RSABlindedEngine();
        }
        else if (upperCase.equals("PKCS1PADDING")) {
            this.cipher = new PKCS1Encoding(new RSABlindedEngine());
        }
        else if (upperCase.equals("ISO9796-1PADDING")) {
            this.cipher = new ISO9796d1Encoding(new RSABlindedEngine());
        }
        else if (upperCase.equals("OAEPWITHMD5ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("MD5", "MGF1", new MGF1ParameterSpec("MD5"), PSource.PSpecified.DEFAULT));
        }
        else if (upperCase.equals("OAEPPADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
        }
        else if (upperCase.equals("OAEPWITHSHA1ANDMGF1PADDING") || upperCase.equals("OAEPWITHSHA-1ANDMGF1PADDING")) {
            this.initFromSpec(OAEPParameterSpec.DEFAULT);
        }
        else if (upperCase.equals("OAEPWITHSHA224ANDMGF1PADDING") || upperCase.equals("OAEPWITHSHA-224ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-224", "MGF1", new MGF1ParameterSpec("SHA-224"), PSource.PSpecified.DEFAULT));
        }
        else if (upperCase.equals("OAEPWITHSHA256ANDMGF1PADDING") || upperCase.equals("OAEPWITHSHA-256ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        }
        else if (upperCase.equals("OAEPWITHSHA384ANDMGF1PADDING") || upperCase.equals("OAEPWITHSHA-384ANDMGF1PADDING")) {
            this.initFromSpec(new OAEPParameterSpec("SHA-384", "MGF1", MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
        }
        else {
            if (!upperCase.equals("OAEPWITHSHA512ANDMGF1PADDING") && !upperCase.equals("OAEPWITHSHA-512ANDMGF1PADDING")) {
                throw new NoSuchPaddingException(str + " unavailable with RSA.");
            }
            this.initFromSpec(new OAEPParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
        }
    }
    
    @Override
    protected void engineInit(final int i, final Key key, final AlgorithmParameterSpec paramSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (paramSpec == null || paramSpec instanceof OAEPParameterSpec) {
            CipherParameters cipherParameters;
            if (key instanceof RSAPublicKey) {
                if (this.privateKeyOnly) {
                    throw new InvalidKeyException("mode 1 requires RSAPrivateKey");
                }
                cipherParameters = RSAUtil.generatePublicKeyParameter((RSAPublicKey)key);
            }
            else {
                if (!(key instanceof RSAPrivateKey)) {
                    throw new InvalidKeyException("unknown key type passed to RSA");
                }
                if (this.publicKeyOnly) {
                    throw new InvalidKeyException("mode 2 requires RSAPublicKey");
                }
                cipherParameters = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)key);
            }
            if (paramSpec != null) {
                final OAEPParameterSpec oaepParameterSpec = (OAEPParameterSpec)paramSpec;
                this.paramSpec = paramSpec;
                if (!oaepParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1") && !oaepParameterSpec.getMGFAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1.getId())) {
                    throw new InvalidAlgorithmParameterException("unknown mask generation function specified");
                }
                if (!(oaepParameterSpec.getMGFParameters() instanceof MGF1ParameterSpec)) {
                    throw new InvalidAlgorithmParameterException("unkown MGF parameters");
                }
                final Digest digest = JCEDigestUtil.getDigest(oaepParameterSpec.getDigestAlgorithm());
                if (digest == null) {
                    throw new InvalidAlgorithmParameterException("no match on digest algorithm: " + oaepParameterSpec.getDigestAlgorithm());
                }
                final MGF1ParameterSpec mgf1ParameterSpec = (MGF1ParameterSpec)oaepParameterSpec.getMGFParameters();
                final Digest digest2 = JCEDigestUtil.getDigest(mgf1ParameterSpec.getDigestAlgorithm());
                if (digest2 == null) {
                    throw new InvalidAlgorithmParameterException("no match on MGF digest algorithm: " + mgf1ParameterSpec.getDigestAlgorithm());
                }
                this.cipher = new OAEPEncoding(new RSABlindedEngine(), digest, digest2, ((PSource.PSpecified)oaepParameterSpec.getPSource()).getValue());
            }
            if (!(this.cipher instanceof RSABlindedEngine)) {
                if (secureRandom != null) {
                    cipherParameters = new ParametersWithRandom(cipherParameters, secureRandom);
                }
                else {
                    cipherParameters = new ParametersWithRandom(cipherParameters, new SecureRandom());
                }
            }
            switch (i) {
                case 1:
                case 3: {
                    this.cipher.init(true, cipherParameters);
                    break;
                }
                case 2:
                case 4: {
                    this.cipher.init(false, cipherParameters);
                    break;
                }
                default: {
                    throw new InvalidParameterException("unknown opmode " + i + " passed to RSA");
                }
            }
            return;
        }
        throw new IllegalArgumentException("unknown parameter type.");
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameters engineParams, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        if (engineParams != null) {
            try {
                parameterSpec = engineParams.getParameterSpec(OAEPParameterSpec.class);
            }
            catch (InvalidParameterSpecException cause) {
                throw new InvalidAlgorithmParameterException("cannot recognise parameters: " + cause.toString(), cause);
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
        catch (InvalidAlgorithmParameterException cause) {
            throw new RuntimeException("Eeeek! " + cause.toString(), cause);
        }
    }
    
    @Override
    protected byte[] engineUpdate(final byte[] b, final int off, final int len) {
        this.bOut.write(b, off, len);
        if (this.cipher instanceof RSABlindedEngine) {
            if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
            }
        }
        else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        return null;
    }
    
    @Override
    protected int engineUpdate(final byte[] b, final int off, final int len, final byte[] array, final int n) {
        this.bOut.write(b, off, len);
        if (this.cipher instanceof RSABlindedEngine) {
            if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
            }
        }
        else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        return 0;
    }
    
    @Override
    protected byte[] engineDoFinal(final byte[] b, final int off, final int len) throws IllegalBlockSizeException, BadPaddingException {
        if (b != null) {
            this.bOut.write(b, off, len);
        }
        if (this.cipher instanceof RSABlindedEngine) {
            if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
            }
        }
        else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        try {
            final byte[] byteArray = this.bOut.toByteArray();
            this.bOut.reset();
            return this.cipher.processBlock(byteArray, 0, byteArray.length);
        }
        catch (InvalidCipherTextException ex) {
            throw new BadPaddingException(ex.getMessage());
        }
    }
    
    @Override
    protected int engineDoFinal(final byte[] b, final int off, final int len, final byte[] array, final int n) throws IllegalBlockSizeException, BadPaddingException {
        if (b != null) {
            this.bOut.write(b, off, len);
        }
        if (this.cipher instanceof RSABlindedEngine) {
            if (this.bOut.size() > this.cipher.getInputBlockSize() + 1) {
                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
            }
        }
        else if (this.bOut.size() > this.cipher.getInputBlockSize()) {
            throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
        }
        byte[] processBlock;
        try {
            final byte[] byteArray = this.bOut.toByteArray();
            this.bOut.reset();
            processBlock = this.cipher.processBlock(byteArray, 0, byteArray.length);
        }
        catch (InvalidCipherTextException ex) {
            throw new BadPaddingException(ex.getMessage());
        }
        for (int i = 0; i != processBlock.length; ++i) {
            array[n + i] = processBlock[i];
        }
        return processBlock.length;
    }
    
    public static class ISO9796d1Padding extends JCERSACipher
    {
        public ISO9796d1Padding() {
            super(new ISO9796d1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class NoPadding extends JCERSACipher
    {
        public NoPadding() {
            super(new RSABlindedEngine());
        }
    }
    
    public static class OAEPPadding extends JCERSACipher
    {
        public OAEPPadding() {
            super(OAEPParameterSpec.DEFAULT);
        }
    }
    
    public static class PKCS1v1_5Padding extends JCERSACipher
    {
        public PKCS1v1_5Padding() {
            super(new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class PKCS1v1_5Padding_PrivateOnly extends JCERSACipher
    {
        public PKCS1v1_5Padding_PrivateOnly() {
            super(false, true, new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class PKCS1v1_5Padding_PublicOnly extends JCERSACipher
    {
        public PKCS1v1_5Padding_PublicOnly() {
            super(true, false, new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
}
