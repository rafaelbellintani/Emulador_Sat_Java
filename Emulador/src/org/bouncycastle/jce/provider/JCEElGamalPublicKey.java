// 
// Decompiled by Procyon v0.5.36
// 

package org.bouncycastle.jce.provider;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import java.io.IOException;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.oiw.ElGamalParameter;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import javax.crypto.spec.DHPublicKeySpec;
import org.bouncycastle.jce.spec.ElGamalPublicKeySpec;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;
import java.math.BigInteger;
import javax.crypto.interfaces.DHPublicKey;
import org.bouncycastle.jce.interfaces.ElGamalPublicKey;

public class JCEElGamalPublicKey implements ElGamalPublicKey, DHPublicKey
{
    static final long serialVersionUID = 8712728417091216948L;
    private BigInteger y;
    private ElGamalParameterSpec elSpec;
    
    JCEElGamalPublicKey(final ElGamalPublicKeySpec elGamalPublicKeySpec) {
        this.y = elGamalPublicKeySpec.getY();
        this.elSpec = new ElGamalParameterSpec(elGamalPublicKeySpec.getParams().getP(), elGamalPublicKeySpec.getParams().getG());
    }
    
    JCEElGamalPublicKey(final DHPublicKeySpec dhPublicKeySpec) {
        this.y = dhPublicKeySpec.getY();
        this.elSpec = new ElGamalParameterSpec(dhPublicKeySpec.getP(), dhPublicKeySpec.getG());
    }
    
    JCEElGamalPublicKey(final ElGamalPublicKey elGamalPublicKey) {
        this.y = elGamalPublicKey.getY();
        this.elSpec = elGamalPublicKey.getParameters();
    }
    
    JCEElGamalPublicKey(final DHPublicKey dhPublicKey) {
        this.y = dhPublicKey.getY();
        this.elSpec = new ElGamalParameterSpec(dhPublicKey.getParams().getP(), dhPublicKey.getParams().getG());
    }
    
    JCEElGamalPublicKey(final ElGamalPublicKeyParameters elGamalPublicKeyParameters) {
        this.y = elGamalPublicKeyParameters.getY();
        this.elSpec = new ElGamalParameterSpec(elGamalPublicKeyParameters.getParameters().getP(), elGamalPublicKeyParameters.getParameters().getG());
    }
    
    JCEElGamalPublicKey(final BigInteger y, final ElGamalParameterSpec elSpec) {
        this.y = y;
        this.elSpec = elSpec;
    }
    
    JCEElGamalPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final ElGamalParameter elGamalParameter = new ElGamalParameter((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
        DERInteger derInteger;
        try {
            derInteger = (DERInteger)subjectPublicKeyInfo.getPublicKey();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in DSA public key");
        }
        this.y = derInteger.getValue();
        this.elSpec = new ElGamalParameterSpec(elGamalParameter.getP(), elGamalParameter.getG());
    }
    
    @Override
    public String getAlgorithm() {
        return "ElGamal";
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public byte[] getEncoded() {
        return new SubjectPublicKeyInfo(new AlgorithmIdentifier(OIWObjectIdentifiers.elGamalAlgorithm, new ElGamalParameter(this.elSpec.getP(), this.elSpec.getG()).getDERObject()), new DERInteger(this.y)).getDEREncoded();
    }
    
    @Override
    public ElGamalParameterSpec getParameters() {
        return this.elSpec;
    }
    
    @Override
    public DHParameterSpec getParams() {
        return new DHParameterSpec(this.elSpec.getP(), this.elSpec.getG());
    }
    
    @Override
    public BigInteger getY() {
        return this.y;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.y = (BigInteger)objectInputStream.readObject();
        this.elSpec = new ElGamalParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getY());
        objectOutputStream.writeObject(this.elSpec.getP());
        objectOutputStream.writeObject(this.elSpec.getG());
    }
}
