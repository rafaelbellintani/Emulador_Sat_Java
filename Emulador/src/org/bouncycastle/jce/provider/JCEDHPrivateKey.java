// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.pkcs.DHParameter;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import javax.crypto.interfaces.DHPrivateKey;

public class JCEDHPrivateKey implements DHPrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 311058815616901812L;
    BigInteger x;
    DHParameterSpec dhSpec;
    private PKCS12BagAttributeCarrier attrCarrier;
    
    protected JCEDHPrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    JCEDHPrivateKey(final DHPrivateKey dhPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKey.getX();
        this.dhSpec = dhPrivateKey.getParams();
    }
    
    JCEDHPrivateKey(final DHPrivateKeySpec dhPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKeySpec.getX();
        this.dhSpec = new DHParameterSpec(dhPrivateKeySpec.getP(), dhPrivateKeySpec.getG());
    }
    
    JCEDHPrivateKey(final PrivateKeyInfo privateKeyInfo) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final DHParameter dhParameter = new DHParameter((ASN1Sequence)privateKeyInfo.getAlgorithmId().getParameters());
        this.x = ((DERInteger)privateKeyInfo.getPrivateKey()).getValue();
        if (dhParameter.getL() != null) {
            this.dhSpec = new DHParameterSpec(dhParameter.getP(), dhParameter.getG(), dhParameter.getL().intValue());
        }
        else {
            this.dhSpec = new DHParameterSpec(dhParameter.getP(), dhParameter.getG());
        }
    }
    
    JCEDHPrivateKey(final DHPrivateKeyParameters dhPrivateKeyParameters) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKeyParameters.getX();
        this.dhSpec = new DHParameterSpec(dhPrivateKeyParameters.getParameters().getP(), dhPrivateKeyParameters.getParameters().getG(), dhPrivateKeyParameters.getParameters().getL());
    }
    
    @Override
    public String getAlgorithm() {
        return "DH";
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public byte[] getEncoded() {
        return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL()).getDERObject()), new DERInteger(this.getX())).getDEREncoded();
    }
    
    @Override
    public DHParameterSpec getParams() {
        return this.dhSpec;
    }
    
    @Override
    public BigInteger getX() {
        return this.x;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.x = (BigInteger)objectInputStream.readObject();
        this.dhSpec = new DHParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), objectInputStream.readInt());
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getX());
        objectOutputStream.writeObject(this.dhSpec.getP());
        objectOutputStream.writeObject(this.dhSpec.getG());
        objectOutputStream.writeInt(this.dhSpec.getL());
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
