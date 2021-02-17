// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.interfaces;

import java.math.BigInteger;
import java.security.PublicKey;

public interface ElGamalPublicKey extends ElGamalKey, PublicKey
{
    BigInteger getY();
}
