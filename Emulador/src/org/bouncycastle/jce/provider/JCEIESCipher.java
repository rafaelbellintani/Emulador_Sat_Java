// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.agreement.DHBasicAgreement;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import javax.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import javax.crypto.BadPaddingException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.IESParameters;
import org.bouncycastle.jce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import javax.crypto.NoSuchPaddingException;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import javax.crypto.interfaces.DHPrivateKey;
import org.bouncycastle.jce.interfaces.IESKey;
import java.security.Key;
import org.bouncycastle.jce.spec.IESParameterSpec;
import java.security.AlgorithmParameters;
import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.engines.IESEngine;

public class JCEIESCipher extends WrapCipherSpi
{
    private IESEngine cipher;
    private int state;
    private ByteArrayOutputStream buffer;
    private AlgorithmParameters engineParam;
    private IESParameterSpec engineParams;
    private Class[] availableSpecs;
    
    public JCEIESCipher(final IESEngine cipher) {
        this.state = -1;
        this.buffer = new ByteArrayOutputStream();
        this.engineParam = null;
        this.engineParams = null;
        this.availableSpecs = new Class[] { IESParameterSpec.class };
        this.cipher = cipher;
    }
    
    @Override
    protected int engineGetBlockSize() {
        return 0;
    }
    
    @Override
    protected byte[] engineGetIV() {
        return null;
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        if (!(key instanceof IESKey)) {
            throw new IllegalArgumentException("must be passed IE key");
        }
        final IESKey iesKey = (IESKey)key;
        if (iesKey.getPrivate() instanceof DHPrivateKey) {
            return ((DHPrivateKey)iesKey.getPrivate()).getX().bitLength();
        }
        if (iesKey.getPrivate() instanceof ECPrivateKey) {
            return ((ECPrivateKey)iesKey.getPrivate()).getD().bitLength();
        }
        throw new IllegalArgumentException("not an IE key!");
    }
    
    @Override
    protected int engineGetOutputSize(final int n) {
        if (this.state == 1 || this.state == 3) {
            return this.buffer.size() + n + 20;
        }
        if (this.state == 2 || this.state == 4) {
            return this.buffer.size() + n - 20;
        }
        throw new IllegalStateException("cipher not initialised");
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParam == null && this.engineParams != null) {
            final String algorithm = "IES";
            try {
                (this.engineParam = AlgorithmParameters.getInstance(algorithm, "BC")).init(this.engineParams);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.toString());
            }
        }
        return this.engineParam;
    }
    
    @Override
    protected void engineSetMode(final String str) {
        throw new IllegalArgumentException("can't support mode " + str);
    }
    
    @Override
    protected void engineSetPadding(final String str) throws NoSuchPaddingException {
        throw new NoSuchPaddingException(str + " unavailable with RSA.");
    }
    
    @Override
    protected void engineInit(final int state, final Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (!(key instanceof IESKey)) {
            throw new InvalidKeyException("must be passed IES key");
        }
        if (algorithmParameterSpec == null && (state == 1 || state == 3)) {
            final byte[] bytes = new byte[16];
            final byte[] bytes2 = new byte[16];
            if (secureRandom == null) {
                secureRandom = new SecureRandom();
            }
            secureRandom.nextBytes(bytes);
            secureRandom.nextBytes(bytes2);
            algorithmParameterSpec = new IESParameterSpec(bytes, bytes2, 128);
        }
        else if (!(algorithmParameterSpec instanceof IESParameterSpec)) {
            throw new InvalidAlgorithmParameterException("must be passed IES parameters");
        }
        final IESKey iesKey = (IESKey)key;
        AsymmetricKeyParameter asymmetricKeyParameter;
        AsymmetricKeyParameter asymmetricKeyParameter2;
        if (iesKey.getPublic() instanceof ECPublicKey) {
            asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(iesKey.getPublic());
            asymmetricKeyParameter2 = ECUtil.generatePrivateKeyParameter(iesKey.getPrivate());
        }
        else {
            asymmetricKeyParameter = DHUtil.generatePublicKeyParameter(iesKey.getPublic());
            asymmetricKeyParameter2 = DHUtil.generatePrivateKeyParameter(iesKey.getPrivate());
        }
        this.engineParams = (IESParameterSpec)algorithmParameterSpec;
        final IESParameters iesParameters = new IESParameters(this.engineParams.getDerivationV(), this.engineParams.getEncodingV(), this.engineParams.getMacKeySize());
        this.state = state;
        this.buffer.reset();
        switch (state) {
            case 1:
            case 3: {
                this.cipher.init(true, asymmetricKeyParameter2, asymmetricKeyParameter, iesParameters);
                break;
            }
            case 2:
            case 4: {
                this.cipher.init(false, asymmetricKeyParameter2, asymmetricKeyParameter, iesParameters);
                break;
            }
            default: {
                System.out.println("eeek!");
                break;
            }
        }
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameters engineParam, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        if (engineParam != null) {
            int i = 0;
            while (i != this.availableSpecs.length) {
                try {
                    parameterSpec = engineParam.getParameterSpec((Class<AlgorithmParameterSpec>)this.availableSpecs[i]);
                }
                catch (Exception ex) {
                    ++i;
                    continue;
                }
                break;
            }
            if (parameterSpec == null) {
                throw new InvalidAlgorithmParameterException("can't handle parameter " + engineParam.toString());
            }
        }
        this.engineParam = engineParam;
        this.engineInit(n, key, parameterSpec, secureRandom);
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        if (n != 1) {
            if (n != 3) {
                throw new IllegalArgumentException("can't handle null parameter spec in IES");
            }
        }
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
            return;
        }
        catch (InvalidAlgorithmParameterException ex) {}
        throw new IllegalArgumentException("can't handle null parameter spec in IES");
    }
    
    @Override
    protected byte[] engineUpdate(final byte[] b, final int off, final int len) {
        this.buffer.write(b, off, len);
        return null;
    }
    
    @Override
    protected int engineUpdate(final byte[] b, final int off, final int len, final byte[] array, final int n) {
        this.buffer.write(b, off, len);
        return 0;
    }
    
    @Override
    protected byte[] engineDoFinal(final byte[] b, final int off, final int len) throws IllegalBlockSizeException, BadPaddingException {
        if (len != 0) {
            this.buffer.write(b, off, len);
        }
        try {
            final byte[] byteArray = this.buffer.toByteArray();
            this.buffer.reset();
            return this.cipher.processBlock(byteArray, 0, byteArray.length);
        }
        catch (InvalidCipherTextException ex) {
            throw new BadPaddingException(ex.getMessage());
        }
    }
    
    @Override
    protected int engineDoFinal(final byte[] b, final int off, final int len, final byte[] array, final int n) throws IllegalBlockSizeException, BadPaddingException {
        if (len != 0) {
            this.buffer.write(b, off, len);
        }
        try {
            final byte[] byteArray = this.buffer.toByteArray();
            this.buffer.reset();
            final byte[] processBlock = this.cipher.processBlock(byteArray, 0, byteArray.length);
            System.arraycopy(processBlock, 0, array, n, processBlock.length);
            return processBlock.length;
        }
        catch (InvalidCipherTextException ex) {
            throw new BadPaddingException(ex.getMessage());
        }
    }
    
    public static class BrokenECIES extends JCEIESCipher
    {
        public BrokenECIES() {
            super(new IESEngine(new ECDHBasicAgreement(), new BrokenKDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest())));
        }
    }
    
    public static class BrokenIES extends JCEIESCipher
    {
        public BrokenIES() {
            super(new IESEngine(new DHBasicAgreement(), new BrokenKDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest())));
        }
    }
    
    public static class ECIES extends JCEIESCipher
    {
        public ECIES() {
            super(new IESEngine(new ECDHBasicAgreement(), new KDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest())));
        }
    }
    
    public static class IES extends JCEIESCipher
    {
        public IES() {
            super(new IESEngine(new DHBasicAgreement(), new KDF2BytesGenerator(new SHA1Digest()), new HMac(new SHA1Digest())));
        }
    }
}
