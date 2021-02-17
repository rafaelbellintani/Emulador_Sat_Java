// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.interfaces;

import java.math.BigInteger;
import java.security.PrivateKey;

public interface ElGamalPrivateKey extends ElGamalKey, PrivateKey
{
    BigInteger getX();
}
