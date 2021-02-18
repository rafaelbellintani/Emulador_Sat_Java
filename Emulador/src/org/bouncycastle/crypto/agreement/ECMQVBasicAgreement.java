// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement;

import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.MQVPublicParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.MQVPrivateParameters;
import org.bouncycastle.crypto.BasicAgreement;

public class ECMQVBasicAgreement implements BasicAgreement
{
    MQVPrivateParameters privParams;
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.privParams = (MQVPrivateParameters)cipherParameters;
    }
    
    @Override
    public BigInteger calculateAgreement(final CipherParameters cipherParameters) {
        final MQVPublicParameters mqvPublicParameters = (MQVPublicParameters)cipherParameters;
        final ECPrivateKeyParameters staticPrivateKey = this.privParams.getStaticPrivateKey();
        return this.calculateMqvAgreement(staticPrivateKey.getParameters(), staticPrivateKey, this.privParams.getEphemeralPrivateKey(), this.privParams.getEphemeralPublicKey(), mqvPublicParameters.getStaticPublicKey(), mqvPublicParameters.getEphemeralPublicKey()).getX().toBigInteger();
    }
    
    private ECPoint calculateMqvAgreement(final ECDomainParameters ecDomainParameters, final ECPrivateKeyParameters ecPrivateKeyParameters, final ECPrivateKeyParameters ecPrivateKeyParameters2, final ECPublicKeyParameters ecPublicKeyParameters, final ECPublicKeyParameters ecPublicKeyParameters2, final ECPublicKeyParameters ecPublicKeyParameters3) {
        final BigInteger n = ecDomainParameters.getN();
        final int bit = (n.bitLength() + 1) / 2;
        final BigInteger shiftLeft = ECConstants.ONE.shiftLeft(bit);
        ECPoint ecPoint;
        if (ecPublicKeyParameters == null) {
            ecPoint = ecDomainParameters.getG().multiply(ecPrivateKeyParameters2.getD());
        }
        else {
            ecPoint = ecPublicKeyParameters.getQ();
        }
        final BigInteger mod = ecPrivateKeyParameters.getD().multiply(ecPoint.getX().toBigInteger().mod(shiftLeft).setBit(bit)).mod(n).add(ecPrivateKeyParameters2.getD()).mod(n);
        final BigInteger setBit = ecPublicKeyParameters3.getQ().getX().toBigInteger().mod(shiftLeft).setBit(bit);
        final BigInteger mod2 = ecDomainParameters.getH().multiply(mod).mod(n);
        final ECPoint sumOfTwoMultiplies = ECAlgorithms.sumOfTwoMultiplies(ecPublicKeyParameters2.getQ(), setBit.multiply(mod2).mod(n), ecPublicKeyParameters3.getQ(), mod2);
        if (sumOfTwoMultiplies.isInfinity()) {
            throw new IllegalStateException("Infinity is not a valid agreement value for MQV");
        }
        return sumOfTwoMultiplies;
    }
}
