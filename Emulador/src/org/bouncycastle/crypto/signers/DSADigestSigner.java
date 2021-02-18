// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.signers;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.ASN1EncodableVector;
import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DSA;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;

public class DSADigestSigner implements Signer
{
    private final Digest digest;
    private final DSA dsaSigner;
    private boolean forSigning;
    
    public DSADigestSigner(final DSA dsaSigner, final Digest digest) {
        this.digest = digest;
        this.dsaSigner = dsaSigner;
    }
    
    @Override
    public void init(final boolean forSigning, final CipherParameters cipherParameters) {
        this.forSigning = forSigning;
        AsymmetricKeyParameter asymmetricKeyParameter;
        if (cipherParameters instanceof ParametersWithRandom) {
            asymmetricKeyParameter = (AsymmetricKeyParameter)((ParametersWithRandom)cipherParameters).getParameters();
        }
        else {
            asymmetricKeyParameter = (AsymmetricKeyParameter)cipherParameters;
        }
        if (forSigning && !asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Signing Requires Private Key.");
        }
        if (!forSigning && asymmetricKeyParameter.isPrivate()) {
            throw new IllegalArgumentException("Verification Requires Public Key.");
        }
        this.reset();
        this.dsaSigner.init(forSigning, cipherParameters);
    }
    
    @Override
    public void update(final byte b) {
        this.digest.update(b);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.digest.update(array, n, n2);
    }
    
    @Override
    public byte[] generateSignature() {
        if (!this.forSigning) {
            throw new IllegalStateException("DSADigestSigner not initialised for signature generation.");
        }
        final byte[] array = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array, 0);
        final BigInteger[] generateSignature = this.dsaSigner.generateSignature(array);
        return this.derEncode(generateSignature[0], generateSignature[1]);
    }
    
    @Override
    public boolean verifySignature(final byte[] array) {
        if (this.forSigning) {
            throw new IllegalStateException("DSADigestSigner not initialised for verification");
        }
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array2, 0);
        try {
            final BigInteger[] derDecode = this.derDecode(array);
            return this.dsaSigner.verifySignature(array2, derDecode[0], derDecode[1]);
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    @Override
    public void reset() {
        this.digest.reset();
    }
    
    private byte[] derEncode(final BigInteger bigInteger, final BigInteger bigInteger2) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new DERInteger(bigInteger));
        asn1EncodableVector.add(new DERInteger(bigInteger2));
        return new DERSequence(asn1EncodableVector).getDEREncoded();
    }
    
    private BigInteger[] derDecode(final byte[] array) throws IOException {
        final ASN1Sequence asn1Sequence = (ASN1Sequence)ASN1Object.fromByteArray(array);
        return new BigInteger[] { ((DERInteger)asn1Sequence.getObjectAt(0)).getValue(), ((DERInteger)asn1Sequence.getObjectAt(1)).getValue() };
    }
}
