// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.math.ec;

import java.math.BigInteger;

interface ECMultiplier
{
    ECPoint multiply(final ECPoint p0, final BigInteger p1, final PreCompInfo p2);
}
