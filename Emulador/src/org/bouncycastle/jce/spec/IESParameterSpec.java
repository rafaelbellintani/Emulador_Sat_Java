// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class IESParameterSpec implements AlgorithmParameterSpec
{
    private byte[] derivation;
    private byte[] encoding;
    private int macKeySize;
    
    public IESParameterSpec(final byte[] array, final byte[] array2, final int macKeySize) {
        System.arraycopy(array, 0, this.derivation = new byte[array.length], 0, array.length);
        System.arraycopy(array2, 0, this.encoding = new byte[array2.length], 0, array2.length);
        this.macKeySize = macKeySize;
    }
    
    public byte[] getDerivationV() {
        return this.derivation;
    }
    
    public byte[] getEncodingV() {
        return this.encoding;
    }
    
    public int getMacKeySize() {
        return this.macKeySize;
    }
}
