// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.util.Arrays;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.NaccacheSternPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import java.math.BigInteger;
import java.util.Vector;
import org.bouncycastle.crypto.params.NaccacheSternKeyParameters;
import org.bouncycastle.crypto.AsymmetricBlockCipher;

public class NaccacheSternEngine implements AsymmetricBlockCipher
{
    private boolean forEncryption;
    private NaccacheSternKeyParameters key;
    private Vector[] lookup;
    private boolean debug;
    private static BigInteger ZERO;
    private static BigInteger ONE;
    
    public NaccacheSternEngine() {
        this.lookup = null;
        this.debug = false;
    }
    
    @Override
    public void init(final boolean forEncryption, CipherParameters parameters) {
        this.forEncryption = forEncryption;
        if (parameters instanceof ParametersWithRandom) {
            parameters = ((ParametersWithRandom)parameters).getParameters();
        }
        this.key = (NaccacheSternKeyParameters)parameters;
        if (!this.forEncryption) {
            if (this.debug) {
                System.out.println("Constructing lookup Array");
            }
            final NaccacheSternPrivateKeyParameters naccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters)this.key;
            final Vector smallPrimes = naccacheSternPrivateKeyParameters.getSmallPrimes();
            this.lookup = new Vector[smallPrimes.size()];
            for (int i = 0; i < smallPrimes.size(); ++i) {
                final BigInteger val = smallPrimes.elementAt(i);
                final int intValue = val.intValue();
                (this.lookup[i] = new Vector()).addElement(NaccacheSternEngine.ONE);
                if (this.debug) {
                    System.out.println("Constructing lookup ArrayList for " + intValue);
                }
                BigInteger bigInteger = NaccacheSternEngine.ZERO;
                for (int j = 1; j < intValue; ++j) {
                    bigInteger = bigInteger.add(naccacheSternPrivateKeyParameters.getPhi_n());
                    this.lookup[i].addElement(naccacheSternPrivateKeyParameters.getG().modPow(bigInteger.divide(val), naccacheSternPrivateKeyParameters.getModulus()));
                }
            }
        }
    }
    
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
    
    @Override
    public int getInputBlockSize() {
        if (this.forEncryption) {
            return (this.key.getLowerSigmaBound() + 7) / 8 - 1;
        }
        return this.key.getModulus().toByteArray().length;
    }
    
    @Override
    public int getOutputBlockSize() {
        if (this.forEncryption) {
            return this.key.getModulus().toByteArray().length;
        }
        return (this.key.getLowerSigmaBound() + 7) / 8 - 1;
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) throws InvalidCipherTextException {
        if (this.key == null) {
            throw new IllegalStateException("NaccacheStern engine not initialised");
        }
        if (n2 > this.getInputBlockSize() + 1) {
            throw new DataLengthException("input too large for Naccache-Stern cipher.\n");
        }
        if (!this.forEncryption && n2 < this.getInputBlockSize()) {
            throw new InvalidCipherTextException("BlockLength does not match modulus for Naccache-Stern cipher.\n");
        }
        byte[] magnitude;
        if (n != 0 || n2 != array.length) {
            magnitude = new byte[n2];
            System.arraycopy(array, n, magnitude, 0, n2);
        }
        else {
            magnitude = array;
        }
        final BigInteger obj = new BigInteger(1, magnitude);
        if (this.debug) {
            System.out.println("input as BigInteger: " + obj);
        }
        byte[] array2;
        if (this.forEncryption) {
            array2 = this.encrypt(obj);
        }
        else {
            final Vector<BigInteger> vector = new Vector<BigInteger>();
            final NaccacheSternPrivateKeyParameters naccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters)this.key;
            final Vector smallPrimes = naccacheSternPrivateKeyParameters.getSmallPrimes();
            for (int i = 0; i < smallPrimes.size(); ++i) {
                final BigInteger modPow = obj.modPow(naccacheSternPrivateKeyParameters.getPhi_n().divide(smallPrimes.elementAt(i)), naccacheSternPrivateKeyParameters.getModulus());
                final Vector vector2 = this.lookup[i];
                if (this.lookup[i].size() != smallPrimes.elementAt(i).intValue()) {
                    if (this.debug) {
                        System.out.println("Prime is " + smallPrimes.elementAt(i) + ", lookup table has size " + vector2.size());
                    }
                    throw new InvalidCipherTextException("Error in lookup Array for " + smallPrimes.elementAt(i).intValue() + ": Size mismatch. Expected ArrayList with length " + smallPrimes.elementAt(i).intValue() + " but found ArrayList of length " + this.lookup[i].size());
                }
                final int index = vector2.indexOf(modPow);
                if (index == -1) {
                    if (this.debug) {
                        System.out.println("Actual prime is " + smallPrimes.elementAt(i));
                        System.out.println("Decrypted value is " + modPow);
                        System.out.println("LookupList for " + smallPrimes.elementAt(i) + " with size " + this.lookup[i].size() + " is: ");
                        for (int j = 0; j < this.lookup[i].size(); ++j) {
                            System.out.println(this.lookup[i].elementAt(j));
                        }
                    }
                    throw new InvalidCipherTextException("Lookup failed");
                }
                vector.addElement(BigInteger.valueOf(index));
            }
            array2 = chineseRemainder(vector, smallPrimes).toByteArray();
        }
        return array2;
    }
    
    public byte[] encrypt(final BigInteger exponent) {
        final byte[] byteArray = this.key.getModulus().toByteArray();
        Arrays.fill(byteArray, (byte)0);
        final byte[] byteArray2 = this.key.getG().modPow(exponent, this.key.getModulus()).toByteArray();
        System.arraycopy(byteArray2, 0, byteArray, byteArray.length - byteArray2.length, byteArray2.length);
        if (this.debug) {
            System.out.println("Encrypted value is:  " + new BigInteger(byteArray));
        }
        return byteArray;
    }
    
    public byte[] addCryptedBlocks(final byte[] magnitude, final byte[] magnitude2) throws InvalidCipherTextException {
        if (this.forEncryption) {
            if (magnitude.length > this.getOutputBlockSize() || magnitude2.length > this.getOutputBlockSize()) {
                throw new InvalidCipherTextException("BlockLength too large for simple addition.\n");
            }
        }
        else if (magnitude.length > this.getInputBlockSize() || magnitude2.length > this.getInputBlockSize()) {
            throw new InvalidCipherTextException("BlockLength too large for simple addition.\n");
        }
        final BigInteger obj = new BigInteger(1, magnitude);
        final BigInteger bigInteger = new BigInteger(1, magnitude2);
        final BigInteger mod = obj.multiply(bigInteger).mod(this.key.getModulus());
        if (this.debug) {
            System.out.println("c(m1) as BigInteger:....... " + obj);
            System.out.println("c(m2) as BigInteger:....... " + bigInteger);
            System.out.println("c(m1)*c(m2)%n = c(m1+m2)%n: " + mod);
        }
        final byte[] byteArray = this.key.getModulus().toByteArray();
        Arrays.fill(byteArray, (byte)0);
        System.arraycopy(mod.toByteArray(), 0, byteArray, byteArray.length - mod.toByteArray().length, mod.toByteArray().length);
        return byteArray;
    }
    
    public byte[] processData(final byte[] array) throws InvalidCipherTextException {
        if (this.debug) {
            System.out.println();
        }
        if (array.length > this.getInputBlockSize()) {
            final int inputBlockSize = this.getInputBlockSize();
            final int outputBlockSize = this.getOutputBlockSize();
            if (this.debug) {
                System.out.println("Input blocksize is:  " + inputBlockSize + " bytes");
                System.out.println("Output blocksize is: " + outputBlockSize + " bytes");
                System.out.println("Data has length:.... " + array.length + " bytes");
            }
            int i = 0;
            int n = 0;
            final byte[] array2 = new byte[(array.length / inputBlockSize + 1) * outputBlockSize];
            while (i < array.length) {
                byte[] array3;
                if (i + inputBlockSize < array.length) {
                    array3 = this.processBlock(array, i, inputBlockSize);
                    i += inputBlockSize;
                }
                else {
                    array3 = this.processBlock(array, i, array.length - i);
                    i += array.length - i;
                }
                if (this.debug) {
                    System.out.println("new datapos is " + i);
                }
                if (array3 == null) {
                    if (this.debug) {
                        System.out.println("cipher returned null");
                    }
                    throw new InvalidCipherTextException("cipher returned null");
                }
                System.arraycopy(array3, 0, array2, n, array3.length);
                n += array3.length;
            }
            final byte[] array4 = new byte[n];
            System.arraycopy(array2, 0, array4, 0, n);
            if (this.debug) {
                System.out.println("returning " + array4.length + " bytes");
            }
            return array4;
        }
        if (this.debug) {
            System.out.println("data size is less then input block size, processing directly");
        }
        return this.processBlock(array, 0, array.length);
    }
    
    private static BigInteger chineseRemainder(final Vector vector, final Vector vector2) {
        BigInteger bigInteger = NaccacheSternEngine.ZERO;
        BigInteger m = NaccacheSternEngine.ONE;
        for (int i = 0; i < vector2.size(); ++i) {
            m = m.multiply(vector2.elementAt(i));
        }
        for (int j = 0; j < vector2.size(); ++j) {
            final BigInteger bigInteger2 = vector2.elementAt(j);
            final BigInteger divide = m.divide(bigInteger2);
            bigInteger = bigInteger.add(divide.multiply(divide.modInverse(bigInteger2)).multiply(vector.elementAt(j)));
        }
        return bigInteger.mod(m);
    }
    
    static {
        NaccacheSternEngine.ZERO = BigInteger.valueOf(0L);
        NaccacheSternEngine.ONE = BigInteger.valueOf(1L);
    }
}
