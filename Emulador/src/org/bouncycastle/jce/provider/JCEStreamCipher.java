// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.engines.VMPCKSA3Engine;
import org.bouncycastle.crypto.engines.VMPCEngine;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.engines.SkipjackEngine;
import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.engines.HC256Engine;
import org.bouncycastle.crypto.engines.HC128Engine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.DataLengthException;
import javax.crypto.ShortBufferException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import javax.crypto.NoSuchPaddingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.BlockCipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.StreamCipher;

public class JCEStreamCipher extends WrapCipherSpi implements PBE
{
    private Class[] availableSpecs;
    private StreamCipher cipher;
    private ParametersWithIV ivParam;
    private int ivLength;
    private PBEParameterSpec pbeSpec;
    private String pbeAlgorithm;
    
    protected JCEStreamCipher(final StreamCipher cipher, final int ivLength) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.cipher = cipher;
        this.ivLength = ivLength;
    }
    
    protected JCEStreamCipher(final BlockCipher blockCipher, final int ivLength) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.ivLength = ivLength;
        this.cipher = new StreamBlockCipher(blockCipher);
    }
    
    @Override
    protected int engineGetBlockSize() {
        return 0;
    }
    
    @Override
    protected byte[] engineGetIV() {
        return (byte[])((this.ivParam != null) ? this.ivParam.getIV() : null);
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        return key.getEncoded().length * 8;
    }
    
    @Override
    protected int engineGetOutputSize(final int n) {
        return n;
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.pbeSpec != null) {
            try {
                final AlgorithmParameters instance = AlgorithmParameters.getInstance(this.pbeAlgorithm, "BC");
                instance.init(this.pbeSpec);
                return instance;
            }
            catch (Exception ex) {
                return null;
            }
        }
        return this.engineParams;
    }
    
    @Override
    protected void engineSetMode(final String str) {
        if (!str.equalsIgnoreCase("ECB")) {
            throw new IllegalArgumentException("can't support mode " + str);
        }
    }
    
    @Override
    protected void engineSetPadding(final String str) throws NoSuchPaddingException {
        if (!str.equalsIgnoreCase("NoPadding")) {
            throw new NoSuchPaddingException("Padding " + str + " unknown.");
        }
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.engineParams = null;
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Key for algorithm " + key.getAlgorithm() + " not suitable for symmetric enryption.");
        }
        CipherParameters cipherParameters;
        if (key instanceof JCEPBEKey) {
            final JCEPBEKey jcepbeKey = (JCEPBEKey)key;
            if (jcepbeKey.getOID() != null) {
                this.pbeAlgorithm = jcepbeKey.getOID().getId();
            }
            else {
                this.pbeAlgorithm = jcepbeKey.getAlgorithm();
            }
            if (jcepbeKey.getParam() != null) {
                cipherParameters = jcepbeKey.getParam();
                this.pbeSpec = new PBEParameterSpec(jcepbeKey.getSalt(), jcepbeKey.getIterationCount());
            }
            else {
                if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                    throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                }
                cipherParameters = Util.makePBEParameters(jcepbeKey, algorithmParameterSpec, this.cipher.getAlgorithmName());
                this.pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
            }
            if (jcepbeKey.getIvSize() != 0) {
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
        }
        else if (algorithmParameterSpec == null) {
            cipherParameters = new KeyParameter(key.getEncoded());
        }
        else {
            if (!(algorithmParameterSpec instanceof IvParameterSpec)) {
                throw new IllegalArgumentException("unknown parameter type.");
            }
            cipherParameters = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)algorithmParameterSpec).getIV());
            this.ivParam = (ParametersWithIV)cipherParameters;
        }
        if (this.ivLength != 0 && !(cipherParameters instanceof ParametersWithIV)) {
            SecureRandom secureRandom2 = secureRandom;
            if (secureRandom2 == null) {
                secureRandom2 = new SecureRandom();
            }
            if (n != 1 && n != 3) {
                throw new InvalidAlgorithmParameterException("no IV set when one expected");
            }
            final byte[] bytes = new byte[this.ivLength];
            secureRandom2.nextBytes(bytes);
            cipherParameters = new ParametersWithIV(cipherParameters, bytes);
            this.ivParam = (ParametersWithIV)cipherParameters;
        }
        switch (n) {
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
        this.engineInit(n, key, parameterSpec, secureRandom);
        this.engineParams = engineParams;
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new InvalidKeyException(ex.getMessage());
        }
    }
    
    @Override
    protected byte[] engineUpdate(final byte[] array, final int n, final int n2) {
        final byte[] array2 = new byte[n2];
        this.cipher.processBytes(array, n, n2, array2, 0);
        return array2;
    }
    
    @Override
    protected int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws ShortBufferException {
        try {
            this.cipher.processBytes(array, n, n2, array2, n3);
            return n2;
        }
        catch (DataLengthException ex) {
            throw new ShortBufferException(ex.getMessage());
        }
    }
    
    @Override
    protected byte[] engineDoFinal(final byte[] array, final int n, final int n2) {
        if (n2 != 0) {
            final byte[] engineUpdate = this.engineUpdate(array, n, n2);
            this.cipher.reset();
            return engineUpdate;
        }
        this.cipher.reset();
        return new byte[0];
    }
    
    @Override
    protected int engineDoFinal(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        if (n2 != 0) {
            this.cipher.processBytes(array, n, n2, array2, n3);
        }
        this.cipher.reset();
        return n2;
    }
    
    public static class Blowfish_CFB8 extends JCEStreamCipher
    {
        public Blowfish_CFB8() {
            super(new CFBBlockCipher(new BlowfishEngine(), 8), 64);
        }
    }
    
    public static class Blowfish_OFB8 extends JCEStreamCipher
    {
        public Blowfish_OFB8() {
            super(new OFBBlockCipher(new BlowfishEngine(), 8), 64);
        }
    }
    
    public static class DES_CFB8 extends JCEStreamCipher
    {
        public DES_CFB8() {
            super(new CFBBlockCipher(new DESEngine(), 8), 64);
        }
    }
    
    public static class DES_OFB8 extends JCEStreamCipher
    {
        public DES_OFB8() {
            super(new OFBBlockCipher(new DESEngine(), 8), 64);
        }
    }
    
    public static class DESede_CFB8 extends JCEStreamCipher
    {
        public DESede_CFB8() {
            super(new CFBBlockCipher(new DESedeEngine(), 8), 64);
        }
    }
    
    public static class DESede_OFB8 extends JCEStreamCipher
    {
        public DESede_OFB8() {
            super(new OFBBlockCipher(new DESedeEngine(), 8), 64);
        }
    }
    
    public static class HC128 extends JCEStreamCipher
    {
        public HC128() {
            super(new HC128Engine(), 16);
        }
    }
    
    public static class HC256 extends JCEStreamCipher
    {
        public HC256() {
            super(new HC256Engine(), 32);
        }
    }
    
    public static class PBEWithSHAAnd128BitRC4 extends JCEStreamCipher
    {
        public PBEWithSHAAnd128BitRC4() {
            super(new RC4Engine(), 0);
        }
    }
    
    public static class PBEWithSHAAnd40BitRC4 extends JCEStreamCipher
    {
        public PBEWithSHAAnd40BitRC4() {
            super(new RC4Engine(), 0);
        }
    }
    
    public static class RC4 extends JCEStreamCipher
    {
        public RC4() {
            super(new RC4Engine(), 0);
        }
    }
    
    public static class Salsa20 extends JCEStreamCipher
    {
        public Salsa20() {
            super(new Salsa20Engine(), 8);
        }
    }
    
    public static class Skipjack_CFB8 extends JCEStreamCipher
    {
        public Skipjack_CFB8() {
            super(new CFBBlockCipher(new SkipjackEngine(), 8), 64);
        }
    }
    
    public static class Skipjack_OFB8 extends JCEStreamCipher
    {
        public Skipjack_OFB8() {
            super(new OFBBlockCipher(new SkipjackEngine(), 8), 64);
        }
    }
    
    public static class Twofish_CFB8 extends JCEStreamCipher
    {
        public Twofish_CFB8() {
            super(new CFBBlockCipher(new TwofishEngine(), 8), 128);
        }
    }
    
    public static class Twofish_OFB8 extends JCEStreamCipher
    {
        public Twofish_OFB8() {
            super(new OFBBlockCipher(new TwofishEngine(), 8), 128);
        }
    }
    
    public static class VMPC extends JCEStreamCipher
    {
        public VMPC() {
            super(new VMPCEngine(), 16);
        }
    }
    
    public static class VMPCKSA3 extends JCEStreamCipher
    {
        public VMPCKSA3() {
            super(new VMPCKSA3Engine(), 16);
        }
    }
}
