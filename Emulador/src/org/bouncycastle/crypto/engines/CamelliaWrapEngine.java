// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;

public class CamelliaWrapEngine extends RFC3394WrapEngine
{
    public CamelliaWrapEngine() {
        super(new CamelliaEngine());
    }
}
