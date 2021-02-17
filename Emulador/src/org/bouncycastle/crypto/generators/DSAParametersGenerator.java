// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.crypto.params.DSAValidationParameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.DSAParameters;
import java.math.BigInteger;
import java.security.SecureRandom;

public class DSAParametersGenerator
{
    private int L;
    private int N;
    private int certainty;
    private SecureRandom random;
    private static final BigInteger ZERO;
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    
    public void init(final int n, final int n2, final SecureRandom secureRandom) {
        this.init(n, getDefaultN(n), n2, secureRandom);
    }
    
    private void init(final int l, final int n, final int certainty, final SecureRandom random) {
        this.L = l;
        this.N = n;
        this.certainty = certainty;
        this.random = random;
    }
    
    public DSAParameters generateParameters() {
        return (this.L > 1024) ? this.generateParameters_FIPS186_3() : this.generateParameters_FIPS186_2();
    }
    
    private DSAParameters generateParameters_FIPS186_2() {
        final byte[] bytes = new byte[20];
        final byte[] array = new byte[20];
        final byte[] array2 = new byte[20];
        final byte[] magnitude = new byte[20];
        final SHA1Digest sha1Digest = new SHA1Digest();
        final int n = (this.L - 1) / 160;
        final byte[] magnitude2 = new byte[this.L / 8];
        BigInteger bigInteger = null;
        int j = 0;
        BigInteger subtract = null;
    Block_6:
        while (true) {
            this.random.nextBytes(bytes);
            hash(sha1Digest, bytes, array);
            System.arraycopy(bytes, 0, array2, 0, bytes.length);
            inc(array2);
            hash(sha1Digest, array2, array2);
            for (int i = 0; i != magnitude.length; ++i) {
                magnitude[i] = (byte)(array[i] ^ array2[i]);
            }
            final byte[] array3 = magnitude;
            final int n2 = 0;
            array3[n2] |= 0xFFFFFF80;
            final byte[] array4 = magnitude;
            final int n3 = 19;
            array4[n3] |= 0x1;
            bigInteger = new BigInteger(1, magnitude);
            if (!bigInteger.isProbablePrime(this.certainty)) {
                continue;
            }
            final byte[] clone = Arrays.clone(bytes);
            inc(clone);
            for (j = 0; j < 4096; ++j) {
                for (int k = 0; k < n; ++k) {
                    inc(clone);
                    hash(sha1Digest, clone, array);
                    System.arraycopy(array, 0, magnitude2, magnitude2.length - (k + 1) * array.length, array.length);
                }
                inc(clone);
                hash(sha1Digest, clone, array);
                System.arraycopy(array, array.length - (magnitude2.length - n * array.length), magnitude2, 0, magnitude2.length - n * array.length);
                final byte[] array5 = magnitude2;
                final int n4 = 0;
                array5[n4] |= 0xFFFFFF80;
                final BigInteger bigInteger2 = new BigInteger(1, magnitude2);
                subtract = bigInteger2.subtract(bigInteger2.mod(bigInteger.shiftLeft(1)).subtract(DSAParametersGenerator.ONE));
                if (subtract.bitLength() == this.L) {
                    if (subtract.isProbablePrime(this.certainty)) {
                        break Block_6;
                    }
                }
            }
        }
        return new DSAParameters(subtract, bigInteger, calculateGenerator_FIPS186_2(subtract, bigInteger, this.random), new DSAValidationParameters(bytes, j));
    }
    
    private static BigInteger calculateGenerator_FIPS186_2(final BigInteger m, final BigInteger val, final SecureRandom secureRandom) {
        final BigInteger divide = m.subtract(DSAParametersGenerator.ONE).divide(val);
        final BigInteger subtract = m.subtract(DSAParametersGenerator.TWO);
        BigInteger modPow;
        do {
            modPow = BigIntegers.createRandomInRange(DSAParametersGenerator.TWO, subtract, secureRandom).modPow(divide, m);
        } while (modPow.bitLength() <= 1);
        return modPow;
    }
    
    private DSAParameters generateParameters_FIPS186_3() {
        final SHA256Digest sha256Digest = new SHA256Digest();
        final int n = sha256Digest.getDigestSize() * 8;
        final byte[] bytes = new byte[this.N / 8];
        final int n2 = (this.L - 1) / n;
        final int n3 = (this.L - 1) % n;
        final byte[] array = new byte[sha256Digest.getDigestSize()];
        BigInteger subtract = null;
        int i = 0;
        BigInteger subtract2 = null;
    Block_6:
        while (true) {
            this.random.nextBytes(bytes);
            hash(sha256Digest, bytes, array);
            final BigInteger mod = new BigInteger(1, array).mod(DSAParametersGenerator.ONE.shiftLeft(this.N - 1));
            subtract = DSAParametersGenerator.ONE.shiftLeft(this.N - 1).add(mod).add(DSAParametersGenerator.ONE).subtract(mod.mod(DSAParametersGenerator.TWO));
            if (!subtract.isProbablePrime(this.certainty)) {
                continue;
            }
            final byte[] clone = Arrays.clone(bytes);
            for (final int n4 = 4 * this.L, i = 0; i < n4; ++i) {
                BigInteger bigInteger = DSAParametersGenerator.ZERO;
                for (int j = 0, n5 = 0; j <= n2; ++j, n5 += n) {
                    inc(clone);
                    hash(sha256Digest, clone, array);
                    BigInteger mod2 = new BigInteger(1, array);
                    if (j == n2) {
                        mod2 = mod2.mod(DSAParametersGenerator.ONE.shiftLeft(n3));
                    }
                    bigInteger = bigInteger.add(mod2.shiftLeft(n5));
                }
                final BigInteger add = bigInteger.add(DSAParametersGenerator.ONE.shiftLeft(this.L - 1));
                subtract2 = add.subtract(add.mod(subtract.shiftLeft(1)).subtract(DSAParametersGenerator.ONE));
                if (subtract2.bitLength() == this.L) {
                    if (subtract2.isProbablePrime(this.certainty)) {
                        break Block_6;
                    }
                }
            }
        }
        return new DSAParameters(subtract2, subtract, calculateGenerator_FIPS186_3_Unverifiable(subtract2, subtract, this.random), new DSAValidationParameters(bytes, i));
    }
    
    private static BigInteger calculateGenerator_FIPS186_3_Unverifiable(final BigInteger bigInteger, final BigInteger bigInteger2, final SecureRandom secureRandom) {
        return calculateGenerator_FIPS186_2(bigInteger, bigInteger2, secureRandom);
    }
    
    private static void hash(final Digest digest, final byte[] array, final byte[] array2) {
        digest.update(array, 0, array.length);
        digest.doFinal(array2, 0);
    }
    
    private static int getDefaultN(final int n) {
        return (n > 1024) ? 256 : 160;
    }
    
    private static void inc(final byte[] array) {
        for (int n = array.length - 1; n >= 0 && (array[n] = (byte)(array[n] + 1 & 0xFF)) == 0; --n) {}
    }
    
    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
}
