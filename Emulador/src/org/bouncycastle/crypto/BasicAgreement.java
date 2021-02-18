// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto;

import java.math.BigInteger;

public interface BasicAgreement
{
    void init(final CipherParameters p0);
    
    BigInteger calculateAgreement(final CipherParameters p0);
}
