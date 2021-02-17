// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement.kdf;

import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.DerivationFunction;

public class DHKEKGenerator implements DerivationFunction
{
    private final Digest digest;
    private DERObjectIdentifier algorithm;
    private int keySize;
    private byte[] z;
    private byte[] partyAInfo;
    
    public DHKEKGenerator(final Digest digest) {
        this.digest = digest;
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        final DHKDFParameters dhkdfParameters = (DHKDFParameters)derivationParameters;
        this.algorithm = dhkdfParameters.getAlgorithm();
        this.keySize = dhkdfParameters.getKeySize();
        this.z = dhkdfParameters.getZ();
        this.partyAInfo = dhkdfParameters.getExtraInfo();
    }
    
    @Override
    public Digest getDigest() {
        return this.digest;
    }
    
    @Override
    public int generateBytes(final byte[] array, int n, int n2) throws DataLengthException, IllegalArgumentException {
        if (array.length - n2 < n) {
            throw new DataLengthException("output buffer too small");
        }
        final long n3 = n2;
        final int digestSize = this.digest.getDigestSize();
        if (n3 > 8589934591L) {
            throw new IllegalArgumentException("Output length too large");
        }
        final int n4 = (int)((n3 + digestSize - 1L) / digestSize);
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        int n5 = 1;
        for (int i = 0; i < n4; ++i) {
            this.digest.update(this.z, 0, this.z.length);
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(this.algorithm);
            asn1EncodableVector2.add(new DEROctetString(this.integerToBytes(n5)));
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
            if (this.partyAInfo != null) {
                asn1EncodableVector.add(new DERTaggedObject(true, 0, new DEROctetString(this.partyAInfo)));
            }
            asn1EncodableVector.add(new DERTaggedObject(true, 2, new DEROctetString(this.integerToBytes(this.keySize))));
            final byte[] derEncoded = new DERSequence(asn1EncodableVector).getDEREncoded();
            this.digest.update(derEncoded, 0, derEncoded.length);
            this.digest.doFinal(array2, 0);
            if (n2 > digestSize) {
                System.arraycopy(array2, 0, array, n, digestSize);
                n += digestSize;
                n2 -= digestSize;
            }
            else {
                System.arraycopy(array2, 0, array, n, n2);
            }
            ++n5;
        }
        this.digest.reset();
        return n2;
    }
    
    private byte[] integerToBytes(final int n) {
        return new byte[] { (byte)(n >> 24), (byte)(n >> 16), (byte)(n >> 8), (byte)n };
    }
}
