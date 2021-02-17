// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.interfaces;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Key;

public interface IESKey extends Key
{
    PublicKey getPublic();
    
    PrivateKey getPrivate();
}
