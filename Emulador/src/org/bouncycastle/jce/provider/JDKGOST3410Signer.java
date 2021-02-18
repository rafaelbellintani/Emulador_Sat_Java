// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.signers.GOST3410Signer;
import org.bouncycastle.crypto.signers.ECGOST3410Signer;
import org.bouncycastle.crypto.digests.GOST3411Digest;
import java.security.spec.AlgorithmParameterSpec;
import java.math.BigInteger;
import java.security.SignatureException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jce.interfaces.ECKey;
import java.security.PrivateKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.CipherParameters;
import java.security.InvalidKeyException;
import org.bouncycastle.jce.interfaces.GOST3410Key;
import org.bouncycastle.jce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.security.SignatureSpi;

public class JDKGOST3410Signer extends SignatureSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private Digest digest;
    private DSA signer;
    private SecureRandom random;
    
    protected JDKGOST3410Signer(final Digest digest, final DSA signer) {
        this.digest = digest;
        this.signer = signer;
    }
    
    @Override
    protected void engineInitVerify(PublicKey publicKeyFromDERStream) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (publicKeyFromDERStream instanceof ECPublicKey) {
            asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(publicKeyFromDERStream);
        }
        else if (publicKeyFromDERStream instanceof GOST3410Key) {
            asymmetricKeyParameter = GOST3410Util.generatePublicKeyParameter(publicKeyFromDERStream);
        }
        else {
            try {
                publicKeyFromDERStream = JDKKeyFactory.createPublicKeyFromDERStream(publicKeyFromDERStream.getEncoded());
                if (!(publicKeyFromDERStream instanceof ECPublicKey)) {
                    throw new InvalidKeyException("can't recognise key type in DSA based signer");
                }
                asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(publicKeyFromDERStream);
            }
            catch (Exception ex) {
                throw new InvalidKeyException("can't recognise key type in DSA based signer");
            }
        }
        this.digest.reset();
        this.signer.init(false, asymmetricKeyParameter);
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey, final SecureRandom random) throws InvalidKeyException {
        this.random = random;
        this.engineInitSign(privateKey);
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (privateKey instanceof ECKey) {
            asymmetricKeyParameter = ECUtil.generatePrivateKeyParameter(privateKey);
        }
        else {
            asymmetricKeyParameter = GOST3410Util.generatePrivateKeyParameter(privateKey);
        }
        this.digest.reset();
        if (this.random != null) {
            this.signer.init(true, new ParametersWithRandom(asymmetricKeyParameter, this.random));
        }
        else {
            this.signer.init(true, asymmetricKeyParameter);
        }
    }
    
    @Override
    protected void engineUpdate(final byte b) throws SignatureException {
        this.digest.update(b);
    }
    
    @Override
    protected void engineUpdate(final byte[] array, final int n, final int n2) throws SignatureException {
        this.digest.update(array, n, n2);
    }
    
    @Override
    protected byte[] engineSign() throws SignatureException {
        final byte[] array = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array, 0);
        try {
            final byte[] array2 = new byte[64];
            final BigInteger[] generateSignature = this.signer.generateSignature(array);
            final byte[] byteArray = generateSignature[0].toByteArray();
            final byte[] byteArray2 = generateSignature[1].toByteArray();
            if (byteArray2[0] != 0) {
                System.arraycopy(byteArray2, 0, array2, 32 - byteArray2.length, byteArray2.length);
            }
            else {
                System.arraycopy(byteArray2, 1, array2, 32 - (byteArray2.length - 1), byteArray2.length - 1);
            }
            if (byteArray[0] != 0) {
                System.arraycopy(byteArray, 0, array2, 64 - byteArray.length, byteArray.length);
            }
            else {
                System.arraycopy(byteArray, 1, array2, 64 - (byteArray.length - 1), byteArray.length - 1);
            }
            return array2;
        }
        catch (Exception ex) {
            throw new SignatureException(ex.toString());
        }
    }
    
    @Override
    protected boolean engineVerify(final byte[] array) throws SignatureException {
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array2, 0);
        BigInteger[] array3;
        try {
            final byte[] magnitude = new byte[32];
            final byte[] magnitude2 = new byte[32];
            System.arraycopy(array, 0, magnitude2, 0, 32);
            System.arraycopy(array, 32, magnitude, 0, 32);
            array3 = new BigInteger[] { new BigInteger(1, magnitude), new BigInteger(1, magnitude2) };
        }
        catch (Exception ex) {
            throw new SignatureException("error decoding signature bytes.");
        }
        return this.signer.verifySignature(array2, array3[0], array3[1]);
    }
    
    @Override
    protected void engineSetParameter(final AlgorithmParameterSpec algorithmParameterSpec) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    @Deprecated
    protected void engineSetParameter(final String s, final Object o) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    @Deprecated
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    public static class ecgost3410 extends JDKGOST3410Signer
    {
        public ecgost3410() {
            super(new GOST3411Digest(), new ECGOST3410Signer());
        }
    }
    
    public static class gost3410 extends JDKGOST3410Signer
    {
        public gost3410() {
            super(new GOST3411Digest(), new GOST3410Signer());
        }
    }
}
