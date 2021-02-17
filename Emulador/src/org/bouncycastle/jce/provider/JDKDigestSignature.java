// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.jce.provider.util.NullDigest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSABlindedEngine;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.io.IOException;
import org.bouncycastle.asn1.x509.DigestInfo;
import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.PrivateKey;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.CipherParameters;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPublicKey;
import java.security.PublicKey;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.Digest;
import java.security.SignatureSpi;

public class JDKDigestSignature extends SignatureSpi
{
    private Digest digest;
    private AsymmetricBlockCipher cipher;
    private AlgorithmIdentifier algId;
    
    protected JDKDigestSignature(final Digest digest, final AsymmetricBlockCipher cipher) {
        this.digest = digest;
        this.cipher = cipher;
        this.algId = null;
    }
    
    protected JDKDigestSignature(final DERObjectIdentifier derObjectIdentifier, final Digest digest, final AsymmetricBlockCipher cipher) {
        this.digest = digest;
        this.cipher = cipher;
        this.algId = new AlgorithmIdentifier(derObjectIdentifier, DERNull.INSTANCE);
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        if (!(publicKey instanceof RSAPublicKey)) {
            throw new InvalidKeyException("Supplied key (" + this.getType(publicKey) + ") is not a RSAPublicKey instance");
        }
        final RSAKeyParameters generatePublicKeyParameter = RSAUtil.generatePublicKeyParameter((RSAPublicKey)publicKey);
        this.digest.reset();
        this.cipher.init(false, generatePublicKeyParameter);
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        if (!(privateKey instanceof RSAPrivateKey)) {
            throw new InvalidKeyException("Supplied key (" + this.getType(privateKey) + ") is not a RSAPrivateKey instance");
        }
        final RSAKeyParameters generatePrivateKeyParameter = RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey);
        this.digest.reset();
        this.cipher.init(true, generatePrivateKeyParameter);
    }
    
    private String getType(final Object o) {
        if (o == null) {
            return null;
        }
        return o.getClass().getName();
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
            final byte[] derEncode = this.derEncode(array);
            return this.cipher.processBlock(derEncode, 0, derEncode.length);
        }
        catch (ArrayIndexOutOfBoundsException ex2) {
            throw new SignatureException("key too small for signature type");
        }
        catch (Exception ex) {
            throw new SignatureException(ex.toString());
        }
    }
    
    @Override
    protected boolean engineVerify(final byte[] array) throws SignatureException {
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array2, 0);
        byte[] processBlock;
        byte[] derEncode;
        try {
            processBlock = this.cipher.processBlock(array, 0, array.length);
            derEncode = this.derEncode(array2);
        }
        catch (Exception ex) {
            return false;
        }
        if (processBlock.length == derEncode.length) {
            for (int i = 0; i < processBlock.length; ++i) {
                if (processBlock[i] != derEncode[i]) {
                    return false;
                }
            }
        }
        else {
            if (processBlock.length != derEncode.length - 2) {
                return false;
            }
            final int n = processBlock.length - array2.length - 2;
            final int n2 = derEncode.length - array2.length - 2;
            final byte[] array3 = derEncode;
            final int n3 = 1;
            array3[n3] -= 2;
            final byte[] array4 = derEncode;
            final int n4 = 3;
            array4[n4] -= 2;
            for (int j = 0; j < array2.length; ++j) {
                if (processBlock[n + j] != derEncode[n2 + j]) {
                    return false;
                }
            }
            for (int k = 0; k < n; ++k) {
                if (processBlock[k] != derEncode[k]) {
                    return false;
                }
            }
        }
        return true;
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
        return null;
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }
    
    private byte[] derEncode(final byte[] array) throws IOException {
        if (this.algId == null) {
            return array;
        }
        return new DigestInfo(this.algId, array).getEncoded("DER");
    }
    
    public static class MD2WithRSAEncryption extends JDKDigestSignature
    {
        public MD2WithRSAEncryption() {
            super(PKCSObjectIdentifiers.md2, new MD2Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class MD4WithRSAEncryption extends JDKDigestSignature
    {
        public MD4WithRSAEncryption() {
            super(PKCSObjectIdentifiers.md4, new MD4Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class MD5WithRSAEncryption extends JDKDigestSignature
    {
        public MD5WithRSAEncryption() {
            super(PKCSObjectIdentifiers.md5, new MD5Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class RIPEMD128WithRSAEncryption extends JDKDigestSignature
    {
        public RIPEMD128WithRSAEncryption() {
            super(TeleTrusTObjectIdentifiers.ripemd128, new RIPEMD128Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class RIPEMD160WithRSAEncryption extends JDKDigestSignature
    {
        public RIPEMD160WithRSAEncryption() {
            super(TeleTrusTObjectIdentifiers.ripemd160, new RIPEMD160Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class RIPEMD256WithRSAEncryption extends JDKDigestSignature
    {
        public RIPEMD256WithRSAEncryption() {
            super(TeleTrusTObjectIdentifiers.ripemd256, new RIPEMD256Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA1WithRSAEncryption extends JDKDigestSignature
    {
        public SHA1WithRSAEncryption() {
            super(X509ObjectIdentifiers.id_SHA1, new SHA1Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA224WithRSAEncryption extends JDKDigestSignature
    {
        public SHA224WithRSAEncryption() {
            super(NISTObjectIdentifiers.id_sha224, new SHA224Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA256WithRSAEncryption extends JDKDigestSignature
    {
        public SHA256WithRSAEncryption() {
            super(NISTObjectIdentifiers.id_sha256, new SHA256Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA384WithRSAEncryption extends JDKDigestSignature
    {
        public SHA384WithRSAEncryption() {
            super(NISTObjectIdentifiers.id_sha384, new SHA384Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class SHA512WithRSAEncryption extends JDKDigestSignature
    {
        public SHA512WithRSAEncryption() {
            super(NISTObjectIdentifiers.id_sha512, new SHA512Digest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
    
    public static class noneRSA extends JDKDigestSignature
    {
        public noneRSA() {
            super(new NullDigest(), new PKCS1Encoding(new RSABlindedEngine()));
        }
    }
}
