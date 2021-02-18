// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.util;

import java.util.Random;
import java.security.SecureRandom;
import java.math.BigInteger;

public final class BigIntegers
{
    private static final int MAX_ITERATIONS = 1000;
    private static final BigInteger ZERO;
    
    public static byte[] asUnsignedByteArray(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray[0] == 0) {
            final byte[] array = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, array, 0, array.length);
            return array;
        }
        return byteArray;
    }
    
    public static BigInteger createRandomInRange(final BigInteger val, final BigInteger bigInteger, final SecureRandom secureRandom) {
        final int compareTo = val.compareTo(bigInteger);
        if (compareTo >= 0) {
            if (compareTo > 0) {
                throw new IllegalArgumentException("'min' may not be greater than 'max'");
            }
            return val;
        }
        else {
            if (val.bitLength() > bigInteger.bitLength() / 2) {
                return createRandomInRange(BigIntegers.ZERO, bigInteger.subtract(val), secureRandom).add(val);
            }
            for (int i = 0; i < 1000; ++i) {
                final BigInteger bigInteger2 = new BigInteger(bigInteger.bitLength(), secureRandom);
                if (bigInteger2.compareTo(val) >= 0 && bigInteger2.compareTo(bigInteger) <= 0) {
                    return bigInteger2;
                }
            }
            return new BigInteger(bigInteger.subtract(val).bitLength() - 1, secureRandom).add(val);
        }
    }
    
    static {
        ZERO = BigInteger.valueOf(0L);
    }
}
