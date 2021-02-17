// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.util.Enumeration;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.spec.GOST3410ParameterSpec;
import org.bouncycastle.jce.spec.GOST3410PublicKeyParameterSetSpec;
import org.bouncycastle.jce.spec.GOST3410PrivateKeySpec;
import org.bouncycastle.jce.interfaces.GOST3410Params;
import java.math.BigInteger;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle.jce.interfaces.GOST3410PrivateKey;

public class JDKGOST3410PrivateKey implements GOST3410PrivateKey, PKCS12BagAttributeCarrier
{
    BigInteger x;
    GOST3410Params gost3410Spec;
    private PKCS12BagAttributeCarrier attrCarrier;
    
    protected JDKGOST3410PrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    JDKGOST3410PrivateKey(final GOST3410PrivateKey gost3410PrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = gost3410PrivateKey.getX();
        this.gost3410Spec = gost3410PrivateKey.getParameters();
    }
    
    JDKGOST3410PrivateKey(final GOST3410PrivateKeySpec gost3410PrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = gost3410PrivateKeySpec.getX();
        this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(gost3410PrivateKeySpec.getP(), gost3410PrivateKeySpec.getQ(), gost3410PrivateKeySpec.getA()));
    }
    
    JDKGOST3410PrivateKey(final PrivateKeyInfo privateKeyInfo) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final GOST3410PublicKeyAlgParameters gost3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters((ASN1Sequence)privateKeyInfo.getAlgorithmId().getParameters());
        final byte[] octets = ((DEROctetString)privateKeyInfo.getPrivateKey()).getOctets();
        final byte[] magnitude = new byte[octets.length];
        for (int i = 0; i != octets.length; ++i) {
            magnitude[i] = octets[octets.length - 1 - i];
        }
        this.x = new BigInteger(1, magnitude);
        this.gost3410Spec = GOST3410ParameterSpec.fromPublicKeyAlg(gost3410PublicKeyAlgParameters);
    }
    
    JDKGOST3410PrivateKey(final GOST3410PrivateKeyParameters gost3410PrivateKeyParameters, final GOST3410ParameterSpec gost3410Spec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = gost3410PrivateKeyParameters.getX();
        this.gost3410Spec = gost3410Spec;
        if (gost3410Spec == null) {
            throw new IllegalArgumentException("spec is null");
        }
    }
    
    @Override
    public String getAlgorithm() {
        return "GOST3410";
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public byte[] getEncoded() {
        final byte[] byteArray = this.getX().toByteArray();
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
        PrivateKeyInfo privateKeyInfo;
        if (this.gost3410Spec instanceof GOST3410ParameterSpec) {
            privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new DERObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new DERObjectIdentifier(this.gost3410Spec.getDigestParamSetOID())).getDERObject()), new DEROctetString(array));
        }
        else {
            privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94), new DEROctetString(array));
        }
        return privateKeyInfo.getDEREncoded();
    }
    
    @Override
    public GOST3410Params getParameters() {
        return this.gost3410Spec;
    }
    
    @Override
    public BigInteger getX() {
        return this.x;
    }
    
    @Override
    public void setBagAttribute(final DERObjectIdentifier derObjectIdentifier, final DEREncodable derEncodable) {
        this.attrCarrier.setBagAttribute(derObjectIdentifier, derEncodable);
    }
    
    @Override
    public DEREncodable getBagAttribute(final DERObjectIdentifier derObjectIdentifier) {
        return this.attrCarrier.getBagAttribute(derObjectIdentifier);
    }
    
    @Override
    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }
}
