// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.symmetric;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.jce.provider.JCEKeyGenerator;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.Grainv1Engine;
import org.bouncycastle.jce.provider.JCEStreamCipher;

public final class Grainv1
{
    private Grainv1() {
    }
    
    public static class Base extends JCEStreamCipher
    {
        public Base() {
            super(new Grainv1Engine(), 8);
        }
    }
    
    public static class KeyGen extends JCEKeyGenerator
    {
        public KeyGen() {
            super("Grainv1", 80, new CipherKeyGenerator());
        }
    }
}
