// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.interfaces;

import java.security.PublicKey;

public interface MQVPublicKey extends PublicKey
{
    PublicKey getStaticKey();
    
    PublicKey getEphemeralKey();
}
