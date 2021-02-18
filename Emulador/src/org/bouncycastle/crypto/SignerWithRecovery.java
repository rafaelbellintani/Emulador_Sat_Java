// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto;

public interface SignerWithRecovery extends Signer
{
    boolean hasFullMessage();
    
    byte[] getRecoveredMessage();
}
