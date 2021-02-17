// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.crypto.agreement.kdf;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.asn1.DEREncodableVector;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.crypto.DerivationFunction;

public class ECDHKEKGenerator implements DerivationFunction
{
    private DerivationFunction kdf;
    private DERObjectIdentifier algorithm;
    private int keySize;
    private byte[] z;
    
    public ECDHKEKGenerator(final Digest digest) {
        this.kdf = new KDF2BytesGenerator(digest);
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        final DHKDFParameters dhkdfParameters = (DHKDFParameters)derivationParameters;
        this.algorithm = dhkdfParameters.getAlgorithm();
        this.keySize = dhkdfParameters.getKeySize();
        this.z = dhkdfParameters.getZ();
    }
    
    @Override
    public Digest getDigest() {
        return this.kdf.getDigest();
    }
    
    @Override
    public int generateBytes(final byte[] array, final int n, final int n2) throws DataLengthException, IllegalArgumentException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new AlgorithmIdentifier(this.algorithm, new DERNull()));
        asn1EncodableVector.add(new DERTaggedObject(true, 2, new DEROctetString(this.integerToBytes(this.keySize))));
        this.kdf.init(new KDFParameters(this.z, new DERSequence(asn1EncodableVector).getDEREncoded()));
        return this.kdf.generateBytes(array, n, n2);
    }
    
    private byte[] integerToBytes(final int n) {
        return new byte[] { (byte)(n >> 24), (byte)(n >> 16), (byte)(n >> 8), (byte)n };
    }
}
