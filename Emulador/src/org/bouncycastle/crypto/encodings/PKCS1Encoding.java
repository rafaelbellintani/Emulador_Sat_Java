// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.encodings;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;

public class PKCS1Encoding implements AsymmetricBlockCipher
{
    public static final String STRICT_LENGTH_ENABLED_PROPERTY = "org.bouncycastle.pkcs1.strict";
    private static final int HEADER_LENGTH = 10;
    private SecureRandom random;
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private boolean forPrivateKey;
    private boolean useStrictLength;
    
    public PKCS1Encoding(final AsymmetricBlockCipher engine) {
        this.engine = engine;
        this.useStrictLength = this.useStrict();
    }
    
    private boolean useStrict() {
        final String s = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction() {
            @Override
            public Object run() {
                return System.getProperty("org.bouncycastle.pkcs1.strict");
            }
        });
        return s == null || s.equals("true");
    }
    
    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.random = parametersWithRandom.getRandom();
            asymmetricKeyParameter = (AsymmetricKeyParameter)parametersWithRandom.getParameters();
        }
        else {
            this.random = new SecureRandom();
            asymmetricKeyParameter = (AsymmetricKeyParameter)cipherParameters;
        }
        this.engine.init(forEncryption, cipherParameters);
        this.forPrivateKey = asymmetricKeyParameter.isPrivate();
        this.forEncryption = forEncryption;
    }
    
    @Override
    public int getInputBlockSize() {
        final int inputBlockSize = this.engine.getInputBlockSize();
        if (this.forEncryption) {
            return inputBlockSize - 10;
        }
        return inputBlockSize;
    }
    
    @Override
    public int getOutputBlockSize() {
        final int outputBlockSize = this.engine.getOutputBlockSize();
        if (this.forEncryption) {
            return outputBlockSize;
        }
        return outputBlockSize - 10;
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (this.forEncryption) {
            return this.encodeBlock(array, n, n2);
        }
        return this.decodeBlock(array, n, n2);
    }
    
    private byte[] encodeBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (n2 > this.getInputBlockSize()) {
            throw new IllegalArgumentException("input data too large");
        }
        final byte[] bytes = new byte[this.engine.getInputBlockSize()];
        if (this.forPrivateKey) {
            bytes[0] = 1;
            for (int i = 1; i != bytes.length - n2 - 1; ++i) {
                bytes[i] = -1;
            }
        }
        else {
            this.random.nextBytes(bytes);
            bytes[0] = 2;
            for (int j = 1; j != bytes.length - n2 - 1; ++j) {
                while (bytes[j] == 0) {
                    bytes[j] = (byte)this.random.nextInt();
                }
            }
        }
        bytes[bytes.length - n2 - 1] = 0;
        System.arraycopy(array, n, bytes, bytes.length - n2, n2);
        return this.engine.processBlock(bytes, 0, bytes.length);
    }
    
    private byte[] decodeBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        final byte[] processBlock = this.engine.processBlock(array, n, n2);
        if (processBlock.length < this.getOutputBlockSize()) {
            throw new InvalidCipherTextException("block truncated");
        }
        final byte b = processBlock[0];
        if (b != 1 && b != 2) {
            throw new InvalidCipherTextException("unknown block type");
        }
        if (this.useStrictLength && processBlock.length != this.engine.getOutputBlockSize()) {
            throw new InvalidCipherTextException("block incorrect size");
        }
        int i;
        for (i = 1; i != processBlock.length; ++i) {
            final byte b2 = processBlock[i];
            if (b2 == 0) {
                break;
            }
            if (b == 1 && b2 != -1) {
                throw new InvalidCipherTextException("block padding incorrect");
            }
        }
        if (++i > processBlock.length || i < 10) {
            throw new InvalidCipherTextException("no data in block");
        }
        final byte[] array2 = new byte[processBlock.length - i];
        System.arraycopy(processBlock, i, array2, 0, array2.length);
        return array2;
    }
}
