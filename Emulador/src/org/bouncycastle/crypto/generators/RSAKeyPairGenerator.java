// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import java.util.Random;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;

public class RSAKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private static final BigInteger ONE;
    private RSAKeyGenerationParameters param;
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (RSAKeyGenerationParameters)keyGenerationParameters;
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final int strength = this.param.getStrength();
        final int bitLength = (strength + 1) / 2;
        final int bitLength2 = strength - bitLength;
        final int n = strength / 3;
        final BigInteger publicExponent = this.param.getPublicExponent();
        BigInteger max;
        while (true) {
            max = new BigInteger(bitLength, 1, this.param.getRandom());
            if (max.mod(publicExponent).equals(RSAKeyPairGenerator.ONE)) {
                continue;
            }
            if (!max.isProbablePrime(this.param.getCertainty())) {
                continue;
            }
            if (publicExponent.gcd(max.subtract(RSAKeyPairGenerator.ONE)).equals(RSAKeyPairGenerator.ONE)) {
                break;
            }
        }
        BigInteger val;
        BigInteger multiply;
        while (true) {
            val = new BigInteger(bitLength2, 1, this.param.getRandom());
            if (val.subtract(max).abs().bitLength() < n) {
                continue;
            }
            if (val.mod(publicExponent).equals(RSAKeyPairGenerator.ONE)) {
                continue;
            }
            if (!val.isProbablePrime(this.param.getCertainty())) {
                continue;
            }
            if (!publicExponent.gcd(val.subtract(RSAKeyPairGenerator.ONE)).equals(RSAKeyPairGenerator.ONE)) {
                continue;
            }
            multiply = max.multiply(val);
            if (multiply.bitLength() == this.param.getStrength()) {
                break;
            }
            max = max.max(val);
        }
        if (max.compareTo(val) < 0) {
            final BigInteger bigInteger = max;
            max = val;
            val = bigInteger;
        }
        final BigInteger subtract = max.subtract(RSAKeyPairGenerator.ONE);
        final BigInteger subtract2 = val.subtract(RSAKeyPairGenerator.ONE);
        final BigInteger modInverse = publicExponent.modInverse(subtract.multiply(subtract2));
        return new AsymmetricCipherKeyPair(new RSAKeyParameters(false, multiply, publicExponent), new RSAPrivateCrtKeyParameters(multiply, publicExponent, modInverse, max, val, modInverse.remainder(subtract), modInverse.remainder(subtract2), val.modInverse(max)));
    }
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
}
