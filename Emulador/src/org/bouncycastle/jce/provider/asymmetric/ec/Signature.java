// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider.asymmetric.ec;

import org.bouncycastle.crypto.signers.ECNRSigner;
import org.bouncycastle.jce.provider.util.NullDigest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jce.interfaces.ECKey;
import java.security.SecureRandom;
import java.security.PrivateKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.CipherParameters;
import java.security.InvalidKeyException;
import org.bouncycastle.jce.provider.JDKKeyFactory;
import java.security.interfaces.ECPublicKey;
import java.security.PublicKey;
import org.bouncycastle.jce.provider.DSAEncoder;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.jce.provider.DSABase;

public class Signature extends DSABase
{
    Signature(final Digest digest, final DSA dsa, final DSAEncoder dsaEncoder) {
        super(digest, dsa, dsaEncoder);
    }
    
    @Override
    protected void engineInitVerify(PublicKey publicKeyFromDERStream) throws InvalidKeyException {
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (publicKeyFromDERStream instanceof ECPublicKey) {
            asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(publicKeyFromDERStream);
        }
        else {
            try {
                publicKeyFromDERStream = JDKKeyFactory.createPublicKeyFromDERStream(publicKeyFromDERStream.getEncoded());
                if (!(publicKeyFromDERStream instanceof ECPublicKey)) {
                    throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
                }
                asymmetricKeyParameter = ECUtil.generatePublicKeyParameter(publicKeyFromDERStream);
            }
            catch (Exception ex) {
                throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
            }
        }
        this.digest.reset();
        this.signer.init(false, asymmetricKeyParameter);
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey, final SecureRandom secureRandom) throws InvalidKeyException {
        if (privateKey instanceof ECKey) {
            final AsymmetricKeyParameter generatePrivateKeyParameter = ECUtil.generatePrivateKeyParameter(privateKey);
            this.digest.reset();
            if (secureRandom != null) {
                this.signer.init(true, new ParametersWithRandom(generatePrivateKeyParameter, secureRandom));
            }
            else {
                this.signer.init(true, generatePrivateKeyParameter);
            }
            return;
        }
        throw new InvalidKeyException("can't recognise key type in ECDSA based signer");
    }
    
    private static class CVCDSAEncoder implements DSAEncoder
    {
        @Override
        public byte[] encode(final BigInteger bigInteger, final BigInteger bigInteger2) throws IOException {
            final byte[] unsigned = this.makeUnsigned(bigInteger);
            final byte[] unsigned2 = this.makeUnsigned(bigInteger2);
            byte[] array;
            if (unsigned.length > unsigned2.length) {
                array = new byte[unsigned.length * 2];
            }
            else {
                array = new byte[unsigned2.length * 2];
            }
            System.arraycopy(unsigned, 0, array, array.length / 2 - unsigned.length, unsigned.length);
            System.arraycopy(unsigned2, 0, array, array.length - unsigned2.length, unsigned2.length);
            return array;
        }
        
        private byte[] makeUnsigned(final BigInteger bigInteger) {
            final byte[] byteArray = bigInteger.toByteArray();
            if (byteArray[0] == 0) {
                final byte[] array = new byte[byteArray.length - 1];
                System.arraycopy(byteArray, 1, array, 0, array.length);
                return array;
            }
            return byteArray;
        }
        
        @Override
        public BigInteger[] decode(final byte[] array) throws IOException {
            final BigInteger[] array2 = new BigInteger[2];
            final byte[] magnitude = new byte[array.length / 2];
            final byte[] magnitude2 = new byte[array.length / 2];
            System.arraycopy(array, 0, magnitude, 0, magnitude.length);
            System.arraycopy(array, magnitude.length, magnitude2, 0, magnitude2.length);
            array2[0] = new BigInteger(1, magnitude);
            array2[1] = new BigInteger(1, magnitude2);
            return array2;
        }
    }
    
    private static class StdDSAEncoder implements DSAEncoder
    {
        @Override
        public byte[] encode(final BigInteger bigInteger, final BigInteger bigInteger2) throws IOException {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            asn1EncodableVector.add(new DERInteger(bigInteger));
            asn1EncodableVector.add(new DERInteger(bigInteger2));
            return new DERSequence(asn1EncodableVector).getEncoded("DER");
        }
        
        @Override
        public BigInteger[] decode(final byte[] array) throws IOException {
            final ASN1Sequence asn1Sequence = (ASN1Sequence)ASN1Object.fromByteArray(array);
            return new BigInteger[] { ((DERInteger)asn1Sequence.getObjectAt(0)).getValue(), ((DERInteger)asn1Sequence.getObjectAt(1)).getValue() };
        }
    }
    
    public static class ecCVCDSA extends Signature
    {
        public ecCVCDSA() {
            super(new SHA1Digest(), new ECDSASigner(), new CVCDSAEncoder());
        }
    }
    
    public static class ecCVCDSA224 extends Signature
    {
        public ecCVCDSA224() {
            super(new SHA224Digest(), new ECDSASigner(), new CVCDSAEncoder());
        }
    }
    
    public static class ecCVCDSA256 extends Signature
    {
        public ecCVCDSA256() {
            super(new SHA256Digest(), new ECDSASigner(), new CVCDSAEncoder());
        }
    }
    
    public static class ecDSA extends Signature
    {
        public ecDSA() {
            super(new SHA1Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSA224 extends Signature
    {
        public ecDSA224() {
            super(new SHA224Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSA256 extends Signature
    {
        public ecDSA256() {
            super(new SHA256Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSA384 extends Signature
    {
        public ecDSA384() {
            super(new SHA384Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSA512 extends Signature
    {
        public ecDSA512() {
            super(new SHA512Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSARipeMD160 extends Signature
    {
        public ecDSARipeMD160() {
            super(new RIPEMD160Digest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecDSAnone extends Signature
    {
        public ecDSAnone() {
            super(new NullDigest(), new ECDSASigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecNR extends Signature
    {
        public ecNR() {
            super(new SHA1Digest(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecNR224 extends Signature
    {
        public ecNR224() {
            super(new SHA224Digest(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecNR256 extends Signature
    {
        public ecNR256() {
            super(new SHA256Digest(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecNR384 extends Signature
    {
        public ecNR384() {
            super(new SHA384Digest(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
    
    public static class ecNR512 extends Signature
    {
        public ecNR512() {
            super(new SHA512Digest(), new ECNRSigner(), new StdDSAEncoder());
        }
    }
}
