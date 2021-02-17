// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import java.security.spec.DSAParameterSpec;
import java.security.spec.DSAPrivateKeySpec;
import java.security.interfaces.DSAParams;
import java.math.BigInteger;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;
import java.security.interfaces.DSAPrivateKey;

public class JDKDSAPrivateKey implements DSAPrivateKey, PKCS12BagAttributeCarrier
{
    private static final long serialVersionUID = -4677259546958385734L;
    BigInteger x;
    DSAParams dsaSpec;
    private PKCS12BagAttributeCarrierImpl attrCarrier;
    
    protected JDKDSAPrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    JDKDSAPrivateKey(final DSAPrivateKey dsaPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dsaPrivateKey.getX();
        this.dsaSpec = dsaPrivateKey.getParams();
    }
    
    JDKDSAPrivateKey(final DSAPrivateKeySpec dsaPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dsaPrivateKeySpec.getX();
        this.dsaSpec = new DSAParameterSpec(dsaPrivateKeySpec.getP(), dsaPrivateKeySpec.getQ(), dsaPrivateKeySpec.getG());
    }
    
    JDKDSAPrivateKey(final PrivateKeyInfo privateKeyInfo) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final DSAParameter dsaParameter = new DSAParameter((ASN1Sequence)privateKeyInfo.getAlgorithmId().getParameters());
        this.x = ((DERInteger)privateKeyInfo.getPrivateKey()).getValue();
        this.dsaSpec = new DSAParameterSpec(dsaParameter.getP(), dsaParameter.getQ(), dsaParameter.getG());
    }
    
    JDKDSAPrivateKey(final DSAPrivateKeyParameters dsaPrivateKeyParameters) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dsaPrivateKeyParameters.getX();
        this.dsaSpec = new DSAParameterSpec(dsaPrivateKeyParameters.getParameters().getP(), dsaPrivateKeyParameters.getParameters().getQ(), dsaPrivateKeyParameters.getParameters().getG());
    }
    
    @Override
    public String getAlgorithm() {
        return "DSA";
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public byte[] getEncoded() {
        return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(this.dsaSpec.getP(), this.dsaSpec.getQ(), this.dsaSpec.getG()).getDERObject()), new DERInteger(this.getX())).getDEREncoded();
    }
    
    @Override
    public DSAParams getParams() {
        return this.dsaSpec;
    }
    
    @Override
    public BigInteger getX() {
        return this.x;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DSAPrivateKey)) {
            return false;
        }
        final DSAPrivateKey dsaPrivateKey = (DSAPrivateKey)o;
        return this.getX().equals(dsaPrivateKey.getX()) && this.getParams().getG().equals(dsaPrivateKey.getParams().getG()) && this.getParams().getP().equals(dsaPrivateKey.getParams().getP()) && this.getParams().getQ().equals(dsaPrivateKey.getParams().getQ());
    }
    
    @Override
    public int hashCode() {
        return this.getX().hashCode() ^ this.getParams().getG().hashCode() ^ this.getParams().getP().hashCode() ^ this.getParams().getQ().hashCode();
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
        this.x = (BigInteger)objectInputStream.readObject();
        this.dsaSpec = new DSAParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
        (this.attrCarrier = new PKCS12BagAttributeCarrierImpl()).readObject(objectInputStream);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.x);
        objectOutputStream.writeObject(this.dsaSpec.getP());
        objectOutputStream.writeObject(this.dsaSpec.getQ());
        objectOutputStream.writeObject(this.dsaSpec.getG());
        this.attrCarrier.writeObject(objectOutputStream);
    }
}
