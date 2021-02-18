// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import org.bouncycastle.crypto.engines.GOST28147Engine;
import java.security.spec.AlgorithmParameterSpec;

public class GOST28147ParameterSpec implements AlgorithmParameterSpec
{
    private byte[] iv;
    private byte[] sBox;
    
    public GOST28147ParameterSpec(final byte[] array) {
        this.iv = null;
        this.sBox = null;
        System.arraycopy(array, 0, this.sBox = new byte[array.length], 0, array.length);
    }
    
    public GOST28147ParameterSpec(final byte[] array, final byte[] array2) {
        this(array);
        System.arraycopy(array2, 0, this.iv = new byte[array2.length], 0, array2.length);
    }
    
    public GOST28147ParameterSpec(final String s) {
        this.iv = null;
        this.sBox = null;
        this.sBox = GOST28147Engine.getSBox(s);
    }
    
    public GOST28147ParameterSpec(final String s, final byte[] array) {
        this(s);
        System.arraycopy(array, 0, this.iv = new byte[array.length], 0, array.length);
    }
    
    public byte[] getSbox() {
        return this.sBox;
    }
    
    public byte[] getIV() {
        if (this.iv == null) {
            return null;
        }
        final byte[] array = new byte[this.iv.length];
        System.arraycopy(this.iv, 0, array, 0, array.length);
        return array;
    }
}
