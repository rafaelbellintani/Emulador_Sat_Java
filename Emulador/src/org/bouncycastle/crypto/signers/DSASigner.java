// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.signers;

import org.bouncycastle.crypto.params.DSAParameters;
import java.util.Random;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.DSAKeyParameters;
import org.bouncycastle.crypto.DSA;

public class DSASigner implements DSA
{
    DSAKeyParameters key;
    SecureRandom random;
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (b) {
            if (cipherParameters instanceof ParametersWithRandom) {
                final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.key = (DSAPrivateKeyParameters)parametersWithRandom.getParameters();
            }
            else {
                this.random = new SecureRandom();
                this.key = (DSAPrivateKeyParameters)cipherParameters;
            }
        }
        else {
            this.key = (DSAPublicKeyParameters)cipherParameters;
        }
    }
    
    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        final DSAParameters parameters = this.key.getParameters();
        final BigInteger calculateE = this.calculateE(parameters.getQ(), array);
        final int bitLength = parameters.getQ().bitLength();
        BigInteger exponent;
        do {
            exponent = new BigInteger(bitLength, this.random);
        } while (exponent.compareTo(parameters.getQ()) >= 0);
        final BigInteger mod = parameters.getG().modPow(exponent, parameters.getP()).mod(parameters.getQ());
        return new BigInteger[] { mod, exponent.modInverse(parameters.getQ()).multiply(calculateE.add(((DSAPrivateKeyParameters)this.key).getX().multiply(mod))).mod(parameters.getQ()) };
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final BigInteger x, final BigInteger bigInteger) {
        final DSAParameters parameters = this.key.getParameters();
        final BigInteger calculateE = this.calculateE(parameters.getQ(), array);
        final BigInteger value = BigInteger.valueOf(0L);
        if (value.compareTo(x) >= 0 || parameters.getQ().compareTo(x) <= 0) {
            return false;
        }
        if (value.compareTo(bigInteger) >= 0 || parameters.getQ().compareTo(bigInteger) <= 0) {
            return false;
        }
        final BigInteger modInverse = bigInteger.modInverse(parameters.getQ());
        return parameters.getG().modPow(calculateE.multiply(modInverse).mod(parameters.getQ()), parameters.getP()).multiply(((DSAPublicKeyParameters)this.key).getY().modPow(x.multiply(modInverse).mod(parameters.getQ()), parameters.getP())).mod(parameters.getP()).mod(parameters.getQ()).equals(x);
    }
    
    private BigInteger calculateE(final BigInteger bigInteger, final byte[] magnitude) {
        if (bigInteger.bitLength() >= magnitude.length * 8) {
            return new BigInteger(1, magnitude);
        }
        final byte[] magnitude2 = new byte[bigInteger.bitLength() / 8];
        System.arraycopy(magnitude, 0, magnitude2, 0, magnitude2.length);
        return new BigInteger(1, magnitude2);
    }
}
