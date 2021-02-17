// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement;

import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.BasicAgreement;

public class ECDHCBasicAgreement implements BasicAgreement
{
    ECPrivateKeyParameters key;
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.key = (ECPrivateKeyParameters)cipherParameters;
    }
    
    @Override
    public BigInteger calculateAgreement(final CipherParameters cipherParameters) {
        final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)cipherParameters;
        return ecPublicKeyParameters.getQ().multiply(ecPublicKeyParameters.getParameters().getH().multiply(this.key.getD())).getX().toBigInteger();
    }
}
