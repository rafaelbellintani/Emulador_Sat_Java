// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.symmetric;

import java.util.HashMap;

public class CAST5Mappings extends HashMap
{
    public CAST5Mappings() {
        this.put("AlgorithmParameters.CAST5", "org.bouncycastle.jce.provider.symmetric.CAST5$AlgParams");
        this.put("Alg.Alias.AlgorithmParameters.1.2.840.113533.7.66.10", "CAST5");
        this.put("AlgorithmParameterGenerator.CAST5", "org.bouncycastle.jce.provider.symmetric.CAST5$AlgParamGen");
        this.put("Alg.Alias.AlgorithmParameterGenerator.1.2.840.113533.7.66.10", "CAST5");
        this.put("Cipher.CAST5", "org.bouncycastle.jce.provider.symmetric.CAST5$ECB");
        this.put("Cipher.1.2.840.113533.7.66.10", "org.bouncycastle.jce.provider.symmetric.CAST5$CBC");
        this.put("KeyGenerator.CAST5", "org.bouncycastle.jce.provider.symmetric.CAST5$KeyGen");
        this.put("Alg.Alias.KeyGenerator.1.2.840.113533.7.66.10", "CAST5");
    }
}
