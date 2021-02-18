// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.util.BigIntegers;
import java.util.Random;
import java.security.SecureRandom;
import java.math.BigInteger;

class DHParametersHelper
{
    private static final BigInteger ONE;
    private static final BigInteger TWO;
    
    static BigInteger[] generateSafePrimes(final int n, final int n2, final SecureRandom rnd) {
        final int bitLength = n - 1;
        BigInteger add;
        BigInteger bigInteger;
        do {
            bigInteger = new BigInteger(bitLength, 2, rnd);
            add = bigInteger.shiftLeft(1).add(DHParametersHelper.ONE);
        } while (!add.isProbablePrime(n2) || (n2 > 2 && !bigInteger.isProbablePrime(n2)));
        return new BigInteger[] { add, bigInteger };
    }
    
    static BigInteger selectGenerator(final BigInteger bigInteger, final BigInteger exponent, final SecureRandom secureRandom) {
        final BigInteger subtract = bigInteger.subtract(DHParametersHelper.TWO);
        BigInteger randomInRange;
        do {
            randomInRange = BigIntegers.createRandomInRange(DHParametersHelper.TWO, subtract, secureRandom);
        } while (randomInRange.modPow(DHParametersHelper.TWO, bigInteger).equals(DHParametersHelper.ONE) || randomInRange.modPow(exponent, bigInteger).equals(DHParametersHelper.ONE));
        return randomInRange;
    }
    
    static {
        ONE = BigInteger.valueOf(1L);
        TWO = BigInteger.valueOf(2L);
    }
}
