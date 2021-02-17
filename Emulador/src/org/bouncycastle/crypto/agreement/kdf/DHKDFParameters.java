// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement.kdf;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.crypto.DerivationParameters;

public class DHKDFParameters implements DerivationParameters
{
    private final DERObjectIdentifier algorithm;
    private final int keySize;
    private final byte[] z;
    private final byte[] extraInfo;
    
    public DHKDFParameters(final DERObjectIdentifier algorithm, final int keySize, final byte[] z) {
        this.algorithm = algorithm;
        this.keySize = keySize;
        this.z = z;
        this.extraInfo = null;
    }
    
    public DHKDFParameters(final DERObjectIdentifier algorithm, final int keySize, final byte[] z, final byte[] extraInfo) {
        this.algorithm = algorithm;
        this.keySize = keySize;
        this.z = z;
        this.extraInfo = extraInfo;
    }
    
    public DERObjectIdentifier getAlgorithm() {
        return this.algorithm;
    }
    
    public int getKeySize() {
        return this.keySize;
    }
    
    public byte[] getZ() {
        return this.z;
    }
    
    public byte[] getExtraInfo() {
        return this.extraInfo;
    }
}
