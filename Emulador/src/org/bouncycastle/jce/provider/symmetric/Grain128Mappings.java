// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.symmetric;

import java.util.HashMap;

public class Grain128Mappings extends HashMap
{
    public Grain128Mappings() {
        this.put("Cipher.Grain128", "org.bouncycastle.jce.provider.symmetric.Grain128$Base");
        this.put("KeyGenerator.Grain128", "org.bouncycastle.jce.provider.symmetric.Grain128$KeyGen");
    }
}
