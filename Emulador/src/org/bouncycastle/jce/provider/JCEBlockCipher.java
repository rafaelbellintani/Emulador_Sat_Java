// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.engines.XTEAEngine;
import org.bouncycastle.crypto.engines.TEAEngine;
import org.bouncycastle.crypto.engines.SkipjackEngine;
import org.bouncycastle.crypto.engines.SerpentEngine;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.engines.RC6Engine;
import org.bouncycastle.crypto.engines.RC564Engine;
import org.bouncycastle.crypto.engines.RC532Engine;
import org.bouncycastle.crypto.engines.TwofishEngine;
import org.bouncycastle.crypto.engines.RC2Engine;
import org.bouncycastle.crypto.engines.GOST28147Engine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.engines.CAST6Engine;
import org.bouncycastle.crypto.engines.CAST5Engine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.InvalidCipherTextException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.bouncycastle.crypto.DataLengthException;
import javax.crypto.ShortBufferException;
import java.security.InvalidParameterException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RC5Parameters;
import org.bouncycastle.crypto.params.RC2Parameters;
import org.bouncycastle.crypto.params.ParametersWithSBox;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import org.bouncycastle.crypto.paddings.TBCPadding;
import org.bouncycastle.crypto.paddings.ISO7816d4Padding;
import org.bouncycastle.crypto.paddings.X923Padding;
import org.bouncycastle.crypto.paddings.ISO10126d2Padding;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.crypto.modes.EAXBlockCipher;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.CCMBlockCipher;
import org.bouncycastle.crypto.modes.CTSBlockCipher;
import org.bouncycastle.crypto.modes.GOFBBlockCipher;
import org.bouncycastle.crypto.modes.SICBlockCipher;
import org.bouncycastle.crypto.modes.OpenPGPCFBBlockCipher;
import org.bouncycastle.crypto.modes.PGPCFBBlockCipher;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.modes.OFBBlockCipher;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.util.Strings;
import java.security.spec.AlgorithmParameterSpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.jce.spec.GOST28147ParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.BlockCipher;

public class JCEBlockCipher extends WrapCipherSpi implements PBE
{
    private Class[] availableSpecs;
    private BlockCipher baseEngine;
    private GenericBlockCipher cipher;
    private ParametersWithIV ivParam;
    private int ivLength;
    private boolean padded;
    private PBEParameterSpec pbeSpec;
    private String pbeAlgorithm;
    private String modeName;
    
    protected JCEBlockCipher(final BlockCipher baseEngine) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class };
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = baseEngine;
        this.cipher = new BufferedGenericBlockCipher(baseEngine);
    }
    
    protected JCEBlockCipher(final BlockCipher baseEngine, final int n) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class };
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = baseEngine;
        this.cipher = new BufferedGenericBlockCipher(baseEngine);
        this.ivLength = n / 8;
    }
    
    protected JCEBlockCipher(final BufferedBlockCipher bufferedBlockCipher, final int n) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class, GOST28147ParameterSpec.class };
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.modeName = null;
        this.baseEngine = bufferedBlockCipher.getUnderlyingCipher();
        this.cipher = new BufferedGenericBlockCipher(bufferedBlockCipher);
        this.ivLength = n / 8;
    }
    
    @Override
    protected int engineGetBlockSize() {
        return this.baseEngine.getBlockSize();
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
        return this.cipher.getOutputSize(n);
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null) {
            if (this.pbeSpec != null) {
                try {
                    (this.engineParams = AlgorithmParameters.getInstance(this.pbeAlgorithm, "BC")).init(this.pbeSpec);
                    return this.engineParams;
                }
                catch (Exception ex2) {
                    return null;
                }
            }
            if (this.ivParam != null) {
                String algorithm = this.cipher.getUnderlyingCipher().getAlgorithmName();
                if (algorithm.indexOf(47) >= 0) {
                    algorithm = algorithm.substring(0, algorithm.indexOf(47));
                }
                try {
                    (this.engineParams = AlgorithmParameters.getInstance(algorithm, "BC")).init(this.ivParam.getIV());
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex.toString());
                }
            }
        }
        return this.engineParams;
    }
    
    @Override
    protected void engineSetMode(final String str) throws NoSuchAlgorithmException {
        this.modeName = Strings.toUpperCase(str);
        if (this.modeName.equals("ECB")) {
            this.ivLength = 0;
            this.cipher = new BufferedGenericBlockCipher(this.baseEngine);
        }
        else if (this.modeName.equals("CBC")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new CBCBlockCipher(this.baseEngine));
        }
        else if (this.modeName.startsWith("OFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
                this.cipher = new BufferedGenericBlockCipher(new OFBBlockCipher(this.baseEngine, Integer.parseInt(this.modeName.substring(3))));
            }
            else {
                this.cipher = new BufferedGenericBlockCipher(new OFBBlockCipher(this.baseEngine, 8 * this.baseEngine.getBlockSize()));
            }
        }
        else if (this.modeName.startsWith("CFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.modeName.length() != 3) {
                this.cipher = new BufferedGenericBlockCipher(new CFBBlockCipher(this.baseEngine, Integer.parseInt(this.modeName.substring(3))));
            }
            else {
                this.cipher = new BufferedGenericBlockCipher(new CFBBlockCipher(this.baseEngine, 8 * this.baseEngine.getBlockSize()));
            }
        }
        else if (this.modeName.startsWith("PGP")) {
            final boolean equalsIgnoreCase = this.modeName.equalsIgnoreCase("PGPCFBwithIV");
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new PGPCFBBlockCipher(this.baseEngine, equalsIgnoreCase));
        }
        else if (this.modeName.equalsIgnoreCase("OpenPGPCFB")) {
            this.ivLength = 0;
            this.cipher = new BufferedGenericBlockCipher(new OpenPGPCFBBlockCipher(this.baseEngine));
        }
        else if (this.modeName.startsWith("SIC")) {
            this.ivLength = this.baseEngine.getBlockSize();
            if (this.ivLength < 16) {
                throw new IllegalArgumentException("Warning: SIC-Mode can become a twotime-pad if the blocksize of the cipher is too small. Use a cipher with a block size of at least 128 bits (e.g. AES)");
            }
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
        }
        else if (this.modeName.startsWith("CTR")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new SICBlockCipher(this.baseEngine)));
        }
        else if (this.modeName.startsWith("GOFB")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(new GOFBBlockCipher(this.baseEngine)));
        }
        else if (this.modeName.startsWith("CTS")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(new CBCBlockCipher(this.baseEngine)));
        }
        else if (this.modeName.startsWith("CCM")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new AEADGenericBlockCipher(new CCMBlockCipher(this.baseEngine));
        }
        else if (this.modeName.startsWith("EAX")) {
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new AEADGenericBlockCipher(new EAXBlockCipher(this.baseEngine));
        }
        else {
            if (!this.modeName.startsWith("GCM")) {
                throw new NoSuchAlgorithmException("can't support mode " + str);
            }
            this.ivLength = this.baseEngine.getBlockSize();
            this.cipher = new AEADGenericBlockCipher(new GCMBlockCipher(this.baseEngine));
        }
    }
    
    @Override
    protected void engineSetPadding(final String str) throws NoSuchPaddingException {
        final String upperCase = Strings.toUpperCase(str);
        if (upperCase.equals("NOPADDING")) {
            if (this.cipher.wrapOnNoPadding()) {
                this.cipher = new BufferedGenericBlockCipher(new BufferedBlockCipher(this.cipher.getUnderlyingCipher()));
            }
        }
        else if (upperCase.equals("WITHCTS")) {
            this.cipher = new BufferedGenericBlockCipher(new CTSBlockCipher(this.cipher.getUnderlyingCipher()));
        }
        else {
            this.padded = true;
            if (this.isAEADModeName(this.modeName)) {
                throw new NoSuchPaddingException("Only NoPadding can be used with AEAD modes.");
            }
            if (upperCase.equals("PKCS5PADDING") || upperCase.equals("PKCS7PADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher());
            }
            else if (upperCase.equals("ZEROBYTEPADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ZeroBytePadding());
            }
            else if (upperCase.equals("ISO10126PADDING") || upperCase.equals("ISO10126-2PADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO10126d2Padding());
            }
            else if (upperCase.equals("X9.23PADDING") || upperCase.equals("X923PADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new X923Padding());
            }
            else if (upperCase.equals("ISO7816-4PADDING") || upperCase.equals("ISO9797-1PADDING")) {
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new ISO7816d4Padding());
            }
            else {
                if (!upperCase.equals("TBCPADDING")) {
                    throw new NoSuchPaddingException("Padding " + str + " unknown.");
                }
                this.cipher = new BufferedGenericBlockCipher(this.cipher.getUnderlyingCipher(), new TBCPadding());
            }
        }
    }
    
    @Override
    protected void engineInit(final int i, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.engineParams = null;
        if (!(key instanceof SecretKey)) {
            throw new InvalidKeyException("Key for algorithm " + key.getAlgorithm() + " not suitable for symmetric enryption.");
        }
        if (algorithmParameterSpec == null && this.baseEngine.getAlgorithmName().startsWith("RC5-64")) {
            throw new InvalidAlgorithmParameterException("RC5 requires an RC5ParametersSpec to be passed in.");
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
                this.pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
                cipherParameters = Util.makePBEParameters(jcepbeKey, algorithmParameterSpec, this.cipher.getUnderlyingCipher().getAlgorithmName());
            }
            if (cipherParameters instanceof ParametersWithIV) {
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
        }
        else if (algorithmParameterSpec == null) {
            cipherParameters = new KeyParameter(key.getEncoded());
        }
        else if (algorithmParameterSpec instanceof IvParameterSpec) {
            if (this.ivLength != 0) {
                final IvParameterSpec ivParameterSpec = (IvParameterSpec)algorithmParameterSpec;
                if (ivParameterSpec.getIV().length != this.ivLength && !this.isAEADModeName(this.modeName)) {
                    throw new InvalidAlgorithmParameterException("IV must be " + this.ivLength + " bytes long.");
                }
                cipherParameters = new ParametersWithIV(new KeyParameter(key.getEncoded()), ivParameterSpec.getIV());
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
            else {
                if (this.modeName != null && this.modeName.equals("ECB")) {
                    throw new InvalidAlgorithmParameterException("ECB mode does not use an IV");
                }
                cipherParameters = new KeyParameter(key.getEncoded());
            }
        }
        else if (algorithmParameterSpec instanceof GOST28147ParameterSpec) {
            final GOST28147ParameterSpec gost28147ParameterSpec = (GOST28147ParameterSpec)algorithmParameterSpec;
            cipherParameters = new ParametersWithSBox(new KeyParameter(key.getEncoded()), ((GOST28147ParameterSpec)algorithmParameterSpec).getSbox());
            if (gost28147ParameterSpec.getIV() != null && this.ivLength != 0) {
                cipherParameters = new ParametersWithIV(cipherParameters, gost28147ParameterSpec.getIV());
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
        }
        else if (algorithmParameterSpec instanceof RC2ParameterSpec) {
            final RC2ParameterSpec rc2ParameterSpec = (RC2ParameterSpec)algorithmParameterSpec;
            cipherParameters = new RC2Parameters(key.getEncoded(), ((RC2ParameterSpec)algorithmParameterSpec).getEffectiveKeyBits());
            if (rc2ParameterSpec.getIV() != null && this.ivLength != 0) {
                cipherParameters = new ParametersWithIV(cipherParameters, rc2ParameterSpec.getIV());
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
        }
        else {
            if (!(algorithmParameterSpec instanceof RC5ParameterSpec)) {
                throw new InvalidAlgorithmParameterException("unknown parameter type.");
            }
            final RC5ParameterSpec rc5ParameterSpec = (RC5ParameterSpec)algorithmParameterSpec;
            cipherParameters = new RC5Parameters(key.getEncoded(), ((RC5ParameterSpec)algorithmParameterSpec).getRounds());
            if (!this.baseEngine.getAlgorithmName().startsWith("RC5")) {
                throw new InvalidAlgorithmParameterException("RC5 parameters passed to a cipher that is not RC5.");
            }
            if (this.baseEngine.getAlgorithmName().equals("RC5-32")) {
                if (rc5ParameterSpec.getWordSize() != 32) {
                    throw new InvalidAlgorithmParameterException("RC5 already set up for a word size of 32 not " + rc5ParameterSpec.getWordSize() + ".");
                }
            }
            else if (this.baseEngine.getAlgorithmName().equals("RC5-64") && rc5ParameterSpec.getWordSize() != 64) {
                throw new InvalidAlgorithmParameterException("RC5 already set up for a word size of 64 not " + rc5ParameterSpec.getWordSize() + ".");
            }
            if (rc5ParameterSpec.getIV() != null && this.ivLength != 0) {
                cipherParameters = new ParametersWithIV(cipherParameters, rc5ParameterSpec.getIV());
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
        }
        if (this.ivLength != 0 && !(cipherParameters instanceof ParametersWithIV)) {
            SecureRandom secureRandom2 = secureRandom;
            if (secureRandom2 == null) {
                secureRandom2 = new SecureRandom();
            }
            if (i == 1 || i == 3) {
                final byte[] bytes = new byte[this.ivLength];
                secureRandom2.nextBytes(bytes);
                cipherParameters = new ParametersWithIV(cipherParameters, bytes);
                this.ivParam = (ParametersWithIV)cipherParameters;
            }
            else if (this.cipher.getUnderlyingCipher().getAlgorithmName().indexOf("PGPCFB") < 0) {
                throw new InvalidAlgorithmParameterException("no IV set when one expected");
            }
        }
        if (secureRandom != null && this.padded) {
            cipherParameters = new ParametersWithRandom(cipherParameters, secureRandom);
        }
        try {
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
                    throw new InvalidParameterException("unknown opmode " + i + " passed");
                }
            }
        }
        catch (Exception ex) {
            throw new InvalidKeyException(ex.getMessage());
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
        final int updateOutputSize = this.cipher.getUpdateOutputSize(n2);
        if (updateOutputSize <= 0) {
            this.cipher.processBytes(array, n, n2, null, 0);
            return null;
        }
        final byte[] array2 = new byte[updateOutputSize];
        final int processBytes = this.cipher.processBytes(array, n, n2, array2, 0);
        if (processBytes == 0) {
            return null;
        }
        if (processBytes != array2.length) {
            final byte[] array3 = new byte[processBytes];
            System.arraycopy(array2, 0, array3, 0, processBytes);
            return array3;
        }
        return array2;
    }
    
    @Override
    protected int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws ShortBufferException {
        try {
            return this.cipher.processBytes(array, n, n2, array2, n3);
        }
        catch (DataLengthException ex) {
            throw new ShortBufferException(ex.getMessage());
        }
    }
    
    @Override
    protected byte[] engineDoFinal(final byte[] array, final int n, final int n2) throws IllegalBlockSizeException, BadPaddingException {
        int processBytes = 0;
        final byte[] array2 = new byte[this.engineGetOutputSize(n2)];
        if (n2 != 0) {
            processBytes = this.cipher.processBytes(array, n, n2, array2, 0);
        }
        int n3;
        try {
            n3 = processBytes + this.cipher.doFinal(array2, processBytes);
        }
        catch (DataLengthException ex) {
            throw new IllegalBlockSizeException(ex.getMessage());
        }
        catch (InvalidCipherTextException ex2) {
            throw new BadPaddingException(ex2.getMessage());
        }
        if (n3 == array2.length) {
            return array2;
        }
        final byte[] array3 = new byte[n3];
        System.arraycopy(array2, 0, array3, 0, n3);
        return array3;
    }
    
    @Override
    protected int engineDoFinal(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws IllegalBlockSizeException, BadPaddingException {
        int processBytes = 0;
        if (n2 != 0) {
            processBytes = this.cipher.processBytes(array, n, n2, array2, n3);
        }
        try {
            return processBytes + this.cipher.doFinal(array2, n3 + processBytes);
        }
        catch (DataLengthException ex) {
            throw new IllegalBlockSizeException(ex.getMessage());
        }
        catch (InvalidCipherTextException ex2) {
            throw new BadPaddingException(ex2.getMessage());
        }
    }
    
    private boolean isAEADModeName(final String anObject) {
        return "CCM".equals(anObject) || "EAX".equals(anObject) || "GCM".equals(anObject);
    }
    
    private static class AEADGenericBlockCipher implements GenericBlockCipher
    {
        private AEADBlockCipher cipher;
        
        AEADGenericBlockCipher(final AEADBlockCipher cipher) {
            this.cipher = cipher;
        }
        
        @Override
        public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
            this.cipher.init(b, cipherParameters);
        }
        
        @Override
        public String getAlgorithmName() {
            return this.cipher.getUnderlyingCipher().getAlgorithmName();
        }
        
        @Override
        public boolean wrapOnNoPadding() {
            return false;
        }
        
        @Override
        public BlockCipher getUnderlyingCipher() {
            return this.cipher.getUnderlyingCipher();
        }
        
        @Override
        public int getOutputSize(final int n) {
            return this.cipher.getOutputSize(n);
        }
        
        @Override
        public int getUpdateOutputSize(final int n) {
            return this.cipher.getUpdateOutputSize(n);
        }
        
        @Override
        public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException {
            return this.cipher.processByte(b, array, n);
        }
        
        @Override
        public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
            return this.cipher.processBytes(array, n, n2, array2, n3);
        }
        
        @Override
        public int doFinal(final byte[] array, final int n) throws IllegalStateException, InvalidCipherTextException {
            return this.cipher.doFinal(array, n);
        }
    }
    
    public static class AES extends JCEBlockCipher
    {
        public AES() {
            super(new AESFastEngine());
        }
    }
    
    public static class AESCBC extends JCEBlockCipher
    {
        public AESCBC() {
            super(new CBCBlockCipher(new AESFastEngine()), 128);
        }
    }
    
    public static class AESCFB extends JCEBlockCipher
    {
        public AESCFB() {
            super(new CFBBlockCipher(new AESFastEngine(), 128), 128);
        }
    }
    
    public static class AESOFB extends JCEBlockCipher
    {
        public AESOFB() {
            super(new OFBBlockCipher(new AESFastEngine(), 128), 128);
        }
    }
    
    public static class Blowfish extends JCEBlockCipher
    {
        public Blowfish() {
            super(new BlowfishEngine());
        }
    }
    
    public static class BlowfishCBC extends JCEBlockCipher
    {
        public BlowfishCBC() {
            super(new CBCBlockCipher(new BlowfishEngine()), 64);
        }
    }
    
    private static class BufferedGenericBlockCipher implements GenericBlockCipher
    {
        private BufferedBlockCipher cipher;
        
        BufferedGenericBlockCipher(final BufferedBlockCipher cipher) {
            this.cipher = cipher;
        }
        
        BufferedGenericBlockCipher(final BlockCipher blockCipher) {
            this.cipher = new PaddedBufferedBlockCipher(blockCipher);
        }
        
        BufferedGenericBlockCipher(final BlockCipher blockCipher, final BlockCipherPadding blockCipherPadding) {
            this.cipher = new PaddedBufferedBlockCipher(blockCipher, blockCipherPadding);
        }
        
        @Override
        public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
            this.cipher.init(b, cipherParameters);
        }
        
        @Override
        public boolean wrapOnNoPadding() {
            return !(this.cipher instanceof CTSBlockCipher);
        }
        
        @Override
        public String getAlgorithmName() {
            return this.cipher.getUnderlyingCipher().getAlgorithmName();
        }
        
        @Override
        public BlockCipher getUnderlyingCipher() {
            return this.cipher.getUnderlyingCipher();
        }
        
        @Override
        public int getOutputSize(final int n) {
            return this.cipher.getOutputSize(n);
        }
        
        @Override
        public int getUpdateOutputSize(final int n) {
            return this.cipher.getUpdateOutputSize(n);
        }
        
        @Override
        public int processByte(final byte b, final byte[] array, final int n) throws DataLengthException {
            return this.cipher.processByte(b, array, n);
        }
        
        @Override
        public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws DataLengthException {
            return this.cipher.processBytes(array, n, n2, array2, n3);
        }
        
        @Override
        public int doFinal(final byte[] array, final int n) throws IllegalStateException, InvalidCipherTextException {
            return this.cipher.doFinal(array, n);
        }
    }
    
    private interface GenericBlockCipher
    {
        void init(final boolean p0, final CipherParameters p1) throws IllegalArgumentException;
        
        boolean wrapOnNoPadding();
        
        String getAlgorithmName();
        
        BlockCipher getUnderlyingCipher();
        
        int getOutputSize(final int p0);
        
        int getUpdateOutputSize(final int p0);
        
        int processByte(final byte p0, final byte[] p1, final int p2) throws DataLengthException;
        
        int processBytes(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4) throws DataLengthException;
        
        int doFinal(final byte[] p0, final int p1) throws IllegalStateException, InvalidCipherTextException;
    }
    
    public static class CAST5 extends JCEBlockCipher
    {
        public CAST5() {
            super(new CAST5Engine());
        }
    }
    
    public static class CAST5CBC extends JCEBlockCipher
    {
        public CAST5CBC() {
            super(new CBCBlockCipher(new CAST5Engine()), 64);
        }
    }
    
    public static class CAST6 extends JCEBlockCipher
    {
        public CAST6() {
            super(new CAST6Engine());
        }
    }
    
    public static class DES extends JCEBlockCipher
    {
        public DES() {
            super(new DESEngine());
        }
    }
    
    public static class DESCBC extends JCEBlockCipher
    {
        public DESCBC() {
            super(new CBCBlockCipher(new DESEngine()), 64);
        }
    }
    
    public static class DESede extends JCEBlockCipher
    {
        public DESede() {
            super(new DESedeEngine());
        }
    }
    
    public static class DESedeCBC extends JCEBlockCipher
    {
        public DESedeCBC() {
            super(new CBCBlockCipher(new DESedeEngine()), 64);
        }
    }
    
    public static class GOST28147 extends JCEBlockCipher
    {
        public GOST28147() {
            super(new GOST28147Engine());
        }
    }
    
    public static class GOST28147cbc extends JCEBlockCipher
    {
        public GOST28147cbc() {
            super(new CBCBlockCipher(new GOST28147Engine()), 64);
        }
    }
    
    public static class PBEWithAESCBC extends JCEBlockCipher
    {
        public PBEWithAESCBC() {
            super(new CBCBlockCipher(new AESFastEngine()));
        }
    }
    
    public static class PBEWithMD5AndDES extends JCEBlockCipher
    {
        public PBEWithMD5AndDES() {
            super(new CBCBlockCipher(new DESEngine()));
        }
    }
    
    public static class PBEWithMD5AndRC2 extends JCEBlockCipher
    {
        public PBEWithMD5AndRC2() {
            super(new CBCBlockCipher(new RC2Engine()));
        }
    }
    
    public static class PBEWithSHA1AndDES extends JCEBlockCipher
    {
        public PBEWithSHA1AndDES() {
            super(new CBCBlockCipher(new DESEngine()));
        }
    }
    
    public static class PBEWithSHA1AndRC2 extends JCEBlockCipher
    {
        public PBEWithSHA1AndRC2() {
            super(new CBCBlockCipher(new RC2Engine()));
        }
    }
    
    public static class PBEWithSHAAnd128BitRC2 extends JCEBlockCipher
    {
        public PBEWithSHAAnd128BitRC2() {
            super(new CBCBlockCipher(new RC2Engine()));
        }
    }
    
    public static class PBEWithSHAAnd40BitRC2 extends JCEBlockCipher
    {
        public PBEWithSHAAnd40BitRC2() {
            super(new CBCBlockCipher(new RC2Engine()));
        }
    }
    
    public static class PBEWithSHAAndDES2Key extends JCEBlockCipher
    {
        public PBEWithSHAAndDES2Key() {
            super(new CBCBlockCipher(new DESedeEngine()));
        }
    }
    
    public static class PBEWithSHAAndDES3Key extends JCEBlockCipher
    {
        public PBEWithSHAAndDES3Key() {
            super(new CBCBlockCipher(new DESedeEngine()));
        }
    }
    
    public static class PBEWithSHAAndTwofish extends JCEBlockCipher
    {
        public PBEWithSHAAndTwofish() {
            super(new CBCBlockCipher(new TwofishEngine()));
        }
    }
    
    public static class RC2 extends JCEBlockCipher
    {
        public RC2() {
            super(new RC2Engine());
        }
    }
    
    public static class RC2CBC extends JCEBlockCipher
    {
        public RC2CBC() {
            super(new CBCBlockCipher(new RC2Engine()), 64);
        }
    }
    
    public static class RC5 extends JCEBlockCipher
    {
        public RC5() {
            super(new RC532Engine());
        }
    }
    
    public static class RC564 extends JCEBlockCipher
    {
        public RC564() {
            super(new RC564Engine());
        }
    }
    
    public static class RC6 extends JCEBlockCipher
    {
        public RC6() {
            super(new RC6Engine());
        }
    }
    
    public static class Rijndael extends JCEBlockCipher
    {
        public Rijndael() {
            super(new RijndaelEngine());
        }
    }
    
    public static class SEED extends JCEBlockCipher
    {
        public SEED() {
            super(new SEEDEngine());
        }
    }
    
    public static class Serpent extends JCEBlockCipher
    {
        public Serpent() {
            super(new SerpentEngine());
        }
    }
    
    public static class Skipjack extends JCEBlockCipher
    {
        public Skipjack() {
            super(new SkipjackEngine());
        }
    }
    
    public static class TEA extends JCEBlockCipher
    {
        public TEA() {
            super(new TEAEngine());
        }
    }
    
    public static class Twofish extends JCEBlockCipher
    {
        public Twofish() {
            super(new TwofishEngine());
        }
    }
    
    public static class XTEA extends JCEBlockCipher
    {
        public XTEA() {
            super(new XTEAEngine());
        }
    }
}
