// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.signers;

import org.bouncycastle.math.ec.ECAlgorithms;
import java.util.Random;
import org.bouncycastle.math.ec.ECConstants;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.DSA;

public class ECGOST3410Signer implements DSA
{
    ECKeyParameters key;
    SecureRandom random;
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (b) {
            if (cipherParameters instanceof ParametersWithRandom) {
                final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                this.random = parametersWithRandom.getRandom();
                this.key = (ECPrivateKeyParameters)parametersWithRandom.getParameters();
            }
            else {
                this.random = new SecureRandom();
                this.key = (ECPrivateKeyParameters)cipherParameters;
            }
        }
        else {
            this.key = (ECPublicKeyParameters)cipherParameters;
        }
    }
    
    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        final byte[] magnitude = new byte[array.length];
        for (int i = 0; i != magnitude.length; ++i) {
            magnitude[i] = array[magnitude.length - 1 - i];
        }
        final BigInteger val = new BigInteger(1, magnitude);
        final BigInteger n = this.key.getParameters().getN();
        BigInteger mod;
        BigInteger mod2;
        do {
            BigInteger bigInteger;
            while (true) {
                bigInteger = new BigInteger(n.bitLength(), this.random);
                if (!bigInteger.equals(ECConstants.ZERO)) {
                    mod2 = this.key.getParameters().getG().multiply(bigInteger).getX().toBigInteger().mod(n);
                    if (!mod2.equals(ECConstants.ZERO)) {
                        break;
                    }
                    continue;
                }
            }
            mod = bigInteger.multiply(val).add(((ECPrivateKeyParameters)this.key).getD().multiply(mod2)).mod(n);
        } while (mod.equals(ECConstants.ZERO));
        return new BigInteger[] { mod2, mod };
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final BigInteger bigInteger, final BigInteger bigInteger2) {
        final byte[] magnitude = new byte[array.length];
        for (int i = 0; i != magnitude.length; ++i) {
            magnitude[i] = array[magnitude.length - 1 - i];
        }
        final BigInteger bigInteger3 = new BigInteger(1, magnitude);
        final BigInteger n = this.key.getParameters().getN();
        if (bigInteger.compareTo(ECConstants.ONE) < 0 || bigInteger.compareTo(n) >= 0) {
            return false;
        }
        if (bigInteger2.compareTo(ECConstants.ONE) < 0 || bigInteger2.compareTo(n) >= 0) {
            return false;
        }
        final BigInteger modInverse = bigInteger3.modInverse(n);
        return ECAlgorithms.sumOfTwoMultiplies(this.key.getParameters().getG(), bigInteger2.multiply(modInverse).mod(n), ((ECPublicKeyParameters)this.key).getQ(), n.subtract(bigInteger).multiply(modInverse).mod(n)).getX().toBigInteger().mod(n).equals(bigInteger);
    }
}
