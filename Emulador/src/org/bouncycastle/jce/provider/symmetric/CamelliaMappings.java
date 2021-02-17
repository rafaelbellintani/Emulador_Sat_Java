// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.symmetric;

import org.bouncycastle.asn1.ntt.NTTObjectIdentifiers;
import java.util.HashMap;

public class CamelliaMappings extends HashMap
{
    public CamelliaMappings() {
        this.put("AlgorithmParameters.CAMELLIA", "org.bouncycastle.jce.provider.symmetric.Camellia$AlgParams");
        this.put("Alg.Alias.AlgorithmParameters." + NTTObjectIdentifiers.id_camellia128_cbc, "CAMELLIA");
        this.put("Alg.Alias.AlgorithmParameters." + NTTObjectIdentifiers.id_camellia192_cbc, "CAMELLIA");
        this.put("Alg.Alias.AlgorithmParameters." + NTTObjectIdentifiers.id_camellia256_cbc, "CAMELLIA");
        this.put("AlgorithmParameterGenerator.CAMELLIA", "org.bouncycastle.jce.provider.symmetric.Camellia$AlgParamGen");
        this.put("Alg.Alias.AlgorithmParameterGenerator." + NTTObjectIdentifiers.id_camellia128_cbc, "CAMELLIA");
        this.put("Alg.Alias.AlgorithmParameterGenerator." + NTTObjectIdentifiers.id_camellia192_cbc, "CAMELLIA");
        this.put("Alg.Alias.AlgorithmParameterGenerator." + NTTObjectIdentifiers.id_camellia256_cbc, "CAMELLIA");
        this.put("Cipher.CAMELLIA", "org.bouncycastle.jce.provider.symmetric.Camellia$ECB");
        this.put("Cipher." + NTTObjectIdentifiers.id_camellia128_cbc, "org.bouncycastle.jce.provider.symmetric.Camellia$CBC");
        this.put("Cipher." + NTTObjectIdentifiers.id_camellia192_cbc, "org.bouncycastle.jce.provider.symmetric.Camellia$CBC");
        this.put("Cipher." + NTTObjectIdentifiers.id_camellia256_cbc, "org.bouncycastle.jce.provider.symmetric.Camellia$CBC");
        this.put("Cipher.CAMELLIARFC3211WRAP", "org.bouncycastle.jce.provider.symmetric.Camellia$RFC3211Wrap");
        this.put("Cipher.CAMELLIAWRAP", "org.bouncycastle.jce.provider.symmetric.Camellia$Wrap");
        this.put("Alg.Alias.Cipher." + NTTObjectIdentifiers.id_camellia128_wrap, "CAMELLIAWRAP");
        this.put("Alg.Alias.Cipher." + NTTObjectIdentifiers.id_camellia192_wrap, "CAMELLIAWRAP");
        this.put("Alg.Alias.Cipher." + NTTObjectIdentifiers.id_camellia256_wrap, "CAMELLIAWRAP");
        this.put("KeyGenerator.CAMELLIA", "org.bouncycastle.jce.provider.symmetric.Camellia$KeyGen");
        this.put("KeyGenerator." + NTTObjectIdentifiers.id_camellia128_wrap, "org.bouncycastle.jce.provider.symmetric.Camellia$KeyGen128");
        this.put("KeyGenerator." + NTTObjectIdentifiers.id_camellia192_wrap, "org.bouncycastle.jce.provider.symmetric.Camellia$KeyGen192");
        this.put("KeyGenerator." + NTTObjectIdentifiers.id_camellia256_wrap, "org.bouncycastle.jce.provider.symmetric.Camellia$KeyGen256");
        this.put("KeyGenerator." + NTTObjectIdentifiers.id_camellia128_cbc, "org.bouncycastle.jce.provider.symmetric.Camellia$KeyGen128");
        this.put("KeyGenerator." + NTTObjectIdentifiers.id_camellia192_cbc, "org.bouncycastle.jce.provider.symmetric.Camellia$KeyGen192");
        this.put("KeyGenerator." + NTTObjectIdentifiers.id_camellia256_cbc, "org.bouncycastle.jce.provider.symmetric.Camellia$KeyGen256");
    }
}
