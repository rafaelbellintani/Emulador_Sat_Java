// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import java.security.spec.RSAPrivateKeySpec;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import java.math.BigInteger;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import java.security.interfaces.RSAPrivateKey;

public class JCERSAPrivateKey implements RSAPrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 5110188922551353628L;
    private static BigInteger ZERO;
    protected BigInteger modulus;
    protected BigInteger privateExponent;
    private PKCS12BagAttributeCarrierImpl attrCarrier;
    
    protected JCERSAPrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    JCERSAPrivateKey(final RSAKeyParameters rsaKeyParameters) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.modulus = rsaKeyParameters.getModulus();
        this.privateExponent = rsaKeyParameters.getExponent();
    }
    
    JCERSAPrivateKey(final RSAPrivateKeySpec rsaPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.modulus = rsaPrivateKeySpec.getModulus();
        this.privateExponent = rsaPrivateKeySpec.getPrivateExponent();
    }
    
    JCERSAPrivateKey(final RSAPrivateKey rsaPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.modulus = rsaPrivateKey.getModulus();
        this.privateExponent = rsaPrivateKey.getPrivateExponent();
    }
    
    @Override
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    @Override
    public BigInteger getPrivateExponent() {
        return this.privateExponent;
    }
    
    @Override
    public String getAlgorithm() {
        return "RSA";
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public byte[] getEncoded() {
        return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, new DERNull()), new RSAPrivateKeyStructure(this.getModulus(), JCERSAPrivateKey.ZERO, this.getPrivateExponent(), JCERSAPrivateKey.ZERO, JCERSAPrivateKey.ZERO, JCERSAPrivateKey.ZERO, JCERSAPrivateKey.ZERO, JCERSAPrivateKey.ZERO).getDERObject()).getDEREncoded();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof RSAPrivateKey)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)o;
        return this.getModulus().equals(rsaPrivateKey.getModulus()) && this.getPrivateExponent().equals(rsaPrivateKey.getPrivateExponent());
    }
    
    @Override
    public int hashCode() {
        return this.getModulus().hashCode() ^ this.getPrivateExponent().hashCode();
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
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.modulus = (BigInteger)objectInputStream.readObject();
        (this.attrCarrier = new PKCS12BagAttributeCarrierImpl()).readObject(objectInputStream);
        this.privateExponent = (BigInteger)objectInputStream.readObject();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.modulus);
        this.attrCarrier.writeObject(objectOutputStream);
        objectOutputStream.writeObject(this.privateExponent);
    }
    
    static {
        JCERSAPrivateKey.ZERO = BigInteger.valueOf(0L);
    }
}
