// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.jce.provider.util.NullDigest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.signers.DSASigner;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import java.io.IOException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.math.BigInteger;
import java.security.SignatureException;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import java.security.PrivateKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.CipherParameters;
import java.security.InvalidKeyException;
import java.security.interfaces.DSAKey;
import org.bouncycastle.jce.interfaces.GOST3410Key;
import java.security.PublicKey;
import java.security.SecureRandom;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.security.SignatureSpi;

public class JDKDSASigner extends SignatureSpi implements PKCSObjectIdentifiers, X509ObjectIdentifiers
{
    private Digest digest;
    private DSA signer;
    private SecureRandom random;
    
    protected JDKDSASigner(final Digest digest, final DSA signer) {
        this.digest = digest;
        this.signer = signer;
    }
    
    @Override
    protected void engineInitVerify(PublicKey publicKeyFromDERStream) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (publicKeyFromDERStream instanceof GOST3410Key) {
            asymmetricKeyParameter = GOST3410Util.generatePublicKeyParameter(publicKeyFromDERStream);
        }
        else if (publicKeyFromDERStream instanceof DSAKey) {
            asymmetricKeyParameter = DSAUtil.generatePublicKeyParameter(publicKeyFromDERStream);
        }
        else {
            try {
                publicKeyFromDERStream = JDKKeyFactory.createPublicKeyFromDERStream(publicKeyFromDERStream.getEncoded());
                if (!(publicKeyFromDERStream instanceof DSAKey)) {
                    throw new InvalidKeyException("can't recognise key type in DSA based signer");
                }
                asymmetricKeyParameter = DSAUtil.generatePublicKeyParameter(publicKeyFromDERStream);
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
        CipherParameters cipherParameters;
        if (privateKey instanceof GOST3410Key) {
            cipherParameters = GOST3410Util.generatePrivateKeyParameter(privateKey);
        }
        else {
            cipherParameters = DSAUtil.generatePrivateKeyParameter(privateKey);
        }
        if (this.random != null) {
            cipherParameters = new ParametersWithRandom(cipherParameters, this.random);
        }
        this.digest.reset();
        this.signer.init(true, cipherParameters);
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
            final BigInteger[] generateSignature = this.signer.generateSignature(array);
            return this.derEncode(generateSignature[0], generateSignature[1]);
        }
        catch (Exception ex) {
            throw new SignatureException(ex.toString());
        }
    }
    
    @Override
    protected boolean engineVerify(final byte[] array) throws SignatureException {
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array2, 0);
        BigInteger[] derDecode;
        try {
            derDecode = this.derDecode(array);
        }
        catch (Exception ex) {
            throw new SignatureException("error decoding signature bytes.");
        }
        return this.signer.verifySignature(array2, derDecode[0], derDecode[1]);
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
    
    private byte[] derEncode(final BigInteger bigInteger, final BigInteger bigInteger2) throws IOException {
        return new DERSequence(new DERInteger[] { new DERInteger(bigInteger), new DERInteger(bigInteger2) }).getEncoded("DER");
    }
    
    private BigInteger[] derDecode(final byte[] array) throws IOException {
        final ASN1Sequence asn1Sequence = (ASN1Sequence)ASN1Object.fromByteArray(array);
        return new BigInteger[] { ((DERInteger)asn1Sequence.getObjectAt(0)).getValue(), ((DERInteger)asn1Sequence.getObjectAt(1)).getValue() };
    }
    
    public static class dsa224 extends JDKDSASigner
    {
        public dsa224() {
            super(new SHA224Digest(), new DSASigner());
        }
    }
    
    public static class dsa256 extends JDKDSASigner
    {
        public dsa256() {
            super(new SHA256Digest(), new DSASigner());
        }
    }
    
    public static class dsa384 extends JDKDSASigner
    {
        public dsa384() {
            super(new SHA384Digest(), new DSASigner());
        }
    }
    
    public static class dsa512 extends JDKDSASigner
    {
        public dsa512() {
            super(new SHA512Digest(), new DSASigner());
        }
    }
    
    public static class noneDSA extends JDKDSASigner
    {
        public noneDSA() {
            super(new NullDigest(), new DSASigner());
        }
    }
    
    public static class stdDSA extends JDKDSASigner
    {
        public stdDSA() {
            super(new SHA1Digest(), new DSASigner());
        }
    }
}
