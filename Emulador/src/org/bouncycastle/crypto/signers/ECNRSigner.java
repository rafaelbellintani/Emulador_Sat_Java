// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.signers;

import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.crypto.DataLengthException;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.DSA;

public class ECNRSigner implements DSA
{
    private boolean forSigning;
    private ECKeyParameters key;
    private SecureRandom random;
    
    @Override
    public void init(final boolean forSigning, final CipherParameters cipherParameters) {
        this.forSigning = forSigning;
        if (forSigning) {
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
    public BigInteger[] generateSignature(final byte[] magnitude) {
        if (!this.forSigning) {
            throw new IllegalStateException("not initialised for signing");
        }
        final BigInteger n = ((ECPrivateKeyParameters)this.key).getParameters().getN();
        final int bitLength = n.bitLength();
        final BigInteger val = new BigInteger(1, magnitude);
        final int bitLength2 = val.bitLength();
        final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)this.key;
        if (bitLength2 > bitLength) {
            throw new DataLengthException("input too large for ECNR key.");
        }
        BigInteger mod;
        AsymmetricCipherKeyPair generateKeyPair;
        do {
            final ECKeyPairGenerator ecKeyPairGenerator = new ECKeyPairGenerator();
            ecKeyPairGenerator.init(new ECKeyGenerationParameters(ecPrivateKeyParameters.getParameters(), this.random));
            generateKeyPair = ecKeyPairGenerator.generateKeyPair();
            mod = ((ECPublicKeyParameters)generateKeyPair.getPublic()).getQ().getX().toBigInteger().add(val).mod(n);
        } while (mod.equals(ECConstants.ZERO));
        return new BigInteger[] { mod, ((ECPrivateKeyParameters)generateKeyPair.getPrivate()).getD().subtract(mod.multiply(ecPrivateKeyParameters.getD())).mod(n) };
    }
    
    @Override
    public boolean verifySignature(final byte[] magnitude, final BigInteger bigInteger, final BigInteger bigInteger2) {
        if (this.forSigning) {
            throw new IllegalStateException("not initialised for verifying");
        }
        final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)this.key;
        final BigInteger n = ecPublicKeyParameters.getParameters().getN();
        final int bitLength = n.bitLength();
        final BigInteger x = new BigInteger(1, magnitude);
        if (x.bitLength() > bitLength) {
            throw new DataLengthException("input too large for ECNR key.");
        }
        return bigInteger.compareTo(ECConstants.ONE) >= 0 && bigInteger.compareTo(n) < 0 && bigInteger2.compareTo(ECConstants.ZERO) >= 0 && bigInteger2.compareTo(n) < 0 && bigInteger.subtract(ECAlgorithms.sumOfTwoMultiplies(ecPublicKeyParameters.getParameters().getG(), bigInteger2, ecPublicKeyParameters.getQ(), bigInteger).getX().toBigInteger()).mod(n).equals(x);
    }
}
