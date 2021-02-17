// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.interfaces.DHPrivateKey;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.util.Strings;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import java.security.InvalidKeyException;
import javax.crypto.interfaces.DHPublicKey;
import java.security.Key;
import java.util.Hashtable;
import java.security.SecureRandom;
import java.math.BigInteger;
import javax.crypto.KeyAgreementSpi;

public class JCEDHKeyAgreement extends KeyAgreementSpi
{
    private BigInteger x;
    private BigInteger p;
    private BigInteger g;
    private BigInteger result;
    private SecureRandom random;
    private static final Hashtable algorithms;
    
    private byte[] bigIntToBytes(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        if (byteArray[0] == 0) {
            final byte[] array = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, array, 0, array.length);
            return array;
        }
        return byteArray;
    }
    
    @Override
    protected Key engineDoPhase(final Key key, final boolean b) throws InvalidKeyException, IllegalStateException {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        if (!(key instanceof DHPublicKey)) {
            throw new InvalidKeyException("DHKeyAgreement doPhase requires DHPublicKey");
        }
        final DHPublicKey dhPublicKey = (DHPublicKey)key;
        if (!dhPublicKey.getParams().getG().equals(this.g) || !dhPublicKey.getParams().getP().equals(this.p)) {
            throw new InvalidKeyException("DHPublicKey not for this KeyAgreement!");
        }
        if (b) {
            this.result = ((DHPublicKey)key).getY().modPow(this.x, this.p);
            return null;
        }
        this.result = ((DHPublicKey)key).getY().modPow(this.x, this.p);
        return new JCEDHPublicKey(this.result, dhPublicKey.getParams());
    }
    
    @Override
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        return this.bigIntToBytes(this.result);
    }
    
    @Override
    protected int engineGenerateSecret(final byte[] array, final int n) throws IllegalStateException, ShortBufferException {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        final byte[] bigIntToBytes = this.bigIntToBytes(this.result);
        if (array.length - n < bigIntToBytes.length) {
            throw new ShortBufferException("DHKeyAgreement - buffer too short");
        }
        System.arraycopy(bigIntToBytes, 0, array, n, bigIntToBytes.length);
        return bigIntToBytes.length;
    }
    
    @Override
    protected SecretKey engineGenerateSecret(final String s) {
        if (this.x == null) {
            throw new IllegalStateException("Diffie-Hellman not initialised.");
        }
        final String upperCase = Strings.toUpperCase(s);
        final byte[] bigIntToBytes = this.bigIntToBytes(this.result);
        if (JCEDHKeyAgreement.algorithms.containsKey(upperCase)) {
            final byte[] array = new byte[JCEDHKeyAgreement.algorithms.get(upperCase) / 8];
            System.arraycopy(bigIntToBytes, 0, array, 0, array.length);
            if (upperCase.startsWith("DES")) {
                DESParameters.setOddParity(array);
            }
            return new SecretKeySpec(array, s);
        }
        return new SecretKeySpec(bigIntToBytes, s);
    }
    
    @Override
    protected void engineInit(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom random) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (!(key instanceof DHPrivateKey)) {
            throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey for initialisation");
        }
        final DHPrivateKey dhPrivateKey = (DHPrivateKey)key;
        this.random = random;
        if (algorithmParameterSpec != null) {
            if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidAlgorithmParameterException("DHKeyAgreement only accepts DHParameterSpec");
            }
            final DHParameterSpec dhParameterSpec = (DHParameterSpec)algorithmParameterSpec;
            this.p = dhParameterSpec.getP();
            this.g = dhParameterSpec.getG();
        }
        else {
            this.p = dhPrivateKey.getParams().getP();
            this.g = dhPrivateKey.getParams().getG();
        }
        final BigInteger x = dhPrivateKey.getX();
        this.result = x;
        this.x = x;
    }
    
    @Override
    protected void engineInit(final Key key, final SecureRandom random) throws InvalidKeyException {
        if (!(key instanceof DHPrivateKey)) {
            throw new InvalidKeyException("DHKeyAgreement requires DHPrivateKey");
        }
        final DHPrivateKey dhPrivateKey = (DHPrivateKey)key;
        this.random = random;
        this.p = dhPrivateKey.getParams().getP();
        this.g = dhPrivateKey.getParams().getG();
        final BigInteger x = dhPrivateKey.getX();
        this.result = x;
        this.x = x;
    }
    
    static {
        algorithms = new Hashtable();
        final Integer value = new Integer(64);
        final Integer value2 = new Integer(192);
        final Integer value3 = new Integer(128);
        JCEDHKeyAgreement.algorithms.put("DES", value);
        JCEDHKeyAgreement.algorithms.put("DESEDE", value2);
        JCEDHKeyAgreement.algorithms.put("BLOWFISH", value3);
    }
}
