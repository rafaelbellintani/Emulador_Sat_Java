// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.openssl;

import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.generators.OpenSSLPBEParametersGenerator;
import java.io.IOException;
import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.IvParameterSpec;

final class PEMUtilities
{
    static byte[] crypt(final boolean b, final String provider, final byte[] input, final char[] array, final String s, final byte[] array2) throws IOException {
        AlgorithmParameterSpec params = new IvParameterSpec(array2);
        String str = "CBC";
        String str2 = "PKCS5Padding";
        if (s.endsWith("-CFB")) {
            str = "CFB";
            str2 = "NoPadding";
        }
        if (s.endsWith("-ECB") || "DES-EDE".equals(s) || "DES-EDE3".equals(s)) {
            str = "ECB";
            params = null;
        }
        if (s.endsWith("-OFB")) {
            str = "OFB";
            str2 = "NoPadding";
        }
        String str3;
        SecretKey secretKey;
        if (s.startsWith("DES-EDE")) {
            str3 = "DESede";
            secretKey = getKey(array, str3, 24, array2, !s.startsWith("DES-EDE3"));
        }
        else if (s.startsWith("DES-")) {
            str3 = "DES";
            secretKey = getKey(array, str3, 8, array2);
        }
        else if (s.startsWith("BF-")) {
            str3 = "Blowfish";
            secretKey = getKey(array, str3, 16, array2);
        }
        else if (s.startsWith("RC2-")) {
            str3 = "RC2";
            int n = 128;
            if (s.startsWith("RC2-40-")) {
                n = 40;
            }
            else if (s.startsWith("RC2-64-")) {
                n = 64;
            }
            secretKey = getKey(array, str3, n / 8, array2);
            if (params == null) {
                params = new RC2ParameterSpec(n);
            }
            else {
                params = new RC2ParameterSpec(n, array2);
            }
        }
        else {
            if (!s.startsWith("AES-")) {
                throw new EncryptionException("unknown encryption with private key");
            }
            str3 = "AES";
            byte[] array3 = array2;
            if (array3.length > 8) {
                array3 = new byte[8];
                System.arraycopy(array2, 0, array3, 0, 8);
            }
            int n2;
            if (s.startsWith("AES-128-")) {
                n2 = 128;
            }
            else if (s.startsWith("AES-192-")) {
                n2 = 192;
            }
            else {
                if (!s.startsWith("AES-256-")) {
                    throw new EncryptionException("unknown AES encryption with private key");
                }
                n2 = 256;
            }
            secretKey = getKey(array, "AES", n2 / 8, array3);
        }
        final String string = str3 + "/" + str + "/" + str2;
        try {
            final Cipher instance = Cipher.getInstance(string, provider);
            final int n3 = b ? 1 : 2;
            if (params == null) {
                instance.init(n3, secretKey);
            }
            else {
                instance.init(n3, secretKey, params);
            }
            return instance.doFinal(input);
        }
        catch (Exception ex) {
            throw new EncryptionException("exception using cipher - please check password and data.", ex);
        }
    }
    
    private static SecretKey getKey(final char[] array, final String s, final int n, final byte[] array2) {
        return getKey(array, s, n, array2, false);
    }
    
    private static SecretKey getKey(final char[] array, final String algorithm, final int n, final byte[] array2, final boolean b) {
        final OpenSSLPBEParametersGenerator openSSLPBEParametersGenerator = new OpenSSLPBEParametersGenerator();
        openSSLPBEParametersGenerator.init(PBEParametersGenerator.PKCS5PasswordToBytes(array), array2);
        final byte[] key = ((KeyParameter)openSSLPBEParametersGenerator.generateDerivedParameters(n * 8)).getKey();
        if (b && key.length >= 24) {
            System.arraycopy(key, 0, key, 16, 8);
        }
        return new SecretKeySpec(key, algorithm);
    }
}
