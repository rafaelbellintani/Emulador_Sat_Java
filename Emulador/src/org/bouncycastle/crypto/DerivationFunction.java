// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto;

public interface DerivationFunction
{
    void init(final DerivationParameters p0);
    
    Digest getDigest();
    
    int generateBytes(final byte[] p0, final int p1, final int p2) throws DataLengthException, IllegalArgumentException;
}
