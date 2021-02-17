// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement;

import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.BasicAgreement;

public class ECDHBasicAgreement implements BasicAgreement
{
    private ECPrivateKeyParameters key;
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        this.key = (ECPrivateKeyParameters)cipherParameters;
    }
    
    @Override
    public BigInteger calculateAgreement(final CipherParameters cipherParameters) {
        return ((ECPublicKeyParameters)cipherParameters).getQ().multiply(this.key.getD()).getX().toBigInteger();
    }
}
