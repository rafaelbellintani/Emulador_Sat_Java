// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import java.io.IOException;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.pkcs.DHParameter;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import javax.crypto.spec.DHPublicKeySpec;
import javax.crypto.spec.DHParameterSpec;
import java.math.BigInteger;
import javax.crypto.interfaces.DHPublicKey;

public class JCEDHPublicKey implements DHPublicKey
{
    static final long serialVersionUID = -216691575254424324L;
    private BigInteger y;
    private DHParameterSpec dhSpec;
    
    JCEDHPublicKey(final DHPublicKeySpec dhPublicKeySpec) {
        this.y = dhPublicKeySpec.getY();
        this.dhSpec = new DHParameterSpec(dhPublicKeySpec.getP(), dhPublicKeySpec.getG());
    }
    
    JCEDHPublicKey(final DHPublicKey dhPublicKey) {
        this.y = dhPublicKey.getY();
        this.dhSpec = dhPublicKey.getParams();
    }
    
    JCEDHPublicKey(final DHPublicKeyParameters dhPublicKeyParameters) {
        this.y = dhPublicKeyParameters.getY();
        this.dhSpec = new DHParameterSpec(dhPublicKeyParameters.getParameters().getP(), dhPublicKeyParameters.getParameters().getG(), dhPublicKeyParameters.getParameters().getL());
    }
    
    JCEDHPublicKey(final BigInteger y, final DHParameterSpec dhSpec) {
        this.y = y;
        this.dhSpec = dhSpec;
    }
    
    JCEDHPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final DHParameter dhParameter = new DHParameter((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
        DERInteger derInteger;
        try {
            derInteger = (DERInteger)subjectPublicKeyInfo.getPublicKey();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in DH public key");
        }
        this.y = derInteger.getValue();
        if (dhParameter.getL() != null) {
            this.dhSpec = new DHParameterSpec(dhParameter.getP(), dhParameter.getG(), dhParameter.getL().intValue());
        }
        else {
            this.dhSpec = new DHParameterSpec(dhParameter.getP(), dhParameter.getG());
        }
    }
    
    @Override
    public String getAlgorithm() {
        return "DH";
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public byte[] getEncoded() {
        return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.dhpublicnumber, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL()).getDERObject()), new DERInteger(this.y)).getDEREncoded();
    }
    
    @Override
    public DHParameterSpec getParams() {
        return this.dhSpec;
    }
    
    @Override
    public BigInteger getY() {
        return this.y;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.y = (BigInteger)objectInputStream.readObject();
        this.dhSpec = new DHParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), objectInputStream.readInt());
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getY());
        objectOutputStream.writeObject(this.dhSpec.getP());
        objectOutputStream.writeObject(this.dhSpec.getG());
        objectOutputStream.writeInt(this.dhSpec.getL());
    }
}
