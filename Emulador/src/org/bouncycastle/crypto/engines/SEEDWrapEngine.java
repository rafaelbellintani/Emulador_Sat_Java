// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;

public class SEEDWrapEngine extends RFC3394WrapEngine
{
    public SEEDWrapEngine() {
        super(new SEEDEngine());
    }
}
