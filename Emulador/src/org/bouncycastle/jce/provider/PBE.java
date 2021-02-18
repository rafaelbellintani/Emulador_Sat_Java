// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.crypto.CipherParameters;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle.crypto.generators.OpenSSLPBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.TigerDigest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;

public interface PBE
{
    public static final int MD5 = 0;
    public static final int SHA1 = 1;
    public static final int RIPEMD160 = 2;
    public static final int TIGER = 3;
    public static final int SHA256 = 4;
    public static final int MD2 = 5;
    public static final int PKCS5S1 = 0;
    public static final int PKCS5S2 = 1;
    public static final int PKCS12 = 2;
    public static final int OPENSSL = 3;
    
    public static class Util
    {
        private static PBEParametersGenerator makePBEGenerator(final int n, final int n2) {
            PBEParametersGenerator pbeParametersGenerator = null;
            if (n == 0) {
                switch (n2) {
                    case 5: {
                        pbeParametersGenerator = new PKCS5S1ParametersGenerator(new MD2Digest());
                        break;
                    }
                    case 0: {
                        pbeParametersGenerator = new PKCS5S1ParametersGenerator(new MD5Digest());
                        break;
                    }
                    case 1: {
                        pbeParametersGenerator = new PKCS5S1ParametersGenerator(new SHA1Digest());
                        break;
                    }
                    default: {
                        throw new IllegalStateException("PKCS5 scheme 1 only supports MD2, MD5 and SHA1.");
                    }
                }
            }
            else if (n == 1) {
                pbeParametersGenerator = new PKCS5S2ParametersGenerator();
            }
            else if (n == 2) {
                switch (n2) {
                    case 5: {
                        pbeParametersGenerator = new PKCS12ParametersGenerator(new MD2Digest());
                        break;
                    }
                    case 0: {
                        pbeParametersGenerator = new PKCS12ParametersGenerator(new MD5Digest());
                        break;
                    }
                    case 1: {
                        pbeParametersGenerator = new PKCS12ParametersGenerator(new SHA1Digest());
                        break;
                    }
                    case 2: {
                        pbeParametersGenerator = new PKCS12ParametersGenerator(new RIPEMD160Digest());
                        break;
                    }
                    case 3: {
                        pbeParametersGenerator = new PKCS12ParametersGenerator(new TigerDigest());
                        break;
                    }
                    case 4: {
                        pbeParametersGenerator = new PKCS12ParametersGenerator(new SHA256Digest());
                        break;
                    }
                    default: {
                        throw new IllegalStateException("unknown digest scheme for PBE encryption.");
                    }
                }
            }
            else {
                pbeParametersGenerator = new OpenSSLPBEParametersGenerator();
            }
            return pbeParametersGenerator;
        }
        
        static CipherParameters makePBEParameters(final JCEPBEKey jcepbeKey, final AlgorithmParameterSpec algorithmParameterSpec, final String s) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            final PBEParametersGenerator pbeGenerator = makePBEGenerator(jcepbeKey.getType(), jcepbeKey.getDigest());
            byte[] encoded = jcepbeKey.getEncoded();
            if (jcepbeKey.shouldTryWrongPKCS12()) {
                encoded = new byte[2];
            }
            pbeGenerator.init(encoded, pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
            CipherParameters cipherParameters;
            if (jcepbeKey.getIvSize() != 0) {
                cipherParameters = pbeGenerator.generateDerivedParameters(jcepbeKey.getKeySize(), jcepbeKey.getIvSize());
            }
            else {
                cipherParameters = pbeGenerator.generateDerivedParameters(jcepbeKey.getKeySize());
            }
            if (s.startsWith("DES")) {
                if (cipherParameters instanceof ParametersWithIV) {
                    DESParameters.setOddParity(((KeyParameter)((ParametersWithIV)cipherParameters).getParameters()).getKey());
                }
                else {
                    DESParameters.setOddParity(((KeyParameter)cipherParameters).getKey());
                }
            }
            for (int i = 0; i != encoded.length; ++i) {
                encoded[i] = 0;
            }
            return cipherParameters;
        }
        
        static CipherParameters makePBEMacParameters(final JCEPBEKey jcepbeKey, final AlgorithmParameterSpec algorithmParameterSpec) {
            if (algorithmParameterSpec == null || !(algorithmParameterSpec instanceof PBEParameterSpec)) {
                throw new IllegalArgumentException("Need a PBEParameter spec with a PBE key.");
            }
            final PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
            final PBEParametersGenerator pbeGenerator = makePBEGenerator(jcepbeKey.getType(), jcepbeKey.getDigest());
            byte[] encoded = jcepbeKey.getEncoded();
            if (jcepbeKey.shouldTryWrongPKCS12()) {
                encoded = new byte[2];
            }
            pbeGenerator.init(encoded, pbeParameterSpec.getSalt(), pbeParameterSpec.getIterationCount());
            final CipherParameters generateDerivedMacParameters = pbeGenerator.generateDerivedMacParameters(jcepbeKey.getKeySize());
            for (int i = 0; i != encoded.length; ++i) {
                encoded[i] = 0;
            }
            return generateDerivedMacParameters;
        }
        
        static CipherParameters makePBEParameters(final PBEKeySpec pbeKeySpec, final int n, final int n2, final int n3, final int n4) {
            final PBEParametersGenerator pbeGenerator = makePBEGenerator(n, n2);
            byte[] array;
            if (n == 2) {
                array = PBEParametersGenerator.PKCS12PasswordToBytes(pbeKeySpec.getPassword());
            }
            else {
                array = PBEParametersGenerator.PKCS5PasswordToBytes(pbeKeySpec.getPassword());
            }
            pbeGenerator.init(array, pbeKeySpec.getSalt(), pbeKeySpec.getIterationCount());
            CipherParameters cipherParameters;
            if (n4 != 0) {
                cipherParameters = pbeGenerator.generateDerivedParameters(n3, n4);
            }
            else {
                cipherParameters = pbeGenerator.generateDerivedParameters(n3);
            }
            for (int i = 0; i != array.length; ++i) {
                array[i] = 0;
            }
            return cipherParameters;
        }
        
        static CipherParameters makePBEMacParameters(final PBEKeySpec pbeKeySpec, final int n, final int n2, final int n3) {
            final PBEParametersGenerator pbeGenerator = makePBEGenerator(n, n2);
            byte[] array;
            if (n == 2) {
                array = PBEParametersGenerator.PKCS12PasswordToBytes(pbeKeySpec.getPassword());
            }
            else {
                array = PBEParametersGenerator.PKCS5PasswordToBytes(pbeKeySpec.getPassword());
            }
            pbeGenerator.init(array, pbeKeySpec.getSalt(), pbeKeySpec.getIterationCount());
            final CipherParameters generateDerivedMacParameters = pbeGenerator.generateDerivedMacParameters(n3);
            for (int i = 0; i != array.length; ++i) {
                array[i] = 0;
            }
            return generateDerivedMacParameters;
        }
    }
}
