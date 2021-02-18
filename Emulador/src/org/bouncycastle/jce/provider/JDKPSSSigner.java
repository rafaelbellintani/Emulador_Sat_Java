// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.engines.RSABlindedEngine;
import java.security.spec.MGF1ParameterSpec;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.security.InvalidParameterException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.CryptoException;
import java.security.SignatureException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.SecureRandom;
import java.security.PrivateKey;
import org.bouncycastle.crypto.CipherParameters;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;
import java.security.PublicKey;
import org.bouncycastle.jce.provider.util.NullDigest;
import org.bouncycastle.crypto.signers.PSSSigner;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import java.security.spec.PSSParameterSpec;
import java.security.AlgorithmParameters;
import java.security.SignatureSpi;

public class JDKPSSSigner extends SignatureSpi
{
    private AlgorithmParameters engineParams;
    private PSSParameterSpec paramSpec;
    private PSSParameterSpec originalSpec;
    private AsymmetricBlockCipher signer;
    private Digest contentDigest;
    private Digest mgfDigest;
    private int saltLength;
    private byte trailer;
    private boolean isRaw;
    private PSSSigner pss;
    
    private byte getTrailer(final int n) {
        if (n == 1) {
            return -68;
        }
        throw new IllegalArgumentException("unknown trailer field");
    }
    
    private void setupContentDigest() {
        if (this.isRaw) {
            this.contentDigest = new NullDigest();
        }
        else {
            this.contentDigest = this.mgfDigest;
        }
    }
    
    protected JDKPSSSigner(final AsymmetricBlockCipher asymmetricBlockCipher, final PSSParameterSpec pssParameterSpec) {
        this(asymmetricBlockCipher, pssParameterSpec, false);
    }
    
    protected JDKPSSSigner(final AsymmetricBlockCipher signer, final PSSParameterSpec pssParameterSpec, final boolean isRaw) {
        this.signer = signer;
        this.originalSpec = pssParameterSpec;
        if (pssParameterSpec == null) {
            this.paramSpec = PSSParameterSpec.DEFAULT;
        }
        else {
            this.paramSpec = pssParameterSpec;
        }
        this.mgfDigest = JCEDigestUtil.getDigest(this.paramSpec.getDigestAlgorithm());
        this.saltLength = this.paramSpec.getSaltLength();
        this.trailer = this.getTrailer(this.paramSpec.getTrailerField());
        this.isRaw = isRaw;
        this.setupContentDigest();
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        if (!(publicKey instanceof RSAPublicKey)) {
            throw new InvalidKeyException("Supplied key is not a RSAPublicKey instance");
        }
        (this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer)).init(false, RSAUtil.generatePublicKeyParameter((RSAPublicKey)publicKey));
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey, final SecureRandom secureRandom) throws InvalidKeyException {
        if (!(privateKey instanceof RSAPrivateKey)) {
            throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
        }
        (this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer)).init(true, new ParametersWithRandom(RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey), secureRandom));
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof RSAPrivateKey)) {
            throw new InvalidKeyException("Supplied key is not a RSAPrivateKey instance");
        }
        (this.pss = new PSSSigner(this.signer, this.contentDigest, this.mgfDigest, this.saltLength, this.trailer)).init(true, RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey));
    }
    
    @Override
    protected void engineUpdate(final byte b) throws SignatureException {
        this.pss.update(b);
    }
    
    @Override
    protected void engineUpdate(final byte[] array, final int n, final int n2) throws SignatureException {
        this.pss.update(array, n, n2);
    }
    
    @Override
    protected byte[] engineSign() throws SignatureException {
        try {
            return this.pss.generateSignature();
        }
        catch (CryptoException ex) {
            throw new SignatureException(ex.getMessage());
        }
    }
    
    @Override
    protected boolean engineVerify(final byte[] array) throws SignatureException {
        return this.pss.verifySignature(array);
    }
    
    @Override
    protected void engineSetParameter(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterException {
        if (!(algorithmParameterSpec instanceof PSSParameterSpec)) {
            throw new InvalidParameterException("Only PSSParameterSpec supported");
        }
        final PSSParameterSpec paramSpec = (PSSParameterSpec)algorithmParameterSpec;
        if (this.originalSpec != null && !JCEDigestUtil.isSameDigest(this.originalSpec.getDigestAlgorithm(), paramSpec.getDigestAlgorithm())) {
            throw new InvalidParameterException("parameter must be using " + this.originalSpec.getDigestAlgorithm());
        }
        if (!paramSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1") && !paramSpec.getMGFAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1.getId())) {
            throw new InvalidParameterException("unknown mask generation function specified");
        }
        if (!(paramSpec.getMGFParameters() instanceof MGF1ParameterSpec)) {
            throw new InvalidParameterException("unkown MGF parameters");
        }
        final MGF1ParameterSpec mgf1ParameterSpec = (MGF1ParameterSpec)paramSpec.getMGFParameters();
        if (!JCEDigestUtil.isSameDigest(mgf1ParameterSpec.getDigestAlgorithm(), paramSpec.getDigestAlgorithm())) {
            throw new InvalidParameterException("digest algorithm for MGF should be the same as for PSS parameters.");
        }
        final Digest digest = JCEDigestUtil.getDigest(mgf1ParameterSpec.getDigestAlgorithm());
        if (digest == null) {
            throw new InvalidParameterException("no match on MGF digest algorithm: " + mgf1ParameterSpec.getDigestAlgorithm());
        }
        this.engineParams = null;
        this.paramSpec = paramSpec;
        this.mgfDigest = digest;
        this.saltLength = this.paramSpec.getSaltLength();
        this.trailer = this.getTrailer(this.paramSpec.getTrailerField());
        this.setupContentDigest();
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.paramSpec != null) {
            try {
                (this.engineParams = AlgorithmParameters.getInstance("PSS", "BC")).init(this.paramSpec);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.toString());
            }
        }
        return this.engineParams;
    }
    
    @Override
    @Deprecated
    protected void engineSetParameter(final String s, final Object o) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineGetParameter unsupported");
    }
    
    public static class PSSwithRSA extends JDKPSSSigner
    {
        public PSSwithRSA() {
            super(new RSABlindedEngine(), null);
        }
    }
    
    public static class SHA1withRSA extends JDKPSSSigner
    {
        public SHA1withRSA() {
            super(new RSABlindedEngine(), PSSParameterSpec.DEFAULT);
        }
    }
    
    public static class SHA224withRSA extends JDKPSSSigner
    {
        public SHA224withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-224", "MGF1", new MGF1ParameterSpec("SHA-224"), 28, 1));
        }
    }
    
    public static class SHA256withRSA extends JDKPSSSigner
    {
        public SHA256withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1));
        }
    }
    
    public static class SHA384withRSA extends JDKPSSSigner
    {
        public SHA384withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-384", "MGF1", new MGF1ParameterSpec("SHA-384"), 48, 1));
        }
    }
    
    public static class SHA512withRSA extends JDKPSSSigner
    {
        public SHA512withRSA() {
            super(new RSABlindedEngine(), new PSSParameterSpec("SHA-512", "MGF1", new MGF1ParameterSpec("SHA-512"), 64, 1));
        }
    }
    
    public static class nonePSS extends JDKPSSSigner
    {
        public nonePSS() {
            super(new RSABlindedEngine(), null, true);
        }
    }
}
