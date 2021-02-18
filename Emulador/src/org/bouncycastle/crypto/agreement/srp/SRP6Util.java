// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement.srp;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.util.BigIntegers;
import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import java.math.BigInteger;

public class SRP6Util
{
    private static BigInteger ZERO;
    private static BigInteger ONE;
    
    public static BigInteger calculateK(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2) {
        return hashPaddedPair(digest, bigInteger, bigInteger, bigInteger2);
    }
    
    public static BigInteger calculateU(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        return hashPaddedPair(digest, bigInteger, bigInteger2, bigInteger3);
    }
    
    public static BigInteger calculateX(final Digest digest, final BigInteger m, final byte[] array, final byte[] array2, final byte[] array3) {
        final byte[] magnitude = new byte[digest.getDigestSize()];
        digest.update(array2, 0, array2.length);
        digest.update((byte)58);
        digest.update(array3, 0, array3.length);
        digest.doFinal(magnitude, 0);
        digest.update(array, 0, array.length);
        digest.update(magnitude, 0, magnitude.length);
        digest.doFinal(magnitude, 0);
        return new BigInteger(1, magnitude).mod(m);
    }
    
    public static BigInteger generatePrivateValue(final Digest digest, final BigInteger bigInteger, final BigInteger bigInteger2, final SecureRandom secureRandom) {
        return BigIntegers.createRandomInRange(SRP6Util.ONE.shiftLeft(Math.min(256, bigInteger.bitLength() / 2) - 1), bigInteger.subtract(SRP6Util.ONE), secureRandom);
    }
    
    public static BigInteger validatePublicValue(final BigInteger m, BigInteger mod) throws CryptoException {
        mod = mod.mod(m);
        if (mod.equals(SRP6Util.ZERO)) {
            throw new CryptoException("Invalid public value: 0");
        }
        return mod;
    }
    
    private static BigInteger hashPaddedPair(final Digest digest, final BigInteger m, final BigInteger bigInteger, final BigInteger bigInteger2) {
        final int n = (m.bitLength() + 7) / 8;
        final byte[] padded = getPadded(bigInteger, n);
        final byte[] padded2 = getPadded(bigInteger2, n);
        digest.update(padded, 0, padded.length);
        digest.update(padded2, 0, padded2.length);
        final byte[] magnitude = new byte[digest.getDigestSize()];
        digest.doFinal(magnitude, 0);
        return new BigInteger(1, magnitude).mod(m);
    }
    
    private static byte[] getPadded(final BigInteger bigInteger, final int n) {
        byte[] unsignedByteArray = BigIntegers.asUnsignedByteArray(bigInteger);
        if (unsignedByteArray.length < n) {
            final byte[] array = new byte[n];
            System.arraycopy(unsignedByteArray, 0, array, n - unsignedByteArray.length, unsignedByteArray.length);
            unsignedByteArray = array;
        }
        return unsignedByteArray;
    }
    
    static {
        SRP6Util.ZERO = BigInteger.valueOf(0L);
        SRP6Util.ONE = BigInteger.valueOf(1L);
    }
}
