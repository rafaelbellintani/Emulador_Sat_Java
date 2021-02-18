// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class ElGamalGenParameterSpec implements AlgorithmParameterSpec
{
    private int primeSize;
    
    public ElGamalGenParameterSpec(final int primeSize) {
        this.primeSize = primeSize;
    }
    
    public int getPrimeSize() {
        return this.primeSize;
    }
}
