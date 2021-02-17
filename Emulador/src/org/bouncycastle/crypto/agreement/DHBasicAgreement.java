// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement;

import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.BasicAgreement;

public class DHBasicAgreement implements BasicAgreement
{
    private DHPrivateKeyParameters key;
    private DHParameters dhParams;
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            asymmetricKeyParameter = (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters();
        }
        else {
            asymmetricKeyParameter = (AsymmetricKeyParameter)cipherParameters;
        }
        if (!(asymmetricKeyParameter instanceof DHPrivateKeyParameters)) {
            throw new IllegalArgumentException("DHEngine expects DHPrivateKeyParameters");
        }
        this.key = (DHPrivateKeyParameters)asymmetricKeyParameter;
        this.dhParams = this.key.getParameters();
    }
    
    @Override
    public BigInteger calculateAgreement(final CipherParameters cipherParameters) {
        final DHPublicKeyParameters dhPublicKeyParameters = (DHPublicKeyParameters)cipherParameters;
        if (!dhPublicKeyParameters.getParameters().equals(this.dhParams)) {
            throw new IllegalArgumentException("Diffie-Hellman public key has wrong parameters.");
        }
        return dhPublicKeyParameters.getY().modPow(this.key.getX(), this.dhParams.getP());
    }
}
