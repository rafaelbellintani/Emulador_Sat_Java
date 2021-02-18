// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;

public class AESWrapEngine extends RFC3394WrapEngine
{
    public AESWrapEngine() {
        super(new AESEngine());
    }
}
