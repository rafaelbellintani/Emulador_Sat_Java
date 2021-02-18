// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.asn1.DERObjectIdentifier;
import javax.crypto.interfaces.PBEKey;

public class JCEPBEKey implements PBEKey
{
    String algorithm;
    DERObjectIdentifier oid;
    int type;
    int digest;
    int keySize;
    int ivSize;
    CipherParameters param;
    PBEKeySpec pbeKeySpec;
    boolean tryWrong;
    
    public JCEPBEKey(final String algorithm, final DERObjectIdentifier oid, final int type, final int digest, final int keySize, final int ivSize, final PBEKeySpec pbeKeySpec, final CipherParameters param) {
        this.tryWrong = false;
        this.algorithm = algorithm;
        this.oid = oid;
        this.type = type;
        this.digest = digest;
        this.keySize = keySize;
        this.ivSize = ivSize;
        this.pbeKeySpec = pbeKeySpec;
        this.param = param;
    }
    
    @Override
    public String getAlgorithm() {
        return this.algorithm;
    }
    
    @Override
    public String getFormat() {
        return "RAW";
    }
    
    @Override
    public byte[] getEncoded() {
        if (this.param != null) {
            KeyParameter keyParameter;
            if (this.param instanceof ParametersWithIV) {
                keyParameter = (KeyParameter)((ParametersWithIV)this.param).getParameters();
            }
            else {
                keyParameter = (KeyParameter)this.param;
            }
            return keyParameter.getKey();
        }
        if (this.type == 2) {
            return PBEParametersGenerator.PKCS12PasswordToBytes(this.pbeKeySpec.getPassword());
        }
        return PBEParametersGenerator.PKCS5PasswordToBytes(this.pbeKeySpec.getPassword());
    }
    
    int getType() {
        return this.type;
    }
    
    int getDigest() {
        return this.digest;
    }
    
    int getKeySize() {
        return this.keySize;
    }
    
    int getIvSize() {
        return this.ivSize;
    }
    
    CipherParameters getParam() {
        return this.param;
    }
    
    @Override
    public char[] getPassword() {
        return this.pbeKeySpec.getPassword();
    }
    
    @Override
    public byte[] getSalt() {
        return this.pbeKeySpec.getSalt();
    }
    
    @Override
    public int getIterationCount() {
        return this.pbeKeySpec.getIterationCount();
    }
    
    public DERObjectIdentifier getOID() {
        return this.oid;
    }
    
    void setTryWrongPKCS12Zero(final boolean tryWrong) {
        this.tryWrong = tryWrong;
    }
    
    boolean shouldTryWrongPKCS12() {
        return this.tryWrong;
    }
}
