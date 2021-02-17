// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import java.io.IOException;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeySpec;
import org.bouncycastle.jce.interfaces.GOST3410Params;
import java.math.BigInteger;
import org.bouncycastle.jce.interfaces.GOST3410PublicKey;

public class JDKGOST3410PublicKey implements GOST3410PublicKey
{
    private BigInteger y;
    private GOST3410Params gost3410Spec;
    
    JDKGOST3410PublicKey(final GOST3410PublicKeySpec gost3410PublicKeySpec) {
        this.y = gost3410PublicKeySpec.getY();
        this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(gost3410PublicKeySpec.getP(), gost3410PublicKeySpec.getQ(), gost3410PublicKeySpec.getA()));
    }
    
    JDKGOST3410PublicKey(final GOST3410PublicKey gost3410PublicKey) {
        this.y = gost3410PublicKey.getY();
        this.gost3410Spec = gost3410PublicKey.getParameters();
    }
    
    JDKGOST3410PublicKey(final GOST3410PublicKeyParameters gost3410PublicKeyParameters, final GOST3410ParameterSpec gost3410Spec) {
        this.y = gost3410PublicKeyParameters.getY();
        this.gost3410Spec = gost3410Spec;
    }
    
    JDKGOST3410PublicKey(final BigInteger y, final GOST3410ParameterSpec gost3410Spec) {
        this.y = y;
        this.gost3410Spec = gost3410Spec;
    }
    
    JDKGOST3410PublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final GOST3410PublicKeyAlgParameters gost3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
        try {
            final byte[] octets = ((DEROctetString)subjectPublicKeyInfo.getPublicKey()).getOctets();
            final byte[] magnitude = new byte[octets.length];
            for (int i = 0; i != octets.length; ++i) {
                magnitude[i] = octets[octets.length - 1 - i];
            }
            this.y = new BigInteger(1, magnitude);
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in GOST3410 public key");
        }
        this.gost3410Spec = GOST3410ParameterSpec.fromPublicKeyAlg(gost3410PublicKeyAlgParameters);
    }
    
    @Override
    public String getAlgorithm() {
        return "GOST3410";
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public byte[] getEncoded() {
        final byte[] byteArray = this.getY().toByteArray();
        byte[] array;
        if (byteArray[0] == 0) {
            array = new byte[byteArray.length - 1];
        }
        else {
            array = new byte[byteArray.length];
        }
        for (int i = 0; i != array.length; ++i) {
            array[i] = byteArray[byteArray.length - 1 - i];
        }
        SubjectPublicKeyInfo subjectPublicKeyInfo;
        if (this.gost3410Spec instanceof GOST3410ParameterSpec) {
            if (this.gost3410Spec.getEncryptionParamSetOID() != null) {
                subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new DERObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new DERObjectIdentifier(this.gost3410Spec.getDigestParamSetOID()), new DERObjectIdentifier(this.gost3410Spec.getEncryptionParamSetOID())).getDERObject()), new DEROctetString(array));
            }
            else {
                subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new DERObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new DERObjectIdentifier(this.gost3410Spec.getDigestParamSetOID())).getDERObject()), new DEROctetString(array));
            }
        }
        else {
            subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94), new DEROctetString(array));
        }
        return subjectPublicKeyInfo.getDEREncoded();
    }
    
    @Override
    public GOST3410Params getParameters() {
        return this.gost3410Spec;
    }
    
    @Override
    public BigInteger getY() {
        return this.y;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String property = System.getProperty("line.separator");
        sb.append("GOST3410 Public Key").append(property);
        sb.append("            y: ").append(this.getY().toString(16)).append(property);
        return sb.toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof JDKGOST3410PublicKey) {
            final JDKGOST3410PublicKey jdkgost3410PublicKey = (JDKGOST3410PublicKey)o;
            return this.y.equals(jdkgost3410PublicKey.y) && this.gost3410Spec.equals(jdkgost3410PublicKey.gost3410Spec);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.y.hashCode() ^ this.gost3410Spec.hashCode();
    }
}
