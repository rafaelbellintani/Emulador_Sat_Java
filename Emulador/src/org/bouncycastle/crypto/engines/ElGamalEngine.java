// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import java.util.Random;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.ElGamalKeyParameters;
import org.bouncycastle.crypto.AsymmetricBlockCipher;

public class ElGamalEngine implements AsymmetricBlockCipher
{
    private ElGamalKeyParameters key;
    private SecureRandom random;
    private boolean forEncryption;
    private int bitSize;
    private static final BigInteger ZERO;
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.key = (ElGamalKeyParameters)parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
        }
        else {
            this.key = (ElGamalKeyParameters)cipherParameters;
            this.random = new SecureRandom();
        }
        this.forEncryption = forEncryption;
        this.bitSize = this.key.getParameters().getP().bitLength();
        if (forEncryption) {
            if (!(this.key instanceof ElGamalPublicKeyParameters)) {
                throw new IllegalArgumentException("ElGamalPublicKeyParameters are required for encryption.");
            }
        }
        else if (!(this.key instanceof ElGamalPrivateKeyParameters)) {
            throw new IllegalArgumentException("ElGamalPrivateKeyParameters are required for decryption.");
        }
    }
    
    @Override
    public int getInputBlockSize() {
        if (this.forEncryption) {
            return (this.bitSize - 1) / 8;
        }
        return 2 * ((this.bitSize + 7) / 8);
    }
    
    @Override
    public int getOutputBlockSize() {
        if (this.forEncryption) {
            return 2 * ((this.bitSize + 7) / 8);
        }
        return (this.bitSize - 1) / 8;
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) {
        if (this.key == null) {
            throw new IllegalStateException("ElGamal engine not initialised");
        }
        if (n2 > (this.forEncryption ? ((this.bitSize - 1 + 7) / 8) : this.getInputBlockSize())) {
            throw new DataLengthException("input too large for ElGamal cipher.\n");
        }
        final BigInteger p3 = this.key.getParameters().getP();
        if (this.key instanceof ElGamalPrivateKeyParameters) {
            final byte[] magnitude = new byte[n2 / 2];
            final byte[] magnitude2 = new byte[n2 / 2];
            System.arraycopy(array, n, magnitude, 0, magnitude.length);
            System.arraycopy(array, n + magnitude.length, magnitude2, 0, magnitude2.length);
            return BigIntegers.asUnsignedByteArray(new BigInteger(1, magnitude).modPow(p3.subtract(ElGamalEngine.ONE).subtract(((ElGamalPrivateKeyParameters)this.key).getX()), p3).multiply(new BigInteger(1, magnitude2)).mod(p3));
        }
        byte[] magnitude3;
        if (n != 0 || n2 != array.length) {
            magnitude3 = new byte[n2];
            System.arraycopy(array, n, magnitude3, 0, n2);
        }
        else {
            magnitude3 = array;
        }
        final BigInteger bigInteger = new BigInteger(1, magnitude3);
        if (bigInteger.bitLength() >= p3.bitLength()) {
            throw new DataLengthException("input too large for ElGamal cipher.\n");
        }
        final ElGamalPublicKeyParameters elGamalPublicKeyParameters = (ElGamalPublicKeyParameters)this.key;
        int bitLength;
        BigInteger bigInteger2;
        for (bitLength = p3.bitLength(), bigInteger2 = new BigInteger(bitLength, this.random); bigInteger2.equals(ElGamalEngine.ZERO) || bigInteger2.compareTo(p3.subtract(ElGamalEngine.TWO)) > 0; bigInteger2 = new BigInteger(bitLength, this.random)) {}
        final BigInteger modPow = this.key.getParameters().getG().modPow(bigInteger2, p3);
        final BigInteger mod = bigInteger.multiply(elGamalPublicKeyParameters.getY().modPow(bigInteger2, p3)).mod(p3);
        final byte[] byteArray = modPow.toByteArray();
        final byte[] byteArray2 = mod.toByteArray();
        final byte[] array2 = new byte[this.getOutputBlockSize()];
        if (byteArray.length > array2.length / 2) {
            System.arraycopy(byteArray, 1, array2, array2.length / 2 - (byteArray.length - 1), byteArray.length - 1);
        }
        else {
            System.arraycopy(byteArray, 0, array2, array2.length / 2 - byteArray.length, byteArray.length);
        }
        if (byteArray2.length > array2.length / 2) {
            System.arraycopy(byteArray2, 1, array2, array2.length - (byteArray2.length - 1), byteArray2.length - 1);
        }
        else {
            System.arraycopy(byteArray2, 0, array2, array2.length - byteArray2.length, byteArray2.length);
        }
        return array2;
    }
    
    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
}
